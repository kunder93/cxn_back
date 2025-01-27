
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.ImageExtension;

class ImageExtensionTest {

    @Test
    void testFromStringInvalidExtensions() {
        assertNull(ImageExtension.fromString("invalid"), "Should return null for an invalid extension.");
        assertNull(ImageExtension.fromString(""), "Should return null for an empty string.");
        assertNull(ImageExtension.fromString(null), "Should return null for a null value.");
    }

    @Test
    void testFromStringValidExtensions() {
        assertEquals(ImageExtension.AVIF, ImageExtension.fromString("avif"), "Should return AVIF for 'avif'.");
        assertEquals(ImageExtension.WEBP, ImageExtension.fromString("webp"), "Should return WEBP for 'webp'.");
        assertEquals(ImageExtension.JPEG, ImageExtension.fromString("jpeg"), "Should return JPEG for 'jpeg'.");
        assertEquals(ImageExtension.JPG, ImageExtension.fromString("jpg"), "Should return JPG for 'jpg'.");
        assertEquals(ImageExtension.PNG, ImageExtension.fromString("png"), "Should return PNG for 'png'.");
        assertEquals(ImageExtension.PROVIDED, ImageExtension.fromString("provided"),
                "Should return PROVIDED for 'provided'.");
    }

    @Test
    void testGetExtension() {
        assertEquals("avif", ImageExtension.AVIF.getExtension(), "AVIF should return 'avif' as its extension.");
        assertEquals("webp", ImageExtension.WEBP.getExtension(), "WEBP should return 'webp' as its extension.");
        assertEquals("jpeg", ImageExtension.JPEG.getExtension(), "JPEG should return 'jpeg' as its extension.");
        assertEquals("jpg", ImageExtension.JPG.getExtension(), "JPG should return 'jpg' as its extension.");
        assertEquals("png", ImageExtension.PNG.getExtension(), "PNG should return 'png' as its extension.");
        assertEquals("provided", ImageExtension.PROVIDED.getExtension(),
                "PROVIDED should return 'provided' as its extension.");
    }

    @Test
    void testIsValidExtensionInvalid() {
        assertFalse(ImageExtension.isValidExtension("invalid"), "'invalid' should not be a valid extension.");
        assertFalse(ImageExtension.isValidExtension(""), "An empty string should not be a valid extension.");
        assertFalse(ImageExtension.isValidExtension(null), "Null should not be a valid extension.");
    }

    @Test
    void testIsValidExtensionValid() {
        assertTrue(ImageExtension.isValidExtension("avif"), "'avif' should be a valid extension.");
        assertTrue(ImageExtension.isValidExtension("webp"), "'webp' should be a valid extension.");
        assertTrue(ImageExtension.isValidExtension("jpeg"), "'jpeg' should be a valid extension.");
        assertTrue(ImageExtension.isValidExtension("jpg"), "'jpg' should be a valid extension.");
        assertTrue(ImageExtension.isValidExtension("png"), "'png' should be a valid extension.");
        assertTrue(ImageExtension.isValidExtension("provided"), "'provided' should be a valid extension.");
    }

    @Test
    void testToString() {
        assertEquals("avif", ImageExtension.AVIF.toString(), "AVIF should return 'avif' as its string representation.");
        assertEquals("webp", ImageExtension.WEBP.toString(), "WEBP should return 'webp' as its string representation.");
        assertEquals("jpeg", ImageExtension.JPEG.toString(), "JPEG should return 'jpeg' as its string representation.");
        assertEquals("jpg", ImageExtension.JPG.toString(), "JPG should return 'jpg' as its string representation.");
        assertEquals("png", ImageExtension.PNG.toString(), "PNG should return 'png' as its string representation.");
        assertEquals("provided", ImageExtension.PROVIDED.toString(),
                "PROVIDED should return 'provided' as its string representation.");
    }
}
