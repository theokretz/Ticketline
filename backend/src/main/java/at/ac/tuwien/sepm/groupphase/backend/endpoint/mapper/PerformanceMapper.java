package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedPerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedPerformanceSectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class PerformanceMapper {
    @Autowired
    protected LocationMapper locationMapper;
    @Autowired
    protected SectorMapper sectorMapper;
    @Autowired
    protected SeatMapper seatMapper;

    public DetailedPerformanceDto performanceToDetailedPerformanceDto(Performance performance) {
        if (performance == null) {
            return null;
        }
        Hall hall = performance.getHall();
        if (hall == null) {
            return null;
        }
        Location location = hall.getLocation();
        if (location == null) {
            return null;
        }
        if (performance.getEvent() == null) {
            return null;
        }

        LocationDto locationDto = locationMapper.locationToLocationDto(location);
        Map<Integer, DetailedPerformanceSectorDto> detailedPerformanceSectorDto =
            sectorMapper.performanceSectorSetToDetailedPerformanceSectorDtoMap(performance.getPerformanceSectors());

        PerformanceTicketDto[][] performanceTickets = seatMapper.performanceToReservedSeat(performance);

        return DetailedPerformanceDto.DetailedPerformanceDtoBuilder.aDetailedPerformanceDto()
            .withTimestamp(performance.getDatetime())
            .withEventName(performance.getEvent().getName())
            .withHallName(hall.getName())
            .withLocation(locationDto)
            .withPerformanceSector(detailedPerformanceSectorDto)
            .withPerformanceTickets(performanceTickets)
            .build();
    }
}


