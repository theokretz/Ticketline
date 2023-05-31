package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.ArtistSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 Describes a service for managing events
 */
@Service
public interface EventService {

    /**

     * Get the events filtered by the given artist/band name.
     *
     * @param artist the artist whose events should be listed
     * @return list of the events of the given artist
     */
    List<Event> getAllEventsOfArtist(ArtistSearchDto artist);

    /**

     * Get the events filtered by the given artist/band name.
     *
     * @param name the name of the artists that should be listed
     * @return all artists matching the given name
     */
    List<Artist> getAllArtists(ArtistSearchDto name);
}
