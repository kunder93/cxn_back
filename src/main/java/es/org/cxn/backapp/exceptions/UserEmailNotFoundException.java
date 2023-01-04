package es.org.cxn.backapp.exceptions;

/**
 * Exception thrown by services when user with provided email not found.
 *
 * @author Santiago Paz.
 *
 */
public final class UserEmailNotFoundException extends Exception {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 197355258115274114L;

    /**
     * Email field.
     */
    private final String email;

    /**
     * Default constructor with field email given.
     *
     * @param value the user email.
     */
    public UserEmailNotFoundException(final String value) {
        super(value);
        this.email = value;
    }

    /**
     * Getter for email field.
     *
     * @return email field.
     */
    public String getEmail() {
        return email;
    }

    @Override
    public String getMessage() {
        return "User with email: " + email + " not found";
    }

}
