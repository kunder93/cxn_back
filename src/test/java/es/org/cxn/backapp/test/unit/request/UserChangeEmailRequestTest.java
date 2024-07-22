
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.UserChangeEmailRequest;

import org.junit.jupiter.api.Test;

class UserChangeEmailRequestTest {
  @Test
  void testGettersAndSetters() {
    // Crear una instancia de UserChangeEmailRequest
    var request = new UserChangeEmailRequest();

    // Establecer valores usando setters
    request.setEmail("user@example.com");
    request.setNewEmail("newuser@example.com");

    assertEquals(
          "user@example.com", request.getEmail(), "valores usando getters"
    );
    assertEquals(
          "newuser@example.com", request.getNewEmail(), "valores usando getters"
    );
  }

  @Test
  void testEquals() {
    // Crear dos instancias de UserChangeEmailRequest con los mismos valores
    var request1 =
          new UserChangeEmailRequest("user@example.com", "newuser@example.com");
    var request2 =
          new UserChangeEmailRequest("user@example.com", "newuser@example.com");

    assertEquals(
          request1, request2, "las instancias son iguales usando equals"
    );
  }

  @Test
  void testNotEquals() {
    // Crear dos instancias de UserChangeEmailRequest con diferentes valores
    var request1 = new UserChangeEmailRequest(
          "user1@example.com", "newuser@example.com"
    );
    var request2 = new UserChangeEmailRequest(
          "user2@example.com", "newuser@example.com"
    );

    assertNotEquals(
          request1, request2, "que las instancias no son iguales usando equals"
    );
  }

  @Test
  void testHashCode() {
    // Crear dos instancias de UserChangeEmailRequest con los mismos valores
    var request1 =
          new UserChangeEmailRequest("user@example.com", "newuser@example.com");
    var request2 =
          new UserChangeEmailRequest("user@example.com", "newuser@example.com");

    assertEquals(
          request1.hashCode(), request2.hashCode(), "los hashCodes son iguales"
    );
  }
}
