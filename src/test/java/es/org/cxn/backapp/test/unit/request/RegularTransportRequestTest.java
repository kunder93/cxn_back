
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.AddRegularTransportRequestForm;

import org.junit.jupiter.api.Test;

class RegularTransportRequestTest {
  @Test
  void testGettersAndSetters() {
    // Crear una instancia de AddRegularTransportRequestForm
    var request = new AddRegularTransportRequestForm();

    // Establecer valores usando setters
    request.setCategory("Category");
    request.setDescription("Description");
    request.setInvoiceNumber(123);
    request.setInvoiceSeries("Series");

    assertEquals("Category", request.getCategory(), "valores usando getters");
    assertEquals(
          "Description", request.getDescription(), "valores usando getters"
    );
    assertEquals(123, request.getInvoiceNumber(), "valores usando getters");
    assertEquals(
          "Series", request.getInvoiceSeries(), "valores usando getters"
    );
  }

  @Test
  void testEquals() {
    // Crear dos instancias de AddRegularTransportRequestForm con los mismos valores
    var request1 = new AddRegularTransportRequestForm(
          "Category", "Description", 123, "Series"
    );
    var request2 = new AddRegularTransportRequestForm(
          "Category", "Description", 123, "Series"
    );

    // Verificar que
    assertEquals(
          request1, request2, "las instancias son iguales usando equals"
    );
    assertEquals(
          request2, request1, "las instancias son iguales usando equals"
    );
  }

  @Test
  void testNotEquals() {
    // Crear dos instancias de AddRegularTransportRequestForm con diferentes valores
    var request1 = new AddRegularTransportRequestForm(
          "Category1", "Description1", 123, "Series1"
    );
    var request2 = new AddRegularTransportRequestForm(
          "Category2", "Description2", 456, "Series2"
    );

    assertNotEquals(
          request1, request2, "las instancias no son iguales usando equals"
    );
    assertNotEquals(
          request2, request1, "las instancias no son iguales usando equals"
    );
  }

  @Test
  void testHashCode() {
    // Crear dos instancias de AddRegularTransportRequestForm con los mismos valores
    var request1 = new AddRegularTransportRequestForm(
          "Category", "Description", 123, "Series"
    );
    var request2 = new AddRegularTransportRequestForm(
          "Category", "Description", 123, "Series"
    );

    assertEquals(
          request1.hashCode(), request2.hashCode(), "los hashCodes son iguales"
    );
  }
}
