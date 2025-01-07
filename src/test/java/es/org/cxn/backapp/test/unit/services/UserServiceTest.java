
package es.org.cxn.backapp.test.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountrySubdivisionEntity;
import es.org.cxn.backapp.model.persistence.PersistentProfileImageEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.model.persistence.user.UserType;
import es.org.cxn.backapp.repository.CountryEntityRepository;
import es.org.cxn.backapp.repository.CountrySubdivisionEntityRepository;
import es.org.cxn.backapp.repository.ImageProfileEntityRepository;
import es.org.cxn.backapp.repository.RoleEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.EmailService;
import es.org.cxn.backapp.service.PaymentsService;
import es.org.cxn.backapp.service.RoleService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.AddressRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserServiceUpdateDto;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import es.org.cxn.backapp.service.impl.DefaultImageStorageService;
import es.org.cxn.backapp.service.impl.DefaultUserService;

/**
 * Unit test class for {@link DefaultUserService}.
 *
 * This class contains tests to validate the behavior of the
 * {@link DefaultUserService} service. It ensures that user-related operations
 * are performed correctly, including handling roles, countries, and user
 * management functionalities.
 *
 * <p>
 * Tests include:
 * </p>
 * <ul>
 * <li>Interactions with user, role, country, and country subdivision
 * repositories.</li>
 * <li>Functionality related to user creation, updating, and validation.</li>
 * </ul>
 *
 * <p>
 * All tests in this class use mocks to simulate the repository layer.
 * </p>
 *
 * @author Santi
 */
class UserServiceTest {

    /**
     * Mock for {@link UserEntityRepository}.
     *
     * This mock simulates interactions with the user repository, allowing tests to
     * control and verify how the service interacts with user data.
     */
    @Mock
    private UserEntityRepository userRepository;

    /**
     * Mock for {@link RoleEntityRepository}.
     *
     * This mock simulates interactions with the role repository, allowing tests to
     * manage and verify role-related operations without accessing a real database.
     */
    @Mock
    private RoleEntityRepository roleRepository;

    /**
     * Mock for {@link CountrySubdivisionEntityRepository}.
     *
     * This mock simulates interactions with the country subdivision repository,
     * allowing tests to verify how the service handles subdivisions without
     * interacting with a real database.
     */
    @Mock
    private CountryEntityRepository countryRepository;

    /**
     * Repository for managing CRUD operations for image profiles.
     */
    @Mock
    private ImageProfileEntityRepository imageProfileRepository;

    /**
     * Repository for managing CRUD operations for country subdivisions.
     */
    @Mock
    private CountrySubdivisionEntityRepository countrySubdivisionRepository;

    /**
     * Service for handling image storage operations, such as saving, loading, and
     * deleting images.
     */
    @Mock
    private DefaultImageStorageService imageStorageService;

    /**
     * The email service used by user service.
     */
    @Mock
    private EmailService emailService;

    /**
     * The role service used by user service.
     */
    @Mock
    private RoleService roleService;

    /**
     * The payments service used by user service.
     */
    @Mock
    private PaymentsService paymentsService;

    /**
     * Service instance being tested.
     *
     * This instance is created with the mocked repositories and contains the
     * business logic for user management that is subject to testing.
     */
    @InjectMocks
    private DefaultUserService userService;

    /**
     * A persistent user entity used in tests.
     *
     * This entity is initialized with test data to simulate a real user for testing
     * user-related operations.
     */
    private PersistentUserEntity persistentUserEntity;

    /**
     * The user profile image.
     */
    private PersistentProfileImageEntity profileImageEntity;

    /**
     * Initializes the test environment before each test is run.
     *
     * This method sets up the mocks and initializes the user entity with test data
     * to ensure each test starts with a clean and consistent state.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the PersistentUserEntity with necessary fields
        persistentUserEntity = new PersistentUserEntity();
        persistentUserEntity.setEmail("test@example.com");
        persistentUserEntity.setPassword(new BCryptPasswordEncoder().encode("password123"));
        persistentUserEntity.setDni("123456789");
        UserProfile userProfile = new UserProfile();
        userProfile.setName("UserName");
        userProfile.setFirstSurname("First name");
        userProfile.setSecondSurname("SecondSurname");
        userProfile.setGender("Male");
        userProfile.setBirthDate(LocalDate.of(1991, 5, 5));
        persistentUserEntity.setProfile(userProfile);
        // Initialize the profile image entity with a placeholder URL
        profileImageEntity = new PersistentProfileImageEntity();
        profileImageEntity.setUrl("path/to/upload/123456789.jpg"); // Placeholder URL
        profileImageEntity.setUserDni("123456789");

        // Set the profile image to the user entity
        persistentUserEntity.setProfileImage(profileImageEntity);
    }

    /**
     * Tests the behavior of the
     * {@link DefaultUserService#add(UserRegistrationDetailsDto)} method when the
     * country associated with the provided numeric code does not exist in the
     * repository.
     *
     * <p>
     * This test verifies that when a user tries to register with a country code
     * that is not found, the service correctly throws a
     * {@link UserServiceException} with the appropriate error message. It also
     * ensures that no user is added to the repository in this scenario.
     * </p>
     *
     * <p>
     * Specifically, the test:
     * </p>
     * <ul>
     * <li>Sets up the {@link UserRegistrationDetailsDto} with test data including a
     * non-existent country code.</li>
     * <li>Configures the mock repository to simulate the absence of the country in
     * the {@link CountryEntityRepository}.</li>
     * <li>Asserts that the {@link UserServiceException} is thrown with the expected
     * message.</li>
     * <li>Verifies that no interaction with the user repository occurs.</li>
     * </ul>
     *
     * @throws UserServiceException if the country with the given code is not found.
     */
    @Test
    void testAddUserCountryNotFound() {
        // Declare constants
        final var testDni = "12345678A";
        final var testEmail = "test@example.com";
        final var testPassword = "password";
        final var testName = "John";
        final var testFirstSurname = "Doe";
        final var testSecondSurname = "Smith";
        final var testGender = "Male";
        final var testBirthDate = LocalDate.of(1990, 1, 1);
        final var testApartmentNumber = "1";
        final var testBuilding = "Building";
        final var testCity = "City";
        final var testPostalCode = "PostalCode";
        final var testStreet = "Street";
        final var testCountryNumericCode = 999;
        final var testCountrySubdivisionName = "Subdivision";
        final var expectedExceptionMessage = "Country with code: " + testCountryNumericCode + " not found.";

        // Create user details
        var addressDetails = new AddressRegistrationDetailsDto(testApartmentNumber, testBuilding, testCity,
                testPostalCode, testStreet, testCountryNumericCode, testCountrySubdivisionName);
        var userDetails = new UserRegistrationDetailsDto(testDni, testName, testFirstSurname, testSecondSurname,
                testBirthDate, testGender, testPassword, testEmail, addressDetails, UserType.SOCIO_NUMERO);

        // Configure mock behavior for countryRepository
        when(countryRepository.findById(testCountryNumericCode)).thenReturn(Optional.empty());

        // Execute the method and verify that the expected exception is thrown
        var exception = assertThrows(UserServiceException.class, () -> {
            userService.add(userDetails);
        }, "Expected UserServiceException to be thrown when the country " + "with code " + testCountryNumericCode
                + " is not found");

        // Verify that the exception message is as expected
        Assertions.assertEquals(expectedExceptionMessage, exception.getMessage(),
                "The exception message should match the expected message " + "for country not found");
    }

