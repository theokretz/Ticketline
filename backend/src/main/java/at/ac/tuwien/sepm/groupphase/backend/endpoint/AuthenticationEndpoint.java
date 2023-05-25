package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1")
public class AuthenticationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public AuthenticationEndpoint(UserService userService) {
        this.userService = userService;
    }


    /**
     * This method is used to login a user.
     *
     * @param userLoginDto containing the email and password of the user
     * @return the JWT token if the user was successfully logged in
     */
    @PermitAll
    @PostMapping("/authentication")
    public String login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    /**
     * This method is used to register a user.
     *
     * @param userRegisterDto containing the email and password of the user
     * @return "success" if the user was successfully registered
     */
    @PermitAll
    @PostMapping("/users")
    public String registerUser(@RequestBody UserRegisterDto userRegisterDto) {
        LOGGER.info(String.format("POST /api/v1/users user: %s", userRegisterDto.toString()));
        try {
            return userService.registerUser(userRegisterDto);
        } catch (ConflictException e) {
            LOGGER.info("Conflict with DB: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.info("Request was formatted incorrectly:" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }


}
