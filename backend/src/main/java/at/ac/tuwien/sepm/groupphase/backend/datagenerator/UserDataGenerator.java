package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class UserDataGenerator {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final int NUMBER_OF_USERS_TO_GENERATE = 100;
    private static final String TEST_FIRST_NAME = "First Name";
    private static final String TEST_LAST_NAME = "Last Name";
    private static final String TEST_E_MAIL = "test@mail.com";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_SALT = "salt";
    private static final Integer TEST_POINTS = 50;

    private final NotUserRepository notUserRepository;


    public UserDataGenerator(NotUserRepository notUserRepository) {
        this.notUserRepository = notUserRepository;
    }

    @PostConstruct
    private void generateUsers() {
        if (notUserRepository.findAll().size() > 0) {
            LOGGER.debug("users already generated");
        } else {
            LOGGER.debug("generating {} user entries", NUMBER_OF_USERS_TO_GENERATE);
            for (int i = 1; i < NUMBER_OF_USERS_TO_GENERATE; i++) {
                ApplicationUser user = ApplicationUser.UserBuilder.aUser()
                    .withAdmin(false)
                    .withFirstName(TEST_FIRST_NAME + " " + i)
                    .withLastName(TEST_LAST_NAME + " " + i)
                    .withEmail(i + TEST_E_MAIL)
                    .withPassword(TEST_PASSWORD + i)
                    .withSalt(TEST_SALT + i)
                    .withPoints(TEST_POINTS + i)
                    .withLocked(false)
                    .build();

                LOGGER.debug("saving user {}", user);
                notUserRepository.save(user);
            }
        }

    }

}
