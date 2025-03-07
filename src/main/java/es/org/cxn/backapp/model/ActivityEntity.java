package es.org.cxn.backapp.model;

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
 * Represents an activity entity with details such as category, title,
 * description, start and end dates, creation timestamp, and unique identifier.
 * This interface provides getter and setter methods for each attribute.
 */
public interface ActivityEntity extends java.io.Serializable {

    /**
     * Gets the category of the activity.
     *
     * @return the activity category as a String.
     */
    String getCategory();

    /**
     * Gets the creation timestamp of the activity.
     *
     * @return the LocalDateTime when the activity was created.
     */
    LocalDateTime getCreatedAt();

    /**
     * Gets the description of the activity.
     *
     * @return the description as a String.
     */
    String getDescription();

    /**
     * Gets the end date and time of the activity.
     *
     * @return the LocalDateTime when the activity ends.
     */
    LocalDateTime getEndDate();

    /**
     * Gets the image source of the activity.
     *
     * @return The image source as String.
     */
    String getImageSrc();

    /**
     * Gets the start date and time of the activity.
     *
     * @return the LocalDateTime when the activity starts.
     */
    LocalDateTime getStartDate();

    /**
     * Gets the title of the activity.
     *
     * @return the title as a String.
     */
    String getTitle();

    /**
     * Sets the category of the activity.
     *
     * @param category the category to set for this activity.
     */
    void setCategory(String category);

    /**
     * Sets the creation timestamp for the activity.
     *
     * @param value the LocalDateTime to set as the creation timestamp.
     */
    void setCreatedAt(LocalDateTime value);

    /**
     * Sets the description of the activity.
     *
     * @param value the description to set for this activity.
     */
    void setDescription(String value);

    /**
     * Sets the end date and time of the activity.
     *
     * @param value the LocalDateTime when the activity ends.
     */
    void setEndDate(LocalDateTime value);

    /**
     * Sets the image source of the activity.
     *
     * @param value the image source to set for this activity.
     */
    void setImageSrc(String value);

    /**
     * Sets the start date and time of the activity.
     *
     * @param value the LocalDateTime when the activity starts.
     */
    void setStartDate(LocalDateTime value);

    /**
     * Sets the title of the activity.
     *
     * @param value the title to set for this activity.
     */
    void setTitle(String value);

}
