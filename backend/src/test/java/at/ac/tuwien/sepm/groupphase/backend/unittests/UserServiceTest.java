package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserServiceTest {


    @Autowired
    private NotUserRepository repository;
    @Autowired
    private UserService service;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private int emailCounter = 0;
    private char nextFirstNameChar = 'a';
    private char nextLastNameChar = 'A';

    private UserLoginDto notExistingUser;
    private UserRegisterDto shortPasswordWithNoUppercaseWithNoNumber;
    private UserRegisterDto passwordWithNoUppercaseWithNoNumber;
    private UserRegisterDto passwordWithNoUppercase;
    private UserRegisterDto correctPassword;
    private UserRegisterDto noEmail;
    private UserRegisterDto noValues;
    private UserRegisterDto dtoIsNull;
    private UserRegisterDto nameIsNonLetter;
    private UserRegisterDto noPassword;
    private UserRegisterDto emailIsNotValid;
    private UserRegisterDto emailIsNotValid1;
    private UserRegisterDto emailIsNotValid2;
    private UserRegisterDto emailIsNotValid3;
    private UserRegisterDto emailIsNotValid4;
    private UserRegisterDto validEmail;
    private UserRegisterDto validEmail1;
    private UserRegisterDto checkEqualDtoAndEntity;
    private UserRegisterDto newUser;
    private UserLoginDto newUserLogin;
    private UserLoginDto falsePasswordLogin;
    private UserRegisterDto existingEmail;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    private String generateEmail() {
        return String.format("newEmail%d@email.com", emailCounter++);
    }

    private String generateFirstName() {
        if (nextFirstNameChar > 'z') {
            nextFirstNameChar = 'a';
        }
        return String.format("Firstname%c", nextFirstNameChar);
    }

    private String generateLastName() {
        if (nextLastNameChar > 'Z') {
            nextLastNameChar = 'A';
        }
        return String.format("Lastname%c", nextLastNameChar);
    }



    @Test
    void loginAsNotExistingUserTest() {
        assertThrows(UsernameNotFoundException.class, () -> this.service.login(notExistingUser));
    }

    @Test
    void shortPasswordWithNoUppercaseWithNoNumberTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(shortPasswordWithNoUppercaseWithNoNumber));
        String expectedText = "Invalid password format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void passwordWithNoUppercaseWithNoNumberTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(passwordWithNoUppercaseWithNoNumber));
        String expectedText = "Invalid password format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void passwordWithNoUppercaseTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(passwordWithNoUppercase));
        String expectedText = "Invalid password format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void correctPasswordTest() {
        assertDoesNotThrow(() -> this.service.registerUser(correctPassword));
    }

    @Test
    void noEmailTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(noEmail));
        String expectedText = "All fields are required";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void noValuesTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(noValues));
        String expectedText = "All fields are required";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void dtoIsNullTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(dtoIsNull));
        String expectedText = "UserRegisterDto is not valid";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void nameIsNonLetterTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(nameIsNonLetter));
        String expectedText = "Invalid first name format";
        String expectedText1 = "Invalid last name format";
        assertTrue(exception.getMessage().contains(expectedText));
        assertTrue(exception.getMessage().contains(expectedText1));
    }

    @Test
    void noPasswordTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(noPassword));
        String expectedText = "All fields are required";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void emailIsNotValidTest() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(emailIsNotValid));
        String expectedText = "Invalid email format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void emailIsNotValid1Test() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(emailIsNotValid1));
        String expectedText = "Invalid email format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void emailIsNotValid2Test() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(emailIsNotValid2));
        String expectedText = "Invalid email format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void emailIsNotValid3Test() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(emailIsNotValid3));
        String expectedText = "Invalid email format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void emailIsNotValid4Test() {
        ValidationException exception = assertThrows(ValidationException.class, () -> this.service.registerUser(emailIsNotValid4));
        String expectedText = "Invalid email format";
        assertTrue(exception.getMessage().contains(expectedText));
    }

    @Test
    void validEmailTest() {
        assertDoesNotThrow(() -> this.service.registerUser(validEmail));
    }

    @Test
    void validEmail1Test() {
        assertDoesNotThrow(() -> this.service.registerUser(validEmail1));
    }

    @Test
    void checkEqualDtoAndEntityTest() {
        assertDoesNotThrow(() -> this.service.registerUser(checkEqualDtoAndEntity));

        ApplicationUser user = this.service.findApplicationUserByEmail(checkEqualDtoAndEntity.getEmail());
        assertAll(
            () -> assertEquals(checkEqualDtoAndEntity.getEmail(), user.getEmail()),
            () -> assertEquals(checkEqualDtoAndEntity.getFirstName(), user.getFirstName()),
            () -> assertEquals(checkEqualDtoAndEntity.getLastName(), user.getLastName()),
            () -> assertTrue(passwordEncoder.matches(checkEqualDtoAndEntity.getPassword(), user.getPassword())),
            () -> assertNotNull(user.getId()),
            () -> assertFalse(user.getAdmin()),
            () -> assertEquals(0, (int) user.getPoints()),
            () -> assertFalse(user.getLocked())
        );

    }

    @Test
    void registerAndLoginTest() {
        assertDoesNotThrow(() -> this.service.registerUser(newUser));
        assertDoesNotThrow(() -> this.service.login(newUserLogin));
    }

    @Test
    void registerWithExistingEmailTest() {
        assertDoesNotThrow(() -> this.service.registerUser(newUser));
        assertDoesNotThrow(() -> this.service.login(newUserLogin));
        assertThrows(ConflictException.class,() -> this.service.registerUser(existingEmail));
    }



    //------------------------LOCK ACCOUNT------------------------//
    @Test
    void failedLoginUserTest() {
        ApplicationUser user = ApplicationUser.UserBuilder.aUser()
            .withId(99)
            .withAdmin(false)
            .withFirstName("user")
            .withLastName("user")
            .withEmail("user@email.com")
            .withPassword(passwordEncoder.encode("password"))
            .withPoints(0)
            .withLocked(false)
            .withFailedLogin(0)
            .build();
        this.repository.save(user);
        assertThrows(BadCredentialsException.class, () -> this.service.login(
            UserLoginDto.UserLoginDtoBuilder.anUserLoginDto()
                .withEmail(user.getEmail())
                .withPassword("falsePassword123")
                .build()
        ));
        assertEquals(1, this.service.findApplicationUserByEmail(user.getEmail()).getFailedLogin());
    }

    @Test
    void failedLoginAdminTest() {
        ApplicationUser admin = ApplicationUser.UserBuilder.aUser()
            .withId(-99)
            .withAdmin(true)
            .withFirstName("admin")
            .withLastName("admin")
            .withEmail("user@email.com")
            .withPassword(passwordEncoder.encode("password"))
            .withPoints(0)
            .withLocked(false)
            .withFailedLogin(0)
            .build();
        this.repository.save(admin);
        assertThrows(BadCredentialsException.class, () -> this.service.login(
            UserLoginDto.UserLoginDtoBuilder.anUserLoginDto()
                .withEmail(admin.getEmail())
                .withPassword("falsePassword123")
                .build()

        ));
        assertEquals(0, this.service.findApplicationUserByEmail(admin.getEmail()).getFailedLogin());

    }

    @Test
    void increaseFailedLoginTest() {
        assertDoesNotThrow(() -> this.service.registerUser(newUser));
        assertThrows(BadCredentialsException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(1, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(2, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(3, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(4, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        assertThrows(LockedException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(5, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        //more logins shouldn't increase "failedLogin"
        assertThrows(LockedException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(5, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        assertEquals(true, this.service.findApplicationUserByEmail(newUser.getEmail()).getLocked());
        assertThrows(LockedException.class, () -> this.service.login(falsePasswordLogin));
        assertEquals(5, this.service.findApplicationUserByEmail(newUser.getEmail()).getFailedLogin());
        assertEquals(true, this.service.findApplicationUserByEmail(newUser.getEmail()).getLocked());
    }

    @Test
    void DoNotIncreaseFailedLoginForAdminTest() {
        ApplicationUser admin = ApplicationUser.UserBuilder.aUser()
            .withId(-99)
            .withAdmin(true)
            .withFirstName("admin")
            .withLastName("admin")
            .withEmail("user@email.com")
            .withPassword(passwordEncoder.encode("password"))
            .withPoints(0)
            .withLocked(false)
            .withFailedLogin(0)
            .build();
        this.repository.save(admin);

        UserLoginDto adminLogin = UserLoginDto.UserLoginDtoBuilder.anUserLoginDto()
            .withEmail(admin.getEmail())
            .withPassword("falsePassword123")
            .build();


        //admins should never be locked or have failedLogin > 0
        assertThrows(BadCredentialsException.class, () -> this.service.login(adminLogin));
        assertEquals(0, this.service.findApplicationUserByEmail(adminLogin.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(adminLogin));
        assertEquals(0, this.service.findApplicationUserByEmail(adminLogin.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(adminLogin));
        assertEquals(0, this.service.findApplicationUserByEmail(adminLogin.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(adminLogin));
        assertEquals(0, this.service.findApplicationUserByEmail(adminLogin.getEmail()).getFailedLogin());
        assertThrows(BadCredentialsException.class, () -> this.service.login(adminLogin));
        assertEquals(0, this.service.findApplicationUserByEmail(adminLogin.getEmail()).getFailedLogin());
    }


    //------------------------SETUP Method------------------------//
    @BeforeAll
    public void setup() {
        final String CORRECT_PASSWORD = "CorrectPassword123";
        final String FALSE_PASSWORD = "FalsePassword123";

        notExistingUser = new UserLoginDto();
        notExistingUser.setEmail("test@email.com");
        notExistingUser.setPassword(CORRECT_PASSWORD);

        // CHECK PASSWORD
        shortPasswordWithNoUppercaseWithNoNumber = new UserRegisterDto();
        shortPasswordWithNoUppercaseWithNoNumber.setEmail(generateEmail());
        shortPasswordWithNoUppercaseWithNoNumber.setFirstName("Hermann");
        shortPasswordWithNoUppercaseWithNoNumber.setLastName("Stoss");
        shortPasswordWithNoUppercaseWithNoNumber.setPassword("weak");

        passwordWithNoUppercaseWithNoNumber = new UserRegisterDto();
        passwordWithNoUppercaseWithNoNumber.setEmail(generateEmail());
        passwordWithNoUppercaseWithNoNumber.setFirstName("Lucca");
        passwordWithNoUppercaseWithNoNumber.setLastName("Dukic");
        passwordWithNoUppercaseWithNoNumber.setPassword("weakpassword");

        passwordWithNoUppercase = new UserRegisterDto();
        passwordWithNoUppercase.setEmail(generateEmail());
        passwordWithNoUppercase.setFirstName("Diyar");
        passwordWithNoUppercase.setLastName("Turan");
        passwordWithNoUppercase.setPassword("weakpassword123");

        correctPassword = new UserRegisterDto();
        correctPassword.setEmail(generateEmail());
        correctPassword.setFirstName("Theo");
        correctPassword.setLastName("Kretz");
        correctPassword.setPassword(CORRECT_PASSWORD);

        // CHECK MANDATORY VALUES -> Validation Check
        noEmail = new UserRegisterDto();
        noEmail.setEmail("");
        noEmail.setFirstName("Vanesa");
        noEmail.setLastName("Basheva");
        noEmail.setPassword(CORRECT_PASSWORD);

        noValues = new UserRegisterDto();
        noValues.setEmail("");
        noValues.setFirstName("");
        noValues.setLastName("");
        noValues.setPassword("");

        dtoIsNull = null;

        nameIsNonLetter = new UserRegisterDto();
        nameIsNonLetter.setEmail(generateEmail());
        nameIsNonLetter.setFirstName("123");
        nameIsNonLetter.setLastName("123");
        nameIsNonLetter.setPassword(CORRECT_PASSWORD);

        noPassword = new UserRegisterDto();
        noPassword.setEmail(generateEmail());
        noPassword.setFirstName(generateFirstName());
        noPassword.setLastName(generateLastName());
        noPassword.setPassword("");

        //CHECK EMAIL
        emailIsNotValid = new UserRegisterDto();
        emailIsNotValid.setEmail("email");
        emailIsNotValid.setFirstName(generateFirstName());
        emailIsNotValid.setLastName(generateLastName());
        emailIsNotValid.setPassword(CORRECT_PASSWORD);

        emailIsNotValid1 = new UserRegisterDto();
        emailIsNotValid1.setEmail("email@");
        emailIsNotValid1.setFirstName(generateFirstName());
        emailIsNotValid1.setLastName(generateLastName());
        emailIsNotValid1.setPassword(CORRECT_PASSWORD);

        emailIsNotValid2 = new UserRegisterDto();
        emailIsNotValid2.setEmail("email@test");
        emailIsNotValid2.setFirstName(generateFirstName());
        emailIsNotValid2.setLastName(generateLastName());
        emailIsNotValid2.setPassword(CORRECT_PASSWORD);

        emailIsNotValid3 = new UserRegisterDto();
        emailIsNotValid3.setEmail("email@test.");
        emailIsNotValid3.setFirstName(generateFirstName());
        emailIsNotValid3.setLastName(generateLastName());
        emailIsNotValid3.setPassword(CORRECT_PASSWORD);

        emailIsNotValid4 = new UserRegisterDto();
        emailIsNotValid4.setEmail("email@&*&%*&.%#%^");
        emailIsNotValid4.setFirstName(generateFirstName());
        emailIsNotValid4.setLastName(generateLastName());
        emailIsNotValid4.setPassword(CORRECT_PASSWORD);

        validEmail = new UserRegisterDto();
        validEmail.setEmail("hermann.stoss@tuwien.ac.at");
        validEmail.setFirstName(generateFirstName());
        validEmail.setLastName(generateLastName());
        validEmail.setPassword(CORRECT_PASSWORD);

        validEmail1 = new UserRegisterDto();
        validEmail1.setEmail("e12122539@student.tuwien.ac.at");
        validEmail1.setFirstName(generateFirstName());
        validEmail1.setLastName(generateLastName());
        validEmail1.setPassword(CORRECT_PASSWORD);

        //CHECK IF DTO AND ENTITY ARE EQUAL

        checkEqualDtoAndEntity = new UserRegisterDto();
        checkEqualDtoAndEntity.setEmail(generateEmail());
        checkEqualDtoAndEntity.setFirstName(generateFirstName());
        checkEqualDtoAndEntity.setLastName(generateLastName());
        checkEqualDtoAndEntity.setPassword(CORRECT_PASSWORD);

        //CHECK IF YOU CAN LOGIN WITH NEW CREATED USER
        newUser = new UserRegisterDto();
        newUser.setEmail(checkEqualDtoAndEntity.getEmail());
        newUser.setFirstName(generateFirstName());
        newUser.setLastName(generateLastName());
        newUser.setPassword(CORRECT_PASSWORD);

        newUserLogin = new UserLoginDto();
        newUserLogin.setEmail(newUser.getEmail());
        newUserLogin.setPassword(newUser.getPassword());

        //LOGIN EXISTING USER WITH FALSE PASSWORD

        existingEmail = new UserRegisterDto();
        existingEmail.setEmail(newUser.getEmail());
        existingEmail.setFirstName(generateFirstName());
        existingEmail.setLastName(generateLastName());
        existingEmail.setPassword(CORRECT_PASSWORD);

        falsePasswordLogin = new UserLoginDto();
        falsePasswordLogin.setEmail(newUser.getEmail());
        falsePasswordLogin.setPassword(FALSE_PASSWORD);

    }

}
