package Dino.Duett.domain.member.entity;

import Dino.Duett.domain.member.enums.MemberState;
import Dino.Duett.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@Table(name = "member")
@RequiredArgsConstructor
public class Member extends BaseEntity {
    @Column(name = "phoneNumber", nullable = false, unique = true, length = 15)
    private String phoneNumber;
    @Column(name = "kakaoId", nullable = false, unique = true, length = 50)
    private String kakaoId;
    @Column(name = "coin", nullable = false)
    private Integer coin;
    @Column(name = "state", nullable = false)
    private MemberState state;
}
