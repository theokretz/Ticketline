package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Integer> {


    /**
     * Find all paymentDetails by userid.
     *
     * @param id user id
     * @return list of paymentDetails
     */
    List<PaymentDetail> findByUserId(Integer id);
}
