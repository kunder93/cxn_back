
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;

class PersistentActivityEntityTest {

    private PersistentActivityEntity activityEntity;

    @BeforeEach
    void setUp() {
        activityEntity = new PersistentActivityEntity();
    }

    @Test
    void testDefaultValues() {
        assertNotNull(activityEntity);
        assertEquals("", activityEntity.getTitle(), "Default title should be an empty string.");
        assertEquals("", activityEntity.getDescription(), "Default description should be an empty string.");
        assertNull(activityEntity.getStartDate(), "Default startDate should be null.");
        assertNull(activityEntity.getEndDate(), "Default endDate should be null.");
        assertNull(activityEntity.getCreatedAt(), "Default createdAt should be null.");
        assertEquals("", activityEntity.getCategory(), "Default category should be an empty string.");
        assertNull(activityEntity.getImageSrc(), "Default imageSrc should be null.");
    }

    @Test
    void testFullEntityValues() {
        LocalDateTime now = LocalDateTime.now();
        activityEntity.setTitle("Chess Training");
        activityEntity.setDescription("Weekly chess training session.");
        activityEntity.setStartDate(now.minusHours(2));
        activityEntity.setEndDate(now);
        activityEntity.setCreatedAt(now.minusDays(1));
        activityEntity.setCategory("TRAINING");
        activityEntity.setImageSrc("/images/chess_training.png");

        assertEquals("Chess Training", activityEntity.getTitle());
        assertEquals("Weekly chess training session.", activityEntity.getDescription());
        assertEquals(now.minusHours(2), activityEntity.getStartDate());
        assertEquals(now, activityEntity.getEndDate());
        assertEquals(now.minusDays(1), activityEntity.getCreatedAt());
        assertEquals("TRAINING", activityEntity.getCategory());
        assertEquals("/images/chess_training.png", activityEntity.getImageSrc());
    }

    @Test
    void testSetAndGetCategory() {
        activityEntity.setCategory("TOURNAMENT");
        assertEquals("TOURNAMENT", activityEntity.getCategory(), "Category should be updated correctly.");
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        activityEntity.setCreatedAt(createdAt);
        assertEquals(createdAt, activityEntity.getCreatedAt(), "CreatedAt should be updated correctly.");
    }

    @Test
    void testSetAndGetDescription() {
        activityEntity.setDescription("A competitive chess event.");
        assertEquals("A competitive chess event.", activityEntity.getDescription(),
                "Description should be updated correctly.");
    }

    @Test
    void testSetAndGetEndDate() {
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 1, 15, 0);
        activityEntity.setEndDate(endDate);
        assertEquals(endDate, activityEntity.getEndDate(), "EndDate should be updated correctly.");
    }

    @Test
    void testSetAndGetImageSrc() {
        String imagePath = "/images/chess_tournament.png";
        activityEntity.setImageSrc(imagePath);
        assertEquals(imagePath, activityEntity.getImageSrc(), "ImageSrc should be updated correctly.");
    }

    @Test
    void testSetAndGetStartDate() {
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 10, 0);
        activityEntity.setStartDate(startDate);
        assertEquals(startDate, activityEntity.getStartDate(), "StartDate should be updated correctly.");
    }

    @Test
    void testSetAndGetTitle() {
        activityEntity.setTitle("Chess Tournament");
        assertEquals("Chess Tournament", activityEntity.getTitle(), "Title should be updated correctly.");
    }

    @Test
    void testToString() {
        activityEntity.setTitle("Chess Show");
        String expectedString = "PersistentActivityEntity(title=Chess Show, description=, startDate=null, endDate=null, createdAt=null, category=, imageSrc=null)";
        assertEquals(expectedString, activityEntity.toString(),
                "toString should return the correct string representation.");
    }
}