    /**
     * Tests the behavior of the
     * {@link DefaultUserService#add(UserRegistrationDetailsDto)} method when the
     * country subdivision associated with the provided name does not exist in the
     * repository.
     *
     * <p>
     * This test verifies that when a user tries to register with a country
     * subdivision name that does not exist, the service correctly throws a
     * {@link UserServiceException} with the appropriate error message. It also
     * ensures that no user is added to the repository in this scenario.
     * </p>
     *
     * <p>
     * Specifically, the test:
     * </p>
     * <ul>
     * <li>Sets up the {@link UserRegistrationDetailsDto} with test data, including
     * a non-existent country subdivision name.</li>
     * <li>Configures the mock repository to simulate the existence of the country
     * but the absence of the country subdivision in the
     * {@link CountrySubdivisionEntityRepository}.</li>
     * <li>Asserts that a {@link UserServiceException} is thrown with the expected
     * message when the country subdivision is not found.</li>
     * <li>Optionally, verifies that the exception message matches the expected
     * message for country subdivision not found.</li>
     * </ul>
     *
     * @throws UserServiceException if the country subdivision with the given name
     *                              is not found.
     */
    @Test
    void testAddUserCountrySubdivisionNotFound() {
        // Declare constants
        final var testDni = "12345678A";
        final var testEmail = "test@example.com";
        final var testPassword = "password";
        final var testName = "John";
        final var testFirstSurname = "Doe";
        final var testSecondSurname = "Smith";
        final var testGender = "Male";
        final var testBirthDate = LocalDate.of(1990, 1, 1);
        final var testApartmentNumber = "1";
        final var testBuilding = "Building";
        final var testCity = "City";
        final var testPostalCode = "PostalCode";
        final var testStreet = "Street";
        final var testCountryNumericCode = 999;
        final var testCountrySubdivisionName = "NonExistentSubdivision";
        final var expectedExceptionMessage = "Country subdivision with code: " + testCountrySubdivisionName
                + " not found.";

        // Create user details
        var addressDetails = new AddressRegistrationDetailsDto(testApartmentNumber, testBuilding, testCity,
                testPostalCode, testStreet, testCountryNumericCode, testCountrySubdivisionName);
        var userDetails = new UserRegistrationDetailsDto(testDni, testName, testFirstSurname, testSecondSurname,
                testBirthDate, testGender, testPassword, testEmail, addressDetails, UserType.SOCIO_NUMERO);

        // Configure mock behavior for countryRepository
        when(countryRepository.findById(anyInt())).thenReturn(Optional.of(new PersistentCountryEntity()));

        // Configure mock behavior for countrySubdivisionRepository
        when(countrySubdivisionRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Execute the method and verify that the expected exception is thrown
        var thrownException = assertThrows(UserServiceException.class, () -> {
            userService.add(userDetails);
        }, "Expected add to throw UserServiceException when country " + "subdivision is not found");

        // Verify that the exception message is as expected
        assertEquals(expectedExceptionMessage, thrownException.getMessage(),
                "The exception message should indicate that the country" + " subdivision was not found");
    }

    /**
     * Tests the {@link UserService#add(UserRegistrationDetailsDto)} method to
     * ensure that a {@link UserServiceException} is thrown when attempting to add a
     * user with a DNI that already exists in the database.
     *
     * <p>
     * This test simulates a user registration where the DNI already exists in the
     * repository. It verifies that the appropriate exception is thrown with a
     * relevant error message.
     * </p>
     *
     * <p>
     * Steps:
     * </p>
     * <ul>
     * <li>Arrange: Set up user details and mock the repository behavior to return
     * an existing user when searched by DNI.</li>
     * <li>Act & Assert: Call {@code userService.add(userDetails)} and assert that
     * {@link UserServiceException} is thrown with the expected message.</li>
     * </ul>
     *
     * @throws UserServiceException if the user service throws an exception when
     *                              adding a user with an existing DNI
     */
    @Test
    void testAddUserDniExists() {
        // Declare constants
        final var existingDni = "123456789";
        final var email = "test@example.com";
        final var password = "password123";
        final var name = "John";
        final var firstSurname = "Doe";
        final var secondSurname = "Smith";
        final var birthDate = LocalDate.of(2000, 1, 1);
        final var gender = "M";
        final var apartmentNumber = "Apt 1";
        final var building = "Building A";
        final var city = "City";
        final var postalCode = "12345";
        final var street = "Street";
        final var countryNumericCode = 1;
        final var countrySubdivisionName = "Subdivision";

        // Arrange: Configurar los detalles del usuario y
        // el comportamiento simulado del repositorio
        // Create an AddressRegistrationDetailsDto object
        var addressDetails = new AddressRegistrationDetailsDto(apartmentNumber, building, city, postalCode, street,
                countryNumericCode, countrySubdivisionName);
        var userDetails = new UserRegistrationDetailsDto(existingDni, name, firstSurname, secondSurname, birthDate,
                gender, password, email, addressDetails, UserType.SOCIO_NUMERO);

        // Simular que el DNI ya existe en la base de datos
        when(userRepository.findByDni(existingDni)).thenReturn(Optional.of(persistentUserEntity));

        // Act & Assert: Verificar que se lanza una excepción cuando
        // se intenta agregar un usuario con un DNI existente
        var thrownException = assertThrows(UserServiceException.class, () -> userService.add(userDetails),
                "Expected add to throw UserServiceException when DNI already exists");

        // Opcional: Verificar el mensaje de la excepción, si es relevante
        assertEquals("User dni already exists.", thrownException.getMessage(),
                "The exception message should be 'User with this DNI already "
                        + "exists' when trying to add a user with an existing DNI");
    }

    /**
     * Tests the {@link UserService#add(UserRegistrationDetailsDto)} method to
     * ensure that a new user is successfully added to the system when the provided
     * DNI and email do not already exist in the database.
     *
     * <p>
     * This test case verifies the successful addition of a user by simulating the
     * creation of a {@link UserRegistrationDetailsDto} with complete user details,
     * including address and personal information. It mocks dependencies to ensure
     * that neither the DNI nor the email are already present in the repository.
     * </p>
     *
     * <p>
     * Steps:
     * </p>
     * <ul>
     * <li>Arrange: Set up constants for user details, create DTO objects for user
     * registration, and mock repository responses.</li>
     * <li>Act: Call {@code userService.add(userDetails)} with valid data.</li>
     * <li>Assert: Verify that the user is saved successfully and that
     * {@link UserService#add(UserRegistrationDetailsDto)} returns the persisted
     * user entity.</li>
     * </ul>
     *
     * @throws UserServiceException if there is an issue adding the user
     */
    @Test
    void testAddUserSuccess() throws UserServiceException {
        // Declare constants
        final var dni = "123456789";
        final var email = "test@example.com";
        final var password = "password123";
        final var name = "John";
        final var firstSurname = "Doe";
        final var secondSurname = "Smith";
        final var birthDate = LocalDate.of(2000, 1, 1);
        final var gender = "M";
        final var apartmentNumber = "Apt 1";
        final var building = "Building A";
        final var city = "City";
        final var postalCode = "12345";
        final var street = "Street";
        final var countryNumericCode = 1;
        final var countrySubdivisionName = "Subdivision";

        // Create an AddressRegistrationDetailsDto object
        var addressDetails = new AddressRegistrationDetailsDto(apartmentNumber, building, city, postalCode, street,
                countryNumericCode, countrySubdivisionName);

        // Create a UserRegistrationDetailsDto object
        var userDetails = new UserRegistrationDetailsDto(dni, name, firstSurname, secondSurname, birthDate, gender,
                password, email, addressDetails, UserType.SOCIO_NUMERO);

        // Prepare test environment
        var persistentCountryEntity = new PersistentCountryEntity();
        var persistentCountrySubdivisionEntity = new PersistentCountrySubdivisionEntity();

        when(userRepository.findByDni(dni)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(countryRepository.findById(countryNumericCode)).thenReturn(Optional.of(persistentCountryEntity));
        when(countrySubdivisionRepository.findByName(countrySubdivisionName))
                .thenReturn(Optional.of(persistentCountrySubdivisionEntity));
        when(userRepository.save(any(PersistentUserEntity.class))).thenReturn(persistentUserEntity);

        // Execute the method under test
        var result = userService.add(userDetails);

        // Verify results
        assertThat(result).as("The result of adding a user should be the persisted user")
                .isEqualTo(persistentUserEntity);
        verify(userRepository, times(1)).save(any(PersistentUserEntity.class));
    }

    /**
     * Tests the behavior of the
     * {@link DefaultUserService#changeKindMember (String, PersistentUserEntity.UserType)}
     * method when successfully changing the user type for an existing user.
     *
     * <p>
     * This test verifies that when a valid request is made to change the user type
     * for an existing user, the user's type is updated correctly in the repository.
     * It also checks that the updated user entity is saved properly with the new
     * user type.
     * </p>
     *
     * <p>
     * Specifically, the test:
     * </p>
     * <ul>
     * <li>Sets up a {@link LocalDate} for a valid birth date ensuring the user is
     * over 18 years old.</li>
     * <li>Mocks the repository to return the existing user when searching by
     * email.</li>
     * <li>Mocks the repository to return the updated user entity after saving.</li>
     * <li>Calls the
     * {@link DefaultUserService#changeKindMember (String, PersistentUserEntity.UserType)}
     * method to change the user type.</li>
     * <li>Verifies that the user type is correctly updated to
     * {@link PersistentUserEntity.UserType#SOCIO_HONORARIO}.</li>
     * </ul>
     *
     * @throws UserServiceException if any error occurs while changing the user
     *                              type.
     */
    @Test
    void testChangeKindMemberSuccess() throws UserServiceException {
        // Declare a valid birth date for the user
        final var validBirthDate = LocalDate.of(2000, 1, 1);
        // Set the user's birth date to ensure they are over 18 years old
        final var userProfile = persistentUserEntity.getProfile();
        userProfile.setBirthDate(validBirthDate);
        persistentUserEntity.setProfile(userProfile);
        // Mock repository to return the existing user when searching by email
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(persistentUserEntity));
        // Mock repository to return the updated user after saving
        when(userRepository.save(any(PersistentUserEntity.class))).thenReturn(persistentUserEntity);

        // Call the method to change the user's type
        var result = userService.changeKindMember("test@example.com", UserType.SOCIO_HONORARIO);

        // Verify that the user type has been updated to SOCIO_HONORARIO
        assertThat(result.getKindMember()).as("Checking that the kind member is set to SOCIO_HONORARIO")
                .isEqualTo(UserType.SOCIO_HONORARIO);
    }

    /**
     * Tests the behavior of the
     * {@link DefaultUserService#changeKindMember (String, PersistentUserEntity.UserType)}
     * method when the user specified by email is not found in the repository.
     *
     * <p>
     * This test ensures that when a request is made to change the user type for a
     * non-existent user, the method throws a {@link UserServiceException} with the
     * appropriate message indicating that the user was not found.
     * </p>
     *
     * <p>
     * Specifically, the test:
     * </p>
     * <ul>
     * <li>Mocks the repository to simulate that no user is found when searching by
     * email.</li>
     * <li>Calls the
     * {@link DefaultUserService#changeKindMember (String, PersistentUserEntity.UserType)}
     * method with a non-existent user email and a user type to change to.</li>
     * <li>Verifies that a {@link UserServiceException} is thrown.</li>
     * <li>Checks that the exception message accurately reflects that the user was
     * not found.</li>
     * </ul>
     *
     * @throws UserServiceException if the user is not found in the repository.
     */
    @Test
    void testChangeKindMemberUserNotFound() {
        // Configure the mock behavior to simulate the user not being found
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Verify that the expected exception is thrown with a clear message
        var exception = assertThrows(UserServiceException.class,
                () -> userService.changeKindMember("test@example.com", UserType.SOCIO_ASPIRANTE),
                "Expected changeKindMember to throw UserServiceException " + "when user is not found.");

        // Optional: Verify the exception message, if relevant
        assertEquals("User not found.", exception.getMessage(),
                "The exception message should indicate that the user was not found.");
    }

    /**
     * Tests the {@link UserService#changeUserEmail(String, String)} method to
     * ensure that a user's email is successfully updated when a valid email change
     * request is made.
     *
     * <p>
     * This test case verifies the successful update of the user's email by
     * simulating a valid existing user in the repository and a new email address.
     * It mocks the {@code userRepository} to provide a user by the existing email
     * and to save the updated entity.
     * </p>
     *
     * <p>
     * Steps:
     * </p>
     * <ul>
     * <li>Arrange: Configure the {@code userRepository} mock to return an existing
     * {@code PersistentUserEntity} for the current email.</li>
     * <li>Act: Call {@code userService.changeUserEmail} with the current email and
     * a new email.</li>
     * <li>Assert: Verify that the returned user has the updated email address.</li>
     * </ul>
     *
     * @throws UserServiceException if there is an issue with updating the user's
     *                              email
     */
    @Test
    void testChangeUserEmailSuccess() throws UserServiceException {
        // Configura el comportamiento del mock para encontrar al usuario por email
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(persistentUserEntity));

        // Configura el comportamiento del mock para guardar el usuario
        when(userRepository.save(any(PersistentUserEntity.class))).thenReturn(persistentUserEntity);

        // Ejecuta el método para cambiar el email del usuario
        var result = userService.changeUserEmail("test@example.com", "newemail@example.com");

        // Verifica que el email del resultado sea el nuevo email esperado
        assertThat(result.getEmail()).as("Expected the email to be updated to 'newemail@example.com'")
                .isEqualTo("newemail@example.com");
    }

