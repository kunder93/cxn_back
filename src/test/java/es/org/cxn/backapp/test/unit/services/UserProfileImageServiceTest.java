
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
import es.org.cxn.backapp.service.impl.DefaultImageStorageService;
import es.org.cxn.backapp.service.impl.DefaultUserProfileImageService;

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

    @Mock
    private PersistentProfileImageEntity profileImageEntity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProfileImageFile_invalidExtension_throwsUserServiceException() throws UserServiceException {
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
