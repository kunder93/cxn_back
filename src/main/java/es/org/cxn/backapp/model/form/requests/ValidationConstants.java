
package es.org.cxn.backapp.model.form.requests;

import java.math.BigDecimal;

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
     * Minimum number of days for the event.
     */
    public static final int MIN_AMOUNT_DAYS = 1;

    /**
     * Maximum number of days for the event.
     */
    public static final int MAX_AMOUNT_DAYS = 20;

    /**
     * Maximum price per day if overnight is included.
     */
    public static final BigDecimal MAX_DAY_PRICE_OVERNIGHT = new BigDecimal("50.00");

    /**
     * Maximum price per day if overnight is not included.
     */
    public static final BigDecimal MAX_DAY_PRICE_NO_OVERNIGHT = new BigDecimal("25.00");

    /**
     * Validation message for invalid number of days.
     */
    public static final String AMOUNT_DAYS_MESSAGE = "Amount of days must be between " + MIN_AMOUNT_DAYS + " and "
            + MAX_AMOUNT_DAYS;

    /**
     * Validation message for invalid day price.
     */
    public static final String DAY_PRICE_MESSAGE_OVERNIGHT = "Day price must be at most " + MAX_DAY_PRICE_OVERNIGHT
            + " when overnight is included.";

    /**
     * Validation message for invalid day price with no overnight.
     */
    public static final String DAY_PRICE_MESSAGE_NO_OVERNIGHT = "Day price must be at most "
            + MAX_DAY_PRICE_NO_OVERNIGHT + " when overnight is not included.";

    /**
     * Validation message for day price being non-negative.
     */
    public static final String DAY_PRICE_NON_NEGATIVE_MESSAGE = "Day price must be non-negative.";

    /**
     * Validation message for overnight field being non-null.
     */
    public static final String OVERNIGHT_NOT_NULL_MESSAGE = "Overnight must not be null.";

    /**
     * The maximum length allowed for the transport category.
     */
    public static final int MAX_CATEGORY_LENGTH = 30;

    /**
     * The maximum length allowed for the transport description.
     */
    public static final int MAX_DESCRIPTION_LENGTH = 100;

    /**
     * The maximum length allowed for the transport invoice series.
     */
    public static final int MAX_INVOICE_SERIES_LENGTH = 10;

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
     * Error message when the transport invoice number is null.
     */
    public static final String INVOICE_NUMBER_NOT_NULL_MESSAGE = "Invoice number must not be null.";

    /**
     * Error message when the transport invoice date is null.
     */
    public static final String INVOICE_DATE_NOT_NULL_MESSAGE = "Invoice date must not be null.";

    /**
     * Error message when the transport invoice series is null.
     */
    public static final String INVOICE_SERIES_NOT_NULL_MESSAGE = "Invoice series must not be null.";

    /**
     * Error message when the transport invoice series exceeds the maximum length.
     */
    public static final String INVOICE_SERIES_SIZE_MESSAGE = "Invoice series must be at most "
            + MAX_INVOICE_SERIES_LENGTH + " characters long.";

    /**
     * Maximum length allowed for the 'places' field.
     */
    public static final int MAX_PLACES_LENGTH = 100;

    /**
     * Maximum value allowed for the 'distance' field.
     * <p>
     * The distance should not exceed this value.
     * </p>
     */
    public static final String MAX_DISTANCE = "50000.0";

    /**
     * Maximum value allowed for the 'kmPrice' field.
     * <p>
     * The price per kilometer should not exceed this value.
     * </p>
     */
    public static final String MAX_KM_PRICE = "0.19";

    /**
     * Error message indicating that the 'places' field exceeds the maximum length.
     */
    public static final String PLACES_SIZE_MESSAGE = "Places cannot exceed " + MAX_PLACES_LENGTH + " characters";

    /**
     * Error message indicating that the 'distance' field is positive value.
     */
    public static final String DISTANCE_MIN_MESSAGE = "Distance must be positive.";

    /**
     * Error message indicating that the 'distance' field exceeds the maximum
     * allowed value.
     */
    public static final String DISTANCE_MAX_MESSAGE = "Distance cannot exceed " + MAX_DISTANCE;

    /**
     * Error message indicating that the 'kmPrice' field ispositive value..
     */
    public static final String KM_PRICE_MIN_MESSAGE = "Price per kilometer must be positive.";

    /**
     * Error message indicating that the 'kmPrice' field exceeds the maximum allowed
     * value.
     */
    public static final String KM_PRICE_MAX_MESSAGE = "Price per kilometer cannot exceed " + MAX_KM_PRICE;

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
     * Maximum length allowed for the invoice series.
     */
    public static final int INVOICE_SERIES_MAX_LENGTH = 10;

    /**
     * Maximum length allowed for the NIF (Tax Identification Number) fields.
     */
    public static final int INVOICE_NIF_MAX_LENGTH = 40;

    /**
     * Validation message indicating that the invoice number must be a positive
     * integer.
     */
    public static final String INVOICE_NUMBER_MIN_MESSAGE = "Invoice number must be a positive integer.";

    /**
     * Validation message indicating that the tax exempt status must be specified.
     */
    public static final String INVOICE_TAX_EXEMPT_NOT_NULL_MESSAGE = "Tax exempt status must be specified.";

    /**
     * Validation message indicating that the NIF must not be blank.
     */
    public static final String INVOICE_NIF_NOT_BLANK_MESSAGE = "NIF must not be blank.";

    /**
     * Validation message indicating that the NIF must not exceed the maximum
     * length.
     */
    public static final String INVOICE_NIF_SIZE_MESSAGE = "NIF must not exceed " + INVOICE_NIF_MAX_LENGTH
            + " characters.";

    /**
     * Maximum invoice number.
     */
    public static final int INVOICE_NUMBER_MAX = 1000;

    /**
     * Validation message indicating that the invoice number must not exceed the
     * maximum.
     */
    public static final String INVOICE_NUMBER_MESSAGE = "Invoice number cannot exceed " + INVOICE_NUMBER_MAX + ".";

    /**
     * The maximum length allowed for the user email.
     */
    public static final int PAYMENT_SHEET_USER_EMAIL_MAX_LENGTH = 50;

    /**
     * The maximum length allowed for the reason of the payment sheet event.
     */
    public static final int PAYMENT_SHEET_REASON_MAX_LENGTH = 60;

    /**
     * The maximum length allowed for the place where the payment sheet event is
     * held.
     */
    public static final int PAYMENT_SHEET_PLACE_MAX_LENGTH = 50;

    /**
     * Validation message indicating that the user email is required.
     */
    public static final String PAYMENT_SHEET_USER_EMAIL_REQUIRED_MESSAGE = "User email is required.";

    /**
     * Validation message indicating that the user email is in an invalid format.
     */
    public static final String PAYMENT_SHEET_USER_EMAIL_INVALID_MESSAGE = "Invalid email format.";

    /**
     * Validation message indicating that the user email exceeds the maximum allowed
     * length.
     */
    public static final String PAYMENT_SHEET_USER_EMAIL_MAX_LENGTH_MESSAGE = "User email must be at most "
            + PAYMENT_SHEET_USER_EMAIL_MAX_LENGTH + " characters.";

    /**
     * Validation message indicating that the reason for the payment sheet event is
     * required.
     */
    public static final String PAYMENT_SHEET_REASON_REQUIRED_MESSAGE = "Reason is required.";

    /**
     * Validation message indicating that the reason exceeds the maximum allowed
     * length.
     */
    public static final String PAYMENT_SHEET_REASON_MAX_LENGTH_MESSAGE = "Reason must be at most "
            + PAYMENT_SHEET_REASON_MAX_LENGTH + " characters.";

    /**
     * Validation message indicating that the place where the payment sheet event is
     * held is required.
     */
    public static final String PAYMENT_SHEET_PLACE_REQUIRED_MESSAGE = "Place is required.";

    /**
     * Validation message indicating that the place exceeds the maximum allowed
     * length.
     */
    public static final String PAYMENT_SHEET_PLACE_MAX_LENGTH_MESSAGE = "Place must be at most "
            + PAYMENT_SHEET_PLACE_MAX_LENGTH + " characters.";

    /**
     * Validation message indicating that the start date of the payment sheet event
     * is required.
     */
    public static final String PAYMENT_SHEET_START_DATE_REQUIRED_MESSAGE = "Start date is required.";

    /**
     * Validation message indicating that the end date of the payment sheet event is
     * required.
     */
    public static final String PAYMENT_SHEET_END_DATE_REQUIRED_MESSAGE = "End date is required.";

    /**
     * Validation message indicating that the end date must be on or after the start
     * date.
     */
    public static final String PAYMENT_SHEET_END_DATE_BEFORE_START_DATE_MESSAGE = "End date must be on or"
            + " after the start date.";

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
