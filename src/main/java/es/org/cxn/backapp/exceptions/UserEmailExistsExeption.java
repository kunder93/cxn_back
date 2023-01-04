package es.org.cxn.backapp.exceptions;

/**
 * Exception thrown by service when user with provided email already exists.
 * Cannot be more than one user with same email.
 *
 * @author Santiago Paz.
 *
 */
public final class UserEmailExistsExeption extends Exception {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 4629630590528223252L;

    /**
     * The email field.
     */
    private final String email;

    /**
     * Main constructor.
     *
     * @param value the user email.
     */
    public UserEmailExistsExeption(final String value) {
        super(value);
        this.email = value;
    }

    /**
     * Getter for email field.
     *
     * @return the email.
     */
    public String getEmail() {
        return email;
    }

    @Override
    public String getMessage() {
        return "Email: " + email + " cannot be used";
    }

}
