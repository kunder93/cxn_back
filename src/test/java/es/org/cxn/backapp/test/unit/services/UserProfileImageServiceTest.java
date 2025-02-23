
package es.org.cxn.backapp.test.unit.services;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import es.org.cxn.backapp.model.persistence.PersistentProfileImageEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.repository.ImageProfileEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import es.org.cxn.backapp.service.impl.DefaultUserProfileImageService;
import es.org.cxn.backapp.service.impl.storage.DefaultImageStorageService;

/**
 * Unit tests for the {@link DefaultUserProfileImageService}. This class tests
 * various functionalities related to saving user profile images, including
 * handling invalid file extensions.
 *
 * @see DefaultUserProfileImageService
 */
class UserProfileImageServiceTest {

    /**
     * Mocked repository for managing {@link PersistentUserEntity}.
     */
    @Mock
    private UserEntityRepository userRepository;

    /**
     * Mocked repository for managing {@link PersistentProfileImageEntity}.
     */
    @Mock
    private ImageProfileEntityRepository imageProfileEntityRepository;

    /**
     * Mocked service for managing user operations.
     */
    @Mock
    private UserService userService;

    /**
     * Mocked service for handling image storage operations.
     */
    @Mock
    private DefaultImageStorageService imageStorageService;

    /**
     * Service under test that handles user profile image operations.
     */
    @InjectMocks
    private DefaultUserProfileImageService userProfileImageService;

    /**
     * Mocked entity representing a profile image.
     */
    @Mock
    private PersistentProfileImageEntity profileImageEntity;

    /**
     * Initializes mocks before each test method is executed.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests that saving a profile image file with an invalid extension throws a
     * {@link UserServiceException}.
     *
     * <p>
     * This test ensures that the
     * {@link DefaultUserProfileImageService#saveProfileImageFile(String, MockMultipartFile)}
     * method correctly identifies and rejects files with unsupported extensions.
     * </p>
     *
     * @throws UserServiceException if the save operation fails.
     */
    @Test
    void testSaveProfileImageFileInvalidExtensionThrowsUserServiceException() throws UserServiceException {
        // Arrange
        String userDni = "123456789";
        MockMultipartFile file = new MockMultipartFile("file", "image.txt", "text/plain", "image content".getBytes());

        when(userService.findByDni(userDni)).thenReturn(mock(PersistentUserEntity.class));

        // Act & Assert
        UserServiceException thrown = assertThrows(UserServiceException.class,
                () -> userProfileImageService.saveProfileImageFile(userDni, file));
        assertEquals("Invalid image extension: txt", thrown.getMessage());
    }
}
