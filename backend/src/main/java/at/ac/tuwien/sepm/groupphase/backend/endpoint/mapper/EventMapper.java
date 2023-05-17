package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedEventDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class EventMapper {
    public DetailedEventDto eventToDetailedEventDto(Event event) {
        if (event == null) {
            return null;
        }
        String artistsFormatted = "";
        // format the set of artists to a string, but only show at most 3 artists!
        int i = 0;
        Set<Artist> artists = event.getArtists();
        int artistsSize = event.getArtists().size();
        for (Artist artist : artists) {
            artistsFormatted += artist.getName();
            if (i < 3) {
                if (artistsSize >= 3) {
                    artistsFormatted += ", ";
                } else if (i < artistsSize - 1) {
                    artistsFormatted += ", ";
                } else {
                    break;
                }
            }
            if (i == 2) {
                if (artistsSize != 3) {
                    artistsFormatted += "...";
                }
                break;
            }
            i++;
        }

        return DetailedEventDto.DetailedEventDtoBuilder.aDetailedEventDto()
            .withName(event.getName())
            .withType(event.getType())
            .withLength(event.getLength())
            .withDescription(event.getDescription())
            .withArtists(artistsFormatted)
            .build();
    }
}
