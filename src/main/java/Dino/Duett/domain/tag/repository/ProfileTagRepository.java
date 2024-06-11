package Dino.Duett.domain.tag.repository;

import Dino.Duett.domain.profile.entity.Profile;
import Dino.Duett.domain.tag.entity.ProfileTag;
import Dino.Duett.domain.tag.enums.PriorityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileTagRepository extends JpaRepository<ProfileTag, Long> {
}