    /**
     * Tests the {@link UserService#changeUserEmail(String, String)} method to
     * ensure that an exception is thrown when attempting to change the email for a
     * non-existent user.
     *
     * <p>
     * This test case simulates a scenario where the specified user is not found in
     * the {@code userRepository}. It verifies that a {@link UserServiceException}
     * is thrown with an appropriate error message.
     * </p>
     *
     * <p>
     * Steps:
     * </p>
     * <ul>
     * <li>Arrange: Configure the {@code userRepository} mock to return
     * {@code Optional.empty()} when searching by the user's current email.</li>
     * <li>Act and Assert: Call {@code userService.changeUserEmail} with a
     * non-existent email and expect a {@code UserServiceException} to be
     * thrown.</li>
     * <li>Optionally: Verify that the exception message is "User not found."</li>
     * </ul>
     *
     * <p>
     * Expected Result:
     * </p>
     * The test should throw a {@code UserServiceException} with the message "User
     * not found."
     */
    @Test
    void testChangeUserEmailUserNotFound() {
        // Configura el mock para devolver vacío al buscar por email
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Verifica que se lanza una excepción cuando el usuario no se encuentra
        var exception = assertThrows(UserServiceException.class,
                () -> userService.changeUserEmail("test@example.com", "newemail@example.com"),
                "Expected UserServiceException to be thrown when attempting "
                        + "to change email for a non-existent user");

        // Opcional: Verifica el mensaje de la excepción, si es relevante
        assertEquals("User not found.", exception.getMessage(),
                "The exception message should be 'User not found' when the " + "user does not exist in the repository");
    }

