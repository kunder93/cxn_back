
package es.org.cxn.backapp.config;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.AddressRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserRegistrationDetailsDto;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class UserDataInitializer {

  @Autowired
  private UserService userService;

  /**
   * Creates an address details object from the provided sign-up request form.
   *
   * @param signUpRequestForm the sign-up request form containing address
   * information.
   * @return an {@link AddressRegistrationDetailsDto} containing address
   * details.
   */
  private static AddressRegistrationDetailsDto
        createAddressDetails(final SignUpRequestForm signUpRequestForm) {
    return new AddressRegistrationDetailsDto(
          signUpRequestForm.apartmentNumber(), signUpRequestForm.building(),
          signUpRequestForm.city(), signUpRequestForm.postalCode(),
          signUpRequestForm.street(), signUpRequestForm.countryNumericCode(),
          signUpRequestForm.countrySubdivisionName()
    );
  }

  /**
   * Creates a user details object from the provided sign-up request form and
   * address details.
   *
   * @param signUpRequestForm the sign-up request form containing user
   * information.
   * @param addressDetails    the address details for the user.
   * @return a {@link UserRegistrationDetailsDto} containing user and address
   * details.
   */
  private static UserRegistrationDetailsDto createUserDetails(
        final SignUpRequestForm signUpRequestForm,
        final AddressRegistrationDetailsDto addressDetails
  ) {
    return new UserRegistrationDetailsDto(
          signUpRequestForm.dni(), signUpRequestForm.name(),
          signUpRequestForm.firstSurname(), signUpRequestForm.secondSurname(),
          signUpRequestForm.birthDate(), signUpRequestForm.gender(),
          signUpRequestForm.password(), signUpRequestForm.email(),
          addressDetails, signUpRequestForm.kindMember()
    );
  }

  @Bean
  public CommandLineRunner init() {
    return args -> {
      // Usuario inicial.
      var adminUserRequest = new SignUpRequestForm(
            "32721859N", // DNI
            "Santiago", // Nombre
            "Paz", // Primer apellido
            "Perez", // Segundo apellido
            LocalDate.of(1993, 5, 8), // Fecha de nacimiento
            "male", // Género
            "123123", "santi@santi.es", // Email
            "15570", // Código postal
            "1ºD", // Número de apartamento
            "Piso", // Edificio
            "Calle marina española", // Calle
            "Narón", // Ciudad
            UserType.SOCIO_NUMERO, // Tipo de miembro
            724, // Código numérico de país
            "A Coruña" // Subdivisión del país
      );
      final var adminRole = UserRoleName.ROLE_ADMIN;
      final var initialUserRolesSet = new ArrayList<UserRoleName>();
      initialUserRolesSet.add(adminRole);

      final var addressDetails = createAddressDetails(adminUserRequest);
      final var userDetails =
            createUserDetails(adminUserRequest, addressDetails);

      userService.add(userDetails);
      userService
            .changeUserRoles(adminUserRequest.email(), initialUserRolesSet);

    };
  }
}