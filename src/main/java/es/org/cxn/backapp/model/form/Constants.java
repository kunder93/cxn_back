
package es.org.cxn.backapp.model.form;

/**
 * Class for controller form constants.
 *
 * @author Santiago Paz
 *
 */
public final class Constants {

  /**
   * Message part that is in some of public constants in this class.
   */
  private static final String MESSAGE_COMMON_PART = "characters or numbers.";

  /**
   * DNI bad format message.
   */
  public static final String DNI_BAD_FORMAT =
        "DNI bad format. DNI must have 8 numbers and 1 letter.";

  /**
   * Email max length.
   */
  public static final int EMAIL_MAX_SIZE = 50;

  /**
   * Email min length.
   */
  public static final int EMAIL_MIN_SIZE = 5;

  /**
   * Postal code max length.
   */
  public static final int POSTAL_CODE_MAX_LENGHT = 10;

  /**
   * City max length.
   */
  public static final int CITY_MAX_LENGHT = 50;

  /**
   * City max length message.
   */
  public static final String CITY_MAX_LENGHT_MESSAGE =
        "City must be max length of: " + CITY_MAX_LENGHT + MESSAGE_COMMON_PART;

  /**
   * City not blank message.
   */
  public static final String CITY_NOT_BLANK =
        "City must not be null or empty.";

  /**
   * Street max lenght.
   */
  public static final int STREET_MAX_LENGHT = 100;

  /**
   * Street max length message.
   */
  public static final String STREET_MAX_LENGHT_MESSAGE =
        "Street must be max length of: " + STREET_MAX_LENGHT
              + MESSAGE_COMMON_PART;

  /**
   * Street not blank message.
   */
  public static final String STREET_NOT_BLANK =
        "Street must not be null or empty.";

  /**
   * Building max length.
   */
  public static final int BUILDING_MAX_LENGHT = 20;

  /**
   * Building max length message.
   */
  public static final String BUILDING_MAX_LENGHT_MESSAGE =
        "Building must be max length of: " + BUILDING_MAX_LENGHT
              + MESSAGE_COMMON_PART;

  /**
   * Building not blank message.
   */
  public static final String BUILDING_NOT_BLANK =
        "Building must not be null or empty.";

  /**
   * Apartment number max length.
   */
  public static final int APARTMENT_NUMBER_MAX_LENGHT = 15;

  /**
   * Apartment number max length message.
   */
  public static final String APARTMENT_NUMBER_MAX_LENGHT_MESSAGE =
        "Apartment number must be max length of: " + APARTMENT_NUMBER_MAX_LENGHT
              + MESSAGE_COMMON_PART;

  /**
   * Apartment number not blank message.
   */
  public static final String APARTMENT_NUMBER_NOT_BLANK_MESSAGE =
        "Aparment number must not be null or empty.";

  /**
   * Message validating not blank postal code.
   */
  public static final String POSTAL_CODE_NOT_BLANK_MESSAGE =
        "Postal code must not be null or empty.";

  /**
   * Postal code max length message.
   */
  public static final String POSTAL_CODE_MAX_LENGHT_MESSAGE =
        "Postal code must be max length of: " + POSTAL_CODE_MAX_LENGHT
              + MESSAGE_COMMON_PART;

  /**
   * Password min length.
   */
  public static final int MIN_PASSWORD_LENGHT = 6;
  /**
   * Password max length.
   */
  public static final int MAX_PASSWORD_LENGHT = 20;
  /**
   * Name max length.
   */
  public static final int NAME_MAX_LENGTH = 25;
  /**
   * First surname max length.
   */
  public static final int FIRST_SURNAME_MAX_LENGTH = 25;
  /**
   * Second surname max length.
   */
  public static final int SECOND_SURNAME_MAX_LENGTH = 25;
  /**
   * Gender max length.
   */
  public static final int GENDER_MAX_LENGTH = 10;
  /**
   * Password max length.
   */
  public static final int PASSWORD_MAX_LENGTH = 20;
  /**
   * Password min length.
   */
  public static final int PASSWORD_MIN_LENGTH = 6;
  /**
   * Message validating NotEmpty email.
   */
  public static final String EMAIL_NOT_VALID =
        "Email not valid format.";

  /**
   * Message validating MaxSize email.
   */
  public static final String MAX_SIZE_EMAIL_MESSAGE =
        "Email must be max length of: " + EMAIL_MAX_SIZE + ".";
  /**
   * Message validating NotEmpty password.
   */
  public static final String NOT_NULL_PASSWORD =
        "Password must not be null or empty.";
  /**
   * Message validating Length password.
   */
  public static final String LENGTH_PASSWORD_MESSAGE =
        "password lenght between " + MIN_PASSWORD_LENGHT + " and "
              + MAX_PASSWORD_LENGHT + " characters";
  /**
   * Message validating Not Blank name.
   */
  public static final String NAME_NOT_BLANK =
        "Name must not be null or empty.";

  /**
   * Message validating Not Blank first surname.
   */
  public static final String FIRST_SURNAME_NOT_BLANK =
        "First surname must not be null or empty.";

  /**
   * Message validating MaxSize name.
   */
  public static final String NAME_MAX_LENGTH_MESSAGE =
        "Name must be max length of: " + NAME_MAX_LENGTH + ".";
  /**
   * Message validating MaxSize First surname.
   */
  public static final String FIRST_SURNAME_MAX_LENGTH_MESSAGE =
        "First surname must be max length of: " + FIRST_SURNAME_MAX_LENGTH
              + ".";
  /**
   * Message validating Not Blank Second surname.
   */
  public static final String SECOND_SURNAME_NOT_BLANK =
        "Second surname must not be null or empty.";
  /**
   * Message validating MaxSize Second surname.
   */
  public static final String SECOND_SURNAME_MAX_LENGTH_MESSAGE =
        "Second surname must be max length of: " + SECOND_SURNAME_MAX_LENGTH
              + ".";
  /**
   * Message validating Birth date past.
   */
  public static final String BIRTH_DATE_PAST =
        "Bith date must not be past.";

  /**
   * Message validating Not Blank Password.
   */
  public static final String PASSWORD_NOT_BLANK_MESSAGE =
        "Password must not be null or empty.";
  /**
   * Message validating MaxSize and MinSize Password.
   */
  public static final String PASSWORD_SIZE_MESSAGE =
        "Password must be between length of: " + PASSWORD_MIN_LENGTH + "AND"
              + PASSWORD_MAX_LENGTH + ".";

  /**
   * Message validating MaxSize Gender.
   */
  public static final String GENDER_MAX_LENGTH_MESSAGE =
        "Gender must be max length of: " + GENDER_MAX_LENGTH + ".";
  /**
   * Message validating Not Blank Gender.
   */
  public static final String GENDER_NOT_BLANK =
        "Gender must not be null or empty.";

  /**
   * Default private constructor to avoid initialization.
   */
  private Constants() {
    super();
  }

}
