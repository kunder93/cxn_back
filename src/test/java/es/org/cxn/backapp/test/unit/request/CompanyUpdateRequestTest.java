
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.CompanyUpdateRequestForm;

import org.junit.jupiter.api.Test;

class CompanyUpdateRequestTest {
  @Test
  void testGettersAndSetters() {
    // Crear una instancia de CompanyUpdateRequestForm
    final var name = "ACME Inc.";
    final var address = "123 Main St.";
    var request = new CompanyUpdateRequestForm(name, address);

    assertEquals(name, request.name(), "valores usando getters");
    assertEquals(address, request.address(), "valores usando getters");
  }

  @Test
  void testEqualsAndHashCode() {
    // Crear dos instancias de CompanyUpdateRequestForm con los mismos valores
    var request1 = new CompanyUpdateRequestForm("ACME Inc.", "123 Main St.");
    var request2 = new CompanyUpdateRequestForm("ACME Inc.", "123 Main St.");

    assertEquals(
          request1, request2, "las instancias son iguales usando equals"
    );

    assertEquals(
          request1.hashCode(), request2.hashCode(), "hashCodes son iguales"
    );

    // Crea otra instancia
    final var request3 =
          new CompanyUpdateRequestForm("ACME Inc.", "456 IIII St.");

    assertNotEquals(
          request1, request3, "las instancias ya no son iguales usando equals"
    );

    assertNotEquals(
          request1.hashCode(), request3.hashCode(),
          "hashCodes ya no son iguales"
    );
  }

}
