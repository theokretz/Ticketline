package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.ArtistSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class CustomEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;


    public CustomEventService(EventRepository eventRepository, ArtistRepository artistRepository, PerformanceRepository performanceRepository) {
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Event> getAllEventsOfArtist(ArtistSearchDto artist) {
        LOGGER.debug("Find all events of artist{}", artist);
        List<Event> events = eventRepository.findEventsByArtistsId(artist.getId());
        if (events.isEmpty()) {
            throw new NotFoundException(String.format("Could not find events associated with the given artist", artist));
        }
        return events;
    }

    @Override
    public List<Artist> getAllArtists(ArtistSearchDto artistName) {
        LOGGER.debug("Find artist with name like{}", artistName);
        List<Artist> artists = artistRepository.findByNameContainingIgnoreCase(artistName.getName());
        if (artists.isEmpty()) {
            throw new NotFoundException(String.format("Could not find artists associated with the given name", artistName));
        }
        return artists;
    }
}
