package com.gongjakso.server.global.security.jwt;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.repository.MemberRepository;
import com.gongjakso.server.global.security.jwt.dto.TokenDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${jwt.secret")
    private String secretKey;
    private Key key;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    // ATK 만료시간: 1일
    private static final long accessTokenExpirationTime = 24 * 60 * 60 * 1000L;

    // RTK 만료시간: 30일
    private static final long refreshTokenExpirationTime = 30 * 24 * 60 * 60 * 1000L;

    /**
     * 의존성 주입 후 초기화를 수행하는 메소드
     */
    @PostConstruct
    protected void init() {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    /**
     * ATK 생성
     * @param user - 사용자 정보를 추출하여 액세스 토큰 생성
     * @return 생성된 액세스 토큰 정보 반환
     */
    private String createAccessToken(Member member) {
        Claims claims = getClaims(user);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * RTK 생성
     * @param user - 사용자 정보를 추출하여 리프레쉬 토큰 생성
     * @return 생성된 리프레쉬 토큰 정보 반환
     */
    private String createRefreshToken(Member member) {
        Claims claims = getClaims(user);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 로그인 시, 액세스 토큰과 리프레쉬 토큰 발급
     * @param user - 로그인한 사용자 정보
     * @return 액세스 토큰과 리프레쉬 토큰이 담긴 TokenDto 반환
     */
    public TokenDto createToken(Member member) {
        return TokenDto.builder()
                .accessToken(createAccessToken(user))
                .refreshToken(createRefreshToken(user))
                .build();
    }

    /**
     * 토큰 유효성 검사
     * @param token - 일반적으로 액세스 토큰 / 토큰 재발급 요청 시에는 리프레쉬 토큰이 들어옴
     * @return 유효하면 true, 유효하지 않으면 false 반환
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 리프레쉬 토큰 기반으로 액세스 토큰 재발급
     * @param token - 리프레쉬 토큰
     * @return 재발급된 액세스 토큰을 담은 TokenDto 객체 반환
     */
    public TokenDto accessTokenReissue(String token) {
        String email = getEmail(token);
        UserRole role = getRole(token);

        Member member = memberRepository.findByEmailAndRole(email, role).orElseThrow(RuntimeException::new); // Exception은 실제 개발에서는 커스텀 필요
        String storedRefreshToken = redisTemplate.opsForValue().get(email + role.toString()); // Key는 email + role로 저장되어 있으며, value가 해당 정보에 대한 refreshToken임.
        if(storedRefreshToken == null || !storedRefreshToken.equals(token)) {
            throw new RuntimeException();
        }

        String accessToken = createAccessToken(member);

        // 해당 부분에 refreshToken의 만료기간이 얼마 남지 않았을 때, 자동 재발급하는 로직을 추가할 수 있음.

        return TokenDto.builder()
                .atk(accessToken)
                .rtk(token)
                .build();
    }

    /**
     * 토큰에서 정보를 추출해서 Authentication 객체를 반환
     * @param token - 액세스 토큰으로, 해당 토큰에서 정보를 추출해서 사용
     * @return 토큰 정보와 일치하는 Authentication 객체 반환
     */
    public Authentication getAuthentication(String token) {
        String email = getEmail(token);
        UserRole role = getRole(token);

        User user = userRepository.findByEmailAndRole(email, role).orElseThrow(RuntimeException::new); // Exception은 실제 개발에서는 커스텀 필요
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(user.getRole().toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails details = new org.springframework.security.core.userdetails.User(email, "", authorities);

        return new UsernamePasswordAuthenticationToken(details, "", authorities);
    }

    /**
     * 토큰의 만료기한 반환
     * @param token - 일반적으로 액세스 토큰 / 토큰 재발급 요청 시에는 리프레쉬 토큰이 들어옴
     * @return 해당 토큰의 만료정보를 반환
     */
    public Date getExpiration(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
    }

    /**
     * Claims 정보 생성
     * @param user - 사용자 정보 중 사용자를 구분할 수 있는 정보 두 개를 활용함
     * @return 사용자 구분 정보인 이메일과 역할을 저장한 Claims 객체 반환
     */
    private Claims getClaims(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("role", user.getRole());

        return claims;
    }

    /**
     * 토큰에서 email 정보 반환
     * @param token - 일반적으로 액세스 토큰 / 토큰 재발급 요청 시에는 리프레쉬 토큰이 들어옴
     * @return 사용자의 email 반환
     */
    private String getEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 토큰에서 사용자의 역할 반환
     * @param token - 일반적으로 액세스 토큰 / 토큰 재발급 요청 시에는 리프레쉬 토큰이 들어옴
     * @return 사용자의 역할 반환 (UserRole)
     */
    private UserRole getRole(String token) {
        return UserRole.valueOf((String) Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role"));
    }

}