    /**
     * Tests the {@link UserService#changeUserPassword(String, String, String)}
     * method to ensure that a user's password is successfully updated and stored in
     * encrypted form.
     *
     * <p>
     * This test simulates a scenario where a user changes their password. It
     * verifies that the current password is correctly matched, the new password is
     * encrypted and saved, and that the new password does not match the old
     * password.
     * </p>
     *
     * <p>
     * Steps:
     * </p>
     * <ul>
     * <li>Arrange: Configure the {@code userRepository} mock to return the user
     * entity with an encoded current password, and mock saving the updated
     * entity.</li>
     * <li>Act: Call {@code userService.changeUserPassword} with the correct current
     * password and a new password.</li>
     * <li>Assert: Verify that the new password is correctly encrypted, and that it
     * does not match the previous password.</li>
     * </ul>
     *
     * <p>
     * Expected Result:
     * </p>
     * The test should pass, confirming that the new password is saved in encrypted
     * form and is different from the old password.
     *
     * @throws UserServiceException if the service fails to update the password
     */
    @Test
    void testChangeUserPasswordSuccess() throws UserServiceException {
        // Configura el objeto mock
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(persistentUserEntity));

        var passwordEncoder = new BCryptPasswordEncoder();
        var currentPassword = "password123";
        var newPassword = "newpassword123";

