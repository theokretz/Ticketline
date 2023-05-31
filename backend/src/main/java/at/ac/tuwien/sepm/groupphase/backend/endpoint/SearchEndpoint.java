package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedEventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.ArtistSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/search")
public class SearchEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final ArtistMapper artistMapper;

    @Autowired
    public SearchEndpoint(EventService eventService, EventMapper eventMapper, ArtistMapper artistMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.artistMapper = artistMapper;
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/events-by-artist")
    @Operation(summary = "Get all events of the given artist", security = @SecurityRequirement(name = "apiKey"))
    public List<DetailedEventDto> getAllEventsOfArtist(@Valid ArtistSearchDto artist) {
        LOGGER.info("GET /api/v1/events/artists");

        try {
            List<Event> events = this.eventService.getAllEventsOfArtist(artist);
            return this.eventMapper.eventToDetailedEventDto(events);
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find events of artist" + e.getMessage());
            HttpStatus status = HttpStatus.OK;
            return new ArrayList<>();
        }
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/artists")
    @Operation(summary = "Get all artist containing the given parameters", security = @SecurityRequirement(name = "apiKey"))
    public List<ArtistSearchDto> getAllArtistWithParams(ArtistSearchDto parameters) {
        LOGGER.info("GET /api/v1/events/artists");
        try {
            List<Artist> artists = this.eventService.getAllArtists(parameters);
            return artistMapper.artistToEventArtistSearchDto(artists);
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find artists" + e.getMessage());
            HttpStatus status = HttpStatus.OK;
            return new ArrayList<>();
        }
    }

}
