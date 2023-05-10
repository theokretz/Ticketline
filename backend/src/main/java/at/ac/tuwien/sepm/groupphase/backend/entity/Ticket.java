package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"seat_id", "performance_id"})})
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "ticket", fetch = FetchType.LAZY)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(final Reservation reservation) {
        this.reservation = reservation;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(final Performance performance) {
        this.performance = performance;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(final Seat seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "Ticket{"
            + "id=" + id
            + ", reservation=" + reservation
            + ", order=" + order
            + ", performance=" + performance
            + ", seat=" + seat
            + '}';
    }
}