        // La contraseña almacenada actualmente (encriptada)
        var encodedCurrentPassword = passwordEncoder.encode(currentPassword);
        persistentUserEntity.setPassword(encodedCurrentPassword);

        // Simula el guardado de la entidad con la nueva contraseña cifrada
        when(userRepository.save(any(PersistentUserEntity.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0, PersistentUserEntity.class);
        });

        // Ejecuta el método
        var result = userService.changeUserPassword("test@example.com", currentPassword, newPassword);

        // Verifica que la nueva contraseña esté correctamente cifrada
        assertThat(passwordEncoder.matches(newPassword, result.getPassword()))
                .withFailMessage("The new password should be correctly encoded and " + "match the encrypted value.")
                .isTrue();
        assertThat(result.getPassword()).withFailMessage("The new password should be different from the old password.")
                .isNotEqualTo(encodedCurrentPassword);
    }

    /**
     * Tests the {@link UserService#changeUserPassword(String, String, String)}
     * method to verify that an exception is thrown when the provided current
     * password does not match the user's stored password.
     *
     * <p>
     * This test simulates the scenario where a user attempts to change their
     * password but provides an incorrect current password. The test verifies that a
     * {@link UserServiceException} is thrown and that the exception message is
     * correct.
     * </p>
     *
     * <p>
     * Steps:
     * </p>
     * <ul>
     * <li>Arrange: Configure the {@code userRepository} mock to return a
     * {@code PersistentUserEntity} object, simulating an existing user.</li>
     * <li>Act: Call {@code userService.changeUserPassword} with an incorrect
     * current password and a new password.</li>
     * <li>Assert: Verify that a {@link UserServiceException} is thrown and that the
     * exception message matches the expected message.</li>
     * </ul>
     *
     * <p>
     * Expected Result:
     * </p>
     * The test should pass, confirming that the service throws an exception when
     * the current password does not match, and that the exception message provides
     * clear feedback.
     */
    @Test
    void testChangeUserPasswordWrongCurrentPassword() {
        // Configura el comportamiento del mock para userRepository
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(persistentUserEntity));

        // Verifica que se lanza la excepción esperada cuando se
        // proporciona una contraseña actual incorrecta
        var thrownException = assertThrows(UserServiceException.class,
                () -> userService.changeUserPassword("test@example.com", "wrongpassword", "newpassword123"),
                "Expected changeUserPassword to throw UserServiceException "
                        + "when the current password is incorrect");

        // Opcional: Verificar el mensaje de la excepción, si es relevante
        assertEquals("User current password dont match.", thrownException.getMessage(),
                "The exception message should indicate that the current " + "password provided is incorrect");
    }

    /**
     * Tests the {@link UserService#changeUserRoles(String, List)} method to ensure
     * that an exception is thrown when attempting to assign a role to a user, but
     * one of the specified roles does not exist.
     *
     * <p>
     * This test simulates the scenario where a user is assigned new roles, but one
     * of the roles is not found in the system. The test verifies that a
     * {@link UserServiceException} is thrown with an appropriate message.
     * </p>
     *
     * <p>
     * Steps:
     * </p>
     * <ul>
     * <li>Arrange: Set up the user's email and a list of roles to assign.</li>
     * <li>Configure {@code userRepository} to return an existing user based on
     * email.</li>
     * <li>Configure {@code roleRepository} to find {@code ROLE_ADMIN} but not
     * {@code ROLE_SOCIO}.</li>
     * <li>Act: Call {@code userService.changeUserRoles} with the email and role
     * list.</li>
     * <li>Assert: Verify that a {@link UserServiceException} is thrown and that the
     * exception message indicates a missing role.</li>
     * </ul>
     *
     * <p>
     * Expected Result:
     * </p>
     * The test should pass, confirming that the service throws a
     * {@link UserServiceException} with the message "Role not found." when any
     * specified role cannot be found in the database.
     */
    @Test
    void testChangeUserRolesRoleNotFound() {
        // Configura el email del usuario y los roles a cambiar
        var email = "test@example.com";
        List<UserRoleName> roleNameList = List.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_SOCIO);

        // Configura el mock del repositorio de usuarios para devolver un usuario
        var usrEntity = new PersistentUserEntity();
        usrEntity.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(usrEntity));

        // Configura el mock del repositorio de roles
        // El rol ROLE_ADMIN está presente
        when(roleRepository.findByName(UserRoleName.ROLE_ADMIN)).thenReturn(Optional.of(new PersistentRoleEntity()));

        // El rol ROLE_SOCIO no está presente
        when(roleRepository.findByName(UserRoleName.ROLE_SOCIO)).thenReturn(Optional.empty());

        // Ejecuta el método y verifica que se lanza la excepción esperada
        var exception = assertThrows(UserServiceException.class, () -> {
            userService.changeUserRoles(email, roleNameList);
        }, "Expected UserServiceException to be thrown when one of " + "the roles does not exist");

        // Verifica el mensaje de la excepción
        Assertions.assertEquals("Role not found.", exception.getMessage(),
                "The exception message should indicate that the role was not found");
    }

    /**
     * Tests the {@link UserService#changeUserRoles(String, List)} method to verify
     * that user roles are successfully changed when valid role names are provided.
     *
     * <p>
     * This test simulates a scenario where the user has an email in the repository,
     * and the roles to be assigned are valid and present in the role repository.
     * The test confirms that after changing the roles, the user's role list is not
     * empty.
     * </p>
     *
     * <p>
     * Steps:
     * </p>
     * <ul>
     * <li>Arrange: Set up the user's email and the role to assign.</li>
     * <li>Mock the {@code userRepository} to return a user entity for the provided
     * email.</li>
     * <li>Mock the {@code roleRepository} to return a role entity for the provided
     * role name.</li>
     * <li>Mock {@code userRepository.save} to return the updated user entity.</li>
     * <li>Act: Call {@code userService.changeUserRoles} with the email and role
     * list.</li>
     * <li>Assert: Verify that the user's role list is not empty after the roles are
     * changed.</li>
     * </ul>
     *
     * <p>
     * Expected Result:
     * </p>
     * The test should pass, confirming that the user's roles are correctly updated
     * and the role list is not empty after the method execution.
     *
     * @throws UserServiceException if any error occurs while changing user roles
     */
    @Test
    void testChangeUserRolesSuccess() throws UserServiceException {
        // Arrange: Configurar los datos y comportamiento esperado
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(persistentUserEntity));
        when(roleRepository.findByName(any())).thenReturn(Optional.of(new PersistentRoleEntity()));
        when(userRepository.save(any(PersistentUserEntity.class))).thenReturn(persistentUserEntity);

        // Act: Llamar al método que se está probando
        var result = userService.changeUserRoles("test@example.com",
                Collections.singletonList(UserRoleName.ROLE_PRESIDENTE));

        // Assert: Verificar resultados
        assertThat(result.getRoles())
                .as("La lista de roles del usuario debería no estar vacía " + "después de cambiar los roles")
                .isNotEmpty();
    }

