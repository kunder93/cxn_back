
package es.org.cxn.backapp.service.dto;

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

/**
 * Data Transfer Object (DTO) representing an activity, including its details
 * and associated image. This DTO is used by services to transfer activity
 * information along with an optional image in binary format.
 *
 * @param title       The title of the activity. Must not be null.
 * @param description A brief description of the activity. Must not be null.
 * @param startDate   The start date and time of the activity. Must not be null.
 * @param endDate     The end date and time of the activity. Must not be null.
 * @param category    category or type of the activity. Must not be null.
 * @param image       A byte array representing the image associated with the
 *                    activity. This field is optional and can be null if there
 *                    is no image provided.
 */
public record ActivityWithImageDto(String title, String description, LocalDateTime startDate, LocalDateTime endDate,
        String category, String image) {

}
