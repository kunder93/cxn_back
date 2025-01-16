
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

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.form.responses.CreatedActivityResponse;
import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;

/**
 * Unit tests for the CreatedActivityResponse class. This test class validates
 * the proper functionality of the CreatedActivityResponse object, ensuring that
 * it accurately represents the response structure for created activities.
 */
class CreatedActivityResponseTest {

    /**
     * Constant representing the start date and time of the activity. Used to test
     * the proper handling of the activity's start date and time in the response.
     */
    private static final LocalDateTime ACTIVITY_START_DATE = LocalDateTime.of(2025, 5, 15, 9, 0);

    /**
     * Constant representing the end date and time of the activity. Used to test the
     * proper handling of the activity's end date and time in the response.
     */
    private static final LocalDateTime ACTIVITY_END_DATE = LocalDateTime.of(2025, 5, 15, 18, 0);

    /**
     * Constant representing the title of the activity. Used to test the accurate
     * representation of the activity's title in the response.
     */
    private static final String ACTIVITY_TITLE = "Chess Tournament";

    /**
     * Constant representing the description of the activity. Used to test the
     * accurate representation of the activity's description in the response.
     */
    private static final String ACTIVITY_DESCRIPTION = "A chess competition for beginners.";

    /**
     * Mock object for the PersistentActivityEntity class. Used to simulate the
     * persistence layer behavior and to validate response creation.
     */
    private PersistentActivityEntity activityEntityMock;

    @BeforeEach
    void setUp() {
        activityEntityMock = mock(PersistentActivityEntity.class);
    }

    @Test
    void testCreatedActivityResponseConstructor() {
        // Arrange

        String category = "Tournament";
        LocalDateTime createdAt = LocalDateTime.now();

        // Mock behavior
        when(activityEntityMock.getTitle()).thenReturn(ACTIVITY_TITLE);
        when(activityEntityMock.getDescription()).thenReturn(ACTIVITY_DESCRIPTION);
        when(activityEntityMock.getStartDate()).thenReturn(ACTIVITY_START_DATE);
        when(activityEntityMock.getEndDate()).thenReturn(ACTIVITY_END_DATE);
        when(activityEntityMock.getCategory()).thenReturn(category);
        when(activityEntityMock.getCreatedAt()).thenReturn(createdAt);

        // Act
        CreatedActivityResponse response = new CreatedActivityResponse(activityEntityMock);

        // Assert
        assertEquals(ACTIVITY_TITLE, response.title());
        assertEquals(ACTIVITY_DESCRIPTION, response.description());
        assertEquals(ACTIVITY_START_DATE, response.startDate());
        assertEquals(ACTIVITY_END_DATE, response.endDate());
        assertEquals(category, response.category());
        assertEquals(createdAt, response.createdAt());
    }

    @Test
    void testCreatedActivityResponseEmptyDescription() {
        // Arrange

        String category = "Workshop";
        LocalDateTime createdAt = LocalDateTime.now();

        // Mock behavior
        when(activityEntityMock.getTitle()).thenReturn(ACTIVITY_TITLE);
        when(activityEntityMock.getDescription()).thenReturn(ACTIVITY_DESCRIPTION);
        when(activityEntityMock.getStartDate()).thenReturn(ACTIVITY_START_DATE);
        when(activityEntityMock.getEndDate()).thenReturn(ACTIVITY_END_DATE);
        when(activityEntityMock.getCategory()).thenReturn(category);
        when(activityEntityMock.getCreatedAt()).thenReturn(createdAt);

        // Act
        CreatedActivityResponse response = new CreatedActivityResponse(activityEntityMock);

        // Assert
        assertEquals(ACTIVITY_TITLE, response.title());
        assertEquals(ACTIVITY_DESCRIPTION, response.description()); // Description should be empty
        assertEquals(ACTIVITY_START_DATE, response.startDate());
        assertEquals(ACTIVITY_END_DATE, response.endDate());
        assertEquals(category, response.category());
        assertEquals(createdAt, response.createdAt());
    }

    @Test
    void testCreatedActivityResponseNullCategory() {
        // Arrange

        String category = null;
        LocalDateTime createdAt = LocalDateTime.now();

        // Mock behavior
        when(activityEntityMock.getTitle()).thenReturn(ACTIVITY_TITLE);
        when(activityEntityMock.getDescription()).thenReturn(ACTIVITY_DESCRIPTION);
        when(activityEntityMock.getStartDate()).thenReturn(ACTIVITY_START_DATE);
        when(activityEntityMock.getEndDate()).thenReturn(ACTIVITY_END_DATE);
        when(activityEntityMock.getCategory()).thenReturn(category);
        when(activityEntityMock.getCreatedAt()).thenReturn(createdAt);

        // Act
        CreatedActivityResponse response = new CreatedActivityResponse(activityEntityMock);

        // Assert
        assertEquals(ACTIVITY_TITLE, response.title());
        assertEquals(ACTIVITY_DESCRIPTION, response.description());
        assertEquals(ACTIVITY_START_DATE, response.startDate());
        assertEquals(ACTIVITY_END_DATE, response.endDate());
        assertNull(response.category()); // Category should be null
        assertEquals(createdAt, response.createdAt());
    }

    @Test
    void testCreatedActivityResponseNullEntity() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new CreatedActivityResponse(null),
                "Should throw NullPointerException when the entity is null");
    }

    @Test
    void testCreatedActivityResponseNullStartDate() {
        // Arrange

        LocalDateTime startDate = null;
        String category = "Class";
        LocalDateTime createdAt = LocalDateTime.now();

        // Mock behavior
        when(activityEntityMock.getTitle()).thenReturn(ACTIVITY_TITLE);
        when(activityEntityMock.getDescription()).thenReturn(ACTIVITY_DESCRIPTION);
        when(activityEntityMock.getStartDate()).thenReturn(startDate);
        when(activityEntityMock.getEndDate()).thenReturn(ACTIVITY_END_DATE);
        when(activityEntityMock.getCategory()).thenReturn(category);
        when(activityEntityMock.getCreatedAt()).thenReturn(createdAt);

        // Act
        CreatedActivityResponse response = new CreatedActivityResponse(activityEntityMock);

        // Assert
        assertEquals(ACTIVITY_TITLE, response.title());
        assertEquals(ACTIVITY_DESCRIPTION, response.description());
        assertNull(response.startDate()); // Start date should be null
        assertEquals(ACTIVITY_END_DATE, response.endDate());
        assertEquals(category, response.category());
        assertEquals(createdAt, response.createdAt());
    }
}
