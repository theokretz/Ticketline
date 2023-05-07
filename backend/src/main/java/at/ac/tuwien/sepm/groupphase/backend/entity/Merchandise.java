package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.Set;


@Entity
public class Merchandise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal price;

    @Column
    private Integer pointsPrice;

    @Column(nullable = false)
    private Integer pointsReward;

    @OneToMany(mappedBy = "merchandise")
    private Set<MerchandiseOrdered> merchandiseMerchandiseOrdereds;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Integer getPointsPrice() {
        return pointsPrice;
    }

    public void setPointsPrice(final Integer pointsPrice) {
        this.pointsPrice = pointsPrice;
    }

    public Integer getPointsReward() {
        return pointsReward;
    }

    public void setPointsReward(final Integer pointsReward) {
        this.pointsReward = pointsReward;
    }

    public Set<MerchandiseOrdered> getMerchandiseMerchandiseOrdereds() {
        return merchandiseMerchandiseOrdereds;
    }

    public void setMerchandiseMerchandiseOrdereds(
        final Set<MerchandiseOrdered> merchandiseMerchandiseOrdereds) {
        this.merchandiseMerchandiseOrdereds = merchandiseMerchandiseOrdereds;
    }

}

