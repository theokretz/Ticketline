package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePaymentDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PaymentDetailRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.h2.security.auth.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final NotUserRepository notUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final int maxFailedLogin = 5;

    private final LocationRepository locationRepository;
    private final PaymentDetailRepository paymentDetailRepository;

    @Autowired
    public CustomUserService(NotUserRepository notUserRepository, PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer,
                             LocationRepository locationRepository, PaymentDetailRepository paymentDetailRepository) {
        this.notUserRepository = notUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
        this.paymentDetailRepository = paymentDetailRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * return the UserDetails of a given user via email address.
     *
     * @param email the email address
     * @return the UserDetails of the user
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load all user by email");

        try {
            ApplicationUser applicationUser = findApplicationUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities;
            if (applicationUser.getAdmin()) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
            } else {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            }
            return User.builder().username(applicationUser.getEmail())
                .password(applicationUser.getPassword())
                .authorities(grantedAuthorities)
                .accountLocked(applicationUser.getLocked()).build();
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    /**
     * returns the corresponding ApplicationUser to the given email address.
     *
     * @param email the email address .
     * @return the corresponding ApplicationUser.
     */
    @Override
    public ApplicationUser findApplicationUserByEmail(String email) {
        LOGGER.debug("Find application user by email");
        Optional<ApplicationUser> applicationUser;
        applicationUser = notUserRepository.findApplicationUsersByEmail(email);
        if (applicationUser.isPresent()) {
            return applicationUser.get();
        }
        throw new NotFoundException(String.format("Could not find the user with the email address %s", email));
    }

    /**
     * login user  with email and password.
     *
     * @param userLoginDto login credentials of the user
     * @return a jwt token if the login was successful
     */
    @Override
    public String login(UserLoginDto userLoginDto) throws BadCredentialsException, LockedException {

        UserDetails userDetails = loadUserByUsername(userLoginDto.getEmail());
        if (userDetails != null
            && userDetails.isAccountNonExpired()
            && userDetails.isAccountNonLocked()
            && userDetails.isCredentialsNonExpired()
        ) {
            List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
            LOGGER.info("roles: %s%n", Arrays.toString(roles.toArray()));

            if (!passwordEncoder.matches(userLoginDto.getPassword(), userDetails.getPassword())) {
                if (!roles.contains("ROLE_ADMIN")) {
                    LOGGER.info("user is not admin");
                    this.notUserRepository.findApplicationUsersByEmail(userLoginDto.getEmail()).ifPresent(user -> {
                        user.setFailedLogin(user.getFailedLogin() + 1);
                        this.notUserRepository.save(user);
                        LOGGER.info("failed login: %d%n", user.getFailedLogin());
                        if (user.getFailedLogin() >= maxFailedLogin) {
                            user.setLocked(true);
                            this.notUserRepository.save(user);
                            throw new LockedException(String.format("User {%s} is locked", user.getEmail()));
                        }

                    });
                }
                throw new BadCredentialsException("Username or password is incorrect");
            } else {

                return jwtTokenizer.getAuthToken(userDetails.getUsername(), roles);
            }
        }
        throw new LockedException(String.format("User {%s} is locked", userLoginDto.getEmail()));
    }

    /**
     * Register a new user.
     *
     * @param userRegisterDto user information for registration
     * @return success if the registration was successful
     */

    public String registerUser(UserRegisterDto userRegisterDto) throws ConflictException, ValidationException {
        List<String> error = new ArrayList<>();
        //check if userRegisterDto is valid and not null
        //validation happens in DTO with isValid() -> return List<String> with errors
        if (userRegisterDto == null || (error = userRegisterDto.validate()).size() > 0) {
            throw new ValidationException("UserRegisterDto is not valid", error);
        }
        LOGGER.trace("registerUser({})", userRegisterDto);

        //id is auto generated
        ApplicationUser applicationUser = ApplicationUser.UserBuilder.aUser()
            .withAdmin(false)
            .withEmail(userRegisterDto.getEmail())
            .withFirstName(userRegisterDto.getFirstName())
            .withLastName(userRegisterDto.getLastName())
            .withPassword(passwordEncoder.encode(userRegisterDto.getPassword()))
            .withPoints(0)
            .withLocked(false)
            .withFailedLogin(0)
            .build();

        //check if user with same email already exists
        if (notUserRepository.findApplicationUsersByEmail(userRegisterDto.getEmail()).isPresent()) {
            error.add(String.format("User with email: {%s} already exists", userRegisterDto.getEmail()));
            throw new ConflictException("Email Conflict", error);
        }
        ApplicationUser saved;
        //if save() throws an error then something dramatic happened
        saved = notUserRepository.save(applicationUser);
        //TODO remove sout and toString() in ApplicationUser  in production
        LOGGER.info("{}", saved);
        //check if entity is the same as DTO (except password)
        if (saved.getEmail().equals(userRegisterDto.getEmail())
            && saved.getFirstName().equals(userRegisterDto.getFirstName())
            && saved.getLastName().equals(userRegisterDto.getLastName())) {
            LOGGER.trace(String.format("successful registration of user: %s", saved));
            return "success";
        } else {

            //should not happen
            throw new FatalException(
                String.format(
                    "Saved Entity does not match UserRegisterDto - Entity:{email: %s, firstName: %s, lastName: %s}, UserRegisterDto: %s",
                    saved.getEmail(),
                    saved.getFirstName(),
                    saved.getLastName(),
                    userRegisterDto));
        }
    }

    @Override
    public boolean isUserAuthenticated(Integer userId, Authentication auth) {
        LOGGER.trace("isUserAuthenticated({},{})", userId, auth);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        return user.getId().equals(auth.getPrincipal());
    }

    @Override
    public List<Location> getUserLocations(Integer userId) {
        LOGGER.trace("getUserLocations({})", userId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        return locationRepository.findAllByUserId(userId);
    }

    @Override
    public Location updateUserLocation(Integer id, LocationDto locationDto) throws ValidationException {
        LOGGER.trace("updateUserLocation({},{})", id, locationDto);
        List<String> error = new ArrayList<>();
        if (locationDto == null || (error = locationDto.validate()).size() > 0) {
            throw new ValidationException("LocationDto is not valid", error);
        }

        ApplicationUser user = notUserRepository.findApplicationUserById(id);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }

        Location location = Location.LocationBuilder.aLocation()
            .withUser(user)
            .withPostalCode(locationDto.getPostalCode())
            .withCity(locationDto.getCity())
            .withCountry(locationDto.getCountry())
            .withStreet(locationDto.getStreet())
            .build();
        return this.locationRepository.save(location);
    }

    @Override
    public void deleteUserLocation(Integer userId, Integer locationId) {
        LOGGER.trace("deleteUserLocation({},{})", userId, locationId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        Location location = locationRepository.findLocationById(locationId);
        if (location == null) {
            throw new NotFoundException("Could not find Location");
        }
        locationRepository.delete(location);
    }


    @Override
    public List<PaymentDetail> getUserPaymentDetails(Integer userId) {
        LOGGER.trace("getUserPaymentDetails({})", userId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        return paymentDetailRepository.findPaymentDetailsByUserId(userId);
    }

    @Override
    public PaymentDetail updateUserPaymentDetails(Integer id, SimplePaymentDetailDto paymentDetails) throws ValidationException {
        LOGGER.trace("updateUserPaymentDetails({},{})", id, paymentDetails);
        ApplicationUser user = notUserRepository.findApplicationUserById(id);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        List<String> error = new ArrayList<>();
        if (paymentDetails == null || (error = paymentDetails.validate()).size() > 0) {
            throw new ValidationException("PaymentDetailsDto is not valid", error);
        }
        PaymentDetail paymentDetail = PaymentDetail.PaymentDetailBuilder.aPaymentDetail()
            .withUser(user)
            .withCardNumber(paymentDetails.getCardNumber())
            .withCardHolder(paymentDetails.getCardHolder())
            .withExpirationDate(paymentDetails.getExpirationDate())
            .withCvv(paymentDetails.getCvv())
            .build();

        PaymentDetail saved = this.paymentDetailRepository.save(paymentDetail);
        Set<PaymentDetail> userPaymentDetails = user.getPaymentDetails();
        userPaymentDetails.add(saved);
        user.setPaymentDetails(userPaymentDetails);
        return saved;
    }

    @Override
    public void deleteUserPaymentDetails(Integer userId, Integer paymentDetailsId) {
        LOGGER.trace("deleteUserPaymentDetails({},{})", userId, paymentDetailsId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }

        Optional<PaymentDetail> paymentDetail = paymentDetailRepository.findPaymentDetailById(paymentDetailsId);
        if (paymentDetail.isEmpty()) {
            throw new NotFoundException("Could not find PaymentDetail");
        }
        paymentDetailRepository.delete(paymentDetail.get());
    }
}
