
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.UserChangeKindMemberRequest;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import org.junit.jupiter.api.Test;

/**
 * Unit test class for {@link UserChangeKindMemberRequest}.
 * <p>
 * This class contains tests to validate the behavior and functionality of
 * the {@link UserChangeKindMemberRequest} class. The tests cover getters,
 * setters, as well as the overridden {@code hashCode()} and {@code equals()}
 * methods.
 * </p>
 */
class UserChangeKindMemberRequestTest {

  /**
   * The user email used in the test cases.
   */
  private static final String USER_EMAIL = "user@houi.uk";

  /**
   * The user type for testing.
   */
  private static final UserType USER_KIND_MEMBER = UserType.SOCIO_NUMERO;

  /**
   * A different user type for testing.
   */
  private static final UserType SECOND_USER_KIND_MEMBER =
        UserType.SOCIO_FAMILIAR;

  /**
   * Tests the getters of the {@link UserChangeKindMemberRequest} class.
   * <p>
   * This test verifies that the getters return the correct values for
   * the {@code email} and {@code kindMember} fields.
   * </p>
   */
  @Test
  void testGetters() {
    // Create an instance of UserChangeKindMemberRequest
    var request = new UserChangeKindMemberRequest(USER_EMAIL, USER_KIND_MEMBER);

    // Verify that the email getter returns the expected value
    assertEquals(
          USER_EMAIL, request.email(), "Expected email value from getter"
    );

    // Verify that the kindMember getter returns the expected value
    assertEquals(
          USER_KIND_MEMBER, request.kindMember(),
          "Expected kindMember value from getter"
    );
  }

  /**
   * Tests the {@code hashCode()} method of the
   * {@link UserChangeKindMemberRequest} class.
   * <p>
   * This test ensures that two instances of
   * {@link UserChangeKindMemberRequest}
   * with the same values produce identical hash codes.
   * </p>
   */
  @Test
  void testHashCode() {
    // Create two instances of UserChangeKindMemberRequest with the same values
    var request1 =
          new UserChangeKindMemberRequest(USER_EMAIL, USER_KIND_MEMBER);
    var request2 =
          new UserChangeKindMemberRequest(USER_EMAIL, USER_KIND_MEMBER);

    // Verify that hash codes are equal for identical instances
    assertEquals(
          request1.hashCode(), request2.hashCode(),
          "Hash codes should be equal for identical instances"
    );
  }

  /**
   * Tests the {@code equals()} method of the
   * {@link UserChangeKindMemberRequest} class.
   * <p>
   * This test verifies that two instances of
   * {@link UserChangeKindMemberRequest}
   * with the same values are considered equal, while instances with different
   * values are not equal.
   * </p>
   */
  @Test
  void testEquals() {
    // Create two instances of UserChangeKindMemberRequest with the same values
    var request1 =
          new UserChangeKindMemberRequest(USER_EMAIL, USER_KIND_MEMBER);
    var request2 =
          new UserChangeKindMemberRequest(USER_EMAIL, USER_KIND_MEMBER);

    // Verify that instances with the same values are equal
    assertEquals(
          request1, request2,
          "Instances should be equal when all field values are the same"
    );

    // Create an instance with a different user type
    var differentRequest =
          new UserChangeKindMemberRequest(USER_EMAIL, SECOND_USER_KIND_MEMBER);

    // Verify that instances with different values are not equal
    assertNotEquals(
          request1, differentRequest,
          "Instances should not be equal when the kindMember value is different"
    );
  }
}
