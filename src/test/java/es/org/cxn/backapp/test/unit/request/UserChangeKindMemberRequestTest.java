
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.UserChangeKindMemberRequest;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import org.junit.jupiter.api.Test;

class UserChangeKindMemberRequestTest {

  @Test
  void testGettersAndSetters() {
    // Crear una instancia de UserChangeKindMemberRequest
    var request = new UserChangeKindMemberRequest();

    // Establecer valores usando setters
    request.setEmail("user@example.com");
    request.setKindMember(UserType.SOCIO_NUMERO);

    assertEquals(
          "user@example.com", request.getEmail(), "valores usando getters"
    );
    assertEquals(
          UserType.SOCIO_NUMERO, request.getKindMember(),
          "valores usando getters"
    );
  }

  @Test
  void testHashCode() {
    // Crear dos instancias de UserChangeKindMemberRequest con los mismos valores
    var request1 = new UserChangeKindMemberRequest(
          "user@example.com", UserType.SOCIO_NUMERO
    );
    var request2 = new UserChangeKindMemberRequest(
          "user@example.com", UserType.SOCIO_NUMERO
    );

    assertEquals(
          request1.hashCode(), request2.hashCode(), "los hashCodes son iguales"
    );
  }

  @Test
  void testEquals() {
    // Crear dos instancias de UserChangeKindMemberRequest con los mismos valores
    var request1 = new UserChangeKindMemberRequest(
          "user@example.com", UserType.SOCIO_NUMERO
    );
    var request2 = new UserChangeKindMemberRequest(
          "user@example.com", UserType.SOCIO_NUMERO
    );

    assertEquals(
          request1, request2, "las instancias son iguales usando equals"
    );

    // Modificar una de las instancias
    request2.setEmail("anotheruser@example.com");

    assertNotEquals(request1, request2, "las instancias ya no son iguales");
  }

}
