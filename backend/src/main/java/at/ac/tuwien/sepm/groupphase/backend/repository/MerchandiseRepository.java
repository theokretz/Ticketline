package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MerchandiseFilterDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MerchandiseRepository extends JpaRepository<Merchandise, Integer> {


    List<Merchandise> searchMerchandise(MerchandiseFilterDto filterParams);

    List<Merchandise> findAllMerchandiseWithPoints();

}

