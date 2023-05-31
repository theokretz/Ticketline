package at.ac.tuwien.sepm.groupphase.backend.service;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePaymentDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.h2.security.auth.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.util.List;

public interface UserService extends UserDetailsService {

    /**
     * Find a user in the context of Spring Security based on the email address
     * <br>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param email the email address
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return a application user
     */
    ApplicationUser findApplicationUserByEmail(String email);

    /**
     * Log in a user.
     *
     * @param userLoginDto login credentials
     * @return the JWT, if successful
     * @throws org.springframework.security.authentication.BadCredentialsException if credentials are bad
     */
    String login(UserLoginDto userLoginDto) throws BadCredentialsException, LockedException;

    /**
     * register a user.
     *
     * @param userRegisterDto the user DTO to register.
     * @return "success" if successful
     * @throws ConflictException   if the email is already ued
     * @throws ValidationException if the DTO is not valid
     */
    String registerUser(UserRegisterDto userRegisterDto) throws ConflictException, ValidationException;

    /**
     * isUserAuthenticated makes sure that a requests authentication matches the given user id.
     *
     * @param auth the authentication of the request
     * @return true if the user is authenticated; false otherwise
     */
    boolean isUserAuthenticated(Integer userId, Authentication auth);

    /**
     * getUserLocations returns all locations of a user.
     *
     * @param userId the id of the user
     * @return a list of locations
     */
    List<Location> getUserLocations(Integer userId);

    /**
     * updateUserLocation creates a new location for a user.
     *
     * @param id the id of the user
     * @param locationDto the location to create
     * @return the created location
     * @throws ValidationException if the DTO is not valid
     */
    Location updateUserLocation(Integer id, LocationDto locationDto) throws ValidationException;

    /**
     * deleteUserLocation deletes a location of a user.
     *
     * @param userId the id of the user
     * @param locationId the id of the location to delete
     */
    void deleteUserLocation(Integer userId, Integer locationId);

    /**
     * getUserPaymentDetails returns all payment details of a user.
     *
     * @param userId the id of the user
     * @return a list of payment details
     */
    List<PaymentDetail> getUserPaymentDetails(Integer userId);

    /**
     * updateUserPaymentDetails creates a new payment detail for a user.
     *
     * @param id the id of the user
     * @param paymentDetails the payment detail to create
     * @return the created payment detail
     * @throws ValidationException if the DTO is not valid
     */
    PaymentDetail updateUserPaymentDetails(Integer id, SimplePaymentDetailDto paymentDetails) throws ValidationException;

    /**
     * deleteUserPaymentDetails deletes a payment detail of a user.
     *
     * @param userId the id of the user
     * @param paymentDetailsId the id of the payment detail to delete
     */
    void deleteUserPaymentDetails(Integer userId, Integer paymentDetailsId);
}
