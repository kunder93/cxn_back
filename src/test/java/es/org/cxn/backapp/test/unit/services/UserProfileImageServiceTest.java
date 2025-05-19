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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.form.responses.user.ProfileImageResponse;
import es.org.cxn.backapp.model.persistence.ImageExtension;
import es.org.cxn.backapp.model.persistence.PersistentProfileImageEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.repository.ImageProfileEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import es.org.cxn.backapp.service.impl.DefaultUserProfileImageService;
import es.org.cxn.backapp.service.impl.storage.DefaultImageStorageService;
import es.org.cxn.backapp.service.impl.storage.FileLocation;

class UserProfileImageServiceTest {

    @Mock
    private UserEntityRepository userRepository;

    @Mock
    private ImageProfileEntityRepository imageProfileEntityRepository;

    @Mock
    private UserService userService;

    @Mock
    private DefaultImageStorageService imageStorageService;

    @InjectMocks
    private DefaultUserProfileImageService userProfileImageService;

    @Test
    void getProfileImage_shouldReturnEmptyResponse_whenNoImageExists() throws Exception {
        String dni = "12345678A";
        PersistentUserEntity user = new PersistentUserEntity();
        user.setDni(dni);
        user.setProfileImage(null);

        when(userService.findByDni(dni)).thenReturn(user);

        ProfileImageResponse response = userProfileImageService.getProfileImage(dni);

        assertNull(response.url());
    }

    @Test
    void getProfileImage_shouldReturnImageResponse_whenStoredImageExists() throws Exception {
        final String dni = "12345678A";

        PersistentProfileImageEntity profileImage = new PersistentProfileImageEntity();
        profileImage.setStored(true);
        profileImage.setUrl("src/test/resources/sample.png");
        profileImage.setExtension(ImageExtension.PNG);

        PersistentUserEntity user = new PersistentUserEntity();
        user.setDni(dni);
        user.setProfileImage(profileImage);

        when(userService.findByDni(dni)).thenReturn(user);

        // Crear archivo de prueba
        File testFile = new File("src/test/resources/sample.png");
        testFile.getParentFile().mkdirs();
        Files.write(testFile.toPath(), new byte[] { 1, 2, 3 });

        ProfileImageResponse response = userProfileImageService.getProfileImage(dni);

        assertNotNull(response.url());

        // Clean up
        testFile.delete();
    }

    @Test
    void saveProfileImage_shouldCreateNew_whenNoPreviousImageExists() throws Exception {
        String email = "test@example.com";
        String dni = "12345678A";
        String imageUrl = "http://example.com/img.png";

        PersistentUserEntity user = new PersistentUserEntity();
        user.setDni(dni);

        when(userService.findByEmail(email)).thenReturn(user);
        when(imageProfileEntityRepository.findById(dni)).thenReturn(Optional.empty());
        when(imageProfileEntityRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PersistentUserEntity result = userProfileImageService.saveProfileImage(email, imageUrl);

        assertEquals(dni, result.getDni());
        verify(imageProfileEntityRepository).save(any());
    }

    @Test
    void saveProfileImage_shouldDeleteOld_whenStoredTrue() throws Exception {
        String email = "test@example.com";
        String dni = "12345678A";
        String imageUrl = "http://example.com/new.png";

        File oldImage = File.createTempFile("old-profile", ".jpg");
        oldImage.deleteOnExit();

        PersistentUserEntity user = new PersistentUserEntity();
        user.setDni(dni);

        PersistentProfileImageEntity oldProfile = new PersistentProfileImageEntity();
        oldProfile.setStored(true);
        oldProfile.setUrl(oldImage.getAbsolutePath());

        when(userService.findByEmail(email)).thenReturn(user);
        when(imageProfileEntityRepository.findById(dni)).thenReturn(Optional.of(oldProfile));
        when(imageProfileEntityRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PersistentUserEntity result = userProfileImageService.saveProfileImage(email, imageUrl);

        assertEquals(dni, result.getDni());
        verify(imageProfileEntityRepository).save(any());
        assertFalse(oldImage.exists());
    }

    @Test
    void saveProfileImageFile_shouldStoreImage_whenValidFileProvided() throws Exception {
        String dni = "12345678A";
        PersistentUserEntity user = new PersistentUserEntity();
        user.setDni(dni);

        MockMultipartFile file = new MockMultipartFile("file", "photo.png", "image/png", new byte[] { 1, 2, 3 });

        when(userService.findByDni(dni)).thenReturn(user);
        when(imageStorageService.saveImage(any(), eq(FileLocation.PROFILE_IMAGES), eq(dni)))
                .thenReturn("/fake/path/photo.png");
        when(imageProfileEntityRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PersistentUserEntity result = userProfileImageService.saveProfileImageFile(dni, file);

        assertEquals(dni, result.getDni());
        verify(imageStorageService).saveImage(any(), eq(FileLocation.PROFILE_IMAGES), eq(dni));
    }

    @Test
    void saveProfileImageFile_shouldThrow_whenInvalidExtension() throws UserServiceException {
        String dni = "12345678A";
        PersistentUserEntity user = new PersistentUserEntity();
        user.setDni(dni);

        MockMultipartFile file = new MockMultipartFile("file", "photo.txt", "text/plain", new byte[] { 1, 2 });

        when(userService.findByDni(dni)).thenReturn(user);

        UserServiceException ex = assertThrows(UserServiceException.class,
                () -> userProfileImageService.saveProfileImageFile(dni, file));

        assertTrue(ex.getMessage().contains("Invalid image extension"));
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProfileImageFile_shouldThrowException_whenOriginalFilenameIsEmpty() throws UserServiceException {
        // Arrange
        final String dni = "12345678A";
        final MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("");

        final PersistentUserEntity mockUser = new PersistentUserEntity();
        when(userService.findByDni(dni)).thenReturn(mockUser);

        // Act & Assert
        UserServiceException ex = assertThrows(UserServiceException.class, () -> {
            userProfileImageService.saveProfileImageFile(dni, mockFile);
        });
        assertEquals("Invalid file name", ex.getMessage());
    }

    @Test
    void testSaveProfileImageFile_shouldThrowException_whenOriginalFilenameIsNull() throws UserServiceException {
        // Arrange
        final String dni = "12345678A";
        final MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn(null);

        final PersistentUserEntity mockUser = new PersistentUserEntity();
        when(userService.findByDni(dni)).thenReturn(mockUser);

        // Act & Assert
        UserServiceException ex = assertThrows(UserServiceException.class, () -> {
            userProfileImageService.saveProfileImageFile(dni, mockFile);
        });
        assertEquals("Invalid file name", ex.getMessage());
    }
}
