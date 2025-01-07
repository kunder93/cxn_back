/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.model.persistence.user.UserType;
import es.org.cxn.backapp.repository.CountryEntityRepository;
import es.org.cxn.backapp.repository.CountrySubdivisionEntityRepository;
import es.org.cxn.backapp.repository.RoleEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.EmailService;
import es.org.cxn.backapp.service.PaymentsService;
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

    /**
     * Age limit for be SOCIO_ASPIRANTE.
     */
    public static final int AGE_LIMIT = 18;

    /**
     * User not found message for exception.
     */
    public static final String USER_NOT_FOUND_MESSAGE = "User not found.";
    /**
     * Role not found message for exception.
     */
    public static final String ROLE_NOT_FOUND_MESSAGE = "Role not found.";
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
     * Repository for the role entities handled by the service.
     */
    private final RoleEntityRepository roleRepository;

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
     * Constructs a DefaultUserService with the specified repositories and image
     * storage service.
     *
     * @param userRepo          The user repository {@link UserEntityRepository}
     *                          used for user-related operations.
     * @param roleRepo          The role repository {@link RoleEntityRepository}
     *                          used for role-related operations.
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
     * @throws NullPointerException if any of the provided repositories or services
     *                              are null.
     */
    public DefaultUserService(final UserEntityRepository userRepo, final RoleEntityRepository roleRepo,
            final CountryEntityRepository countryRepo, final CountrySubdivisionEntityRepository countrySubdivRepo,
            final EmailService emailServ, final PaymentsService paymentsServ) {
        super();
        this.userRepository = checkNotNull(userRepo, "Received a null pointer as user repository");
        this.roleRepository = checkNotNull(roleRepo, "Received a null pointer as role repository");
        this.countryRepository = checkNotNull(countryRepo, "Received a null pointer as country repository");
        this.countrySubdivisionRepo = checkNotNull(countrySubdivRepo,
                "Received a null pointer as country subdivision repository");
        this.emailService = checkNotNull(emailServ, "Received a null pointer as email service.");
        this.paymentsService = checkNotNull(paymentsServ, "Received a null pointer as payments service.");
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
        default -> false;
        };
    }

    @Override
    public UserEntity acceptUserAsMember(String userDni) throws UserServiceException {
        final var userEntity = findByDni(userDni);
        final var userRoles = userEntity.getRoles();
        final Integer numberRolesExpected = Integer.valueOf(1);
        if (numberRolesExpected.equals(userRoles.size())) {
            // Get the only one role.
            final var roleName = userRoles.iterator().next();
            if (roleName.getName().equals(UserRoleName.ROLE_CANDIDATO_SOCIO)) {
                // Modify user roles for have only UserRoleName.ROLE_SOCIO
                final var roles = new ArrayList<UserRoleName>();
                roles.add(UserRoleName.ROLE_SOCIO);
                final UserEntity userWithchangedRoles = changeUserRoles(userEntity.getEmail(), roles);

                try {

                    final UserEntity result = userRepository.save(asPersistentUserEntity(userWithchangedRoles));

                    emailService.sendWelcome(result.getEmail(), result.getCompleteName());
                    generatePaymentForAcceptedUser(result);
                    return result;
                } catch (MessagingException e) {
                    throw new UserServiceException("User with dni: " + userDni + "cannot send email.", e);
                } catch (IOException e) {
                    throw new UserServiceException(
                            "User with dni: " + userDni + "cannot send email: cannot load template.", e);
                } catch (PaymentsServiceException e) {
                    throw new UserServiceException(
                            "User with dni: " + userDni + "cannot generate payment for this user.", e);
                }

            } else {
                throw new UserServiceException(
                        "User with dni: " + userDni + "no have " + UserRoleName.ROLE_CANDIDATO_SOCIO + " role.");
            }
        } else {
            throw new UserServiceException(
                    "User with dni: " + userDni + "have no only " + UserRoleName.ROLE_CANDIDATO_SOCIO + " role.");
        }
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
        final var email = userDetails.email();
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
    @Transactional
    public UserEntity changeUserEmail(final String email, final String newEmail) throws UserServiceException {
        final var userWithNewEmail = userRepository.findByEmail(newEmail);
        if (userWithNewEmail.isPresent()) {
            throw new UserServiceException("User with email: " + newEmail + " exists.");
        }

        final var userEntity = findByEmail(email);
        userEntity.setEmail(newEmail);
        // Guardar la entidad de usuario actualizada en la base de datos
        final var persistentUserEntity = asPersistentUserEntity(userEntity);
        final var result = userRepository.save(persistentUserEntity);
        try {
            emailService.sendChangeEmail(email, newEmail, result.getCompleteName());
            return result;
        } catch (MessagingException e) {
            throw new UserServiceException("Cannot send email to: " + email + " or " + newEmail + ".", e);
        } catch (IOException e) {
            throw new UserServiceException("Cannot load email template.", e);
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

        // Check password with stored.
        final var passwordEncoder = new BCryptPasswordEncoder();
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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UserEntity changeUserRoles(final String email, final List<UserRoleName> roleNameList)
            throws UserServiceException {
        final var userEntity = findByEmail(email);

        final Set<PersistentRoleEntity> rolesSet = new HashSet<>();
        for (final UserRoleName roleName : roleNameList) {

            final var role = roleRepository.findByName(roleName);
            if (role.isEmpty()) {
                throw new UserServiceException(ROLE_NOT_FOUND_MESSAGE);
            }
            rolesSet.add(role.get());

        }
        final var persistentUserEntity = asPersistentUserEntity(userEntity);
        persistentUserEntity.setRoles(rolesSet);
        return userRepository.save(persistentUserEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void delete(final String userEmail) throws UserServiceException {
        final var userEntity = findByEmail(userEmail);
        userRepository.delete((PersistentUserEntity) userEntity);
    }

    @Override
    public UserEntity findByDni(final String value) throws UserServiceException {
        final Optional<PersistentUserEntity> entity;
        checkNotNull(value, "Received a null pointer as identifier");
        entity = userRepository.findByDni(value);
        if (entity.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        return entity.get();
    }

    @Override
    public UserEntity findByEmail(final String email) throws UserServiceException {
        checkNotNull(email, "Received a null pointer as identifier");

        final var result = userRepository.findByEmail(email);

        if (result.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        return result.get();
    }

    private void generatePaymentForAcceptedUser(UserEntity userEntity) throws PaymentsServiceException {
        final UserType kindMember = userEntity.getKindMember();
        BigDecimal amountOfPayment = null;

        switch (kindMember) {
        case SOCIO_ASPIRANTE:
            amountOfPayment = BigDecimal.valueOf(20.00);
            break;
        case SOCIO_NUMERO:
            amountOfPayment = BigDecimal.valueOf(40.00);
            break;
        case SOCIO_FAMILIAR:
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

    @Transactional
    @Override
    public void unsubscribe(final String email) throws UserServiceException {
        final Optional<PersistentUserEntity> userOptional;
        userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        final var userEntity = userOptional.get();

        userEntity.setEnabled(false);
        userRepository.save(userEntity);
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

}
