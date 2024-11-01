
package es.org.cxn.backapp.model.form.requests;

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
     * Error message for when the ISBN field is null.
     */
    public static final String ISBN_NOT_NULL = "ISBN must not be null";

    /**
     * Error message for when the title field is null.
     */
    public static final String TITLE_NOT_NULL = "Title must not be null";

    /**
     * Error message for when the title length is outside the allowed range. The
     * allowed length is between {@code ValidationConstants.MIN_TITLE_LENGTH} and
     * {@code ValidationConstants.MAX_TITLE_LENGTH} characters.
     */
    public static final String TITLE_SIZE = "Title must be between " + ValidationConstants.MIN_TITLE_LENGTH + " and "
            + ValidationConstants.MAX_TITLE_LENGTH + " characters";

    /**
     * Error message for when the gender length is outside the allowed range. The
     * allowed length is between {@code ValidationConstants.MIN_GENDER_LENGTH} and
     * {@code ValidationConstants.MAX_GENDER_LENGTH} characters.
     */
    public static final String GENDER_SIZE = "Gender must be between " + ValidationConstants.MIN_GENDER_LENGTH + " and "
            + ValidationConstants.MAX_GENDER_LENGTH + " characters";

    /**
     * Error message for when the language length is outside the allowed range. The
     * allowed length is between {@code ValidationConstants.MIN_LANGUAGE_LENGTH} and
     * {@code ValidationConstants.MAX_LANGUAGE_LENGTH} characters.
     */
    public static final String LANGUAGE_SIZE = "Language must be between " + ValidationConstants.MIN_LANGUAGE_LENGTH
            + " and " + ValidationConstants.MAX_LANGUAGE_LENGTH + " characters";

    /**
     * Error message for when the authors list is null.
     */
    public static final String AUTHORS_LIST_NOT_NULL = "Authors list must not be null";

    /**
     * Error message for when the authors list size is smaller than the minimum
     * allowed. The list must contain at least
     * {@code ValidationConstants.MIN_AUTHORS_LIST_SIZE} author.
     */
    public static final String AUTHORS_LIST_SIZE = "Authors list must contain at least "
            + ValidationConstants.MIN_AUTHORS_LIST_SIZE + " author";

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
    public static final String ACTIVITY_IMAGE_FILE_VALID = "Image must be a JPEG, PNG, WebP, or AVIF file and no larger than 10 MB.";

    /**
     * Private no args constructor.
     */
    private ValidationMessages() {
        // Private constructor.
    }
}
