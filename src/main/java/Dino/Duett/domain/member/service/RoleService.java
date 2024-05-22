package Dino.Duett.domain.member.service;

import Dino.Duett.domain.member.entity.Role;
import Dino.Duett.domain.member.enums.RoleName;
import Dino.Duett.domain.member.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    @PostConstruct
    protected void init() {
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByName(roleName.name())) {
                roleRepository.save(Role.builder()
                        .name(roleName.name()).build()
                );
            }
        }
    }
}
