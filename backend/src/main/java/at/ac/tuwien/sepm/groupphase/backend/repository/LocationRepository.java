package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findAllByUserId(Integer userId);

    Location findFirstByUserIdIsNull();

    Location findLocationById(Integer locationId);

    @Query("SELECT a FROM Location a WHERE (:postalCode is null OR a.postalCode = :postalCode) AND "
        + "(:city is null OR :city='' OR UPPER(a.city) LIKE UPPER(CONCAT( '%', :city, '%'))) "
        + "AND (:country is null OR :country='' OR UPPER(a.country) LIKE UPPER(CONCAT( '%', :country, '%'))) AND "
        + "(:street is null OR :street='' OR UPPER(a.street) LIKE UPPER(CONCAT( '%', :street, '%')))")
    List<Location> findAllContaining(@Param("city") String city, @Param("country") String country,
                                     @Param("street") String street, @Param("postalCode") Integer postalCode);
}

