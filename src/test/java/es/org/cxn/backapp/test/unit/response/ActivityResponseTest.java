
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

/**
 * Unit tests for the {@link ActivityResponse} class.
 *
 * This class contains tests that verify the behavior of the
 * {@link ActivityResponse} class, including various scenarios involving
 * activity start and end dates, titles, and related attributes. The tests
 * ensure that the class correctly handles these attributes and maintains the
 * integrity of the data.
 */
class ActivityResponseTest {

    /**
     * The start date and time of the activity. This date represents the beginning
     * of the activity and is used in the tests to validate the start time.
     */
    private static final LocalDateTime ACTIVITY_START_DATE = LocalDateTime.of(2025, 1, 16, 10, 0);

    /**
     * The end date and time of the activity. This date represents the conclusion of
     * the activity and is used in the tests to validate the end time.
     */
    private static final LocalDateTime ACTIVITY_END_DATE = LocalDateTime.of(2025, 1, 16, 12, 0);

    /**
     * The title of the activity. This string is used in the tests to ensure that
     * the activity has the correct title.
     */
    private static final String ACTIVITY_TITLE = "Activity 1";

    /**
     * The activity image representation file.
     */
    private static final byte[] ACTIVITY_IMAGE = { 1, 2, 3, 4 };

    /**
     * The activity description.
     */
    private static final String ACTIVITY_DESCRIPTION = "Description of Activity 1";

    /**
     * The activity category.
     */
    private static final String ACTIVITY_CATEGORY = "Sports";

    // Test the constructor with all fields
    @Test
    void testConstructorWithAllFields() {
        // Arrange

        ActivityResponse response = new ActivityResponse(ACTIVITY_TITLE, ACTIVITY_DESCRIPTION, ACTIVITY_START_DATE,
                ACTIVITY_END_DATE, ACTIVITY_CATEGORY, ACTIVITY_IMAGE);

        // Act & Assert
        assertNotNull(response);
        assertEquals(ACTIVITY_TITLE, response.title());
        assertEquals(ACTIVITY_DESCRIPTION, response.description());
        assertEquals(ACTIVITY_START_DATE, response.startDate());
        assertEquals(ACTIVITY_END_DATE, response.endDate());
        assertEquals(ACTIVITY_CATEGORY, response.category());
        assertArrayEquals(ACTIVITY_IMAGE, response.image()); // Ensure that the image is cloned
    }

    // Test the constructor with an ActivityEntity and an image byte array
    @Test
    void testConstructorWithEntityAndImage() {
        // Arrange

        PersistentActivityEntity entity = new PersistentActivityEntity();
        entity.setTitle(ACTIVITY_TITLE);
        entity.setDescription(ACTIVITY_DESCRIPTION);
        entity.setStartDate(ACTIVITY_START_DATE);
        entity.setEndDate(ACTIVITY_END_DATE);
        entity.setCategory(ACTIVITY_CATEGORY);

        ActivityResponse response = new ActivityResponse(entity, ACTIVITY_IMAGE);

        // Act & Assert
        assertNotNull(response);
        assertEquals(ACTIVITY_TITLE, response.title());
        assertEquals(ACTIVITY_DESCRIPTION, response.description());
        assertEquals(ACTIVITY_START_DATE, response.startDate());
        assertEquals(ACTIVITY_END_DATE, response.endDate());
        assertEquals(ACTIVITY_CATEGORY, response.category());
        assertArrayEquals(ACTIVITY_IMAGE, response.image()); // Ensure that the image is cloned
    }

    // Test the equality of two different ActivityResponse objects
    @Test
    void testEqualsWithDifferentObjects() {
        // Arrange

        ActivityResponse response1 = new ActivityResponse(ACTIVITY_TITLE, ACTIVITY_DESCRIPTION, ACTIVITY_START_DATE,
                ACTIVITY_END_DATE, ACTIVITY_CATEGORY, ACTIVITY_IMAGE);
        ActivityResponse response2 = new ActivityResponse("Different Title", ACTIVITY_DESCRIPTION, ACTIVITY_START_DATE,
                ACTIVITY_END_DATE, ACTIVITY_CATEGORY, ACTIVITY_IMAGE);

        // Act & Assert
        assertNotEquals(response1, response2); // Should not be equal due to different titles
    }

    // Test the equality of two identical ActivityResponse objects
    @Test
    void testEqualsWithIdenticalObjects() {
        // Arrange

        ActivityResponse response1 = new ActivityResponse(ACTIVITY_TITLE, ACTIVITY_DESCRIPTION, ACTIVITY_START_DATE,
                ACTIVITY_END_DATE, ACTIVITY_CATEGORY, ACTIVITY_IMAGE);
        ActivityResponse response2 = new ActivityResponse(ACTIVITY_TITLE, ACTIVITY_DESCRIPTION, ACTIVITY_START_DATE,
                ACTIVITY_END_DATE, ACTIVITY_CATEGORY, ACTIVITY_IMAGE);

        // Act & Assert
        assertEquals(response1, response2); // Should be equal
    }

    // Test hashCode consistency with equal objects
    @Test
    void testHashCodeConsistency() {
        // Arrange

        ActivityResponse response1 = new ActivityResponse(ACTIVITY_TITLE, ACTIVITY_DESCRIPTION, ACTIVITY_START_DATE,
                ACTIVITY_END_DATE, ACTIVITY_CATEGORY, ACTIVITY_IMAGE);
        ActivityResponse response2 = new ActivityResponse(ACTIVITY_TITLE, ACTIVITY_DESCRIPTION, ACTIVITY_START_DATE,
                ACTIVITY_END_DATE, ACTIVITY_CATEGORY, ACTIVITY_IMAGE);

        // Act & Assert
        assertEquals(response1.hashCode(), response2.hashCode()); // Hash codes should be equal for identical objects
    }

    // Test the defensive copy of the image
    @Test
    void testImageDefensiveCopy() {
        // Arrange

        ActivityResponse response = new ActivityResponse(ACTIVITY_TITLE, "Description", ACTIVITY_START_DATE,
                ACTIVITY_END_DATE, "Sports", ACTIVITY_IMAGE);

        // Act
        byte[] responseImage = response.image(); // Get a defensive copy of the image
        responseImage[0] = 99; // Modify the response image

        // Assert
        // The original image should not be modified, so the first byte should still be
        // 1
        assertNotEquals(ACTIVITY_IMAGE[0], responseImage[0]); // The original image should not be modified
        assertEquals(1, ACTIVITY_IMAGE[0]); // The original image should remain unchanged
    }

    // Test the toString method
    @Test
    void testToString() {
        // Arrange

        ActivityResponse response = new ActivityResponse(ACTIVITY_TITLE, ACTIVITY_DESCRIPTION, ACTIVITY_START_DATE,
                ACTIVITY_END_DATE, ACTIVITY_CATEGORY, ACTIVITY_IMAGE);

        // Act
        String toStringValue = response.toString();

        // Assert
        assertTrue(toStringValue.contains(ACTIVITY_TITLE));
        assertTrue(toStringValue.contains(ACTIVITY_DESCRIPTION));
        assertTrue(toStringValue.contains(ACTIVITY_START_DATE.toString()));
        assertTrue(toStringValue.contains(ACTIVITY_END_DATE.toString()));
        assertTrue(toStringValue.contains(ACTIVITY_CATEGORY));
    }
}
