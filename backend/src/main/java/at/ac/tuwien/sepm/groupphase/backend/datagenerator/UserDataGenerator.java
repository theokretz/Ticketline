package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Collections;


@Profile("generateData")
@Component
@DependsOn({"locationDataGenerator"})
public class UserDataGenerator {


    public static final int NUMBER_OF_USERS_TO_GENERATE = 20;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String TEST_FIRST_NAME = "First Name";
    private static final String TEST_LAST_NAME = "Last Name";
    private static final String TEST_E_MAIL = "test@mail.com";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_SALT = "salt";
    private static final Integer TEST_POINTS = 50;

    private final PasswordEncoder passwordEncoder;

    private final NotUserRepository notUserRepository;
    private final LocationRepository locationRepository;


    public UserDataGenerator(PasswordEncoder passwordEncoder, NotUserRepository notUserRepository, LocationRepository locationRepository) {
        this.passwordEncoder = passwordEncoder;
        this.notUserRepository = notUserRepository;
        this.locationRepository = locationRepository;
    }

    @PostConstruct
    private void generateUsers() {
        if (notUserRepository.findAll().size() > 0) {
            LOGGER.debug("users already generated");
        } else {
            LOGGER.debug("generating {} user entries", NUMBER_OF_USERS_TO_GENERATE);
            // find first location without user


            for (int i = 1; i < NUMBER_OF_USERS_TO_GENERATE; i++) {
                Location firstWithoutUser = locationRepository.findFirstByUserIdIsNull();
                ApplicationUser user = ApplicationUser.UserBuilder.aUser()
                    .withAdmin(false)
                    .withFirstName(TEST_FIRST_NAME + " " + i)
                    .withLastName(TEST_LAST_NAME + " " + i)
                    .withEmail(i + TEST_E_MAIL)
                    .withPassword(passwordEncoder.encode(TEST_PASSWORD + i))
                    .withPoints(TEST_POINTS + i)
                    .withLocked(false)
                    .withLocations(Collections.singleton(firstWithoutUser))
                    .build();

                LOGGER.debug("saving user {}", user);
                notUserRepository.save(user);
                if (firstWithoutUser != null) {
                    firstWithoutUser.setUser(user);
                    locationRepository.save(firstWithoutUser);
                }
            }
        }

    }

}
