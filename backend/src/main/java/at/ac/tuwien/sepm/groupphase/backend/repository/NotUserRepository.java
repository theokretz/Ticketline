package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface NotUserRepository extends JpaRepository<ApplicationUser, Integer> {

    @EntityGraph(attributePaths = {
        "reservations",
        "reservations.ticket",
        "locations",
        "paymentDetails",
        "paymentDetails.orders",
        "orders",
    })
    ApplicationUser findApplicationUserById(Integer id);

    /**
     * Find all users with location & paymentdetails JOINED (eagerly).
     *
     * @return list of all users
     */
    @Query("SELECT DISTINCT u FROM ApplicationUser u JOIN FETCH u.locations JOIN FETCH u.paymentDetails")
    List<ApplicationUser> findAllWithLocationsAndPaymentDetails();

    /**
     * Find user by id.
     *
     * @param id of user to find
     * @return user with given id
     */
    ApplicationUser getApplicationUserById(Integer id);

    /**
     * get application user by email.
     *
     * @param email of user to find
     * @return ApplicationUser with given email
     */
    Optional<ApplicationUser> findApplicationUsersByEmail(String email);


    /**
     * get application user by password reset token.
     *
     * @param token of user to find
     * @return ApplicationUser with given token
     */
    Optional<ApplicationUser> getApplicationUserByPasswordResetToken(String token);

}
