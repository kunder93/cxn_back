
package es.org.cxn.backapp.config;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.requests.SignUpRequestForm;
import es.org.cxn.backapp.model.persistence.user.UserType;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.RoleService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.AddressRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserRegistrationDetailsDto;

/**
 * Initialize a user with admin privileges for start using app.
 */
@Configuration
@Profile("!test")
public class UserDataInitializer {

    /**
     * The user service.
     */
    private final UserService userService;

    /**
     * The role service.
     */
    private final RoleService roleService;

    /**
     * The user repository.
     */
    private final UserEntityRepository userRepository;

    /**
     * Default public constructor.
     *
     * @param userServ The user service for use in this class.
     * @param userRepo user repository for use in this class.
     * @param roleServ The role service.
     */
    public UserDataInitializer(final UserService userServ, final UserEntityRepository userRepo,
            final RoleService roleServ) {
        userService = Objects.requireNonNull(userServ, "Received user service as null.");
        userRepository = Objects.requireNonNull(userRepo, "Received user repository as null.");
        roleService = Objects.requireNonNull(roleServ, "Received role service as null.");
    }

    /**
     * Creates an address details object from the provided sign-up request form.
     *
     * @param signUpRequestForm the sign-up request form containing address
     *                          information.
     * @return an {@link AddressRegistrationDetailsDto} containing address details.
     */
    private static AddressRegistrationDetailsDto createAddressDetails(final SignUpRequestForm signUpRequestForm) {
        return new AddressRegistrationDetailsDto(signUpRequestForm.apartmentNumber(), signUpRequestForm.building(),
                signUpRequestForm.city(), signUpRequestForm.postalCode(), signUpRequestForm.street(),
                signUpRequestForm.countryNumericCode(), signUpRequestForm.countrySubdivisionName());
    }

    /**
     * Creates a user details object from the provided sign-up request form and
     * address details.
     *
     * @param signUpRequestForm the sign-up request form containing user
     *                          information.
     * @param addressDetails    the address details for the user.
     * @return a {@link UserRegistrationDetailsDto} containing user and address
     *         details.
     */
    private static UserRegistrationDetailsDto createUserDetails(final SignUpRequestForm signUpRequestForm,
            final AddressRegistrationDetailsDto addressDetails) {
        return new UserRegistrationDetailsDto(signUpRequestForm.dni(), signUpRequestForm.name(),
                signUpRequestForm.firstSurname(), signUpRequestForm.secondSurname(), signUpRequestForm.birthDate(),
                signUpRequestForm.gender(), signUpRequestForm.password(), signUpRequestForm.email(), addressDetails,
                signUpRequestForm.kindMember());
    }

    /**
     * Create initial user with admin privileges.
     *
     * @return Creates initial admin user if email and dni is not present in db.
     */
    @Bean
    CommandLineRunner init() {
        return _ -> {
            // Usuario inicial.
            final var adminUserRequest = new SignUpRequestForm("32721859N", // DNI
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
            final var userDetails = createUserDetails(adminUserRequest, addressDetails);

            final var dniExists = userRepository.findByDni(adminUserRequest.dni()).isPresent();
            final var emailExists = userRepository.findByEmail(adminUserRequest.email()).isPresent();

            if (!dniExists && !emailExists) {
                userService.add(userDetails);
                roleService.changeUserRoles(adminUserRequest.email(), initialUserRolesSet);
            }

        };
    }
}
