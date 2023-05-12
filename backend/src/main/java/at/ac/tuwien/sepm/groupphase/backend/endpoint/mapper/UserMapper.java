package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDto applicationUserToDto(ApplicationUser user);
}
