
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
    var invoiceResponse1 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    // Crear una instancia de InvoiceListResponse usando el constructor
    var response = new InvoiceListResponse(invoices);

    // Verificar lista de invoices en la respuesta coincide con lista original
    assertNotNull(
          response.getInvoicesList(), "The invoices list should not be null"
    );
    assertEquals(
          2, response.getInvoicesList().size(),
          "The invoices list size should be 2"
    );
    assertTrue(
          response.getInvoicesList().contains(invoiceResponse1),
          "The invoices list should contain invoiceResponse1"
    );
    assertTrue(
          response.getInvoicesList().contains(invoiceResponse2),
          "The invoices list should contain invoiceResponse2"
    );
  }

  @Test
  void testSetInvoicesList() {
    // Crear una lista de InvoiceResponse
    var invoiceResponse1 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    // Crear una instancia de InvoiceListResponse
    var invoicesList = new ArrayList<InvoiceResponse>();
    var response = new InvoiceListResponse(invoicesList);

    // Establecer la lista de invoices
    response.setInvoicesList(invoices);

    // Verificar que la lista de invoices en la
    // respuesta coincide con la lista establecida
    assertNotNull(
          response.getInvoicesList(),
          "The invoices list should not be null after setting it"
    );
    assertEquals(
          2, response.getInvoicesList().size(),
          "The invoices list size should be 2 after setting it"
    );
    assertTrue(
          response.getInvoicesList().contains(invoiceResponse1),
          "The invoices list should contain invoiceResponse1 after setting it"
    );
    assertTrue(
          response.getInvoicesList().contains(invoiceResponse2),
          "The invoices list should contain invoiceResponse2 after setting it"
    );
  }

  @Test
  void testEqualsAndHashCode() {
    var invoiceResponse1 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices1 =
          List.of(invoiceResponse1, invoiceResponse2);
    List<InvoiceResponse> invoices2 =
          List.of(invoiceResponse1, invoiceResponse2);

    var response1 = new InvoiceListResponse(invoices1);
    var response2 = new InvoiceListResponse(invoices2);

    // Verificar que dos instancias con las mismas listas sean iguales
    assertEquals(
          response1, response2,
          "Responses with identical invoice lists should be equal"
    );
    assertEquals(
          response1.hashCode(), response2.hashCode(),
          "Hash codes should be equal for responses with identical "
                + "invoice lists"
    );

    // Crear una instancia con una lista diferente
    var differentInvoiceResponse = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> differentInvoices = List.of(differentInvoiceResponse);
    var response3 = new InvoiceListResponse(differentInvoices);

    // Verificar que las instancias con listas diferentes no sean iguales
    assertNotEquals(
          response1, response3,
          "Responses with different invoice lists should not be equal"
    );
  }

  @Test
  void testToString() {
    var invoiceResponse1 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    var response = new InvoiceListResponse(invoices);
    var expectedToString =
          "InvoiceListResponse [invoicesList=" + invoices + "]";

    // Verificar que el método toString retorna el formato esperado
    assertEquals(
          expectedToString, response.toString(),
          "The toString method should return the expected "
                + "string format for InvoiceListResponse"
    );
  }

  @Test
  void testEqualsSameInstance() {
    var invoiceResponse1 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    var response = new InvoiceListResponse(invoices);

    // Verificar que la instancia se compare con ella misma
    assertEquals(response, response, "The instance should be equal to itself");
  }

  @Test
  void testEqualsNullObject() {
    var invoiceResponse1 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    var response = new InvoiceListResponse(invoices);

    // Verificar que se devuelve false cuando se compara con null
    Assertions
          .assertNotNull(response, "The instance should not be equal to null");
  }

  @Test
  void testEqualsDifferentClass() {
    var invoiceResponse1 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    var response = new InvoiceListResponse(invoices);

    // Verifica que devuelve false cuando se compara con un objeto de otra clase
    Assertions.assertNotEquals("String", response, "No es igual.");
  }

  @Test
  void testEqualsDifferentInvoicesList() {
    var invoiceResponse1 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices1 =
          List.of(invoiceResponse1, invoiceResponse2);

    var differentInvoiceResponse = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices2 = List.of(differentInvoiceResponse);

    var response1 = new InvoiceListResponse(invoices1);
    var response2 = new InvoiceListResponse(invoices2);

    // Verificar que se devuelve false cuando la lista de invoices es diferente
    Assertions.assertNotEquals(response1, response2, "responses not equals.");
  }

  @Test
  void testEqualsSameInvoicesList() {
    var invoiceResponse1 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    var invoiceResponse2 = new InvoiceResponse();
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices1 =
          List.of(invoiceResponse1, invoiceResponse2);

    var response1 = new InvoiceListResponse(invoices1);
    var response2 = new InvoiceListResponse(invoices1);

    // Verificar que se devuelve true cuando ambas
    // instancias tienen la misma lista de invoices
    assertEquals(response1, response2, "responses are equals");
  }

}
