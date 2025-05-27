
package es.org.cxn.backapp.service.impl;

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

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;
import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.model.persistence.user.UserType;
import es.org.cxn.backapp.repository.CountryEntityRepository;
import es.org.cxn.backapp.repository.CountrySubdivisionEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.EmailService;
import es.org.cxn.backapp.service.PaymentsService;
import es.org.cxn.backapp.service.RoleService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.UserRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserServiceUpdateDto;
import es.org.cxn.backapp.service.exceptions.PaymentsServiceException;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import jakarta.mail.MessagingException;

/**
 * Default implementation of the {@link UserService}.
 *
 * @author Santiago Paz.
 *
 */
@Service
public final class DefaultUserService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUserService.class);

    /**
     * Age limit for be SOCIO_ASPIRANTE.
     */
    public static final int AGE_LIMIT = 18;

    /**
     * Yearly amount of payment by SOCIO_ASPIRANTE.
     */
    public static final int SOCIO_ASPIRANTE_PAYMENT_AMOUNT = 20;

    /**
     * Yearly amount of payment by SOCIO_NUMERARIO.
     */
    public static final int SOCIO_NUMERARIO_PAYMENT_AMOUNT = 40;
    /**
     * User not found message for exception.
     */
    public static final String USER_NOT_FOUND_MESSAGE = "User not found.";

    /**
     * User with this email exists message for exception.
     */
    public static final String USER_EMAIL_EXISTS_MESSAGE = "User email already exists.";
    /**
     * User with this dni exists message for exception.
     */
    public static final String USER_DNI_EXISTS_MESSAGE = "User dni already exists.";
    /**
     * User password not match with provided message for exception.
     */
    public static final String USER_PASSWORD_NOT_MATCH = "User current password dont match.";

    /**
     * Repository for the user entities handled by the service.
     */
    private final UserEntityRepository userRepository;

    /**
     * Repository for the country entities handled by the service.
     */
    private final CountryEntityRepository countryRepository;

    /**
     * Repository for the country subdivision entities handled by the service.
     */
    private final CountrySubdivisionEntityRepository countrySubdivisionRepo;

    /**
     * The email service for sending emails.
     */
    private final EmailService emailService;

    /**
     * The payments service for generating payments.
     */
    private final PaymentsService paymentsService;

    /**
     * The payments service for generating payments.
     */
    private final RoleService roleService;

    /**
     * The password encoder.
     */
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructs a DefaultUserService with the specified repositories and image
     * storage service.
     *
     * @param userRepo          The user repository {@link UserEntityRepository}
     *                          used for user-related operations.
     * @param roleServ          The role service {@link RoleService} used for
     *                          role-related operations.
     * @param countryRepo       The country repository
     *                          {@link CountryEntityRepository} used for
     *                          country-related operations.
     * @param countrySubdivRepo The country subdivisions repository
     *                          {@link CountrySubdivisionEntityRepository} used for
     *                          country subdivision-related operations.
     * @param paymentsServ      The payments service.
     *
     * @param emailServ         The email service.
     *
     * @param passwordEncoder   The password enconder.
     *
     * @throws NullPointerException if any of the provided repositories or services
     *                              are null.
     */
    public DefaultUserService(final UserEntityRepository userRepo, final CountryEntityRepository countryRepo,
            final CountrySubdivisionEntityRepository countrySubdivRepo, final EmailService emailServ,
            final PaymentsService paymentsServ, final RoleService roleServ,
            final BCryptPasswordEncoder passwordEncoder) {
        super();
        this.userRepository = Objects.requireNonNull(userRepo, "Received a null pointer as user repository");
        this.countryRepository = Objects.requireNonNull(countryRepo, "Received a null pointer as country repository");
        this.countrySubdivisionRepo = Objects.requireNonNull(countrySubdivRepo,
                "Received a null pointer as country subdivision repository");
        this.emailService = Objects.requireNonNull(emailServ, "Received a null pointer as email service.");
        this.paymentsService = Objects.requireNonNull(paymentsServ, "Received a null pointer as payments service.");
        this.roleService = Objects.requireNonNull(roleServ, "Received a null pointer as role service.");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "Received a null pointer as password encoder.");
    }

    /**
     * Converts a {@link UserEntity} to a {@link PersistentUserEntity} if the
     * provided entity is of the expected type.
     *
     * @param userEntity the {@link UserEntity} to be converted
     * @return the {@link PersistentUserEntity} instance if the provided entity is
     *         of the expected type
     * @throws UserServiceException if the provided entity is not an instance of
     *                              {@link PersistentUserEntity}
     */
    private static PersistentUserEntity asPersistentUserEntity(final UserEntity userEntity)
            throws UserServiceException {
        if (userEntity instanceof PersistentUserEntity persistentUserEntity) {
            return persistentUserEntity;
        } else {
            throw new UserServiceException("User entity is not of expected type.");
        }

    }

    private static boolean checkAgeUnder18(final UserEntity user) {

        final var birthDate = user.getProfile().getBirthDate();
        final var today = LocalDate.now();
        final var age = Period.between(birthDate, today).getYears();
        // Return if under 18.
        return age < AGE_LIMIT;
    }

    /**
     * Validate change user kind member.
     *
     * @param userType kind of member to change.
     * @param user     The user entity.
     * @return true if can change false if not.
     */
    private static boolean validateKindMemberChange(final UserType userType, final UserEntity user) {
        return switch (userType) {
        case SOCIO_NUMERO -> true;
        case SOCIO_ASPIRANTE -> checkAgeUnder18(user);
        case SOCIO_HONORARIO -> true;
        case SOCIO_FAMILIAR -> true;
        };
    }

    @Override
    public UserEntity acceptUserAsMember(final String userDni) throws UserServiceException {
        LOGGER.info("acceptUserAsMember: Inicio para userDni={}", userDni);

        final var userEntity = findByDni(userDni);
        LOGGER.info("acceptUserAsMember: Usuario encontrado con email={}", userEntity.getEmail());

        validateCandidateRole(userEntity, userDni);
        LOGGER.info("acceptUserAsMember: Rol candidato validado para userDni={}", userDni);

        final var updatedUser = changeUserRoleToSocio(userEntity);
        LOGGER.info("acceptUserAsMember: Rol cambiado a socio para userDni={}", userDni);

        final var savedUser = userRepository.save(asPersistentUserEntity(updatedUser));
        LOGGER.info("acceptUserAsMember: Usuario guardado con éxito, email={}", savedUser.getEmail());

        // Llamada asíncrona a procesos posteriores
        LOGGER.info("acceptUserAsMember: Lanzando proceso asíncrono post-aceptación para userDni={}", userDni);
        processPostAcceptAsync(savedUser);

        LOGGER.info("acceptUserAsMember: Método finalizado correctamente para userDni={}", userDni);
        return savedUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity add(final UserRegistrationDetailsDto userDetails) throws UserServiceException {
        final var dni = userDetails.dni();
        final var noVeridicDniDate = LocalDate.of(1900, 2, 2);
        if (userRepository.findByDni(dni).isPresent()) {
            throw new UserServiceException(USER_DNI_EXISTS_MESSAGE);
        }
        final var email = normalizeEmail(userDetails.email());
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserServiceException(USER_EMAIL_EXISTS_MESSAGE);
        } else {
            final UserProfile userProfile = new UserProfile();
            userProfile.setName(userDetails.name());
            userProfile.setFirstSurname(userDetails.firstSurname());
            userProfile.setSecondSurname(userDetails.secondSurname());
            userProfile.setGender(userDetails.gender());
            userProfile.setBirthDate(userDetails.birthDate());
            final var saveBuidler = PersistentUserEntity.builder().dni(dni).enabled(true)
                    .password(new BCryptPasswordEncoder().encode(userDetails.password())).profile(userProfile)
                    .email(email).kindMember(UserType.SOCIO_NUMERO); // Set kindMember directly
            // Build the instance
            final var save = saveBuidler.build();
            final var addressDetails = userDetails.addressDetails();
            final var apartmentNumber = addressDetails.apartmentNumber();
            final var building = addressDetails.building();
            final var city = addressDetails.city();
            final var postalCode = addressDetails.postalCode();
            final var street = addressDetails.street();
            final var countryNumericCode = addressDetails.countryNumericCode();

            final var address = new PersistentAddressEntity();
            address.setApartmentNumber(apartmentNumber);
            address.setBuilding(building);
            address.setCity(city);
            address.setPostalCode(postalCode);
            address.setStreet(street);
            // FindById in country entity (Id = numericCode)
            final var countryOptional = countryRepository.findById(countryNumericCode);
            if (countryOptional.isEmpty()) {
                throw new UserServiceException("Country with code: " + countryNumericCode + " not found.");
            }
            final var countryEntity = countryOptional.get();
            address.setCountry(countryEntity);
            final var countrySubdivisionName = addressDetails.countrySubdivisionName();
            final var countryDivisionOptional = countrySubdivisionRepo.findByName(countrySubdivisionName);
            if (countryDivisionOptional.isEmpty()) {
                throw new UserServiceException(
                        "Country subdivision with code: " + countrySubdivisionName + " not found.");
            }
            address.setCountrySubdivision(countryDivisionOptional.get());
            address.setUser(save);
            save.setAddress(address);
            final PersistentFederateStateEntity federateState = new PersistentFederateStateEntity();
            federateState.setUserDni(dni);
            federateState.setState(FederateState.NO_FEDERATE);
            federateState.setDniBackImageUrl("");
            federateState.setDniFrontImageUrl("");
            federateState.setAutomaticRenewal(false);
            federateState.setDniLastUpdate(noVeridicDniDate);
            save.setFederateState(federateState);

            return userRepository.save(save);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity changeKindMember(final String userEmail, final UserType newKindMember)
            throws UserServiceException {
        final var userEntity = findByEmail(userEmail);
        if (!validateKindMemberChange(newKindMember, userEntity)) {
            throw new UserServiceException("Cannot change the kind of member");
        }
        final var persistentUserEntity = asPersistentUserEntity(userEntity);
        persistentUserEntity.setKindMember(newKindMember);
        return userRepository.save(persistentUserEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = UserServiceException.class) // Ensure rollback on UserServiceException
    public UserEntity changeUserEmail(final String email, final String newEmail) throws UserServiceException {
        final var normalizedNewEmail = normalizeEmail(newEmail); // Normalize new email
        final var userWithNewEmail = userRepository.findByEmail(normalizedNewEmail);
        if (userWithNewEmail.isPresent()) {
            throw new UserServiceException("User with email: " + normalizedNewEmail + " exists.");
        }

        final var userEntity = findByEmail(email);
        userEntity.setEmail(normalizedNewEmail);
        // Guardar la entidad de usuario actualizada en la base de datos
        final var persistentUserEntity = asPersistentUserEntity(userEntity);

        try {
            // Attempt to save the updated user entity
            final var result = userRepository.save(persistentUserEntity);

            // Send the email about the email change
            emailService.sendChangeEmail(email, newEmail, result.getCompleteName());

            return result;
        } catch (DataAccessException e) {
            // Handle the failure to save the user entity (rollback happens automatically)
            throw new UserServiceException("Failed to save user entity after changing email.", e);
        } catch (MessagingException | IOException e) {
            // Handle the failure in sending the email and rollback the transaction
            // Rethrow as UserServiceException to trigger rollback
            throw new UserServiceException("Cannot send email to: " + email + " or " + newEmail + ".", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserEntity changeUserPassword(final String email, final String currentPassword, final String newPassword)
            throws UserServiceException {
        final var userEntity = findByEmail(email);

        final String storedPassword = userEntity.getPassword();
        if (!passwordEncoder.matches(currentPassword, storedPassword)) {
            throw new UserServiceException(USER_PASSWORD_NOT_MATCH);
        }
        // new password hashed.
        final var hashedNewPassword = passwordEncoder.encode(newPassword);
        // Update user password with new user password hash.
        userEntity.setPassword(hashedNewPassword);
        final var persistentUserEntity = asPersistentUserEntity(userEntity);
        return userRepository.save(persistentUserEntity);

    }

    private UserEntity changeUserRoleToSocio(final UserEntity userEntity) throws UserServiceException {
        return roleService.changeUserRoles(userEntity.getEmail(), List.of(UserRoleName.ROLE_SOCIO));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void delete(final String userEmail) throws UserServiceException {
        final var userEntity = findByEmail(userEmail);
        try {
            userRepository.delete((PersistentUserEntity) userEntity);
            emailService.sendDeletedUser(userEntity.getEmail(), userEntity.getCompleteName());
        } catch (Exception e) {
            throw new UserServiceException("Error sending email to user: " + userEntity.getDni(), e);
        }
    }

    @Override
    public UserEntity findByDni(final String value) throws UserServiceException {
        final Optional<PersistentUserEntity> entity;
        Objects.requireNonNull(value, "Received a null pointer as identifier");
        entity = userRepository.findByDni(value);
        if (entity.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        return entity.get();
    }

    @Override
    public UserEntity findByEmail(final String email) throws UserServiceException {
        Objects.requireNonNull(email, "Received a null pointer as identifier");
        final var normalizedEmail = normalizeEmail(email); // Normalize input
        final var result = userRepository.findByEmail(normalizedEmail);
        if (result.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        return result.get();
    }

    private void generatePaymentForAcceptedUser(final UserEntity userEntity) throws PaymentsServiceException {

        final UserType kindMember = userEntity.getKindMember();
        BigDecimal amountOfPayment = null;

        switch (kindMember) {
        case SOCIO_ASPIRANTE:
            amountOfPayment = BigDecimal.valueOf(SOCIO_ASPIRANTE_PAYMENT_AMOUNT);
            break;
        case SOCIO_NUMERO:
            amountOfPayment = BigDecimal.valueOf(SOCIO_NUMERARIO_PAYMENT_AMOUNT);
            break;
        case SOCIO_FAMILIAR:
            break;
        case SOCIO_HONORARIO:
            // No payments required for these types
            return;
        }

        if (amountOfPayment != null) {
            paymentsService.createPayment(amountOfPayment, PaymentsCategory.MEMBERSHIP_PAYMENT,
                    "Pago cuota de socio para el año: " + LocalDate.now().getYear() + ".", "Cuota socio",
                    userEntity.getDni());
        }
    }

    @Override
    public List<UserEntity> getAll() {
        final var persistentUsers = userRepository.findAll();
        return new ArrayList<>(persistentUsers);
    }

    private String normalizeEmail(final String email) {
        return email != null ? email.trim().toLowerCase() : null;
    }

    /**
     * Asynchronously processes post-acceptance actions for a user.
     * <p>
     * This method is executed asynchronously using Spring's {@code @Async} support.
     * It performs two main tasks after a user is accepted:
     * <ul>
     * <li>Sends a welcome email to the user's email address.</li>
     * <li>Generates a payment record for the accepted user.</li>
     * </ul>
     * All operations are wrapped in a try-catch block to handle and log any
     * exceptions without interrupting the execution flow.
     *
     * @param user The {@link UserEntity} instance representing the accepted user.
     *             Must not be {@code null}.
     */
    @Async
    public void processPostAcceptAsync(final UserEntity user) {
        LOGGER.info("processPostAcceptAsync: Inicio para userDni={}", user.getDni());
        try {
            emailService.sendWelcome(user.getEmail(), user.getCompleteName());
            LOGGER.info("processPostAcceptAsync: Email de bienvenida enviado a {}", user.getEmail());

            generatePaymentForAcceptedUser(user);
            LOGGER.info("processPostAcceptAsync: Pago generado para userDni={}", user.getDni());

        } catch (Exception e) {
            LOGGER.error("Error en procesamiento asíncrono post-aceptación de usuario {}: {}", user.getDni(),
                    e.getMessage(), e);
        }
        LOGGER.info("processPostAcceptAsync: Finalizado para userDni={}", user.getDni());
    }

    @Override
    public void recoverPassword(final String userEmail, final String newPassword) throws UserServiceException {
        final var userEntity = findByEmail(userEmail);

        // new password hashed.
        final var hashedNewPassword = passwordEncoder.encode(newPassword);
        // Update user password with new user password hash.
        userEntity.setPassword(hashedNewPassword);
        final var persistentUserEntity = asPersistentUserEntity(userEntity);
        userRepository.save(persistentUserEntity);

    }

    @Transactional
    @Override
    public void unsubscribe(final String email, final String validationPass) throws UserServiceException {
        final Optional<PersistentUserEntity> userOptional;
        userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        final var userEntity = userOptional.get();

        if (passwordEncoder.matches(validationPass, userEntity.getPassword())) {
            userEntity.setEnabled(false);
            userRepository.save(userEntity);
            try {
                emailService.sendUnsubscribe(userEntity.getEmail(), userEntity.getCompleteName());
            } catch (MessagingException | IOException e) {
                throw new UserServiceException("Error sending email to user: " + userEntity.getDni(), e);
            }
        } else {
            throw new UserServiceException("Password provided is not valid.");
        }

    }

    @Transactional
    @Override
    public UserEntity update(final UserServiceUpdateDto userForm, final String userEmail) throws UserServiceException {

        final var userEntity = findByEmail(userEmail);

        final UserProfile userProfile = new UserProfile();
        final var name = userForm.name();
        userProfile.setName(name);
        final var firstSurname = userForm.firstSurname();
        userProfile.setFirstSurname(firstSurname);
        final var secondSurname = userForm.secondSurname();
        userProfile.setSecondSurname(secondSurname);
        final var birthDate = userForm.birthDate();
        userProfile.setBirthDate(birthDate);
        final var gender = userForm.gender();
        userProfile.setGender(gender);
        userEntity.setProfile(userProfile);
        return userRepository.save(asPersistentUserEntity(userEntity));
    }

    private void validateCandidateRole(final UserEntity userEntity, final String userDni) throws UserServiceException {
        final var roles = userEntity.getRoles();
        if (roles.size() != 1 || !roles.iterator().next().getName().equals(UserRoleName.ROLE_CANDIDATO_SOCIO)) {
            throw new UserServiceException("User with dni: " + userDni + " must have only ROLE_CANDIDATO_SOCIO.");
        }
    }

}
