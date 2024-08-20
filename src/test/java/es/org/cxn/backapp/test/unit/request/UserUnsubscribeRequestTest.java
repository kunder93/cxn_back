
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.UserUnsubscribeRequest;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link UserUnsubscribeRequest}.
 * <p>
 * This class contains tests to verify the correctness of the
 * {@link UserUnsubscribeRequest}
 * record. It ensures that the record's methods, such as getters, `equals`,
 * and `hashCode`, behave as expected.
 * </p>
 *
 * @author Santiago Paz
 */
class UserUnsubscribeRequestTest {

  /**
   * The user email field for use in tests.
   * <p>
   * This value is used as a sample email for creating instances of
   * {@link UserUnsubscribeRequest} in test scenarios.
   * </p>
   */
  private static final String USER_EMAIL = "santi@santi.es";

  /**
   * Different user email for use in tests.
   * <p>
   * This value represents an alternate email address used to verify
   * the behavior of the `equals` method in test scenarios.
   * </p>
   */
  private static final String SECOND_USER_EMAIL = "other@other.es";

  /**
   * Different user password for use in tests.
   * <p>
   * This value represents an alternate password used to verify the
   * behavior of the `equals` method in test scenarios.
   * </p>
   */
  private static final String SECOND_USER_PASSWORD = "qwodSA23SA2";

  /**
   * The user password field for use in tests.
   * <p>
   * This value is used as a sample password for creating instances of
   * {@link UserUnsubscribeRequest} in test scenarios.
   * </p>
   */
  private static final String USER_PASSWORD = "dfkjqfi23DJ";

  /**
   * Tests the getters of the {@link UserUnsubscribeRequest} record.
   * <p>
   * This test verifies that the getters of the record correctly return
   * the values provided during the instantiation of the record.
   * </p>
   */
  @Test
  void testGettersAndSetters() {
    // Create an instance of UserUnsubscribeRequest
    var request = new UserUnsubscribeRequest(USER_EMAIL, USER_PASSWORD);

    assertEquals(
          USER_EMAIL, request.email(), "Values should match using getters."
    );
    assertEquals(
          USER_PASSWORD, request.password(),
          "Values should match using getters."
    );
  }

  /**
   * Tests the {@link UserUnsubscribeRequest#equals(Object)} method.
   * <p>
   * This test verifies that two instances of {@link UserUnsubscribeRequest}
   * with the same values are considered equal.
   * </p>
   */
  @Test
  void testEquals() {
    // Create two instances of UserUnsubscribeRequest with the same values
    var request1 = new UserUnsubscribeRequest(USER_EMAIL, USER_PASSWORD);
    var request2 = new UserUnsubscribeRequest(USER_EMAIL, USER_PASSWORD);

    assertEquals(request1, request2, "Instances should be equal using equals.");
  }

  /**
   * Tests the {@link UserUnsubscribeRequest#equals(Object)} method for
   * inequality.
   * <p>
   * This test verifies that two instances of {@link UserUnsubscribeRequest}
   * with different values are not considered equal.
   * </p>
   */
  @Test
  void testNotEquals() {
    // Create two instances of UserUnsubscribeRequest with different values
    var request1 = new UserUnsubscribeRequest(USER_EMAIL, USER_PASSWORD);
    var request2 =
          new UserUnsubscribeRequest(SECOND_USER_EMAIL, SECOND_USER_PASSWORD);

    assertNotEquals(
          request1, request2, "Instances should not be equal using equals."
    );
  }

  /**
   * Tests the {@link UserUnsubscribeRequest#hashCode()} method.
   * <p>
   * This test verifies that the `hashCode` method produces the same hash code
   * for instances of {@link UserUnsubscribeRequest} with the same values.
   * </p>
   */
  @Test
  void testHashCode() {
    // Create two instances of UserUnsubscribeRequest with the same values
    var request1 = new UserUnsubscribeRequest(USER_EMAIL, USER_PASSWORD);
    var request2 = new UserUnsubscribeRequest(USER_EMAIL, USER_PASSWORD);

    assertEquals(
          request1.hashCode(), request2.hashCode(),
          "Hash codes should match for equal values."
    );
  }

}
