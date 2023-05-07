package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public ApplicationUser dtoToApplicationUser(UserDto userDto) {
        ApplicationUser user = new ApplicationUser();
        user.setId(userDto.getId());
        return user;
    }

    public UserDto applicationUserToDto(ApplicationUser user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setAdmin(user.getAdmin());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setLocations(user.getLocations());
        dto.setPassword(user.getPassword());
        dto.setPoints(user.getPoints());
        dto.setLocked(user.getLocked());
        dto.setSalt(user.getSalt());

        if (user.getOrders() != null) {
            dto.setOrders(user.getOrders());
        }
        if (user.getReservations() != null) {
            dto.setReservations(user.getReservations());
        }
        if (user.getPaymentDetails() != null) {
            dto.setPaymentDetails(user.getPaymentDetails());
        }
        return dto;
    }
}
