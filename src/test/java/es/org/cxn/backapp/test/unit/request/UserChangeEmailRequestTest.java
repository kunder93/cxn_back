
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.UserChangeEmailRequest;

import org.junit.jupiter.api.Test;

/**
 * Unit test class for {@link UserChangeEmailRequest}.
 * <p>
 * This class contains tests for verifying the functionality of the
 * {@link UserChangeEmailRequest} class, including its getters, setters,
 * and overridden methods like {@code hashCode()} and {@code equals()}.
 * </p>
 */
class UserChangeEmailRequestTest {

  /**
   * The current user email used in the test cases.
   */
  private static final String USER_EMAIL = "user@email.com";

  /**
   * The new email address to be set.
   */
  private static final String USER_NEW_EMAIL = "user@other.com";

  /**
   * A different new email address for testing inequality.
   */
  private static final String SECOND_USER_NEW_EMAIL = "otherema@mail.in";

  /**
   * Tests the getters of the {@link UserChangeEmailRequest} class.
   * <p>
   * This test verifies that the getters return the correct values for
   * the {@code email} and {@code newEmail} fields.
   * </p>
   */
  @Test
  void testGetters() {
    // Create an instance of UserChangeEmailRequest
    var request = new UserChangeEmailRequest(USER_EMAIL, USER_NEW_EMAIL);

    // Verify that the email getter returns the expected value
    assertEquals(
          USER_EMAIL, request.email(), "Expected email value from getter"
    );

    // Verify that the newEmail getter returns the expected value
    assertEquals(
          USER_NEW_EMAIL, request.newEmail(),
          "Expected newEmail value from getter"
    );
  }

  /**
   * Tests the {@code equals()} method of the
   * {@link UserChangeEmailRequest} class.
   * <p>
   * This test checks that two instances of {@link UserChangeEmailRequest}
   * with the same values are considered equal.
   * </p>
   */
  @Test
  void testEquals() {
    // Create two instances of UserChangeEmailRequest with the same values
    var request1 = new UserChangeEmailRequest(USER_EMAIL, USER_NEW_EMAIL);
    var request2 = new UserChangeEmailRequest(USER_EMAIL, USER_NEW_EMAIL);

    // Verify that instances with the same values are equal
    assertEquals(
          request1, request2, "Instances with the same values should be equal"
    );
  }

  /**
   * Tests the inequality of the {@code equals()} method of the
   * {@link UserChangeEmailRequest} class.
   * <p>
   * This test checks that two instances of {@link UserChangeEmailRequest}
   * with different values are not considered equal.
   * </p>
   */
  @Test
  void testNotEquals() {
    // Create two instances of UserChangeEmailRequest with different values
    var request1 = new UserChangeEmailRequest(USER_EMAIL, USER_NEW_EMAIL);
    var request2 =
          new UserChangeEmailRequest(USER_EMAIL, SECOND_USER_NEW_EMAIL);

    // Verify that instances with different values are not equal
    assertNotEquals(
          request1, request2,
          "Instances with different values should not be equal"
    );
  }

  /**
   * Tests the {@code hashCode()} method of the
   * {@link UserChangeEmailRequest} class.
   * <p>
   * This test ensures that two instances of
   * {@link UserChangeEmailRequest} with the same values produce identical
   * hash codes.
   * </p>
   */
  @Test
  void testHashCode() {
    // Create two instances of UserChangeEmailRequest with the same values
    var request1 = new UserChangeEmailRequest(USER_EMAIL, USER_NEW_EMAIL);
    var request2 = new UserChangeEmailRequest(USER_EMAIL, USER_NEW_EMAIL);

    // Verify that hash codes are equal for identical instances
    assertEquals(
          request1.hashCode(), request2.hashCode(),
          "Hash codes should be equal for identical instances"
    );
  }
}
