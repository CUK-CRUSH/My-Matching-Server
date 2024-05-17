package Dino.Duett.domain.member.repository;

import Dino.Duett.domain.member.entity.Role;
import Dino.Duett.domain.member.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String roleName);

    Optional<Role> findByName(String name);
}
