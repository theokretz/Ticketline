package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findAllByUserId(Integer userId);

    Location findFirstByUserIdIsNull();

    Location findLocationById(Integer locationId);
}

