
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

/**
 * Unit tests for the {@link PersistentActivityEntity} class. This class tests
 * the default values, getter and setter methods, and behavior of the entity
 * under different conditions.
 */
class PersistentActivityEntityTest {

    /** The title for a training activity. */
    private static final String TITLE_TRAINING = "Chess Training";

    /** The title for a tournament activity. */
    private static final String TITLE_TOURNAMENT = "Chess Tournament";

    /** The title for a show activity. */
    private static final String TITLE_SHOW = "Chess Show";

    /** Description for a weekly chess training session. */
    private static final String DESCRIPTION_TRAINING = "Weekly chess training session.";

    /** Description for a competitive chess event. */
    private static final String DESCRIPTION_EVENT = "A competitive chess event.";

    /** Category for training activities. */
    private static final String CATEGORY_TRAINING = "TRAINING";

    /** Category for tournament activities. */
    private static final String CATEGORY_TOURNAMENT = "TOURNAMENT";

    /** Image path for chess training sessions. */
    private static final String IMAGE_TRAINING = "/images/chess_training.png";

    /** Image path for chess tournaments. */
    private static final String IMAGE_TOURNAMENT = "/images/chess_tournament.png";

    /** The year 2025 used for date testing. */
    private static final int YEAR_2025 = 2025;

    /** The month of January used for date testing. */
    private static final int MONTH_JANUARY = 1;

    /** The first day of the month used for date testing. */
    private static final int DAY_1 = 1;

    /** The hour 10 AM used for time testing. */
    private static final int HOUR_10 = 10;

    /** The hour 3 PM used for time testing. */
    private static final int HOUR_15 = 15;

    /** Two hours before the current time, used for testing. */
    private static final int HOURS_BEFORE_2 = 2;

    /** One day before the current date, used for testing. */
    private static final int DAYS_BEFORE_1 = 1;

    /**
     * Activity entity.
     */
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
        activityEntity.setTitle(TITLE_TRAINING);
        activityEntity.setDescription(DESCRIPTION_TRAINING);
        activityEntity.setStartDate(now.minusHours(HOURS_BEFORE_2));
        activityEntity.setEndDate(now);
        activityEntity.setCreatedAt(now.minusDays(DAYS_BEFORE_1));
        activityEntity.setCategory(CATEGORY_TRAINING);
        activityEntity.setImageSrc(IMAGE_TRAINING);

        assertEquals(TITLE_TRAINING, activityEntity.getTitle());
        assertEquals(DESCRIPTION_TRAINING, activityEntity.getDescription());
        assertEquals(now.minusHours(HOURS_BEFORE_2), activityEntity.getStartDate());
        assertEquals(now, activityEntity.getEndDate());
        assertEquals(now.minusDays(DAYS_BEFORE_1), activityEntity.getCreatedAt());
        assertEquals(CATEGORY_TRAINING, activityEntity.getCategory());
        assertEquals(IMAGE_TRAINING, activityEntity.getImageSrc());
    }

    @Test
    void testSetAndGetCategory() {
        activityEntity.setCategory(CATEGORY_TOURNAMENT);
        assertEquals(CATEGORY_TOURNAMENT, activityEntity.getCategory(), "Category should be updated correctly.");
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        activityEntity.setCreatedAt(createdAt);
        assertEquals(createdAt, activityEntity.getCreatedAt(), "CreatedAt should be updated correctly.");
    }

    @Test
    void testSetAndGetDescription() {
        activityEntity.setDescription(DESCRIPTION_EVENT);
        assertEquals(DESCRIPTION_EVENT, activityEntity.getDescription(), "Description should be updated correctly.");
    }

    @Test
    void testSetAndGetEndDate() {
        LocalDateTime endDate = LocalDateTime.of(YEAR_2025, MONTH_JANUARY, DAY_1, HOUR_15, 0);
        activityEntity.setEndDate(endDate);
        assertEquals(endDate, activityEntity.getEndDate(), "EndDate should be updated correctly.");
    }

    @Test
    void testSetAndGetImageSrc() {
        activityEntity.setImageSrc(IMAGE_TOURNAMENT);
        assertEquals(IMAGE_TOURNAMENT, activityEntity.getImageSrc(), "ImageSrc should be updated correctly.");
    }

    @Test
    void testSetAndGetStartDate() {
        LocalDateTime startDate = LocalDateTime.of(YEAR_2025, MONTH_JANUARY, DAY_1, HOUR_10, 0);
        activityEntity.setStartDate(startDate);
        assertEquals(startDate, activityEntity.getStartDate(), "StartDate should be updated correctly.");
    }

    @Test
    void testSetAndGetTitle() {
        activityEntity.setTitle(TITLE_TOURNAMENT);
        assertEquals(TITLE_TOURNAMENT, activityEntity.getTitle(), "Title should be updated correctly.");
    }

    @Test
    void testToString() {
        activityEntity.setTitle(TITLE_SHOW);
        String expectedString = "PersistentActivityEntity(title=" + TITLE_SHOW
                + ", description=, startDate=null, endDate=null, createdAt=null, category=, imageSrc=null)";
        assertEquals(expectedString, activityEntity.toString(),
                "toString should return the correct string representation.");
    }
}
