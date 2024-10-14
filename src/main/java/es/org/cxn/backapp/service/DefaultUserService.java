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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.responses.ProfileImageResponse;
import es.org.cxn.backapp.model.persistence.ImageExtension;
import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;
import es.org.cxn.backapp.model.persistence.PersistentProfileImageEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;
import es.org.cxn.backapp.repository.CountryEntityRepository;
import es.org.cxn.backapp.repository.CountrySubdivisionEntityRepository;
import es.org.cxn.backapp.repository.ImageProfileEntityRepository;
import es.org.cxn.backapp.repository.RoleEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.dto.UserRegistrationDetailsDto;
import es.org.cxn.backapp.service.dto.UserServiceUpdateDto;

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

    private static boolean checkAgeUnder18(final PersistentUserEntity user) {

        final var birthDate = user.getBirthDate();
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
    private static boolean validateKindMemberChange(final UserType userType, final PersistentUserEntity user) {
        return switch (userType) {
        case SOCIO_NUMERO -> true;
        case SOCIO_ASPIRANTE -> checkAgeUnder18(user);
        case SOCIO_HONORARIO -> true;
        case SOCIO_FAMILIAR -> true;
        default -> false;
        };
    }

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
     * Repository for image profile user data.
     */
    private final ImageProfileEntityRepository imageProfileEntityRepository;

    /**
     * Constructs a DefaultUserService with the specified repositories.
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
     * @param imgRepo           The image profile repository
     *                          {@link ImageProfileEntityRepository} used for
     *                          managing user profile images.
     *
     * @throws NullPointerException if any of the provided repositories are null.
     */
    public DefaultUserService(final UserEntityRepository userRepo, final RoleEntityRepository roleRepo,
            final CountryEntityRepository countryRepo, final CountrySubdivisionEntityRepository countrySubdivRepo,
            final ImageProfileEntityRepository imgRepo) {
        super();

        this.userRepository = checkNotNull(userRepo, "Received a null pointer as user repository");
        this.roleRepository = checkNotNull(roleRepo, "Received a null pointer as role repository");
        this.countryRepository = checkNotNull(countryRepo, "Received a null pointer as country repository");
        this.imageProfileEntityRepository = checkNotNull(imgRepo,
                "Received a null pointer as image profile repository");
        this.countrySubdivisionRepo = checkNotNull(countrySubdivRepo,
                "Received a null pointer as country subdivision repository");

    }

    @Override
    public UserEntity add(final UserRegistrationDetailsDto userDetails) throws UserServiceException {
        final var dni = userDetails.dni();

        if (userRepository.findByDni(dni).isPresent()) {
            throw new UserServiceException(USER_DNI_EXISTS_MESSAGE);
        }
        final var email = userDetails.email();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserServiceException(USER_EMAIL_EXISTS_MESSAGE);
        } else {
            PersistentUserEntity save = PersistentUserEntity.builder().dni(dni).name(userDetails.name())
                    .firstSurname(userDetails.firstSurname()).secondSurname(userDetails.secondSurname())
                    .gender(userDetails.gender()).birthDate(userDetails.birthDate())
                    .password(new BCryptPasswordEncoder().encode(userDetails.password())) // Encrypt the password
                    .email(email).kindMember(PersistentUserEntity.UserType.SOCIO_NUMERO) // Set kindMember directly
                    .build(); // Build the instance

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

            PersistentFederateStateEntity federateState = new PersistentFederateStateEntity();
            federateState.setUserDni(dni);
            federateState.setState(FederateState.NO_FEDERATE);
            federateState.setDniBackImageUrl("");
            federateState.setDniFrontImageUrl("");
            federateState.setAutomaticRenewal(false);
            federateState.setDniLastUpdate(LocalDate.of(1900, 2, 2));

            save.setFederateState(federateState);

            return userRepository.save(save);
        }
    }

    @Override
    public UserEntity changeKindMember(final String userEmail, final UserType newKindMember)
            throws UserServiceException {
        final var userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        } else {
            final var userEntity = userOptional.get();

            if (!validateKindMemberChange(newKindMember, userEntity)) {
                throw new UserServiceException("Cannot change the kind of member");
            }
            userEntity.setKindMember(newKindMember);
            userRepository.save(userEntity);
            return userEntity;
        }
    }

    @Override
    @Transactional
    public UserEntity changeUserEmail(final String email, final String newEmail) throws UserServiceException {
        final var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        final var userEntity = user.get();
        userEntity.setEmail(newEmail);
        return userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity changeUserPassword(final String email, final String currentPassword, final String newPassword)
            throws UserServiceException {

        // Buscar al usuario por su correo electrónico en la base de datos
        final var userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        // Obtener la entidad de usuario desde el Optional
        final var userEntity = userOptional.get();
        // Verificar la contraseña proporcionada coincide con la almacenada
        final var passwordEncoder = new BCryptPasswordEncoder();
        final String storedPassword = userEntity.getPassword();
        if (!passwordEncoder.matches(currentPassword, storedPassword)) {
            throw new UserServiceException(USER_PASSWORD_NOT_MATCH);
        }
        // Hash de la nueva contraseña antes de guardarla en la base de datos
        final var hashedNewPassword = passwordEncoder.encode(newPassword);
        // Actualizar la contraseña del usuario con la nueva contraseña hash
        userEntity.setPassword(hashedNewPassword);
        // Guardar la entidad de usuario actualizada en la base de datos
        return userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity changeUserRoles(final String email, final List<UserRoleName> roleNameList)
            throws UserServiceException {
        final var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        final Set<PersistentRoleEntity> rolesSet = new HashSet<>();
        for (final UserRoleName roleName : roleNameList) {

            final var role = roleRepository.findByName(roleName);
            if (role.isEmpty()) {
                throw new UserServiceException(ROLE_NOT_FOUND_MESSAGE);
            }
            rolesSet.add(role.get());

        }
        final var userEntity = user.get();
        userEntity.setRoles(rolesSet);
        return userRepository.save(userEntity);
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

    @Override
    public List<UserEntity> getAll() {
        final var persistentUsers = userRepository.findAll();
        return new ArrayList<>(persistentUsers);
    }

    @Override
    public ProfileImageResponse getProfileImage(final String dni) throws UserServiceException {
        final var user = findByDni(dni);
        final var profileImage = user.getProfileImage();

        if (profileImage == null) {
            throw new UserServiceException("No profile image found for user with DNI: " + dni);
        }

        if (profileImage.getStored()) {
            // Load the image file from the filesystem
            File imageFile = new File(profileImage.getUrl());

            if (!imageFile.exists()) {
                throw new UserServiceException("Profile image file not found at path: " + profileImage.getUrl());
            }

            try {
                // Read the image file and encode it in Base64
                byte[] imageData = Files.readAllBytes(imageFile.toPath());
                String base64Image = Base64.getEncoder().encodeToString(imageData);

                // Determine the MIME type based on the file extension
                String mimeType;
                switch (profileImage.getExtension()) {
                case ImageExtension.PNG:
                    mimeType = "data:image/png;base64,";
                    break;
                case ImageExtension.JPG:
                case ImageExtension.JPEG:
                    mimeType = "data:image/jpeg;base64,";
                    break;
                case ImageExtension.WEBP:
                    mimeType = "data:image/webp;base64,";
                    break;
                default:
                    throw new UserServiceException("Unsupported image extension: " + profileImage.getExtension());
                }

                // Prepend the MIME type to the Base64 image data
                String base64ImageWithPrefix = mimeType + base64Image;

                // Return the response with the Base64-encoded image
                ProfileImageResponse profileImageResponse = new ProfileImageResponse(profileImage,
                        base64ImageWithPrefix);
                return profileImageResponse;

            } catch (IOException e) {
                throw new UserServiceException("Error reading profile image file: " + e.getMessage(), e);
            }

        } else {
            // If the image is not stored, return the external URL
            return new ProfileImageResponse(profileImage);
        }
    }

    @Transactional
    @Override
    public void remove(final String email) throws UserServiceException {
        final Optional<PersistentUserEntity> userOptional;
        userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        userRepository.delete(userOptional.get());
    }

    /**
     * Saves the profile image for a user based on the provided URL. If an existing
     * profile image is stored for the user, it will be deleted from the filesystem
     * before saving the new URL.
     *
     * @param userEmail the email of the user for whom the profile image is being
     *                  saved.
     * @param url       the URL where the profile image is located. This can be a
     *                  direct URL or a relative path to the image file.
     * @return the saved {@link PersistentProfileImageEntity} object containing the
     *         profile image information.
     * @throws UserServiceException if there is an error while saving the profile
     *                              image for the user, including issues with
     *                              deleting the existing image file.
     */
    @Override
    public PersistentUserEntity saveProfileImage(final String userEmail, final String url) throws UserServiceException {
        PersistentUserEntity userEntity = (PersistentUserEntity) findByEmail(userEmail);
        final var userDni = userEntity.getDni();
        var imageProfileOptional = imageProfileEntityRepository.findById(userDni);
        if (imageProfileOptional.isPresent()) {
            var image = imageProfileOptional.get();
            if (image.getStored()) { // Borrar la imagen almacenada.
                // Get the file path from the existing image entity
                String existingImagePath = image.getUrl(); // Assuming getUrl() returns the complete path
                // Create a File object
                File existingFile = new File(existingImagePath);
                // Check if the file exists and delete it
                if (existingFile.exists()) {
                    boolean deleted = existingFile.delete();
                    if (!deleted) {
                        // Handle the case where the file could not be deleted
                        throw new UserServiceException(
                                "Could not delete existing profile image file: " + existingImagePath);
                    }
                }
            }
        }
        userEntity = (PersistentUserEntity) findByDni(userDni);
        PersistentProfileImageEntity profileImageEntity = new PersistentProfileImageEntity();
        profileImageEntity.setExtension(ImageExtension.PROVIDED);
        profileImageEntity.setStored(false);
        profileImageEntity.setUrl(url);
        profileImageEntity.setUserDni(userDni);
        final var savedImageEntity = imageProfileEntityRepository.save(profileImageEntity);
        userEntity.setProfileImage(savedImageEntity);
        final var updatedUser = userRepository.save(userEntity);
        return updatedUser;
    }

    /**
     * Saves the profile image for a user based on the uploaded file. If an existing
     * profile image is stored for the user, it will be deleted from the filesystem
     * before saving the new image file.
     *
     * @param userDni the DNI (Document Number of Identification) of the user for
     *                whom the profile image is being saved.
     * @param file    the uploaded {@link MultipartFile} representing the profile
     *                image.
     * @return the saved {@link PersistentProfileImageEntity} object containing the
     *         profile image information.
     * @throws IllegalStateException if the method is invoked when the application
     *                               is in an invalid state for this operation.
     * @throws IOException           if an I/O error occurs while saving the file to
     *                               the filesystem.
     * @throws UserServiceException  if there is an error while saving the profile
     *                               image for the user, including invalid image
     *                               extensions or file handling issues.
     */
    @Override
    public PersistentUserEntity saveProfileImageFile(final String userDni, final MultipartFile file)
            throws IllegalStateException, IOException, UserServiceException {

        // Fetch the user entity by DNI
        PersistentUserEntity userEntity = (PersistentUserEntity) findByDni(userDni);

        // Directory path for saving images
        String uploadDir = "C:\\Users\\Santi\\Desktop\\AlmacenDni\\";
        final String originalFileName = file.getOriginalFilename();

        // Split the filename to get the extension
        String[] tokens = originalFileName.split("\\.");
        String fileExtension = tokens[tokens.length - 1];

        // Validate the file extension
        ImageExtension imageExtension = ImageExtension.fromString(fileExtension);
        if (imageExtension == null) {
            throw new UserServiceException("Invalid image extension: " + fileExtension);
        }

        // Set the file name as the user DNI
        String fileName = userDni;

        // Create the directory if it doesn't exist
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // Full path to store the image
        String filePath = uploadDir + fileName;

        // Save the file to the file system
        File dest = new File(filePath);
        file.transferTo(dest);

        // Create the profile image entity
        PersistentProfileImageEntity profileImageEntity = new PersistentProfileImageEntity();
        profileImageEntity.setExtension(imageExtension);
        profileImageEntity.setStored(true);
        profileImageEntity.setUrl(filePath);
        profileImageEntity.setUserDni(userDni);

        // Save the profile image entity to the database
        final var savedImageEntity = imageProfileEntityRepository.save(profileImageEntity);

        // Update the user's profile image and save the user entity
        userEntity.setProfileImage(savedImageEntity);

        return userRepository.save(userEntity);
    }

    @Transactional
    @Override
    public void unsubscribe(final String email, final String password) throws UserServiceException {
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
        final Optional<PersistentUserEntity> userOptional;

        userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new UserServiceException(USER_NOT_FOUND_MESSAGE);
        }
        final PersistentUserEntity userEntity;
        userEntity = userOptional.get();
        final var name = userForm.name();
        userEntity.setName(name);
        final var firstSurname = userForm.firstSurname();
        userEntity.setFirstSurname(firstSurname);
        final var secondSurname = userForm.secondSurname();
        userEntity.setSecondSurname(secondSurname);
        final var birthDate = userForm.birthDate();
        userEntity.setBirthDate(birthDate);
        final var gender = userForm.gender();
        userEntity.setGender(gender);

        return userRepository.save(userEntity);
    }

}
