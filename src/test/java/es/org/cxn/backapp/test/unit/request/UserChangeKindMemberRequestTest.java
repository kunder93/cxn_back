
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.UserChangeKindMemberRequest;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import org.junit.jupiter.api.Test;

/**
 * Unit test class for {@link UserChangeKindMemberRequest}.
 * <p>
 * This class contains tests for verifying the functionality of the
 * {@link UserChangeKindMemberRequest} class, including its getters,
 * setters, and overridden methods like {@code hashCode()} and
 * {@code equals()}.
 * </p>
 */
class UserChangeKindMemberRequestTest {

  /**
   * Tests the getters and setters of the
   * {@link UserChangeKindMemberRequest} class.
   * <p>
   * This test sets values using the setters and verifies that the
   * getters return the correct values.
   * </p>
   */
  @Test
  void testGettersAndSetters() {
    // Create an instance of UserChangeKindMemberRequest
    var request = new UserChangeKindMemberRequest();

    // Set values using setters
    request.setEmail("user@example.com");
    request.setKindMember(UserType.SOCIO_NUMERO);

    // Verify that getters return the expected values
    assertEquals(
          "user@example.com", request.getEmail(),
          "Expected email value from getter"
    );
    assertEquals(
          UserType.SOCIO_NUMERO, request.getKindMember(),
          "Expected kindMember value from getter"
    );
  }

  /**
   * Tests the {@code hashCode()} method of the
   * {@link UserChangeKindMemberRequest} class.
   * <p>
   * This test ensures that two instances of
   * {@link UserChangeKindMemberRequest} with the same values have
   * identical hash codes.
   * </p>
   */
  @Test
  void testHashCode() {
    // Create two instances of UserChangeKindMemberRequest with the same values
    var request1 = new UserChangeKindMemberRequest(
          "user@example.com", UserType.SOCIO_NUMERO
    );
    var request2 = new UserChangeKindMemberRequest(
          "user@example.com", UserType.SOCIO_NUMERO
    );

    // Verify that hash codes are equal
    assertEquals(
          request1.hashCode(), request2.hashCode(),
          "Hash codes should be equal for identical instances"
    );
  }

  /**
   * Tests the {@code equals()} method of the
   * {@link UserChangeKindMemberRequest} class.
   * <p>
   * This test checks that two instances of
   * {@link UserChangeKindMemberRequest} with the same values are
   * considered equal, and instances with different values are not.
   * </p>
   */
  @Test
  void testEquals() {
    // Create two instances of UserChangeKindMemberRequest with the same values
    var request1 = new UserChangeKindMemberRequest(
          "user@example.com", UserType.SOCIO_NUMERO
    );
    var request2 = new UserChangeKindMemberRequest(
          "user@example.com", UserType.SOCIO_NUMERO
    );

    // Verify that instances are equal
    assertEquals(
          request1, request2, "Instances should be equal using equals()"
    );

    // Modify one of the instances
    request2.setEmail("anotheruser@example.com");

    // Verify that instances are no longer equal
    assertNotEquals(
          request1, request2, "Instances should not be equal after modification"
    );
  }

}
