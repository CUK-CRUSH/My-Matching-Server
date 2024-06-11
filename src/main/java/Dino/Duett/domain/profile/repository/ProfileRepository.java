package Dino.Duett.domain.profile.repository;

import Dino.Duett.domain.profile.entity.Profile;
import Dino.Duett.domain.profile.enums.GenderType;
import com.google.api.Page;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.catalina.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>{
    @Query("SELECT u FROM Profile u WHERE " +
            "u.gender = :oppositeGender AND " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(u.region.latitude)) * " +
            "cos(radians(u.region.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(u.region.latitude)))) < :radius")
    List<Profile> findAllUsersWithinRadius(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius,
            @Param("oppositeGender") GenderType oppositeGender,
            PageRequest pageRequest
    );
}
