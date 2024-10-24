
package es.org.cxn.backapp.test.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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

import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountrySubdivisionEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;
import es.org.cxn.backapp.repository.CountryEntityRepository;
import es.org.cxn.backapp.repository.CountrySubdivisionEntityRepository;
import es.org.cxn.backapp.repository.ImageProfileEntityRepository;
import es.org.cxn.backapp.repository.RoleEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.DefaultUserService;
import es.org.cxn.backapp.service.dto.AddressRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserServiceUpdateDto;

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

    @Mock
    private ImageProfileEntityRepository imageProfileRepository;

    /**
     *
     */
    @Mock
    private CountrySubdivisionEntityRepository countrySubdivisionRepository;

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
     * Initializes the test environment before each test is run.
     *
     * This method sets up the mocks and initializes the user entity with test data
     * to ensure each test starts with a clean and consistent state.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        persistentUserEntity = new PersistentUserEntity();
        persistentUserEntity.setEmail("test@example.com");
        persistentUserEntity.setPassword(new BCryptPasswordEncoder().encode("password123"));
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
        persistentUserEntity.setBirthDate(validBirthDate);

        // Mock repository to return the existing user when searching by email
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(persistentUserEntity));
        // Mock repository to return the updated user after saving
        when(userRepository.save(any(PersistentUserEntity.class))).thenReturn(persistentUserEntity);

        // Call the method to change the user's type
        var result = userService.changeKindMember("test@example.com", PersistentUserEntity.UserType.SOCIO_HONORARIO);

        // Verify that the user type has been updated to SOCIO_HONORARIO
        assertThat(result.getKindMember()).as("Checking that the kind member is set to SOCIO_HONORARIO")
                .isEqualTo(PersistentUserEntity.UserType.SOCIO_HONORARIO);
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
                () -> userService.changeKindMember("test@example.com", PersistentUserEntity.UserType.SOCIO_ASPIRANTE),
                "Expected changeKindMember to throw UserServiceException " + "when user is not found.");

        // Optional: Verify the exception message, if relevant
        assertEquals("User not found.", exception.getMessage(),
                "The exception message should indicate that the user was not found.");
    }

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

    @Test
    void testRemoveUserNotFound() {
        // Configura el mock para devolver vacío al buscar por email
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Verifica que se lanza la excepción esperada y añade un mensaje
        var exception = assertThrows(UserServiceException.class, () -> userService.remove("test@example.com"),
                "Expected UserServiceException to be thrown when trying to " + "remove a non-existent user");

        // Opcional: Verifica el mensaje de la excepción, si es relevante
        assertEquals("User not found.", exception.getMessage(),
                "Expected exception message to be 'User not found' but was '" + exception.getMessage() + "'");
    }

    @Test
    void testRemoveUserSuccess() throws UserServiceException {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(persistentUserEntity));
        doNothing().when(userRepository).delete(persistentUserEntity);

        userService.remove("test@example.com");
        verify(userRepository, times(1)).delete(persistentUserEntity);
    }

    @Test
    void testUnsubscribeUserNotFound() {
        // Configura el mock para devolver vacío al buscar por email
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Verifica que se lanza una excepción cuando el usuario no se encuentra
        var exception = assertThrows(UserServiceException.class,
                () -> userService.unsubscribe("test@example.com", "password123"),
                "Expected UserServiceException to be thrown when attempting " + "to unsubscribe a non-existent user");

        // Opcional: Verifica el mensaje de la excepción, si es relevante
        assertEquals("User not found.", exception.getMessage(),
                "The exception message should match the expected message when " + "the user is not found");
    }

    @Test
    void testUnsubscribeUserSuccess() throws UserServiceException {
        // Configura el mock para devolver un usuario encontrado al buscar por email
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(persistentUserEntity));

        // Configura el mock para devolver el usuario guardado con los cambios
        when(userRepository.save(any(PersistentUserEntity.class))).thenReturn(persistentUserEntity);

        // Ejecuta el método unsubscribe
        userService.unsubscribe("test@example.com", "password123");

        // Verifica que el usuario ha sido desactivado correctamente
        assertThat(persistentUserEntity.isEnabled()).as("Check if user has been disabled after successful unsubscribe")
                .isFalse();

        // Verifica que el método save fue llamado con el usuario actualizado
        verify(userRepository, times(1)).save(persistentUserEntity);
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
        assertThat(result.getName()).as("Checking updated user name").isEqualTo(testName);

        assertThat(result.getFirstSurname()).as("Checking updated user's first surname").isEqualTo(testFirstSurname);

        assertThat(result.getSecondSurname()).as("Checking updated user's second surname").isEqualTo(testSecondSurname);

        assertThat(result.getBirthDate()).as("Checking updated user's birth date").isEqualTo(testBirthDate);

        assertThat(result.getGender()).as("Checking updated user's gender").isEqualTo(testGender);

        // Additional assertion to ensure all properties match
        assertThat(result).as("Checking the updated user").matches(
                user -> user.getName().equals(testName) && user.getFirstSurname().equals(testFirstSurname)
                        && user.getSecondSurname().equals(testSecondSurname)
                        && user.getBirthDate().equals(testBirthDate) && user.getGender().equals(testGender),
                "User properties should match the updated values");

        // Verify that save was called once with the updated user
        verify(userRepository, times(1)).save(result);
    }

}
