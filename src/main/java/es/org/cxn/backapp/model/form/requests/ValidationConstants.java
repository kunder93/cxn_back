
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
 * Contains constants for validation constraints.
 * <p>
 * These constants define the minimum and maximum lengths for various fields to
 * ensure that the input data meets the required criteria.
 * </p>
 */
public final class ValidationConstants {

    /**
     * The minimum allowed length for the title of a book.
     */
    public static final int MIN_TITLE_LENGTH = 1;

    /**
     * The maximum allowed length for the title of a book.
     */
    public static final int MAX_TITLE_LENGTH = 100;

    /**
     * The minimum allowed length for the gender of a book.
     */
    public static final int MIN_GENDER_LENGTH = 1;

    /**
     * The maximum allowed length for the gender of a book.
     */
    public static final int MAX_GENDER_LENGTH = 30;

    /**
     * The minimum allowed length for the language of a book.
     */
    public static final int MIN_LANGUAGE_LENGTH = 1;

    /**
     * The maximum allowed length for the language of a book.
     */
    public static final int MAX_LANGUAGE_LENGTH = 20;

    /**
     * The minimum number of authors required for the authors list of a book.
     */
    public static final int MIN_AUTHORS_LIST_SIZE = 1;

    /**
     * The maximum length allowed for the transport category.
     */
    public static final int MAX_CATEGORY_LENGTH = 30;

    /**
     * The maximum length allowed for the transport description.
     */
    public static final int MAX_DESCRIPTION_LENGTH = 100;

    /**
     * Error message when the transport category is null.
     */
    public static final String CATEGORY_NOT_NULL_MESSAGE = "Category must not be null.";

    /**
     * Error message when the transport category exceeds the maximum length.
     */
    public static final String CATEGORY_SIZE_MESSAGE = "Category must be at most " + MAX_CATEGORY_LENGTH
            + " characters long.";

    /**
     * Error message when the transport description is null.
     */
    public static final String DESCRIPTION_NOT_NULL_MESSAGE = "Description must not be null.";

    /**
     * Error message when the transport description exceeds the maximum length.
     */
    public static final String DESCRIPTION_SIZE_MESSAGE = "Description must be at most " + MAX_DESCRIPTION_LENGTH
            + " characters long.";

    /**
     * Maximum length allowed for the 'places' field.
     */
    public static final int MAX_PLACES_LENGTH = 100;

    /**
     * Error message indicating that a field cannot be null.
     */
    public static final String NOT_NULL_MESSAGE = "This field cannot be null";

    /**
     * Error message indicating that the email field must not be blank.
     */
    public static final String EMAIL_NOT_BLANK_MESSAGE = "Email must not be blank";

    /**
     * Error message indicating that the email must be in a valid format.
     */
    public static final String EMAIL_INVALID_MESSAGE = "Email should be valid";

    /**
     * Maximum allowed length for the email field.
     */
    public static final int EMAIL_MAX_SIZE = 50;

    /**
     * Error message indicating that the email field must not exceed 50 characters.
     */
    public static final String EMAIL_SIZE_MESSAGE = "Email must not exceed " + EMAIL_MAX_SIZE + " characters";

    /**
     * Error message indicating that the category field must not be blank.
     */
    public static final String CATEGORY_NOT_BLANK_MESSAGE = "Category must not be blank";

    /**
     * Maximum allowed length for the category field.
     */
    public static final int CATEGORY_MAX_SIZE = 30;

    /**
     * Error message indicating that the topic field must not be blank.
     */
    public static final String TOPIC_NOT_BLANK_MESSAGE = "Topic must not be blank";

    /**
     * Maximum allowed length for the topic field.
     */
    public static final int TOPIC_MAX_SIZE = 50;

    /**
     * Error message indicating that the topic field must not exceed 50 characters.
     */
    public static final String TOPIC_SIZE_MESSAGE = "Topic must not exceed " + TOPIC_MAX_SIZE + " characters";

    /**
     * Error message indicating that the message field must not be blank.
     */
    public static final String MESSAGE_NOT_BLANK_MESSAGE = "Message must not be blank";

    /**
     * Maximum allowed length for the message field.
     */
    public static final int MESSAGE_MAX_LENGTH = 400;

    /**
     * Error message indicating that the message field must not exceed 400
     * characters.
     */
    public static final String MESSAGE_MAX_LENGTH_MESSAGE = "Message length cannot exceed " + MESSAGE_MAX_LENGTH
            + " characters.";

    /**
     * The message used when the ID is not positive.
     * <p>
     * This message is used in validation annotations to indicate that the ID must
     * be a positive integer value.
     * </p>
     */
    public static final String ID_POSITIVE_MESSAGE = "The ID must be a positive integer.";

    /**
     * The message used when the ID exceeds the maximum allowed value.
     * <p>
     * This message is used in validation annotations to indicate that the ID must
     * not exceed the maximum allowed value defined by {@link #MAX_ID}.
     * </p>
     */
    public static final String ID_MAX_MESSAGE = "The ID must not exceed 1000.";

    /**
     * The maximum allowed value for the ID.
     * <p>
     * This constant defines the upper limit for valid ID values. The ID must be a
     * positive integer and must not exceed this value.
     * </p>
     */
    public static final int MAX_ID = 1000;

    /**
     * The message used when the ID is null.
     * <p>
     * This message is used in validation annotations to indicate that the ID cannot
     * be null.
     * </p>
     */
    public static final String ID_NOT_NULL_MESSAGE = "The ID must be a positive integer.";

    /**
     * The minimum length for passwords.
     * <p>
     * Passwords shorter than this length will be considered invalid.
     */
    public static final int PASSWORD_MIN_LENGTH = 6;

    /**
     * The maximum length for passwords.
     * <p>
     * Passwords longer than this length will be considered invalid.
     */
    public static final int PASSWORD_MAX_LENGTH = 20;

    /**
     * Error message for when a password field is left blank.
     * <p>
     * This message is used in validation to indicate that the password field must
     * not be empty.
     */
    public static final String PASSWORD_NOT_BLANK_MESSAGE = "Password cannot be blank.";

    /**
     * Error message for when a password does not meet size requirements.
     * <p>
     * This message is used in validation to indicate that the password must be
     * between {@value #PASSWORD_MIN_LENGTH} and {@value #PASSWORD_MAX_LENGTH}
     * characters in length.
     */
    public static final String PASSWORD_SIZE_MESSAGE = "Password must be between " + PASSWORD_MIN_LENGTH + " and "
            + PASSWORD_MAX_LENGTH + " characters.";

    /**
     * Minimum length for the activity title. Must be at least 4 characters.
     */
    public static final int ACTIVITY_TITLE_MIN_LENGTH = 4;

    /**
     * Maximum length for the activity title. Must not exceed 30 characters.
     */
    public static final int ACTIVITY_TITLE_MAX_LENGTH = 30;

    /**
     * Minimum length for the activity description. Must be at least 10 characters.
     */
    public static final int ACTIVITY_DESCRIPTION_MIN_LENGTH = 10;

    /**
     * Maximum length for the activity description. Must not exceed 150 characters.
     */
    public static final int ACTIVITY_DESCRIPTION_MAX_LENGTH = 150;

    /**
     * Maximum file size for an activity image file. Should not exceed 10 MB (10 *
     * 1024 * 1024 bytes).
     */
    public static final int ACTIVITY_IMAGE_FILE_MAX_SIZE = 10 * 1024 * 1024;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ValidationConstants() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }

}
