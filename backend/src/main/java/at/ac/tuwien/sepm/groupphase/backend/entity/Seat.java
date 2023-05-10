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
import jakarta.persistence.UniqueConstraint;

import java.util.Set;


@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"row", "number", "sector_id"})})
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, name = "\"row\"")
    private Integer row;

    @Column(nullable = false, unique = true)
    private Integer number;

    @OneToMany(mappedBy = "seat")
    private Set<Ticket> tickets;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(final Integer row) {
        this.row = row;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(final Integer number) {
        this.number = number;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(final Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(final Sector sector) {
        this.sector = sector;
    }

    @Override
    public String toString() {
        return "Seat{"
            + "id=" + id
            + ", row=" + row
            + ", number=" + number
            + ", tickets=" + tickets
            + ", sector=" + sector
            + '}';
    }
}
