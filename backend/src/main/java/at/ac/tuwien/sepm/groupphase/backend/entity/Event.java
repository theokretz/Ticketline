package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.time.Duration;
import java.util.Set;


@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private String type;

    @Column(nullable = false)
    private Duration length;

    @Column
    private String description;

    @ManyToMany(mappedBy = "events")
    private Set<Artist> artists;

    @OneToMany(mappedBy = "event")
    private Set<Performance> performances;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Duration getLength() {
        return length;
    }

    public void setLength(final Duration length) {
        this.length = length;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(final Set<Artist> artists) {
        this.artists = artists;
    }

    public Set<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(final Set<Performance> performances) {
        this.performances = performances;
    }

    @Override
    public String toString() {
        return "Event{"
            +
            "id=" + id
            +
            ", name='" + name + '\''
            +
            ", type='" + type + '\''
            +
            ", length=" + length
            +
            ", description='" + description + '\''
            +
            ", artists=" + artists
            +
            ", performances=" + performances
            +
            '}';
    }
}
