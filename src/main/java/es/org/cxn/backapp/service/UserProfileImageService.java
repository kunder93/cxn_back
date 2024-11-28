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

import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.form.responses.ProfileImageResponse;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;

/**
 * Service interface for managing user profile images. Provides methods for
 * retrieving and saving profile images for users.
 */
public interface UserProfileImageService {

    /**
     * Retrieves the profile image for a user identified by their DNI.
     *
     * @param dni the DNI (Document Number of Identification) of the user whose
     *            profile image is being retrieved.
     * @return a {@link ProfileImageResponse} object containing the profile image
     *         details.
     * @throws UserServiceException if an error occurs while retrieving the profile
     *                              image.
     */
    ProfileImageResponse getProfileImage(String dni) throws UserServiceException;

    /**
     * Saves the profile image for a user using the provided URL. If an existing
     * profile image is stored for the user, it will be deleted from the filesystem
     * before saving the new URL.
     *
     * @param userEmail the email of the user for whom the profile image is being
     *                  saved.
     * @param url       the URL where the profile image is located. This can be a
     *                  direct URL or a relative path to the image file.
     * @return the updated {@link PersistentUserEntity} object containing the user's
     *         profile image information.
     * @throws UserServiceException if there is an error while saving the profile
     *                              image, including issues with deleting an
     *                              existing image file.
     */
    PersistentUserEntity saveProfileImage(String userEmail, String url) throws UserServiceException;

    /**
     * Saves the profile image for a user based on the uploaded file. If an existing
     * profile image is stored for the user, it will be deleted from the filesystem
     * before saving the new image file.
     *
     * @param userDni the DNI (Document Number of Identification) of the user for
     *                whom the profile image is being saved.
     * @param file    the uploaded {@link MultipartFile} representing the profile
     *                image.
     * @return the updated {@link PersistentUserEntity} object containing the user's
     *         profile image information.
     * @throws UserServiceException if there is an error while saving the profile
     *                              image, including invalid image extensions or
     *                              file handling issues.
     */
    PersistentUserEntity saveProfileImageFile(String userDni, MultipartFile file) throws UserServiceException;
}
