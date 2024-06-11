package Dino.Duett.domain.mood.repository;

import Dino.Duett.domain.mood.entity.Mood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoodRepository extends JpaRepository<Mood, Long>{
}
