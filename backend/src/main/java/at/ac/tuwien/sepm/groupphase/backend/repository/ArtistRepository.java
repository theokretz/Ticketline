package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    /**
     * Find all events and LEFT JOIN them (eager fetch).
     *
     * @return list of all artists
     */
    @Query("SELECT a FROM Artist a LEFT JOIN FETCH a.events")
    List<Artist> findAllFetchEvents();

    /**
     * Find all artists with name LIKE the given.
     *
     * @return list of all found artist
     */
    List<Artist> findByNameContainingIgnoreCase(String name);



}

