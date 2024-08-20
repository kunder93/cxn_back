
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.requests.UserChangeRoleRequestForm;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link UserChangeRoleRequestForm} class.
 * <p>
 * This class tests the behavior of the {@link UserChangeRoleRequestForm} class,
 * including its getters, equality, and hash code methods.
 * <p>
 * Tests include:
 * <ul>
 *   <li>Verification of getter methods</li>
 *   <li>Checking equality of instances with the same values</li>
 *   <li>Ensuring instances with different values are not considered equal</li>
 *   <li>Consistency of hash codes for equal instances</li>
 * </ul>
 * <p>
 * The test data includes various user emails and role names to ensure thorough
 * coverage.
 *
 * @author Santiago Paz
 */
class UserChangeRoleRequestTest {

  /**
   * User email field for use in tests.
   */
  private static final String USER_EMAIL = "user@example.es";

  /**
   * A different email field for use in equality and hash code tests.
   */
  private static final String SECOND_USER_EMAIL = "other@other.es";

  /**
   * User roles for use in equality and hash code tests.
   */
  private static final List<UserRoleName> USER_ROLES =
        Arrays.asList(UserRoleName.ROLE_SECRETARIO, UserRoleName.ROLE_SOCIO);

  /**
   * Different user roles for use in equality and hash code tests.
   */
  private static final List<UserRoleName> SECOND_USER_ROLES =
        Arrays.asList(UserRoleName.ROLE_PRESIDENTE, UserRoleName.ROLE_SOCIO);

  @Test
  void testGetters() {
    // Create an instance of UserChangeRoleRequestForm
    var request = new UserChangeRoleRequestForm(USER_EMAIL, USER_ROLES);

    assertEquals(
          USER_EMAIL, request.email(), "Values should match using" + " getters"
    );
    assertEquals(
          USER_ROLES, request.userRoles(),
          "Values should match using" + " getters"
    );
  }

  @Test
  void testEquals() {
    // Create two instances of UserChangeRoleRequestForm with the same values
    var request1 = new UserChangeRoleRequestForm(USER_EMAIL, USER_ROLES);
    var request2 = new UserChangeRoleRequestForm(USER_EMAIL, USER_ROLES);

    assertEquals(request1, request2, "Instances should be equal using equals");
  }

  @Test
  void testNotEquals() {
    // Create two instances of UserChangeRoleRequestForm with different values
    var request1 = new UserChangeRoleRequestForm(USER_EMAIL, USER_ROLES);
    var request2 =
          new UserChangeRoleRequestForm(SECOND_USER_EMAIL, SECOND_USER_ROLES);

    assertNotEquals(
          request1, request2, "Instances should not be equal using equals"
    );
  }

  @Test
  void testHashCode() {
    // Create two instances of UserChangeRoleRequestForm with the same values
    var request1 = new UserChangeRoleRequestForm(USER_EMAIL, USER_ROLES);
    var request2 = new UserChangeRoleRequestForm(USER_EMAIL, USER_ROLES);

    assertEquals(
          request1.hashCode(), request2.hashCode(),
          "Hash codes should be equal for equal instances"
    );
  }
}
