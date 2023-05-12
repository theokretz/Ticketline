package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedPerformanceSectorDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface SectorMapper {

    //@Mapping(source = "sector.id", target = "id")
    @Mapping(source = "sector.sector.name", target = "name")
    @Mapping(source = "sector.sector.standing", target = "standing")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "pointsReward", target = "pointsReward")
    DetailedPerformanceSectorDto performanceSectorToDetailedPerformanceSectorDto(PerformanceSector sector);

    DetailedPerformanceSectorDto[] performanceSectorSetToDetailedPerformanceSectorDtoArray(Set<PerformanceSector> sector);

    default Map<Integer, DetailedPerformanceSectorDto> performanceSectorSetToDetailedPerformanceSectorDtoMap(Set<PerformanceSector> performanceSectors) {
        if (performanceSectors == null) {
            return null;
        }
        //return sectors.stream()
        //     .map(this::performanceSectorToDetailedPerformanceSectorDto)
        //    .collect(Collectors.toMap(DetailedPerformanceSectorDto::getId, Function.identity()))
        return performanceSectors.stream()
            .collect(Collectors.toMap(performanceSector -> performanceSector.getSector().getId(),
                this::performanceSectorToDetailedPerformanceSectorDto));
    }
}