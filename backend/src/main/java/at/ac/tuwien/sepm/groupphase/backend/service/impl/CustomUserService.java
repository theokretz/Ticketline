package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;

    private final NotUserRepository notUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final int maxFailedLogin = 5;

    @Autowired
    public CustomUserService(UserRepository userRepository, NotUserRepository notUserRepository, PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer) {
        this.userRepository = userRepository;
        this.notUserRepository = notUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
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
        //TODO only for testing. REMOVE IN PRODUCTION
        if (email.equals("user@email.com") || email.equals("admin@email.com")) {
            ApplicationUser user = userRepository.findUserByEmail(email);
            applicationUser = Optional.of(user);
        } else {
            applicationUser = notUserRepository.getApplicationUsersByEmail(email);
        }
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
            System.out.printf("roles: %s%n", Arrays.toString(roles.toArray()));

            if (!passwordEncoder.matches(userLoginDto.getPassword(), userDetails.getPassword())) {
                if (!roles.contains("ROLE_ADMIN")) {
                    System.out.println("user is not admin");
                    this.notUserRepository.getApplicationUsersByEmail(userLoginDto.getEmail()).ifPresent(user -> {
                        user.setFailedLogin(user.getFailedLogin() + 1);
                        this.notUserRepository.save(user);
                        System.out.printf("failed login: %d%n", user.getFailedLogin());
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
        if (!(userRepository.findUserByEmail(userRegisterDto.getEmail()) == null)) {
            error.add(String.format("User with email: {%s} already exists", userRegisterDto.getEmail()));
            throw new ConflictException("Email Conflict", error);
        }
        ApplicationUser saved;
        //if save() throws an error then something dramatic happened
        saved = notUserRepository.save(applicationUser);
        //TODO remove sout and toString() in ApplicationUser  in production
        System.out.println(saved);
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
}
