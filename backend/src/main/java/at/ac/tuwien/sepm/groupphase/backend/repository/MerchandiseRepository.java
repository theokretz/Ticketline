package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MerchandiseRepository extends JpaRepository<Merchandise, Integer> {
}

