
package es.org.cxn.backapp.test.unit.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.responses.InvoiceListResponse;
import es.org.cxn.backapp.model.form.responses.InvoiceResponse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InvoiceListResponseTest {

  @Test
  void testConstructorAndGetters() {
    // Crear una lista de InvoiceResponse
    var invoiceResponse1 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    // Crear una instancia de InvoiceListResponse usando el constructor
    var response = new InvoiceListResponse(invoices);

    // Verificar que la lista de invoices en la respuesta coincide con la lista original
    assertNotNull(response.getInvoicesList());
    assertEquals(2, response.getInvoicesList().size());
    assertTrue(response.getInvoicesList().contains(invoiceResponse1));
    assertTrue(response.getInvoicesList().contains(invoiceResponse2));
  }

  @Test
  void testSetInvoicesList() {
    // Crear una lista de InvoiceResponse
    var invoiceResponse1 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    // Crear una instancia de InvoiceListResponse
    var invoicesList = new ArrayList<InvoiceResponse>();
    var response = new InvoiceListResponse(invoicesList);

    // Establecer la lista de invoices
    response.setInvoicesList(invoices);

    // Verificar que la lista de invoices en la respuesta coincide con la lista establecida
    assertNotNull(response.getInvoicesList());
    assertEquals(2, response.getInvoicesList().size());
    assertTrue(response.getInvoicesList().contains(invoiceResponse1));
    assertTrue(response.getInvoicesList().contains(invoiceResponse2));
  }

  @Test
  void testEqualsAndHashCode() {
    var invoiceResponse1 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices1 =
          List.of(invoiceResponse1, invoiceResponse2);
    List<InvoiceResponse> invoices2 =
          List.of(invoiceResponse1, invoiceResponse2);

    var response1 = new InvoiceListResponse(invoices1);
    var response2 = new InvoiceListResponse(invoices2);

    // Verificar que dos instancias con las mismas listas sean iguales
    assertEquals(response1, response2);
    assertEquals(response1.hashCode(), response2.hashCode());

    // Crear una instancia con una lista diferente
    var differentInvoiceResponse = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    List<InvoiceResponse> differentInvoices = List.of(differentInvoiceResponse);
    var response3 = new InvoiceListResponse(differentInvoices);

    // Verificar que las instancias con listas diferentes no sean iguales
    assertNotEquals(response1, response3);
  }

  @Test
  void testToString() {
    var invoiceResponse1 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    var response = new InvoiceListResponse(invoices);
    var expectedToString =
          "InvoiceListResponse [invoicesList=" + invoices + "]";

    // Verificar que el método toString retorna el formato esperado
    assertEquals(expectedToString, response.toString());
  }

  @Test
  void testEquals_SameInstance() {
    var invoiceResponse1 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    var response = new InvoiceListResponse(invoices);

    // Verificar que la instancia se compare con ella misma
    assertTrue(response.equals(response));
  }

  @Test
  void testEquals_NullObject() {
    var invoiceResponse1 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    var response = new InvoiceListResponse(invoices);

    // Verificar que se devuelve false cuando se compara con null
    Assertions.assertFalse(response.equals(null));
  }

  @Test
  void testEquals_DifferentClass() {
    var invoiceResponse1 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    var response = new InvoiceListResponse(invoices);

    // Verificar que se devuelve false cuando se compara con un objeto de otra clase
    Assertions.assertFalse(response.equals("String"));
  }

  @Test
  void testEquals_DifferentInvoicesList() {
    var invoiceResponse1 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices1 =
          List.of(invoiceResponse1, invoiceResponse2);

    var differentInvoiceResponse = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices2 = List.of(differentInvoiceResponse);

    var response1 = new InvoiceListResponse(invoices1);
    var response2 = new InvoiceListResponse(invoices2);

    // Verificar que se devuelve false cuando la lista de invoices es diferente
    Assertions.assertFalse(response1.equals(response2));
  }

  @Test
  void testEquals_SameInvoicesList() {
    var invoiceResponse1 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse(); // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices1 =
          List.of(invoiceResponse1, invoiceResponse2);

    var response1 = new InvoiceListResponse(invoices1);
    var response2 = new InvoiceListResponse(invoices1);

    // Verificar que se devuelve true cuando ambas instancias tienen la misma lista de invoices
    assertTrue(response1.equals(response2));
  }

}
