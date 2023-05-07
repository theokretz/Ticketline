package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Table(name = "\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime orderTs;

    @Column(nullable = false)
    private Boolean cancelled;

    @OneToMany(mappedBy = "order")
    private Set<MerchandiseOrdered> merchandiseOrdered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_adress_id", nullable = false)
    private Location deliveryAdress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_detail_id", nullable = false)
    private PaymentDetail paymentDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @OneToMany(mappedBy = "order")
    private Set<Ticket> tickets;

    @OneToMany(mappedBy = "order")
    private Set<Transaction> transactions;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public LocalDateTime getOrderTs() {
        return orderTs;
    }

    public void setOrderTs(final LocalDateTime orderTs) {
        this.orderTs = orderTs;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(final Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Set<MerchandiseOrdered> getMerchandiseOrdered() {
        return merchandiseOrdered;
    }

    public void setMerchandiseOrdered(final Set<MerchandiseOrdered> merchandiseOrdered) {
        this.merchandiseOrdered = merchandiseOrdered;
    }

    public Location getDeliveryAdress() {
        return deliveryAdress;
    }

    public void setDeliveryAdress(final Location deliveryAdress) {
        this.deliveryAdress = deliveryAdress;
    }

    public PaymentDetail getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(final PaymentDetail paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(final ApplicationUser user) {
        this.user = user;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(final Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(final Set<Transaction> transactions) {
        this.transactions = transactions;
    }

}

