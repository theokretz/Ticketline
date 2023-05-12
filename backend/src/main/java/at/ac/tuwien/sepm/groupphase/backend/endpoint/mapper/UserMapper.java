package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.Mapper;

/**
 * The interface User mapper.
 */
@Mapper
public interface UserMapper {
    /**
     * Application user to dto user dto.
     *
     * @param user the user
     * @return the user dto
     */
    UserDto applicationUserToDto(ApplicationUser user);
}
