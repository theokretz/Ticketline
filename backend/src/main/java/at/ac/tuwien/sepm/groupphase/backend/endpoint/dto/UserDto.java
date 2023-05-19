package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;

import java.time.LocalDateTime;
import java.util.Set;

public class UserDto {
    private int id;
    private boolean admin;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String salt;
    private Integer points;
    private String passwordResetToken;
    private LocalDateTime passwordResetTs;
    private Boolean locked;
    private Set<Order> orders;
    private Set<PaymentDetail> paymentDetails;
    private Set<Reservation> reservations;
    private Set<Location> locations;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public Set<PaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(Set<PaymentDetail> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getLocked() {
        return locked;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public LocalDateTime getPasswordResetTs() {
        return passwordResetTs;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

    public void setPasswordResetTs(LocalDateTime passwordResetTs) {
        this.passwordResetTs = passwordResetTs;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    @Override
    public String toString() {
        return "UserDto{"
            +
            "id=" + id
            +
            ", admin=" + admin
            +
            ", firstName='" + firstName + '\''
            +
            ", lastName='" + lastName + '\''
            +
            ", email='" + email + '\''
            +
            ", password='" + password + '\''
            +
            ", salt='" + salt + '\''
            +
            ", points=" + points
            +
            ", passwordResetToken='" + passwordResetToken + '\''
            +
            ", passwordResetTs=" + passwordResetTs
            +
            ", locked=" + locked
            +
            ", orders=" + orders
            +
            ", paymentDetails=" + paymentDetails
            +
            ", reservations=" + reservations
            +
            ", locations=" + locations
            +
            '}';
    }
}
