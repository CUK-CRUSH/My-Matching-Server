package Dino.Duett.domain.member.entity;

import Dino.Duett.domain.member.enums.MemberState;
import Dino.Duett.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phoneNumber", nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(name = "kakaoId", nullable = false, unique = true, length = 50)
    private String kakaoId;

    @Column(name = "coin", nullable = false)
    private Integer coin;

    @Column(name = "state", nullable = false)
    private MemberState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Builder
    public Member(Long id, String phoneNumber, String kakaoId, Integer coin, MemberState state, Role role) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.kakaoId = kakaoId;
        this.coin = coin;
        this.state = state;
        this.role = role;
    }
}
