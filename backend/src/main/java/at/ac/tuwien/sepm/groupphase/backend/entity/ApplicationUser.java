package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Set;

//TODO: replace this class with a correct ApplicationUser Entity implementation
@Entity
@Table(name = "\"user\"")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Boolean admin;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 16)
    private String salt;

    @Column(nullable = false)
    private Integer points;

    @Column
    private String passwordResetToken;

    @Column
    private LocalDateTime passwordResetTs;

    @Column(nullable = false)
    private Boolean locked;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders;

    @OneToMany(mappedBy = "user")
    private Set<PaymentDetail> paymentDetails;

    @OneToMany(mappedBy = "user")
    private Set<Reservation> reservations;

    @OneToMany(mappedBy = "user")
    private Set<Location> locations;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public ApplicationUser() {
    }

    public ApplicationUser(String email, String password, Boolean admin) {
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    public void setAdmin(final Boolean admin) {
        this.admin = admin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(final String salt) {
        this.salt = salt;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(final Integer points) {
        this.points = points;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(final String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public LocalDateTime getPasswordResetTs() {
        return passwordResetTs;
    }

    public void setPasswordResetTs(final LocalDateTime passwordResetTs) {
        this.passwordResetTs = passwordResetTs;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(final Boolean locked) {
        this.locked = locked;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(final Set<Order> orders) {
        this.orders = orders;
    }

    public Set<PaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(final Set<PaymentDetail> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(final Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(final Set<Location> locations) {
        this.locations = locations;
    }


    public static final class UserBuilder {
        private Integer id;
        private Boolean admin;
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

        private UserBuilder() {
        }

        public static UserBuilder aUser() {
            return new UserBuilder();
        }

        public UserBuilder withId(final Integer id) {
            this.id = id;
            return this;
        }

        public UserBuilder withAdmin(final Boolean admin) {
            this.admin = admin;
            return this;
        }

        public UserBuilder withFirstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder withLastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder withEmail(final String email) {
            this.email = email;
            return this;
        }

        public UserBuilder withPassword(final String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withSalt(final String salt) {
            this.salt = salt;
            return this;
        }

        public UserBuilder withPoints(final Integer points) {
            this.points = points;
            return this;
        }

        public UserBuilder withPasswordResetToken(final String passwordResetToken) {
            this.passwordResetToken = passwordResetToken;
            return this;
        }

        public UserBuilder withPasswordResetTs(final LocalDateTime passwordResetTs) {
            this.passwordResetTs = passwordResetTs;
            return this;
        }

        public UserBuilder withLocked(final Boolean locked) {
            this.locked = locked;
            return this;
        }

        public UserBuilder withOrders(final Set<Order> orders) {
            this.orders = orders;
            return this;
        }

        public UserBuilder withPaymentDetails(final Set<PaymentDetail> paymentDetails) {
            this.paymentDetails = paymentDetails;
            return this;
        }

        public UserBuilder withReservations(final Set<Reservation> reservations) {
            this.reservations = reservations;
            return this;
        }

        public UserBuilder withLocations(final Set<Location> locations) {
            this.locations = locations;
            return this;
        }

        public ApplicationUser build() {
            ApplicationUser user = new ApplicationUser();
            user.setId(id);
            user.setAdmin(admin);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setSalt(salt);
            user.setPoints(points);
            user.setPasswordResetToken(passwordResetToken);
            user.setPasswordResetTs(passwordResetTs);
            user.setLocked(locked);
            user.setOrders(orders);
            user.setPaymentDetails(paymentDetails);
            user.setReservations(reservations);
            user.setLocations(locations);
            return user;
        }
    }

}
