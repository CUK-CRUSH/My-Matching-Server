package Dino.Duett.domain.image.repository;

import Dino.Duett.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteByName(String fileName);
}
