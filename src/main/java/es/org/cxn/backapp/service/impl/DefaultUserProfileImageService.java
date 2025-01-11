
package es.org.cxn.backapp.service.impl;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.form.responses.ProfileImageResponse;
import es.org.cxn.backapp.model.persistence.ImageExtension;
import es.org.cxn.backapp.model.persistence.PersistentProfileImageEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.repository.ImageProfileEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.UserProfileImageService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.exceptions.UserServiceException;

/**
 * Service for manage user profile image.
 */
@Service
public final class DefaultUserProfileImageService implements UserProfileImageService {

    /**
     * Path for profile's image.
     */
    @Value("${image.location.profiles}")
    private String imageLocationProfiles;

    /**
     * Repository for the user entities handled by the service.
     */
    private final UserEntityRepository userRepository;

    /**
     * Repository for image profile user data.
     */
    private final ImageProfileEntityRepository imageProfileEntityRepository;

    /**
     * User service for handle users who have profile images.
     */
    private final UserService userService;

    /**
     * The image storage service implementation.
     */
    private final DefaultImageStorageService imageStorageService;

    /**
     * Constructs a new {@code DefaultUserProfileImageService}.
     *
     * @param userRepo          the repository for user entities.
     * @param usrServ           the service for user operations.
     * @param imgRepo           the repository for profile image entities.
     * @param imgStorageService the image storage service implementation.
     * @throws NullPointerException if any of the parameters are {@code null}.
     */
    public DefaultUserProfileImageService(final UserEntityRepository userRepo, final UserService usrServ,

            final ImageProfileEntityRepository imgRepo, final DefaultImageStorageService imgStorageService) {
        super();
        userService = checkNotNull(usrServ, "Receiver a null pointer as user service.");
        this.userRepository = checkNotNull(userRepo, "Received a null pointer as user repository");
        this.imageProfileEntityRepository = checkNotNull(imgRepo,
                "Received a null pointer as image profile repository");
        this.imageStorageService = checkNotNull(imgStorageService, "Received a null pointer as image storage service");
    }

    @Override
    public ProfileImageResponse getProfileImage(final String dni) throws UserServiceException {
        final var user = userService.findByDni(dni);
        final var profileImage = user.getProfileImage();

        final ProfileImageResponse response;

        if (profileImage == null) {
            response = new ProfileImageResponse(null, null, null, null);
        } else if (profileImage.isStored().equals(Boolean.TRUE)) {
            // Load the image file from the filesystem
            final File imageFile = new File(profileImage.getUrl());

            if (!imageFile.exists()) {
                throw new UserServiceException("Profile image file not found at path: " + profileImage.getUrl());
            }

            try {
                // Read the image file and encode it in Base64
                final byte[] imageData = Files.readAllBytes(imageFile.toPath());
                final String base64Image = Base64.getEncoder().encodeToString(imageData);

                // Determine the MIME type based on the file extension
                final String mimeType;
                switch (profileImage.getExtension()) {
                case ImageExtension.PNG:
                    mimeType = "data:image/png;base64,";
                    break;
                case ImageExtension.JPG:
                    mimeType = "data:image/jpeg;base64,";
                    break;
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
                final String base64ImageWithPrefix = mimeType + base64Image;

                // Assign to response
                response = new ProfileImageResponse(profileImage, base64ImageWithPrefix);

            } catch (IOException e) {
                throw new UserServiceException("Error reading profile image file: " + e.getMessage(), e);
            }
        } else {
            // If the image is not stored, return the external URL
            response = new ProfileImageResponse(profileImage);
        }

        return response;
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
        PersistentUserEntity userEntity = (PersistentUserEntity) userService.findByEmail(userEmail);
        final var userDni = userEntity.getDni();
        final var imageProfileOptional = imageProfileEntityRepository.findById(userDni);
        if (imageProfileOptional.isPresent()) {
            final var image = imageProfileOptional.get();
            if (image.isStored().equals(Boolean.TRUE)) { // Borrar la imagen almacenada.
                // Get the file path from the existing image entity
                final String existingImagePath = image.getUrl(); // Assuming getUrl() returns the complete path
                // Create a File object
                final File existingFile = new File(existingImagePath);
                // Check if the file exists and delete it
                if (existingFile.exists()) {
                    final boolean deleted = existingFile.delete();
                    if (!deleted) {
                        // Handle the case where the file could not be deleted
                        throw new UserServiceException(
                                "Could not delete existing profile image file: " + existingImagePath);
                    }
                }
            }
        }
        userEntity = (PersistentUserEntity) userService.findByDni(userDni);
        final PersistentProfileImageEntity profileImageEntity = new PersistentProfileImageEntity();
        profileImageEntity.setExtension(ImageExtension.PROVIDED);
        profileImageEntity.setStored(false);
        profileImageEntity.setUrl(url);
        profileImageEntity.setUserDni(userDni);
        final var savedImageEntity = imageProfileEntityRepository.save(profileImageEntity);
        userEntity.setProfileImage(savedImageEntity);
        return userRepository.save(userEntity);
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
     * @throws UserServiceException if there is an error while saving the profile
     *                              image for the user, including invalid image
     *                              extensions or file handling issues.
     */
    @Override
    public PersistentUserEntity saveProfileImageFile(final String userDni, final MultipartFile file)
            throws UserServiceException {

        // Fetch the user entity by DNI
        final PersistentUserEntity userEntity = (PersistentUserEntity) userService.findByDni(userDni);

        // Get the original file name
        final String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new UserServiceException("Invalid file name");
        }

        // Split the filename to get the extension
        final String[] tokens = originalFileName.split("\\.");
        final String fileExtension = tokens[tokens.length - 1];

        // Validate the file extension
        final ImageExtension imageExtension = ImageExtension.fromString(fileExtension);
        if (imageExtension == null) {
            throw new UserServiceException("Invalid image extension: " + fileExtension);
        }

        // Use the image storage service to save the image
        final String savedImagePath;
        // Directory path for saving images (already loaded from properties file)
        final String uploadDir = imageLocationProfiles; // Use the property loaded from application properties
        try {
            savedImagePath = imageStorageService.saveImage(file, uploadDir, "profile", userDni);
        } catch (IOException e) {
            throw new UserServiceException("Error saving profile image: " + e.getMessage(), e);
        }

        // Create the profile image entity
        final PersistentProfileImageEntity profileImageEntity = new PersistentProfileImageEntity();
        profileImageEntity.setExtension(imageExtension);
        profileImageEntity.setStored(true);
        profileImageEntity.setUrl(savedImagePath); // Store the full path
        profileImageEntity.setUserDni(userDni);

        // Save the profile image entity to the database
        final var savedImageEntity = imageProfileEntityRepository.save(profileImageEntity);

        // Update the user's profile image and save the user entity
        userEntity.setProfileImage(savedImageEntity);

        return userRepository.save(userEntity);
    }

}
