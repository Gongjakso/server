package com.gongjakso.server.global.security.jwt;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.enumerate.MemberType;
import com.gongjakso.server.domain.member.repository.MemberRepository;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import com.gongjakso.server.global.security.PrincipalDetails;
import com.gongjakso.server.global.security.jwt.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;
    private final MemberRepository memberRepository;

    // ATK 만료시간: 1일
    private static final long accessTokenExpirationTime = 7 * 24 * 60 * 60 * 1000L;

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
     * @param member - 사용자 정보를 추출하여 액세스 토큰 생성
     * @return 생성된 액세스 토큰 정보 반환
     */
    private String createAccessToken(Member member) {
        Claims claims = getClaims(member);

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
     * @param member - 사용자 정보를 추출하여 리프레쉬 토큰 생성
     * @return 생성된 리프레쉬 토큰 정보 반환
     */
    private String createRefreshToken(Member member) {
        Claims claims = getClaims(member);

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
     * @param member - 로그인한 사용자 정보
     * @return 액세스 토큰과 리프레쉬 토큰이 담긴 TokenDto 반환
     */
    public TokenDto createToken(Member member) {
        return TokenDto.builder()
                .accessToken(createAccessToken(member))
                .refreshToken(createRefreshToken(member))
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
        MemberType memberType = getType(token);

        Member member = memberRepository.findMemberByEmailAndMemberTypeAndDeletedAtIsNull(email, memberType).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        String accessToken = createAccessToken(member);

        // 해당 부분에 refreshToken의 만료기간이 얼마 남지 않았을 때, 자동 재발급하는 로직을 추가할 수 있음.

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(token)
                .build();
    }

    /**
     * 토큰에서 정보를 추출해서 Authentication 객체를 반환
     * @param token - 액세스 토큰으로, 해당 토큰에서 정보를 추출해서 사용
     * @return 토큰 정보와 일치하는 Authentication 객체 반환
     */
    public Authentication getAuthentication(String token) {
        String email = getEmail(token);
        MemberType memberType = getType(token);
        Member member = memberRepository.findMemberByEmailAndMemberTypeAndDeletedAtIsNull(email, memberType).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        PrincipalDetails principalDetails = new PrincipalDetails(member);

        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
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
     * @param member - 사용자 정보 중 사용자를 구분할 수 있는 정보 두 개를 활용함
     * @return 사용자 구분 정보인 이메일과 역할을 저장한 Claims 객체 반환
     */
    private Claims getClaims(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        claims.put("role", member.getMemberType());

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
    private MemberType getType(String token) {
        return MemberType.valueOf((String) Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role"));
    }

}
