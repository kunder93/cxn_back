
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
   * Error message for when the title length is outside the allowed range.
   * The allowed length is between {@code ValidationConstants.MIN_TITLE_LENGTH}
   * and {@code ValidationConstants.MAX_TITLE_LENGTH} characters.
   */
  public static final String TITLE_SIZE =
        "Title must be between " + ValidationConstants.MIN_TITLE_LENGTH
              + " and " + ValidationConstants.MAX_TITLE_LENGTH + " characters";

  /**
   * Error message for when the gender length is outside the allowed range.
   * The allowed length is between
   * {@code ValidationConstants.MIN_GENDER_LENGTH}
   * and {@code ValidationConstants.MAX_GENDER_LENGTH} characters.
   */
  public static final String GENDER_SIZE =
        "Gender must be between " + ValidationConstants.MIN_GENDER_LENGTH
              + " and " + ValidationConstants.MAX_GENDER_LENGTH + " characters";

  /**
   * Error message for when the language length is outside the allowed range.
   * The allowed length is between
   * {@code ValidationConstants.MIN_LANGUAGE_LENGTH}
   * and {@code ValidationConstants.MAX_LANGUAGE_LENGTH} characters.
   */
  public static final String LANGUAGE_SIZE = "Language must be between "
        + ValidationConstants.MIN_LANGUAGE_LENGTH + " and "
        + ValidationConstants.MAX_LANGUAGE_LENGTH + " characters";

  /**
   * Error message for when the authors list is null.
   */
  public static final String AUTHORS_LIST_NOT_NULL =
        "Authors list must not be null";

  /**
   * Error message for when the authors list size is smaller than the minimum
   * allowed.
   * The list must contain at least
   * {@code ValidationConstants.MIN_AUTHORS_LIST_SIZE} author.
   */
  public static final String AUTHORS_LIST_SIZE =
        "Authors list must contain at least "
              + ValidationConstants.MIN_AUTHORS_LIST_SIZE + " author";

  /**
   * Private no args constructor.
   */
  private ValidationMessages() {
    //Private constructor.
  }
}
