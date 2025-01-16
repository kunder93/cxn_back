
package es.org.cxn.backapp.test.unit.response;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.form.responses.ActivityResponse;
import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;

class ActivityResponseTest {
    // Test the constructor with all fields
    @Test
    void testConstructorWithAllFields() {
        // Arrange
        String title = "Activity 1";
        String description = "Description of Activity 1";
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 16, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 16, 12, 0);
        String category = "Sports";
        byte[] image = { 1, 2, 3, 4 };

        ActivityResponse response = new ActivityResponse(title, description, startDate, endDate, category, image);

        // Act & Assert
        assertNotNull(response);
        assertEquals(title, response.title());
        assertEquals(description, response.description());
        assertEquals(startDate, response.startDate());
        assertEquals(endDate, response.endDate());
        assertEquals(category, response.category());
        assertArrayEquals(image, response.image()); // Ensure that the image is cloned
    }

    // Test the constructor with an ActivityEntity and an image byte array
    @Test
    void testConstructorWithEntityAndImage() {
        // Arrange
        String title = "Activity 1";
        String description = "Description of Activity 1";
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 16, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 16, 12, 0);
        String category = "Sports";
        byte[] image = { 1, 2, 3, 4 };

        PersistentActivityEntity entity = new PersistentActivityEntity();
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setCategory(category);

        ActivityResponse response = new ActivityResponse(entity, image);

        // Act & Assert
        assertNotNull(response);
        assertEquals(title, response.title());
        assertEquals(description, response.description());
        assertEquals(startDate, response.startDate());
        assertEquals(endDate, response.endDate());
        assertEquals(category, response.category());
        assertArrayEquals(image, response.image()); // Ensure that the image is cloned
    }

    // Test the equality of two different ActivityResponse objects
    @Test
    void testEqualsWithDifferentObjects() {
        // Arrange
        String title = "Activity 1";
        String description = "Description of Activity 1";
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 16, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 16, 12, 0);
        String category = "Sports";
        byte[] image = { 1, 2, 3, 4 };

        ActivityResponse response1 = new ActivityResponse(title, description, startDate, endDate, category, image);
        ActivityResponse response2 = new ActivityResponse("Different Title", description, startDate, endDate, category,
                image);

        // Act & Assert
        assertNotEquals(response1, response2); // Should not be equal due to different titles
    }

    // Test the equality of two identical ActivityResponse objects
    @Test
    void testEqualsWithIdenticalObjects() {
        // Arrange
        String title = "Activity 1";
        String description = "Description of Activity 1";
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 16, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 16, 12, 0);
        String category = "Sports";
        byte[] image = { 1, 2, 3, 4 };

        ActivityResponse response1 = new ActivityResponse(title, description, startDate, endDate, category, image);
        ActivityResponse response2 = new ActivityResponse(title, description, startDate, endDate, category, image);

        // Act & Assert
        assertEquals(response1, response2); // Should be equal
    }

    // Test hashCode consistency with equal objects
    @Test
    void testHashCodeConsistency() {
        // Arrange
        String title = "Activity 1";
        String description = "Description of Activity 1";
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 16, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 16, 12, 0);
        String category = "Sports";
        byte[] image = { 1, 2, 3, 4 };

        ActivityResponse response1 = new ActivityResponse(title, description, startDate, endDate, category, image);
        ActivityResponse response2 = new ActivityResponse(title, description, startDate, endDate, category, image);

        // Act & Assert
        assertEquals(response1.hashCode(), response2.hashCode()); // Hash codes should be equal for identical objects
    }

    // Test the defensive copy of the image
    @Test
    void testImageDefensiveCopy() {
        // Arrange
        byte[] image = { 1, 2, 3, 4 };
        ActivityResponse response = new ActivityResponse("Activity", "Description", LocalDateTime.now(),
                LocalDateTime.now(), "Sports", image);

        // Act
        byte[] responseImage = response.image(); // Get a defensive copy of the image
        responseImage[0] = 99; // Modify the response image

        // Assert
        // The original image should not be modified, so the first byte should still be
        // 1
        assertNotEquals(image[0], responseImage[0]); // The original image should not be modified
        assertEquals(1, image[0]); // The original image should remain unchanged
    }

    // Test the toString method
    @Test
    void testToString() {
        // Arrange
        String title = "Activity 1";
        String description = "Description of Activity 1";
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 16, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 16, 12, 0);
        String category = "Sports";
        byte[] image = { 1, 2, 3, 4 };

        ActivityResponse response = new ActivityResponse(title, description, startDate, endDate, category, image);

        // Act
        String toStringValue = response.toString();

        // Assert
        assertTrue(toStringValue.contains(title));
        assertTrue(toStringValue.contains(description));
        assertTrue(toStringValue.contains(startDate.toString()));
        assertTrue(toStringValue.contains(endDate.toString()));
        assertTrue(toStringValue.contains(category));
    }

}
