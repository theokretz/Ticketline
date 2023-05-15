package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.MerchandiseOrdered;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

public class OrderDto {
    private Integer id;
    private LocalDateTime orderTs;
    private Boolean cancelled;
    private Set<MerchandiseOrdered> merchandiseOrdered;
    private Location deliveryAddress;
    private SimplePaymentDetailDto paymentDetail;
    private SimpleUserDto user;
    private Set<Ticket> tickets;
    private Set<Transaction> transactions;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getOrderTs() {
        return orderTs;
    }

    public void setOrderTs(LocalDateTime orderTs) {
        this.orderTs = orderTs;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Set<MerchandiseOrdered> getMerchandiseOrdered() {
        return merchandiseOrdered;
    }

    public void setMerchandiseOrdered(Set<MerchandiseOrdered> merchandiseOrdered) {
        this.merchandiseOrdered = merchandiseOrdered;
    }

    public Location getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Location deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public SimplePaymentDetailDto getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(SimplePaymentDetailDto paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public SimpleUserDto getUser() {
        return user;
    }

    public void setUser(SimpleUserDto user) {
        this.user = user;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
}
