package Dino.Duett.domain.member.repository;

import Dino.Duett.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByKakaoId(String kakaoId);
}
