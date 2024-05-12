
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.CompanyUpdateRequestForm;

import org.junit.jupiter.api.Test;

class CompanyUpdateRequestTest {
  @Test
  void testGettersAndSetters() {
    // Crear una instancia de CompanyUpdateRequestForm
    var request = new CompanyUpdateRequestForm();

    // Establecer valores usando setters
    request.setName("ACME Inc.");
    request.setAddress("123 Main St.");

    assertEquals("ACME Inc.", request.getName(), "valores usando getters");
    assertEquals(
          "123 Main St.", request.getAddress(), "valores usando getters"
    );
  }

  @Test
  void testEqualsAndHashCode() {
    // Crear dos instancias de CompanyUpdateRequestForm con los mismos valores
    var request1 =
          new CompanyUpdateRequestForm("ACME Inc.", "123 Main St.");
    var request2 =
          new CompanyUpdateRequestForm("ACME Inc.", "123 Main St.");

    assertEquals(
          request1, request2, "las instancias son iguales usando equals"
    );

    assertEquals(
          request1.hashCode(), request2.hashCode(), "hashCodes son iguales"
    );

    // Modificar una de las instancias
    request2.setAddress("456 Elm St.");

    assertNotEquals(
          request1, request2, "las instancias ya no son iguales usando equals"
    );

    assertNotEquals(
          request1.hashCode(), request2.hashCode(),
          "hashCodes ya no son iguales"
    );
  }

}
