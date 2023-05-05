package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Integer> {
}
