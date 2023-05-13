package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.MerchandiseOrdered;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

public class OrderDto {
    private Integer id;
    private LocalDateTime orderTs;
    private Boolean cancelled;
    private Set<MerchandiseOrdered> merchandiseOrdered;
    private Location deliveryAdress;
    private PaymentDetail paymentDetail;
    private ApplicationUser user;
    private Set<Ticket> tickets;
    private Set<Transaction> transactions;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setOrderTs(LocalDateTime orderTs) {
        this.orderTs = orderTs;
    }

    public LocalDateTime getOrderTs() {
        return orderTs;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setMerchandiseOrdered(Set<MerchandiseOrdered> merchandiseOrdered) {
        this.merchandiseOrdered = merchandiseOrdered;
    }

    public Set<MerchandiseOrdered> getMerchandiseOrdered() {
        return merchandiseOrdered;
    }

    public void setDeliveryAdress(Location deliveryAdress) {
        this.deliveryAdress = deliveryAdress;
    }

    public Location getDeliveryAdress() {
        return deliveryAdress;
    }

    public void setPaymentDetail(PaymentDetail paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public PaymentDetail getPaymentDetail() {
        return paymentDetail;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "OrderDto{"
            +
            "id=" + id
            +
            ", orderTs=" + orderTs
            +
            ", cancelled=" + cancelled
            +
            ", merchandiseOrdered=" + merchandiseOrdered
            +
            ", deliveryAdress=" + deliveryAdress
            +
            ", paymentDetail=" + paymentDetail
            +
            ", user=" + user
            +
            ", tickets=" + tickets
            +
            ", transactions=" + transactions
            +
            '}';
    }
}
