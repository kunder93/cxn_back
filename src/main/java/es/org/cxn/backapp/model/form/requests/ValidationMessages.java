
package es.org.cxn.backapp.model.form.requests;

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

/**
 * Contains validation error messages.
 * <p>
 * These messages are used to provide informative feedback when validation
 * constraints are violated. They include dynamic values from
 * {@link ValidationConstants} to ensure consistency.
 * </p>
 */
public final class ValidationMessages {

    /**
     * Message indicating that the activity description cannot be null.
     */
    public static final String ACTIVITY_DESCRIPTION_NOT_NULL = "Activity description cannot be null.";

    /**
     * Message indicating that the activity title cannot be null.
     */
    public static final String ACTIVITY_TITLE_NOT_NULL = "Activity title cannot be null.";

    /**
     * Message indicating that the activity start date cannot be null.
     */
    public static final String ACTIVITY_START_DATE_NOT_NULL = "Activity start date cannot be null.";

    /**
     * Message indicating that the activity end date cannot be null.
     */
    public static final String ACTIVITY_END_DATE_NOT_NULL = "Activity end date cannot be null";

    /**
     * Message indicating that the activity category cannot be null.
     */
    public static final String ACTIVITY_CATEGORY_NOT_NULL = "Activity category cannot be null";

    /**
     * Message specifying the length constraints for the activity title. Requires
     * the title length to be between
     * {@link ValidationConstants#ACTIVITY_TITLE_MIN_LENGTH} and
     * {@link ValidationConstants#ACTIVITY_TITLE_MAX_LENGTH} characters.
     */
    public static final String ACTIVITY_TITLE_MAX_MIN_LENGTH = "Activity title must have length between "
            + ValidationConstants.ACTIVITY_TITLE_MIN_LENGTH + " and " + ValidationConstants.ACTIVITY_TITLE_MAX_LENGTH
            + " characters.";

    /**
     * Message specifying the length constraints for the activity description.
     * Requires the description length to be between
     * {@link ValidationConstants#ACTIVITY_DESCRIPTION_MIN_LENGTH} and
     * {@link ValidationConstants#ACTIVITY_DESCRIPTION_MAX_LENGTH} characters.
     */
    public static final String ACTIVITY_DESCRIPTION_MAX_MIN_LENGTH = "Activity description must have length between "
            + ValidationConstants.ACTIVITY_DESCRIPTION_MIN_LENGTH + " and "
            + ValidationConstants.ACTIVITY_DESCRIPTION_MAX_LENGTH + " characters.";

    /**
     * Message indicating that the activity start date must be in the present or
     * future.
     */
    public static final String ACTIVITY_START_DATE_PRESENT_OR_FUTURE = "Start date must be present or future.";

    /**
     * Message indicating that the activity end date must be in the present or
     * future.
     */
    public static final String ACTIVITY_END_DATE_PRESENT_OR_FUTURE = "End date must be present or future.";

    /**
     * Message specifying the valid formats and size constraints for an activity
     * image file. Acceptable formats include JPEG, PNG, WebP, and AVIF with a
     * maximum size of 10 MB.
     */
    public static final String ACTIVITY_IMAGE_FILE_VALID = "Image must be a JPEG, PNG, WebP, or AVIF file and no"
            + " larger than 10 MB.";

    /**
     * Private no args constructor.
     */
    private ValidationMessages() {
        // Private constructor.
    }
}
