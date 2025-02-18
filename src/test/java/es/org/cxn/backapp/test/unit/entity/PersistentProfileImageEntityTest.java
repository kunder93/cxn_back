
package es.org.cxn.backapp.test.unit.entity;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.ImageExtension;
import es.org.cxn.backapp.model.persistence.PersistentProfileImageEntity;

class PersistentProfileImageEntityTest {

    /**
     * Entity that represents profile image.
     */
    private PersistentProfileImageEntity profileImageEntity;

    @BeforeEach
    void setUp() {
        profileImageEntity = new PersistentProfileImageEntity();
    }

    @Test
    void testDefaultValues() {
        assertNotNull(profileImageEntity, "ProfileImageEntity should be initialized.");
        assertNull(profileImageEntity.getUserDni(), "Default userDni should be null.");
        assertNull(profileImageEntity.getUrl(), "Default URL should be null.");
        assertNull(profileImageEntity.getExtension(), "Default extension should be null.");
        assertNull(profileImageEntity.isStored(), "Default stored flag should be null.");
        assertNull(profileImageEntity.getUser(), "Default user should be null.");
    }

    @Test
    void testFullInitialization() {
        String userDni = "12345678X";
        String url = "https://example.com/image.jpg";
        ImageExtension extension = ImageExtension.PNG;

        profileImageEntity.setUserDni(userDni);
        profileImageEntity.setUrl(url);
        profileImageEntity.setExtension(extension);
        profileImageEntity.setStored(false);

        assertEquals(userDni, profileImageEntity.getUserDni(), "User DNI should match.");
        assertEquals(url, profileImageEntity.getUrl(), "URL should match.");
        assertEquals(extension, profileImageEntity.getExtension(), "Extension should match.");
        assertFalse(profileImageEntity.isStored(), "Stored flag should match.");
    }

    @Test
    void testNullValuesHandling() {
        profileImageEntity.setUserDni(null);
        profileImageEntity.setUrl(null);
        profileImageEntity.setExtension(null);
        profileImageEntity.setStored(null);

        assertNull(profileImageEntity.getUserDni(), "User DNI should be null.");
        assertNull(profileImageEntity.getUrl(), "URL should be null.");
        assertNull(profileImageEntity.getExtension(), "Extension should be null.");
        assertNull(profileImageEntity.isStored(), "Stored flag should be null.");
        assertNull(profileImageEntity.getUser(), "User should be null.");
    }

    @Test
    void testSetAndGetExtension() {
        ImageExtension extension = ImageExtension.JPG;
        profileImageEntity.setExtension(extension);
        assertEquals(extension, profileImageEntity.getExtension(), "Extension should be set and retrieved correctly.");
    }

    @Test
    void testSetAndGetStored() {
        profileImageEntity.setStored(true);
        assertTrue(profileImageEntity.isStored(), "Stored flag should be set and retrieved correctly.");
    }

    @Test
    void testSetAndGetUrl() {
        String url = "https://example.com/image.jpg";
        profileImageEntity.setUrl(url);
        assertEquals(url, profileImageEntity.getUrl(), "URL should be set and retrieved correctly.");
    }

    @Test
    void testSetAndGetUserDni() {
        String userDni = "12345678X";
        profileImageEntity.setUserDni(userDni);
        assertEquals(userDni, profileImageEntity.getUserDni(), "User DNI should be set and retrieved correctly.");
    }
}
