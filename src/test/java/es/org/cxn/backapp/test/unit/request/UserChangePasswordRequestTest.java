
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.UserChangePasswordRequest;

import org.junit.jupiter.api.Test;

class UserChangePasswordRequestTest {
  @Test
  void testGettersAndSetters() {
    // Crear una instancia de UserChangePasswordRequest
    var request = new UserChangePasswordRequest();

    // Establecer valores usando setters
    request.setEmail("user@example.com");
    request.setCurrentPassword("oldPassword");
    request.setNewPassword("newPassword");

    assertEquals(
          "user@example.com", request.getEmail(),
          " Verifica los valores usando getters"
    );
    assertEquals(
          "oldPassword", request.getCurrentPassword(),
          "Verifica los valores usando getters"
    );
    assertEquals(
          "newPassword", request.getNewPassword(),
          "Verifica los valores usando getters"
    );
  }

  @Test
  void testEquals() {
    // Crear dos instancias de UserChangePasswordRequest con los mismos valores
    var request1 = new UserChangePasswordRequest(
          "user@example.com", "oldPassword", "newPassword"
    );
    var request2 = new UserChangePasswordRequest(
          "user@example.com", "oldPassword", "newPassword"
    );

    assertEquals(
          request1, request2, "las instancias son iguales usando equals"
    );
    assertEquals(
          request2, request1, "las instancias son iguales usando equals"
    );
  }

  @Test
  void testNotEquals() {
    // Crear dos instancias de UserChangePasswordRequest con diferentes valores
    var request1 = new UserChangePasswordRequest(
          "user1@example.com", "oldPassword1", "newPassword1"
    );
    var request2 = new UserChangePasswordRequest(
          "user2@example.com", "oldPassword2", "newPassword2"
    );

    assertNotEquals(
          request1, request2,
          "Verifica que las instancias no son iguales usando equals"
    );
    assertNotEquals(
          request2, request1,
          "Verifica que las instancias no son iguales usando equals"
    );
  }

  @Test
  void testHashCode() {
    // Crear dos instancias de UserChangePasswordRequest con los mismos valores
    var request1 = new UserChangePasswordRequest(
          "user@example.com", "oldPassword", "newPassword"
    );
    var request2 = new UserChangePasswordRequest(
          "user@example.com", "oldPassword", "newPassword"
    );

    assertEquals(
          request1.hashCode(), request2.hashCode(), "los hashCodes son iguales"
    );
  }

  @Test
  void testBuilder() {
    // Crear una instancia de UserChangePasswordRequest usando el builder
    var request = UserChangePasswordRequest.builder().email("user@example.com")
          .currentPassword("oldPassword").newPassword("newPassword").build();

    assertEquals(
          "user@example.com", request.getEmail(),
          "los valores establecidos por el builder son correctos"
    );
    assertEquals(
          "oldPassword", request.getCurrentPassword(),
          "los valores establecidos por el builder son correctos"
    );
    assertEquals(
          "newPassword", request.getNewPassword(),
          "los valores establecidos por el builder son correctos"
    );
  }

}
