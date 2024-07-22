
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.requests.UserChangeRoleRequestForm;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class UserChangeRoleRequestTest {
  @Test
  void testGettersAndSetters() {
    // Crear una instancia de UserChangeRoleRequestForm
    var request = new UserChangeRoleRequestForm();

    // Establecer valores usando setters
    request.setEmail("user@example.com");
    List<UserRoleName> userRoles =
          Arrays.asList(UserRoleName.ROLE_SECRETARIO, UserRoleName.ROLE_SOCIO);
    request.setUserRoles(userRoles);

    assertEquals(
          "user@example.com", request.getEmail(), "valores usando getters"
    );
    assertEquals(userRoles, request.getUserRoles(), "valores usando getters");
  }

  @Test
  void testEquals() {
    // Crear dos instancias de UserChangeRoleRequestForm con los mismos valores
    List<UserRoleName> userRoles =
          Arrays.asList(UserRoleName.ROLE_ADMIN, UserRoleName.ROLE_PRESIDENTE);
    var request1 = new UserChangeRoleRequestForm("user@example.com", userRoles);
    var request2 = new UserChangeRoleRequestForm("user@example.com", userRoles);

    assertEquals(
          request1, request2, "las instancias son iguales usando equals"
    );
  }

  @Test
  void testNotEquals() {
    // Crear dos instancias de UserChangeRoleRequestForm con diferentes valores
    List<UserRoleName> userRoles1 =
          Arrays.asList(UserRoleName.ROLE_TESORERO, UserRoleName.ROLE_SOCIO);
    List<UserRoleName> userRoles2 = Arrays.asList(UserRoleName.ROLE_SOCIO);
    var request1 =
          new UserChangeRoleRequestForm("user1@example.com", userRoles1);
    var request2 =
          new UserChangeRoleRequestForm("user2@example.com", userRoles2);

    assertNotEquals(
          request1, request2, "las instancias no son iguales usando equals"
    );
  }

  @Test
  void testHashCode() {
    // Crear dos instancias de UserChangeRoleRequestForm con los mismos valores
    List<UserRoleName> userRoles =
          Arrays.asList(UserRoleName.ROLE_SOCIO, UserRoleName.ROLE_PRESIDENTE);
    var request1 = new UserChangeRoleRequestForm("user@example.com", userRoles);
    var request2 = new UserChangeRoleRequestForm("user@example.com", userRoles);

    assertEquals(
          request1.hashCode(), request2.hashCode(), "los hashCodes son iguales"
    );
  }
}
