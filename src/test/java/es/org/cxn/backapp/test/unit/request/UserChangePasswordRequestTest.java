
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.UserChangePasswordRequest;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link UserChangePasswordRequest}.
 * <p>
 * This test class verifies the functionality of the
 * {@link UserChangePasswordRequest} class
 * including the correctness of its getters, equals, hashCode methods, and the
 * immutability of
 * its fields.
 * <p>
 * It ensures that the class correctly handles equality, hashing, and value
 * retrieval operations.
 * </p>
 */
class UserChangePasswordRequestTest {

  /**
   * User email field for testing.
   */
  private static final String USER_EMAIL = "user@example.com";

  /**
   * User current password for testing.
   */
  private static final String USER_CURRENT_PASSWORD = "qwoj3123PP";

  /**
   * User new password for testing.
   */
  private static final String USER_NEW_PASSWORD = "UUOQ324lkda";

  /**
   * Different user email for testing.
   */
  private static final String SECOND_USER_EMAIL = "other@email.com";

  /**
   * Different current password for testing.
   */
  private static final String SECOND_USER_CURRENT_PASSWORD = "opqropi321";

  /**
   * Different new password for testing.
   */
  private static final String SECOND_USER_NEW_PASSWORD = "jqw234jdu23";

  /**
   * Tests the getters of the {@link UserChangePasswordRequest} class to ensure
   * they return the correct values.
   */
  @Test
  void testGetters() {
    // Create an instance of UserChangePasswordRequest
    var request = new UserChangePasswordRequest(
          USER_EMAIL, USER_CURRENT_PASSWORD, USER_NEW_PASSWORD
    );

    assertEquals(
          USER_EMAIL, request.email(),
          "Verify that the email getter returns the correct value"
    );
    assertEquals(
          USER_CURRENT_PASSWORD, request.currentPassword(),
          "Verify that the currentPassword getter returns the correct value"
    );
    assertEquals(
          USER_NEW_PASSWORD, request.newPassword(),
          "Verify that the newPassword getter returns the correct value"
    );
  }

  /**
   * Tests the equals method of the {@link UserChangePasswordRequest} class to
   * ensure that two instances with the same values are considered equal.
   */
  @Test
  void testEquals() {
    // Create two instances of UserChangePasswordRequest with the same values
    var request1 = new UserChangePasswordRequest(
          USER_EMAIL, USER_CURRENT_PASSWORD, USER_NEW_PASSWORD
    );
    var request2 = new UserChangePasswordRequest(
          USER_EMAIL, USER_CURRENT_PASSWORD, USER_NEW_PASSWORD
    );

    assertEquals(
          request1, request2,
          "Verify that two instances with the same values are considered equal"
    );
    assertEquals(request2, request1, "Verify that equality is symmetric");
  }

  /**
   * Tests the equals method of the {@link UserChangePasswordRequest} class to
   * ensure that two instances with different values are not considered equal.
   */
  @Test
  void testNotEquals() {
    // Create two instances of UserChangePasswordRequest with different values
    var request1 = new UserChangePasswordRequest(
          USER_EMAIL, USER_CURRENT_PASSWORD, USER_NEW_PASSWORD
    );
    var request2 = new UserChangePasswordRequest(
          SECOND_USER_EMAIL, SECOND_USER_CURRENT_PASSWORD,
          SECOND_USER_NEW_PASSWORD
    );

    assertNotEquals(
          request1, request2,
          "Verify that two instances with different values are not considered"
                + " equal"
    );
    assertNotEquals(request2, request1, "Verify that inequality is symmetric");
  }

  /**
   * Tests the hashCode method of the {@link UserChangePasswordRequest} class
   * to ensure that two instances with the same values produce the same
   * hash code.
   */
  @Test
  void testHashCode() {
    // Create two instances of UserChangePasswordRequest with the same values
    var request1 = new UserChangePasswordRequest(
          USER_EMAIL, USER_CURRENT_PASSWORD, USER_NEW_PASSWORD
    );
    var request2 = new UserChangePasswordRequest(
          USER_EMAIL, USER_CURRENT_PASSWORD, USER_NEW_PASSWORD
    );

    assertEquals(
          request1.hashCode(), request2.hashCode(),
          "Verify that two instances with the same values have the same"
                + " hash code"
    );
  }

  /**
   * Tests the hashCode method of the {@link UserChangePasswordRequest} class
   * to ensure that two instances with different values produce different hash
   * codes.
   */
  @Test
  void testHashCodeDifferentValues() {
    // Create two instances of UserChangePasswordRequest with different values
    var request1 = new UserChangePasswordRequest(
          USER_EMAIL, USER_CURRENT_PASSWORD, USER_NEW_PASSWORD
    );
    var request2 = new UserChangePasswordRequest(
          SECOND_USER_EMAIL, SECOND_USER_CURRENT_PASSWORD,
          SECOND_USER_NEW_PASSWORD
    );

    assertNotEquals(
          request1.hashCode(), request2.hashCode(),
          "Verify that two instances with different values have different"
                + " hash codes"
    );
  }
}
