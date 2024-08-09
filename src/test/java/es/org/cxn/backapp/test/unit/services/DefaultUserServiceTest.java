
package es.org.cxn.backapp.test.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountrySubdivisionEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;
import es.org.cxn.backapp.repository.CountryEntityRepository;
import es.org.cxn.backapp.repository.CountrySubdivisionEntityRepository;
import es.org.cxn.backapp.repository.RoleEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.DefaultUserService;
import es.org.cxn.backapp.service.dto.AddressRegistrationDetails;
import es.org.cxn.backapp.service.dto.UserRegistrationDetails;
import es.org.cxn.backapp.service.dto.UserServiceUpdateForm;

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

class DefaultUserServiceTest {

  @Mock
  private UserEntityRepository userRepository;

  @Mock
  private RoleEntityRepository roleRepository;

  @Mock
  private CountryEntityRepository countryRepository;

  @Mock
  private CountrySubdivisionEntityRepository countrySubdivisionRepository;

  @InjectMocks
  private DefaultUserService userService;

  private UserEntity userEntity;
  private PersistentUserEntity persistentUserEntity;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userEntity = new PersistentUserEntity();
    persistentUserEntity = new PersistentUserEntity();
    persistentUserEntity.setEmail("test@example.com");
    persistentUserEntity
          .setPassword(new BCryptPasswordEncoder().encode("password123"));
  }

  @Test
  void testFindByDniSuccess() throws UserServiceException {
    when(userRepository.findByDni("123456789"))
          .thenReturn(Optional.of(persistentUserEntity));
    var result = userService.findByDni("123456789");
    assertThat(result).isEqualTo(persistentUserEntity);
  }

  @Test
  void testFindByEmailUserFound() throws UserServiceException {
    // Configura el email del usuario y el usuario esperado
    var email = "test@example.com";
    var expectedUser = new PersistentUserEntity();
    expectedUser.setEmail(email);

    // Configura el mock del repositorio de usuarios para devolver el usuario encontrado
    when(userRepository.findByEmail(email))
          .thenReturn(Optional.of(expectedUser));

    // Ejecuta el método
    var result = userService.findByEmail(email);

    // Verifica que el resultado es el usuario esperado
    Assertions.assertNotNull(result);
    Assertions.assertEquals(email, result.getEmail());
  }

  @Test
  void testFindByDniNotFound() {
    when(userRepository.findByDni("123456789")).thenReturn(Optional.empty());
    assertThrows(
          UserServiceException.class, () -> userService.findByDni("123456789")
    );
  }

  @Test
  void testGetAll() {
    List<PersistentUserEntity> users = Arrays.asList(persistentUserEntity);
    when(userRepository.findAll()).thenReturn(users);
    var result = userService.getAll();
    assertThat(result).containsExactly(persistentUserEntity);
  }

  @Test
  void testAddUserSuccess() throws UserServiceException {
    // Crear un objeto AddressRegistrationDetails
    var addressDetails = new AddressRegistrationDetails();
    addressDetails.setApartmentNumber("Apt 1");
    addressDetails.setBuilding("Building A");
    addressDetails.setCity("City");
    addressDetails.setPostalCode("12345");
    addressDetails.setStreet("Street");
    addressDetails.setCountryNumericCode(1); // Asegúrate de usar un código válido
    addressDetails.setCountrySubdivisionName("Subdivision");

    // Crear un objeto UserRegistrationDetails
    var userDetails = UserRegistrationDetails.builder().dni("123456789")
          .email("test@example.com").password("password123").name("John")
          .firstSurname("Doe").secondSurname("Smith")
          .birthDate(LocalDate.of(2000, 1, 1)).gender("M")
          .addressDetails(addressDetails).build();

    // Preparar el entorno de pruebas
    var persistentCountryEntity = new PersistentCountryEntity();
    var persistentCountrySubdivisionEntity =
          new PersistentCountrySubdivisionEntity();

    when(userRepository.findByDni("123456789")).thenReturn(Optional.empty());
    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.empty());
    when(countryRepository.findById(1))
          .thenReturn(Optional.of(persistentCountryEntity));
    when(countrySubdivisionRepository.findByName("Subdivision"))
          .thenReturn(Optional.of(persistentCountrySubdivisionEntity));
    when(userRepository.save(any(PersistentUserEntity.class)))
          .thenReturn(persistentUserEntity);

    // Ejecutar el método que se está probando
    var result = userService.add(userDetails);

    // Verificar resultados
    assertThat(result).isEqualTo(persistentUserEntity);
    verify(userRepository, times(1)).save(any(PersistentUserEntity.class));
  }

  @Test
  void testAddUserDniExists() {
    var userDetails = new UserRegistrationDetails();
    userDetails.setDni("123456789");
    when(userRepository.findByDni("123456789"))
          .thenReturn(Optional.of(persistentUserEntity));
    assertThrows(
          UserServiceException.class, () -> userService.add(userDetails)
    );
  }

  @Test
  void testAddUserCountryNotFound() {
    // Configura los datos de entrada
    var userDetails = new UserRegistrationDetails();
    userDetails.setDni("12345678A");
    userDetails.setEmail("test@example.com");
    userDetails.setPassword("password");
    userDetails.setName("John");
    userDetails.setFirstSurname("Doe");
    userDetails.setSecondSurname("Smith");
    userDetails.setGender("Male");
    userDetails.setBirthDate(LocalDate.of(1990, 1, 1));

    // Usa AddressRegistrationDetails
    var addressDetails = new AddressRegistrationDetails(
          "1", "Building", "City", "PostalCode", "Street", 999, "Subdivision"
    );
    userDetails.setAddressDetails(addressDetails);

    // Configura el comportamiento del mock para countryRepository
    when(countryRepository.findById(anyInt())).thenReturn(Optional.empty());

    // Verifica que se lanza la excepción esperada
    var exception = assertThrows(UserServiceException.class, () -> {
      userService.add(userDetails);
    });

    Assertions.assertEquals(
          "Country with code: 999 not found.", exception.getMessage()
    );
  }

  @Test
  void testAddUserCountrySubdivisionNotFound() {
    // Configura los datos de entrada
    var userDetails = new UserRegistrationDetails();
    userDetails.setDni("12345678A");
    userDetails.setEmail("test@example.com");
    userDetails.setPassword("password");
    userDetails.setName("John");
    userDetails.setFirstSurname("Doe");
    userDetails.setSecondSurname("Smith");
    userDetails.setGender("Male");
    userDetails.setBirthDate(LocalDate.of(1990, 1, 1));

    // Usa AddressRegistrationDetails
    var addressDetails = new AddressRegistrationDetails(
          "1", "Building", "City", "PostalCode", "Street", 999,
          "NonExistentSubdivision"
    );
    userDetails.setAddressDetails(addressDetails);

    // Configura el comportamiento del mock para countryRepository
    when(countryRepository.findById(anyInt()))
          .thenReturn(Optional.of(new PersistentCountryEntity()));

    // Configura el comportamiento del mock para countrySubdivisionRepository
    when(countrySubdivisionRepository.findByName(anyString()))
          .thenReturn(Optional.empty());

    // Verifica que se lanza la excepción esperada
    var exception = assertThrows(UserServiceException.class, () -> {
      userService.add(userDetails);
    });

    Assertions.assertEquals(
          "Country subidivision with code: NonExistentSubdivision not found.",
          exception.getMessage()
    );
  }

  @Test
  void testChangeUserRolesSuccess() throws UserServiceException {
    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.of(persistentUserEntity));
    when(roleRepository.findByName(any()))
          .thenReturn(Optional.of(new PersistentRoleEntity()));
    when(userRepository.save(any(PersistentUserEntity.class)))
          .thenReturn(persistentUserEntity);

    var result = userService.changeUserRoles(
          "test@example.com",
          Collections.singletonList(UserRoleName.ROLE_PRESIDENTE)
    );
    assertThat(result.getRoles()).isNotEmpty();
  }

  @Test
  void testChangeUserRolesUserNotFound() {
    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.empty());
    assertThrows(
          UserServiceException.class,
          () -> userService.changeUserRoles(
                "test@example.com",
                Collections.singletonList(UserRoleName.ROLE_SOCIO)
          )
    );
  }

  @Test
  void testChangeUserRolesRoleNotFound() throws UserServiceException {
    // Configura el email del usuario y los roles
    var email = "test@example.com";
    List<UserRoleName> roleNameList =
          List.of(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_SOCIO);

    // Configura el mock del repositorio de usuarios para devolver un usuario
    var persistentUserEntity = new PersistentUserEntity();
    persistentUserEntity.setEmail(email);
    when(userRepository.findByEmail(email))
          .thenReturn(Optional.of(persistentUserEntity));

    // Configura el mock del repositorio de roles para devolver empty para uno de los roles
    when(roleRepository.findByName(UserRoleName.ROLE_ADMIN))
          .thenReturn(Optional.of(new PersistentRoleEntity()));
    when(roleRepository.findByName(UserRoleName.ROLE_SOCIO))
          .thenReturn(Optional.empty());

    // Ejecuta el método y verifica que se lanza la excepción
    var exception = assertThrows(UserServiceException.class, () -> {
      userService.changeUserRoles(email, roleNameList);
    });

    // Verifica el mensaje de la excepción
    Assertions.assertEquals("Role not found.", exception.getMessage());
  }

  @Test
  void testChangeUserPasswordSuccess() throws UserServiceException {
    // Configura el objeto mock
    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.of(persistentUserEntity));

    var passwordEncoder = new BCryptPasswordEncoder();
    var currentPassword = "password123";
    var newPassword = "newpassword123";

    // La contraseña almacenada actualmente (encriptada)
    var encodedCurrentPassword = passwordEncoder.encode(currentPassword);
    persistentUserEntity.setPassword(encodedCurrentPassword);

    // Simula el guardado de la entidad con la nueva contraseña cifrada
    when(userRepository.save(any(PersistentUserEntity.class)))
          .thenAnswer(invocation -> {
            var user = invocation.getArgument(0, PersistentUserEntity.class);
            return user;
          });

    // Ejecuta el método
    var result = userService
          .changeUserPassword("test@example.com", currentPassword, newPassword);

    // Verifica que la nueva contraseña esté correctamente cifrada
    assertThat(passwordEncoder.matches(newPassword, result.getPassword()))
          .isTrue();
    assertThat(result.getPassword()).isNotEqualTo(encodedCurrentPassword);
  }

  @Test
  void testChangeUserPasswordWrongCurrentPassword() {
    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.of(persistentUserEntity));
    assertThrows(
          UserServiceException.class,
          () -> userService.changeUserPassword(
                "test@example.com", "wrongpassword", "newpassword123"
          )
    );
  }

  @Test
  void testChangeUserEmailSuccess() throws UserServiceException {
    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.of(persistentUserEntity));
    when(userRepository.save(any(PersistentUserEntity.class)))
          .thenReturn(persistentUserEntity);

    var result = userService
          .changeUserEmail("test@example.com", "newemail@example.com");
    assertThat(result.getEmail()).isEqualTo("newemail@example.com");
  }

  @Test
  void testChangeUserEmailUserNotFound() {
    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.empty());
    assertThrows(
          UserServiceException.class,
          () -> userService
                .changeUserEmail("test@example.com", "newemail@example.com")
    );
  }

  @Test
  void testRemoveUserSuccess() throws UserServiceException {
    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.of(persistentUserEntity));
    doNothing().when(userRepository).delete(persistentUserEntity);

    userService.remove("test@example.com");
    verify(userRepository, times(1)).delete(persistentUserEntity);
  }

  @Test
  void testRemoveUserNotFound() {
    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.empty());
    assertThrows(
          UserServiceException.class,
          () -> userService.remove("test@example.com")
    );
  }

  @Test
  void testUnsubscribeUserSuccess() throws UserServiceException {
    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.of(persistentUserEntity));
    when(userRepository.save(any(PersistentUserEntity.class)))
          .thenReturn(persistentUserEntity);

    userService.unsubscribe("test@example.com", "password123");
    assertThat(persistentUserEntity.isEnabled()).isFalse();
  }

  @Test
  void testUnsubscribeUserNotFound() {
    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.empty());
    assertThrows(
          UserServiceException.class,
          () -> userService.unsubscribe("test@example.com", "password123")
    );
  }

  @Test
  void testUpdateUserSuccess() throws UserServiceException {
    var userForm = new UserServiceUpdateForm();
    userForm.setName("John");
    userForm.setFirstSurname("Doe");
    userForm.setSecondSurname("Smith");
    userForm.setBirthDate(LocalDate.of(2000, 1, 1));
    userForm.setGender("M");

    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.of(persistentUserEntity));
    when(userRepository.save(any(PersistentUserEntity.class)))
          .thenReturn(persistentUserEntity);

    var result = userService.update(userForm, "test@example.com");
    assertThat(result.getName()).isEqualTo("John");
  }

  @Test
  void testUpdateUserNotFound() {
    var userForm = new UserServiceUpdateForm();
    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.empty());
    assertThrows(
          UserServiceException.class,
          () -> userService.update(userForm, "test@example.com")
    );
  }

  @Test
  void testChangeKindMemberSuccess() throws UserServiceException {
    // Configura una fecha de nacimiento válida
    var birthDate = LocalDate.of(2000, 1, 1); // Fecha de nacimiento válida para un usuario mayor de 18 años
    persistentUserEntity.setBirthDate(birthDate);

    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.of(persistentUserEntity));
    when(userRepository.save(any(PersistentUserEntity.class)))
          .thenReturn(persistentUserEntity);

    // Llama al método changeKindMember
    var result = userService.changeKindMember(
          "test@example.com", PersistentUserEntity.UserType.SOCIO_HONORARIO
    );

    // Verifica el resultado
    assertThat(result.getKindMember())
          .isEqualTo(PersistentUserEntity.UserType.SOCIO_HONORARIO);
  }

  @Test
  void testChangeKindMemberUserNotFound() {
    when(userRepository.findByEmail("test@example.com"))
          .thenReturn(Optional.empty());
    assertThrows(
          UserServiceException.class,
          () -> userService.changeKindMember(
                "test@example.com",
                PersistentUserEntity.UserType.SOCIO_ASPIRANTE
          )
    );
  }
}
