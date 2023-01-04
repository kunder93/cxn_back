package es.org.cxn.backapp.exceptions;

/**
 * Exception throw when service cannot found user with identifier given.
 *
 * @author Santiago Paz.
 *
 */
public final class UserIdNotFoundException extends Exception {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 197355258225274114L;

    /**
     * Identifier field.
     */
    private final int userId;

    /**
     * Main constructor.
     *
     * @param identifier the user identifier.
     */
    public UserIdNotFoundException(final int identifier) {
        super(String.valueOf(identifier));
        this.userId = identifier;
    }

    /**
     * Identifier getter.
     *
     * @return Exception identifier field.
     */
    public int getId() {
        return userId;
    }

    @Override
    public String getMessage() {
        return "User with identifier: " + userId + " not found";

    }

}
