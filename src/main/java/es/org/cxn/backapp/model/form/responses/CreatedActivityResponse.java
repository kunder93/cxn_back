
package es.org.cxn.backapp.model.form.responses;

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

import java.time.LocalDateTime;

import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;

/**
 * Represents a response containing details of a newly created activity.
 *
 * @param title       The title of the activity.
 * @param description A description of the activity.
 * @param startDate   The start date and time of the activity.
 * @param endDate     The end date and time of the activity.
 * @param category    The category of the activity.
 * @param createdAt   The date and time when the activity was created.
 */
public record CreatedActivityResponse(String title, String description, LocalDateTime startDate, LocalDateTime endDate,
        String category, LocalDateTime createdAt) {

    /**
     * Constructs a new {@code CreatedActivityResponse} from a
     * {@code PersistentActivityEntity}.
     *
     * @param activity The {@code PersistentActivityEntity} representing the
     *                 activity data.
     */
    public CreatedActivityResponse(final PersistentActivityEntity activity) {
        this(activity.getTitle(), activity.getDescription(), activity.getStartDate(), activity.getEndDate(),
                activity.getCategory(), activity.getCreatedAt());
    }
}
