package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MerchandiseFilterDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;


public class MerchandiseRepositoryImpl extends SimpleJpaRepository<Merchandise, Integer> implements MerchandiseRepository {

    public MerchandiseRepositoryImpl(EntityManager em) {
        super(Merchandise.class, em);
    }

    public List<Merchandise> searchMerchandise(MerchandiseFilterDto filterParams) {

        //TODO add more filter params later
        //TODO maybe Validation or maybe later

        //filter for price range
        Specification<Merchandise> filter = (root, query, cb) -> cb.between(root.get("price"), filterParams.getMinPrice(), filterParams.getMaxPrice());

        //filter for title if in DTO
        if (!filterParams.getTitle().isBlank()) {
            Specification<Merchandise> titleSpec = (root, query, cb) -> cb.like(root.get("title"), filterParams.getTitle());
            filter = filter.and(titleSpec);
        }
        return findAll(filter);
    }

    /*
    LANGE VERSION:

    private Specification<Merchandise> createPriceSpec() {
        return new Specification<Merchandise>() {
            @Override
            public Predicate toPredicate(Root<Merchandise> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.between(root.get("price"), 100, 200);
            }
        };
    }

     */

}
