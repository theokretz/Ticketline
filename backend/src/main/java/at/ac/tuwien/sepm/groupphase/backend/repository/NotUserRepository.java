package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface NotUserRepository extends JpaRepository<User, Integer> {


    /**
     * Find all users with location & paymentdetails JOINED (eagerly).
     *
     * @return list of all users
     */
    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.locations JOIN FETCH u.paymentDetails")
    List<User> findAllWithLocationsAndPaymentDetails();
}
