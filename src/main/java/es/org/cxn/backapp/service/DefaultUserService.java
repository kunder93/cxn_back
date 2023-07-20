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

package es.org.cxn.backapp.service;

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserServiceUpdateForm;
import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;
import es.org.cxn.backapp.repository.CountryEntityRepository;
import es.org.cxn.backapp.repository.CountrySubdivisionEntityRepository;
import es.org.cxn.backapp.repository.RoleEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of the {@link UserService}.
 *
 * @author Santiago Paz.
 *
 */
@Service
public final class DefaultUserService implements UserService {

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
  public static final String USER_EMAIL_EXISTS_MESSAGE =
        "User email already exists.";
  /**
   * User with this dni exists message for exception.
   */
  public static final String USER_DNI_EXISTS_MESSAGE =
        "User dni already exists.";
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
  private final CountrySubdivisionEntityRepository countrySubdivisionRepository;

  /**
   * Constructs an entities service with the specified repository.
   *
   * @param userRepo           The user repository{@link UserEntityRepository}
   * @param roleRepo           The role repository{@link RoleEntityRepository}
   * @param countryRepo        The country
   *                           repository{@link CountryEntityRepository}
   * @param countrySubsionRepo The country subdivisions
   *                           repository
   *                           {@link CountrySubdivisionEntityRepository}
   */
  public DefaultUserService(
        final UserEntityRepository userRepo,
        final RoleEntityRepository roleRepo,
        final CountryEntityRepository countryRepo,
        final CountrySubdivisionEntityRepository countrySubsionRepo
  ) {
    super();

    this.userRepository =
          checkNotNull(userRepo, "Received a null pointer as user repository");
    this.roleRepository =
          checkNotNull(roleRepo, "Received a null pointer as role repository");
    this.countryRepository = checkNotNull(
          countryRepo, "Received a null pointer as country repository"
    );

    this.countrySubdivisionRepository = checkNotNull(
          countrySubsionRepo,
          "Received a null pointer as country subdivision repository"
    );

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
  public List<PersistentUserEntity> getAll() {
    return userRepository.findAll();
  }

  @Override
  public UserEntity add(
        final String dni, final String name, final String firstSurname,
        final String secondSurname, final LocalDate birthDate,
        final String gender, final String password, final String email,
        final String apartmentNumber, final String building, final String city,
        final String postalCode, final String street,
        final Integer countryNumericCode, final String countrySubdivisionName,
        final UserType kindMember
  ) throws UserServiceException {

    if (userRepository.findByDni(dni).isPresent()) {
      throw new UserServiceException(USER_DNI_EXISTS_MESSAGE);
    }
    if (userRepository.findByEmail(email).isPresent()) {
      throw new UserServiceException(USER_EMAIL_EXISTS_MESSAGE);
    } else {
      final PersistentUserEntity save;
      save = new PersistentUserEntity();
      save.setDni(dni);
      save.setName(name);
      save.setFirstSurname(firstSurname);
      save.setSecondSurname(secondSurname);
      save.setGender(gender);
      save.setBirthDate(birthDate);
      save.setPassword(new BCryptPasswordEncoder().encode(password));
      save.setEmail(email);
      save.setKindMember(kindMember);

      var address = new PersistentAddressEntity();
      address.setApartmentNumber(apartmentNumber);
      address.setBuilding(building);
      address.setCity(city);
      address.setPostalCode(postalCode);
      address.setStreet(street);
      // FindById in country entity (Id = numericCode)
      var countryOptional = countryRepository.findById(countryNumericCode);
      if (countryOptional.isEmpty()) {
        throw new UserServiceException(
              "Country with code: " + countryNumericCode + " not found."
        );
      }
      var countryEntity = countryOptional.get();
      address.setCountry(countryEntity);
      var countryDivisionOptional =
            countrySubdivisionRepository.findByName(countrySubdivisionName);
      if (countryDivisionOptional.isEmpty()) {
        throw new UserServiceException(
              "Country subidivision with code: " + countrySubdivisionName
                    + " not found."
        );
      }
      address.setCountrySubdivision(countryDivisionOptional.get());
      address.setUser(save);
      save.setAddress(address);

      return userRepository.save(save);
    }
  }

  @Override
  @Transactional
  public UserEntity addRole(final String email, final String roleName)
        throws UserServiceException {
    var user = userRepository.findByEmail(email);

    if (user.isEmpty()) {
      throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
    }
    var role = roleRepository.findByName(roleName);
    if (role.isEmpty()) {
      throw new UserServiceException(ROLE_NOT_FOUND_MESSAGE);
    }
    var userEntity = user.get();
    userEntity.addRole(role.get());
    return userRepository.save(userEntity);

  }

  @Override
  @Transactional
  public UserEntity removeRole(final String email, final String roleName)
        throws UserServiceException {
    var userOptional = userRepository.findByEmail(email);

    if (userOptional.isEmpty()) {
      throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
    }
    var user = userOptional.get();
    var roleOptional = roleRepository.findByName(roleName);
    if (roleOptional.isEmpty()
          || !user.getRoles().contains(roleOptional.get())) {
      throw new UserServiceException(ROLE_NOT_FOUND_MESSAGE);
    }
    user.removeRole(roleOptional.get());
    return userRepository.save(user);

  }

  @Override
  public UserEntity findByEmail(final String email)
        throws UserServiceException {
    checkNotNull(email, "Received a null pointer as identifier");

    var result = userRepository.findByEmail(email);

    if (!result.isPresent()) {
      throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
    }
    return result.get();
  }

  @Override
  public void remove(final String email) throws UserServiceException {
    final Optional<PersistentUserEntity> userOptional;
    userOptional = userRepository.findByEmail(email);
    if (userOptional.isEmpty()) {
      throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
    }
    userRepository.delete(userOptional.get());
  }

  @Override
  public UserEntity
        update(final UserServiceUpdateForm userForm, final String userEmail)
              throws UserServiceException {
    Optional<PersistentUserEntity> userOptional;

    userOptional = userRepository.findByEmail(userEmail);
    if (userOptional.isEmpty()) {
      throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
    }
    PersistentUserEntity userEntity;
    userEntity = userOptional.get();
    userEntity.setName(userForm.getName());
    userEntity.setFirstSurname(userForm.getFirstSurname());
    userEntity.setSecondSurname(userForm.getSecondSurname());
    userEntity.setBirthDate(userForm.getBirthDate());
    userEntity.setGender(userForm.getGender());

    return userRepository.save(userEntity);
  }

  @Override
  public UserEntity
        changeKindMember(final String userEmail, final UserType newKindMember)
              throws UserServiceException {
    Optional<PersistentUserEntity> userOptional;

    userOptional = userRepository.findByEmail(userEmail);
    if (userOptional.isEmpty()) {
      throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
    } else {
      var userEntity = userOptional.get();
      userEntity.setKindMember(newKindMember);
      userRepository.save(userEntity);

      return userEntity;
    }
  }

}
