package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedHallDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import org.mapstruct.Mapper;

@Mapper
public interface HallMapper {

    DetailedHallDto hallToDetailedHallDto(Hall hall);
}
