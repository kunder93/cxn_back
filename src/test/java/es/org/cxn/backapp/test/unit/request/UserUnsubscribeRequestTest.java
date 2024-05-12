
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.UserUnsubscribeRequest;

import org.junit.jupiter.api.Test;

class UserUnsubscribeRequestTest {

  @Test
  void testGettersAndSetters() {
    // Crear una instancia de UserUnsubscribeRequest
    var request = new UserUnsubscribeRequest();

    // Establecer valores usando setters
    request.setEmail("user@example.com");
    request.setPassword("password123");

    assertEquals(
          "user@example.com", request.getEmail(), "valores usando getters"
    );
    assertEquals(
          "password123", request.getPassword(), "valores usando getters"
    );
  }

  @Test
  void testEquals() {
    // Crear dos instancias de UserUnsubscribeRequest con los mismos valores
    var request1 =
          new UserUnsubscribeRequest("user@example.com", "password123");
    var request2 =
          new UserUnsubscribeRequest("user@example.com", "password123");

    assertEquals(
          request1, request2, "las instancias son iguales usando equals"
    );
  }

  @Test
  void testNotEquals() {
    // Crear dos instancias de UserUnsubscribeRequest con diferentes valores
    var request1 =
          new UserUnsubscribeRequest("user1@example.com", "password123");
    var request2 =
          new UserUnsubscribeRequest("user2@example.com", "password456");

    // Verificar que
    assertNotEquals(
          request1, request2, "las instancias no son iguales usando equals"
    );
  }

  @Test
  void testHashCode() {
    // Crear dos instancias de UserUnsubscribeRequest con los mismos valores
    var request1 =
          new UserUnsubscribeRequest("user@example.com", "password123");
    var request2 =
          new UserUnsubscribeRequest("user@example.com", "password123");

    assertEquals(
          request1.hashCode(), request2.hashCode(), "los hashCodes son iguales"
    );
  }

}
