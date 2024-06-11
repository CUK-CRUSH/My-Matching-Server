package Dino.Duett.domain.tag.repository;

import Dino.Duett.domain.tag.entity.Tag;
import Dino.Duett.domain.tag.enums.TagType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByNameAndType(String name, String type);
    boolean existsByNameAndType(String name, String type);
    List<Tag> findAllByType(String type);
}