//    @Test
//    void testSaveProfileImageFileErrorSavingImage() throws IOException {
//        // Arrange
//        MultipartFile file = mock(MultipartFile.class);
//        when(file.getOriginalFilename()).thenReturn("profile.jpg");
//        when(userRepository.findByDni("123456789")).thenReturn(Optional.of(persistentUserEntity));
//        when(imageStorageService.saveImage(file, "path/to/upload", "profile", "123456789"))
//                .thenThrow(new IOException("File save error"));
//
//        // Act & Assert
//        UserServiceException exception = assertThrows(UserServiceException.class, () -> {
//            userService.saveProfileImageFile("123456789", file);
//        });
//        assertEquals("Error saving profile image: File save error", exception.getMessage());
//    }

    /**
     * Tests the {@link UserService#changeUserRoles(String, List)} method to verify
     * that an exception is thrown when attempting to change roles for a user that
     * does not exist.
     *
     * <p>
     * This test simulates a scenario where no user is found with the specified
     * email in the repository, which should result in a
     * {@link UserServiceException} being thrown.
     * </p>
     *
     * <p>
     * Steps:
     * </p>
     * <ul>
     * <li>Arrange: Mock the {@code userRepository} to return an empty result for
     * the provided email.</li>
     * <li>Act: Call {@code userService.changeUserRoles} with the email of a
     * non-existent user.</li>
     * <li>Assert: Verify that a {@link UserServiceException} is thrown and check
     * the exception message.</li>
     * </ul>
     *
     * <p>
     * Expected Result:
     * </p>
     * The test should pass if the {@link UserServiceException} is thrown with the
     * message "User not found.", confirming the system's behavior when a
     * non-existent user is targeted for role changes.
     */
    @Test
    void testChangeUserRolesUserNotFound() {
        // Configura el mock para devolver vacío al buscar por email
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Ejecuta el método y verifica que se lanza una excepción esperada
        var exception = assertThrows(UserServiceException.class,
                () -> userService.changeUserRoles("test@example.com",
                        Collections.singletonList(UserRoleName.ROLE_SOCIO)),
                "Expected UserServiceException to be thrown when changing " + "roles for a non-existent user");

        // Verifica el mensaje de la excepción se asegura de ser el esperado
        Assertions.assertEquals("User not found.", exception.getMessage(),
                "The exception message should match the expected message " + "when user is not found");
    }

    /**
     * Tests the {@link UserService#findByDni(String)} method to ensure that it
     * throws an exception when the user is not found by their DNI.
     *
     * <p>
     * This test verifies that the {@code findByDni} method in {@link UserService}
     * throws a {@link UserServiceException} when the given DNI does not correspond
     * to any existing user in the repository.
     * </p>
     *
     * <p>
     * Steps:
     * </p>
     * <ul>
     * <li>Arrange: Set up the mock repository to return {@code Optional.empty()}
     * when the given DNI is searched, simulating a scenario where no user is
     * found.</li>
     * <li>Act: Call the {@code userService.findByDni(dni)} method with a DNI that
     * does not exist in the repository.</li>
     * <li>Assert: Verify that a {@link UserServiceException} is thrown with the
     * expected message.</li>
     * </ul>
     *
     * <p>
     * Expected Result:
     * </p>
     * The test should pass if the method throws a {@link UserServiceException} with
     * the correct message when the user is not found.
     */
    @Test
    void testFindByDniNotFound() {
        // Configura el comportamiento del mock para devolver vacío al buscar DNI
        when(userRepository.findByDni("123456789")).thenReturn(Optional.empty());

        // Verifica que se lanza una excepción cuando el usuario no se encuentra
        var exception = assertThrows(UserServiceException.class, () -> userService.findByDni("123456789"),
                "Expected UserServiceException to be thrown when finding " + "a user with a non-existent DNI");

        // Opcional: Verifica el mensaje de la excepción, si es relevante
        assertEquals("User not found.", exception.getMessage(),
                "The exception message should be 'User with DNI 123456789 not" + " found' when the user is not found");
    }

    /**
     * Tests the {@link UserService#findByDni(String)} method to ensure that it
     * retrieves a user by their DNI.
     *
     * <p>
     * This test verifies that the {@code findByDni} method in {@link UserService}
     * correctly returns a user when a valid DNI is provided. It mocks the user
     * repository to return a predefined user entity when the specified DNI is
     * searched, and asserts that the result is the expected user.
     * </p>
     *
     * <p>
     * Steps:
     * </p>
     * <ul>
     * <li>Arrange: Set up the mock repository to return a predefined user entity
     * when the given DNI is searched.</li>
     * <li>Act: Call the {@code userService.findByDni(dni)} method to fetch the user
     * by DNI.</li>
     * <li>Assert: Verify that the result is equal to the expected user entity.</li>
     * </ul>
     *
     * <p>
     * Expected Result:
     * </p>
     * The test should pass if the user entity is found by the specified DNI and the
     * result matches the expected entity.
     */
    @Test
    void testFindByDniSuccess() throws UserServiceException {
        // Arrange: Configurar el comportamiento simulado del repositorio
        when(userRepository.findByDni("123456789")).thenReturn(Optional.of(persistentUserEntity));

        // Act: Llamar al método del servicio
        var result = userService.findByDni("123456789");

        // Assert: Verificar el resultado
        assertThat(result).as(
                "Expected the result to be equal to the persistent " + "user entity when finding by DNI '123456789'")
                .isEqualTo(persistentUserEntity);
    }

    /**
     * Tests the {@link UserService#findByEmail(String)} method to ensure that it
     * retrieves a user by their email.
     *
     * <p>
     * This test verifies that the {@code findByEmail} method in {@link UserService}
     * returns the correct user when a valid email is provided. It mocks the user
     * repository to return a predefined user and ensures that the email of the
     * retrieved user matches the expected value.
     * </p>
     *
     * <p>
     * Steps:
     * </p>
     * <ul>
     * <li>Arrange: Set up the mock repository to return a predefined user when the
     * given email is searched.</li>
     * <li>Act: Call the {@code userService.findByEmail(email)} method to fetch the
     * user by email.</li>
     * <li>Assert: Verify that the result is not {@code null} and that the email of
     * the retrieved user matches the expected email.</li>
     * </ul>
     *
     * <p>
     * Expected Result:
     * </p>
     * The test should pass if the user is found, the result is not null, and the
     * email of the returned user matches the email provided in the input.
     */
    @Test
    void testFindByEmailUserFound() throws UserServiceException {
        // Configura el email del usuario y el usuario esperado
        var email = "test@example.com";
        var expectedUser = new PersistentUserEntity();
        expectedUser.setEmail(email);

        // Configura el mock del repositorio de usuarios para
        // devolver el usuario encontrado
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        // Ejecuta el método
        var result = userService.findByEmail(email);

        // Verifica que el resultado no sea null
        Assertions.assertNotNull(result, "Expected user to be found, but result was null");

        // Verifica que el email del usuario devuelto es el esperado
        Assertions.assertEquals(email, result.getEmail(),
                "The email of the found user does not match the expected email");
    }

    /**
     * Tests the {@link UserService#getAll()} method to ensure that it retrieves all
     * users from the repository.
     *
     * <p>
     * This test verifies that the {@code getAll} method in {@link UserService}
     * correctly fetches the list of users from the repository. It mocks the
     * repository to return a list of users and ensures that the method returns the
     * expected result.
     * </p>
     *
     * <p>
     * Steps:
     * </p>
     * <ul>
     * <li>Arrange: Mock the {@code userRepository} to return a predefined list of
     * users (in this case, the list contains a single {@code PersistentUserEntity}
     * object).</li>
     * <li>Act: Call the {@code userService.getAll()} method to fetch the list of
     * users.</li>
     * <li>Assert: Verify that the result returned from {@code userService.getAll()}
     * contains exactly the mocked user entity.</li>
     * </ul>
     *
     * <p>
     * Expected Result:
     * </p>
     * The test should pass if the list returned by the service contains exactly the
     * expected user entity (in this case, {@code persistentUserEntity}).
     */
    @Test
    void testGetAll() {
        // Arrange: Configurar el comportamiento simulado del repositorio
        List<PersistentUserEntity> users = Arrays.asList(persistentUserEntity);
        when(userRepository.findAll()).thenReturn(users);

        // Act: Llamar al método del servicio
        var result = userService.getAll();

        // Assert: Verifica el resultado contiene el usuario esperado
        assertThat(result).as("Expected the result to contain exactly the persistent user entity")
                .containsExactly(persistentUserEntity);
    }

    /**
     * Tests the behavior of the
     * {@link DefaultUserService#update(UserServiceUpdateDto, String)} method when
     * the user specified by email is not found in the repository.
     *
     * <p>
     * This test ensures that when an update request is made for a user who does not
     * exist, the method throws a {@link UserServiceException} with a clear message
     * indicating that the user was not found.
     * </p>
     *
     * <p>
     * Specifically, the test:
     * </p>
     * <ul>
     * <li>Mocks the repository to simulate that no user is found when searching by
     * email.</li>
     * <li>Calls the {@link DefaultUserService#update(UserServiceUpdateDto, String)}
     * method with a non-existent user email and a user update form.</li>
     * <li>Verifies that a {@link UserServiceException} is thrown.</li>
     * <li>Checks that the exception message accurately reflects that the user was
     * not found.</li>
     * </ul>
     *
     * @throws UserServiceException if the user is not found in the repository.
     */
    @Test
    void testUpdateUserNotFound() {
        final var name = "Santiago";
        final var firstSurname = "Paz";
        final var secondSurname = "Pérez";
        final var birthDate = LocalDate.of(1993, 5, 8);
        final var gender = "male";
        final var email = "santi@santi,es";
        // Set up the user update form
        var userForm = new UserServiceUpdateDto(name, firstSurname, secondSurname, birthDate, gender);

        // Mock the repository to simulate that the user is not found
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Verify that the expected exception is thrown with a clear message
        var exception = assertThrows(UserServiceException.class, () -> userService.update(userForm, email),
                "Expected update to throw UserServiceException when " + "user is not found.");

        // Optionally: Verify the exception message, if relevant
        assertEquals("User not found.", exception.getMessage(),
                "The exception message should indicate that the user " + "was not found.");
    }

    /**
     * Tests the behavior of the
     * {@link DefaultUserService#update(UserServiceUpdateDto, String)} method when
     * successfully updating an existing user's information.
     *
     * <p>
     * This test verifies that when a valid update request is made for an existing
     * user, the user's information is updated correctly in the repository. It also
     * checks that the updated user is saved properly and that the expected fields
     * are modified as intended.
     * </p>
     *
     * <p>
     * Specifically, the test:
     * </p>
     * <ul>
     * <li>Sets up a {@link UserServiceUpdateDto} with new user details.</li>
     * <li>Mocks the repository to return a {@link PersistentUserEntity} when
     * searching by email.</li>
     * <li>Mocks the repository to return the updated user entity after saving.</li>
     * <li>Executes the update method and verifies that the fields of the returned
     * user match the updated values.</li>
     * <li>Includes additional assertions to ensure all properties are correctly
     * updated.</li>
     * <li>Verifies that the {@link UserEntityRepository#save(PersistentUserEntity)}
     * method is called once with the updated user entity.</li>
     * </ul>
     *
     * @throws UserServiceException if any error occurs during the update process.
     */
    @Test
    void testUpdateUserSuccess() throws UserServiceException {
        // Declare constants
        final var testName = "John";
        final var testFirstSurname = "Doe";
        final var testSecondSurname = "Smith";
        final var testBirthDate = LocalDate.of(2000, 1, 1);
        final var testGender = "M";
        final var testEmail = "test@example.com";

        // Create update form with new user details
        var userForm = new UserServiceUpdateDto(testName, testFirstSurname, testSecondSurname, testBirthDate,
                testGender);

        // Mock repository to return the existing user when searching by email
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(persistentUserEntity));

        // Mock repository to return the updated user after saving
        when(userRepository.save(any(PersistentUserEntity.class))).thenReturn(persistentUserEntity);

        // Execute the method under test
        var result = (PersistentUserEntity) userService.update(userForm, testEmail);

        // Verify that user fields are updated correctly
        assertThat(result.getProfile().getName()).as("Checking updated user name").isEqualTo(testName);

        assertThat(result.getProfile().getFirstSurname()).as("Checking updated user's first surname")
                .isEqualTo(testFirstSurname);

        assertThat(result.getProfile().getSecondSurname()).as("Checking updated user's second surname")
                .isEqualTo(testSecondSurname);

        assertThat(result.getProfile().getBirthDate()).as("Checking updated user's birth date")
                .isEqualTo(testBirthDate);

        assertThat(result.getProfile().getGender()).as("Checking updated user's gender").isEqualTo(testGender);

        // Additional assertion to ensure all properties match
        assertThat(result).as("Checking the updated user").matches(
                user -> user.getProfile().getName().equals(testName)
                        && user.getProfile().getFirstSurname().equals(testFirstSurname)
                        && user.getProfile().getSecondSurname().equals(testSecondSurname)
                        && user.getProfile().getBirthDate().equals(testBirthDate)
                        && user.getProfile().getGender().equals(testGender),
                "User properties should match the updated values");

        // Verify that save was called once with the updated user
        verify(userRepository, times(1)).save(result);
    }

}
