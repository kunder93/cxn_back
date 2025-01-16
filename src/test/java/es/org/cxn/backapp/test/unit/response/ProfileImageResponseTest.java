
package es.org.cxn.backapp.test.unit.response;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.form.responses.ProfileImageResponse;
import es.org.cxn.backapp.model.persistence.ImageExtension;
import es.org.cxn.backapp.model.persistence.PersistentProfileImageEntity;

class ProfileImageResponseTest {
    /**
     * The ProfileImage mocked entity.
     */
    private PersistentProfileImageEntity profileImageEntityMock;

    @BeforeEach
    void setUp() {
        profileImageEntityMock = mock(PersistentProfileImageEntity.class);
    }

    @Test
    void testProfileImageResponseConstructorNullFile() {
        // Arrange
        String expectedExtension = "PNG";
        Boolean expectedStored = true;
        String expectedUrl = "https://example.com/profile-image.png";

        // Mock behavior
        when(profileImageEntityMock.getExtension()).thenReturn(ImageExtension.PNG);
        when(profileImageEntityMock.isStored()).thenReturn(true);
        when(profileImageEntityMock.getUrl()).thenReturn(expectedUrl);

        // Act
        ProfileImageResponse response = new ProfileImageResponse(profileImageEntityMock);

        // Assert
        assertEquals(expectedExtension, response.imageExtension());
        assertEquals(expectedStored, response.stored());
        assertEquals(expectedUrl, response.url());
        assertNull(response.file()); // The file data should be null by default
    }

    @Test
    void testProfileImageResponseConstructorWithFile() {
        // Arrange
        String expectedExtension = "PNG";
        Boolean expectedStored = false;
        String expectedUrl = "https://example.com/profile-image.png";
        String imageFileData = "filedata";

        // Mock behavior
        when(profileImageEntityMock.getExtension()).thenReturn(ImageExtension.PNG);
        when(profileImageEntityMock.isStored()).thenReturn(false);
        when(profileImageEntityMock.getUrl()).thenReturn(expectedUrl);

        // Act
        ProfileImageResponse response = new ProfileImageResponse(profileImageEntityMock, imageFileData);

        // Assert
        assertEquals(expectedExtension, response.imageExtension());
        assertEquals(expectedStored, response.stored());
        assertEquals(expectedUrl, response.url());
        assertEquals(imageFileData, response.file()); // The file data should be passed correctly
    }

    @Test
    void testProfileImageResponseConstructorWithoutFile() {
        // Arrange
        String expectedExtension = "JPEG";
        Boolean expectedStored = true;
        String expectedUrl = "https://example.com/profile-image.jpeg";

        // Mock behavior
        when(profileImageEntityMock.getExtension()).thenReturn(ImageExtension.JPEG);
        when(profileImageEntityMock.isStored()).thenReturn(true);
        when(profileImageEntityMock.getUrl()).thenReturn(expectedUrl);

        // Act
        ProfileImageResponse response = new ProfileImageResponse(profileImageEntityMock);

        // Assert
        assertEquals(expectedExtension, response.imageExtension());
        assertEquals(expectedStored, response.stored());
        assertEquals(expectedUrl, response.url());
        assertNull(response.file()); // The file data should be null
    }

    @Test
    void testProfileImageResponseEmptyUrl() {
        // Arrange
        String expectedExtension = "WEBP";
        Boolean expectedStored = true;
        String emptyUrl = "";

        // Mock behavior
        when(profileImageEntityMock.getExtension()).thenReturn(ImageExtension.WEBP);
        when(profileImageEntityMock.isStored()).thenReturn(true);
        when(profileImageEntityMock.getUrl()).thenReturn(emptyUrl);

        // Act
        ProfileImageResponse response = new ProfileImageResponse(profileImageEntityMock);

        // Assert
        assertEquals(expectedExtension, response.imageExtension());
        assertEquals(expectedStored, response.stored());
        assertEquals(emptyUrl, response.url());
        assertNull(response.file()); // The file data should be null
    }

    @Test
    void testProfileImageResponseWithNullEntity() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new ProfileImageResponse(null),
                "Should throw NullPointerException when the entity is null");
    }
}
