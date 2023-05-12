package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotUserRepository extends JpaRepository<ApplicationUser, Integer> {

    @EntityGraph(attributePaths = {
        "reservations",
        "reservations.ticket",
        "reservations.ticket.id"
    })
    ApplicationUser findApplicationUserById(Integer id);
}
