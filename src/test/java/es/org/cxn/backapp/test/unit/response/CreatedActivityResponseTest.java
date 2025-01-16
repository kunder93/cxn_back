
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

class CreatedActivityResponseTest {
    private PersistentActivityEntity activityEntityMock;

    @BeforeEach
    void setUp() {
        activityEntityMock = mock(PersistentActivityEntity.class);
    }

    @Test
    void testCreatedActivityResponseConstructor() {
        // Arrange
        String title = "Chess Tournament";
        String description = "A chess competition for beginners.";
        LocalDateTime startDate = LocalDateTime.of(2025, 5, 15, 9, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 5, 15, 18, 0);
        String category = "Tournament";
        LocalDateTime createdAt = LocalDateTime.now();

        // Mock behavior
        when(activityEntityMock.getTitle()).thenReturn(title);
        when(activityEntityMock.getDescription()).thenReturn(description);
        when(activityEntityMock.getStartDate()).thenReturn(startDate);
        when(activityEntityMock.getEndDate()).thenReturn(endDate);
        when(activityEntityMock.getCategory()).thenReturn(category);
        when(activityEntityMock.getCreatedAt()).thenReturn(createdAt);

        // Act
        CreatedActivityResponse response = new CreatedActivityResponse(activityEntityMock);

        // Assert
        assertEquals(title, response.title());
        assertEquals(description, response.description());
        assertEquals(startDate, response.startDate());
        assertEquals(endDate, response.endDate());
        assertEquals(category, response.category());
        assertEquals(createdAt, response.createdAt());
    }

    @Test
    void testCreatedActivityResponseEmptyDescription() {
        // Arrange
        String title = "Chess Workshop";
        String description = "";
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 1, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 6, 1, 12, 0);
        String category = "Workshop";
        LocalDateTime createdAt = LocalDateTime.now();

        // Mock behavior
        when(activityEntityMock.getTitle()).thenReturn(title);
        when(activityEntityMock.getDescription()).thenReturn(description);
        when(activityEntityMock.getStartDate()).thenReturn(startDate);
        when(activityEntityMock.getEndDate()).thenReturn(endDate);
        when(activityEntityMock.getCategory()).thenReturn(category);
        when(activityEntityMock.getCreatedAt()).thenReturn(createdAt);

        // Act
        CreatedActivityResponse response = new CreatedActivityResponse(activityEntityMock);

        // Assert
        assertEquals(title, response.title());
        assertEquals(description, response.description()); // Description should be empty
        assertEquals(startDate, response.startDate());
        assertEquals(endDate, response.endDate());
        assertEquals(category, response.category());
        assertEquals(createdAt, response.createdAt());
    }

    @Test
    void testCreatedActivityResponseNullCategory() {
        // Arrange
        String title = "Chess Show";
        String description = "An exhibition of chess matches.";
        LocalDateTime startDate = LocalDateTime.of(2025, 7, 1, 15, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 7, 1, 17, 0);
        String category = null;
        LocalDateTime createdAt = LocalDateTime.now();

        // Mock behavior
        when(activityEntityMock.getTitle()).thenReturn(title);
        when(activityEntityMock.getDescription()).thenReturn(description);
        when(activityEntityMock.getStartDate()).thenReturn(startDate);
        when(activityEntityMock.getEndDate()).thenReturn(endDate);
        when(activityEntityMock.getCategory()).thenReturn(category);
        when(activityEntityMock.getCreatedAt()).thenReturn(createdAt);

        // Act
        CreatedActivityResponse response = new CreatedActivityResponse(activityEntityMock);

        // Assert
        assertEquals(title, response.title());
        assertEquals(description, response.description());
        assertEquals(startDate, response.startDate());
        assertEquals(endDate, response.endDate());
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
        String title = "Chess Class";
        String description = "A class for chess enthusiasts.";
        LocalDateTime startDate = null;
        LocalDateTime endDate = LocalDateTime.of(2025, 5, 20, 16, 0);
        String category = "Class";
        LocalDateTime createdAt = LocalDateTime.now();

        // Mock behavior
        when(activityEntityMock.getTitle()).thenReturn(title);
        when(activityEntityMock.getDescription()).thenReturn(description);
        when(activityEntityMock.getStartDate()).thenReturn(startDate);
        when(activityEntityMock.getEndDate()).thenReturn(endDate);
        when(activityEntityMock.getCategory()).thenReturn(category);
        when(activityEntityMock.getCreatedAt()).thenReturn(createdAt);

        // Act
        CreatedActivityResponse response = new CreatedActivityResponse(activityEntityMock);

        // Assert
        assertEquals(title, response.title());
        assertEquals(description, response.description());
        assertNull(response.startDate()); // Start date should be null
        assertEquals(endDate, response.endDate());
        assertEquals(category, response.category());
        assertEquals(createdAt, response.createdAt());
    }
}