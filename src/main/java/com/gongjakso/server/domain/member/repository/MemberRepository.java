package com.gongjakso.server.domain.member.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.enumerate.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByEmailAndDeletedAtIsNull(String email);

    Optional<Member> findMemberByEmailAndMemberTypeAndDeletedAtIsNull(String email, MemberType memberType);
}
