package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface NotUserRepository extends JpaRepository<ApplicationUser, Integer> {

    @EntityGraph(attributePaths = {
        "reservations",
        "reservations.ticket",
        "locations",
        "paymentDetails"
    })
    ApplicationUser findApplicationUserById(Integer id);

    /**
     * Find all users with location & paymentdetails JOINED (eagerly).
     *
     * @return list of all users
     */
    @Query("SELECT DISTINCT u FROM ApplicationUser u JOIN FETCH u.locations JOIN FETCH u.paymentDetails")
    List<ApplicationUser> findAllWithLocationsAndPaymentDetails();
}
