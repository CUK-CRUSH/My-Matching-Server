package Dino.Duett.config.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthMember implements UserDetails {
    private final Long id;
    private final String phoneNumber;
    private final String kakaoId;
    private final String role;
    // 로그인 인증 코드
    private final String verificationCode;

    @Builder
    public AuthMember(Long id, String phoneNumber, String kakaoId, String role, String verificationCode) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.kakaoId = kakaoId;
        this.role = role;
        this.verificationCode = verificationCode;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public String getPassword() {
        return verificationCode;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
