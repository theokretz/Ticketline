package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;


@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal deductedAmount;

    @Column(nullable = false)
    private Integer deductedPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public BigDecimal getDeductedAmount() {
        return deductedAmount;
    }

    public void setDeductedAmount(final BigDecimal deductedAmount) {
        this.deductedAmount = deductedAmount;
    }

    public Integer getDeductedPoints() {
        return deductedPoints;
    }

    public void setDeductedPoints(final Integer deductedPoints) {
        this.deductedPoints = deductedPoints;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Transaction{"
            + "id=" + id
            + ", deductedAmount=" + deductedAmount
            + ", deductedPoints=" + deductedPoints
            + ", order=" + order
            + '}';
    }

    public static final class TransactionBuilder {
        private Integer id;
        private BigDecimal deductedAmount;
        private Integer deductedPoints;
        private Order order;

        private TransactionBuilder() {
        }

        public static TransactionBuilder aTransaction() {
            return new TransactionBuilder();
        }

        public TransactionBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public TransactionBuilder withDeductedAmount(BigDecimal deductedAmount) {
            this.deductedAmount = deductedAmount;
            return this;
        }

        public TransactionBuilder withDeductedPoints(Integer deductedPoints) {
            this.deductedPoints = deductedPoints;
            return this;
        }

        public TransactionBuilder withOrder(Order order) {
            this.order = order;
            return this;
        }

        public Transaction build() {
            Transaction transaction = new Transaction();
            transaction.setId(id);
            transaction.setDeductedAmount(deductedAmount);
            transaction.setDeductedPoints(deductedPoints);
            transaction.setOrder(order);
            return transaction;
        }
    }

}

