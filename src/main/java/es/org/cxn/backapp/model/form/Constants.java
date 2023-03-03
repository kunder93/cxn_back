package es.org.cxn.backapp.model.form;

/**
 * Class for controller form constants.
 *
 * @author Santiago Paz
 *
 */
public final class Constants {

    /**
     * Email max length.
     */
    public static final int MAX_SIZE_EMAIL = 50;
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
    public static final String NOT_EMPTY_EMAIL = "Email must not be null or empty.";
    /**
     * Message validating MaxSize email.
     */
    public static final String MAX_SIZE_EMAIL_MESSAGE = "Email must be max length of: "
            + MAX_SIZE_EMAIL + ".";
    /**
     * Message validating NotEmpty password.
     */
    public static final String NOT_NULL_PASSWORD = "Password must not be null or empty.";
    /**
     * Message validating Length password.
     */
    public static final String LENGTH_PASSWORD_MESSAGE = "password lenght between "
            + MIN_PASSWORD_LENGHT + " and " + MAX_PASSWORD_LENGHT
            + " characters";
    /**
     * Message validating Not Blank name.
     */
    public static final String NAME_NOT_BLANK_MESSAGE = "Name must not be null or empty.";

    /**
     * Message validating Not Blank first surname.
     */
    public static final String FIRST_SURNAME_NOT_BLANK_MESSAGE = "First surname must not be null or empty.";
    /**
     * Message validating MaxSize name.
     */
    public static final String NAME_MAX_LENGTH_MESSAGE = "Name must be max length of: "
            + NAME_MAX_LENGTH + ".";
    /**
     * Message validating MaxSize First surname.
     */
    public static final String FIRST_SURNAME_MAX_LENGTH_MESSAGE = "First surname must be max length of: "
            + FIRST_SURNAME_MAX_LENGTH + ".";
    /**
     * Message validating Not Blank Second surname.
     */
    public static final String SECOND_SURNAME_NOT_BLANK_MESSAGE = "Second surname must not be null or empty.";
    /**
     * Message validating MaxSize Second surname.
     */
    public static final String SECOND_SURNAME_MAX_LENGTH_MESSAGE = "Second surname must be max length of: "
            + SECOND_SURNAME_MAX_LENGTH + ".";
    /**
     * Message validating Birth date past.
     */
    public static final String BIRTH_DATE_PAST_MESSAGE = "Bith date must not be past.";

    /**
     * Message validating Not Blank Password.
     */
    public static final String PASSWORD_NOT_BLANK_MESSAGE = "Password must not be null or empty.";
    /**
     * Message validating MaxSize and MinSize Password.
     */
    public static final String PASSWORD_SIZE_MESSAGE = "Password must be between length of: "
            + PASSWORD_MIN_LENGTH + "AND" + PASSWORD_MAX_LENGTH + ".";

    /**
     * Message validating MaxSize Gender.
     */
    public static final String GENDER_MAX_LENGTH_MESSAGE = "Gender must be max length of: "
            + GENDER_MAX_LENGTH + ".";
    /**
     * Message validating Not Blank Gender.
     */
    public static final String GENDER_NOT_BLANK_MESSAGE = "Gender must not be null or empty.";

    /**
     * Default private constructor to avoid initialization.
     */
    private Constants() {
        super();
    }

}
