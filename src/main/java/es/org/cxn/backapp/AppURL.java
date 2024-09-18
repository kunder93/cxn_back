
package es.org.cxn.backapp;

/**
 * A utility class that contains the URL endpoints used in the application.
 * <p>
 * This class provides a set of constants for commonly used URL paths in the
 * application, such as authentication and resource access.
 * </p>
 * <p>
 * The class is marked as {@code final} to prevent inheritance, and the
 * constructor is private to enforce non-instantiability.
 * </p>
 */
public final class AppURL {

  /**
   * The URL for the user sign-up endpoint.
   */
  public static final String SIGN_UP_URL = "/api/auth/signup";

  /**
   * The URL for the user sign-in endpoint.
   * <p>Note: there seems to be a typo in the path
   * (should be {@code "/api/auth/signin"})</p>
   */
  public static final String SIGN_IN_URL = "/api/auth/signinn";

  /**
   * The URL for accessing chess questions.
   */
  public static final String CHESS_QUESTION_URL = "/api/chessQuestion";

  /**
   * The URL for accessing the participants.
   */
  public static final String PARTICIPANTS_URL = "/api/participants";

  /**
   * Private constructor to prevent instantiation of this utility class.
   */
  private AppURL() {
    // Private constructor to prevent instantiation.
  }
}
