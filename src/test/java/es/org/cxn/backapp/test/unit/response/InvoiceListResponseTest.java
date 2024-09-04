
package es.org.cxn.backapp.test.unit.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.responses.InvoiceListResponse;
import es.org.cxn.backapp.model.form.responses.InvoiceResponse;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InvoiceListResponseTest {

  /**
   * Invoice number for the first test invoice.
   */
  private static final BigInteger INVOICE_1_NUMBER = BigInteger.valueOf(1);

  /**
   * Invoice series for the first test invoice.
   */
  private static final String INVOICE_1_SERIES = "AA";

  /**
   * Advance payment date for the first test invoice.
   */
  private static final LocalDate INVOICE_1_ADVANCE_PAYMENT_DATE =
        LocalDate.now();

  /**
   * Expedition date for the first test invoice.
   */
  private static final LocalDate INVOICE_1_EXPEDITION_DATE = LocalDate.now();

  /**
   * Tax exemption status for the first test invoice.
   */
  private static final Boolean INVOICE_1_TAX_EXEMPT = Boolean.TRUE;

  /**
   * Seller NIF (Tax ID) for the first test invoice.
   */
  private static final String INVOICE_1_SELLER_NIF = "41222332G";

  /**
   * Buyer NIF (Tax ID) for the first test invoice.
   */
  private static final String INVOICE_1_BUYER_NIF = "41241242G";

  /**
   * Invoice number for the second test invoice.
   */
  private static final BigInteger INVOICE_2_NUMBER = BigInteger.valueOf(1);

  /**
   * Invoice series for the second test invoice.
   */
  private static final String INVOICE_2_SERIES = "BB";

  /**
   * Advance payment date for the second test invoice.
   */
  private static final LocalDate INVOICE_2_ADVANCE_PAYMENT_DATE =
        LocalDate.now();

  /**
   * Expedition date for the second test invoice.
   */
  private static final LocalDate INVOICE_2_EXPEDITION_DATE = LocalDate.now();

  /**
   * Tax exemption status for the second test invoice.
   */
  private static final Boolean INVOICE_2_TAX_EXEMPT = Boolean.TRUE;

  /**
   * Seller NIF (Tax ID) for the second test invoice.
   */
  private static final String INVOICE_2_SELLER_NIF = "41232332G";

  /**
   * Buyer NIF (Tax ID) for the second test invoice.
   */
  private static final String INVOICE_2_BUYER_NIF = "41241342G";

  /**
   * Invoice number for the third test invoice.
   */
  private static final BigInteger INVOICE_3_NUMBER = BigInteger.valueOf(3);

  /**
   * Invoice series for the third test invoice.
   */
  private static final String INVOICE_3_SERIES = "CC";

  /**
   * Advance payment date for the third test invoice.
   */
  private static final LocalDate INVOICE_3_ADVANCE_PAYMENT_DATE =
        LocalDate.now().minusDays(2);

  /**
   * Expedition date for the third test invoice.
   */
  private static final LocalDate INVOICE_3_EXPEDITION_DATE =
        LocalDate.now().minusDays(2);

  /**
   * Tax exemption status for the third test invoice.
   */
  private static final Boolean INVOICE_3_TAX_EXEMPT = Boolean.TRUE;

  /**
   * Seller NIF (Tax ID) for the third test invoice.
   */
  private static final String INVOICE_3_SELLER_NIF = "41233332G";

  /**
   * Buyer NIF (Tax ID) for the third test invoice.
   */
  private static final String INVOICE_3_BUYER_NIF = "41242342G";

  @Test
  void testConstructorAndGetters() {
    // Crear una lista de InvoiceResponse
    var invoiceResponse1 = new InvoiceResponse(
          INVOICE_1_NUMBER, INVOICE_1_SERIES, INVOICE_1_ADVANCE_PAYMENT_DATE,
          INVOICE_1_EXPEDITION_DATE, INVOICE_1_TAX_EXEMPT, INVOICE_1_SELLER_NIF,
          INVOICE_1_BUYER_NIF
    );
    var invoiceResponse2 = new InvoiceResponse(
          INVOICE_2_NUMBER, INVOICE_2_SERIES, INVOICE_2_ADVANCE_PAYMENT_DATE,
          INVOICE_2_EXPEDITION_DATE, INVOICE_2_TAX_EXEMPT, INVOICE_2_SELLER_NIF,
          INVOICE_2_BUYER_NIF
    );
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    // Crear una instancia de InvoiceListResponse usando el constructor
    var response = new InvoiceListResponse(invoices);

    // Verificar lista de invoices en la respuesta coincide con lista original
    assertNotNull(
          response.invoicesList(), "The invoices list should not be null"
    );
    assertEquals(
          2, response.invoicesList().size(),
          "The invoices list size should be 2"
    );
    assertTrue(
          response.invoicesList().contains(invoiceResponse1),
          "The invoices list should contain invoiceResponse1"
    );
    assertTrue(
          response.invoicesList().contains(invoiceResponse2),
          "The invoices list should contain invoiceResponse2"
    );
  }

  @Test
  void testInvoicesList() {
    // Crear una lista de InvoiceResponse
    var invoiceResponse1 = new InvoiceResponse(
          INVOICE_1_NUMBER, INVOICE_1_SERIES, INVOICE_1_ADVANCE_PAYMENT_DATE,
          INVOICE_1_EXPEDITION_DATE, INVOICE_1_TAX_EXEMPT, INVOICE_1_SELLER_NIF,
          INVOICE_1_BUYER_NIF
    );
    var invoiceResponse2 = new InvoiceResponse(
          INVOICE_2_NUMBER, INVOICE_2_SERIES, INVOICE_2_ADVANCE_PAYMENT_DATE,
          INVOICE_2_EXPEDITION_DATE, INVOICE_2_TAX_EXEMPT, INVOICE_2_SELLER_NIF,
          INVOICE_2_BUYER_NIF
    );
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    // Crear una instancia de InvoiceListResponse usando el constructor
    var response = new InvoiceListResponse(invoices);

    // Verificar que la lista de invoices en la respuesta coincide con
    // la lista proporcionada
    assertNotNull(
          response.invoicesList(), "The invoices list should not be null"
    );
    assertEquals(
          2, response.invoicesList().size(),
          "The invoices list size should be 2"
    );
    assertTrue(
          response.invoicesList().contains(invoiceResponse1),
          "The invoices list should contain invoiceResponse1"
    );
    assertTrue(
          response.invoicesList().contains(invoiceResponse2),
          "The invoices list should contain invoiceResponse2"
    );
  }

  @Test
  void testEqualsAndHashCode() {
    var invoiceResponse1 = new InvoiceResponse(
          INVOICE_1_NUMBER, INVOICE_1_SERIES, INVOICE_1_ADVANCE_PAYMENT_DATE,
          INVOICE_1_EXPEDITION_DATE, INVOICE_1_TAX_EXEMPT, INVOICE_1_SELLER_NIF,
          INVOICE_1_BUYER_NIF
    );
    var invoiceResponse2 = new InvoiceResponse(
          INVOICE_2_NUMBER, INVOICE_2_SERIES, INVOICE_2_ADVANCE_PAYMENT_DATE,
          INVOICE_2_EXPEDITION_DATE, INVOICE_2_TAX_EXEMPT, INVOICE_2_SELLER_NIF,
          INVOICE_2_BUYER_NIF
    );
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
    var differentInvoiceResponse = new InvoiceResponse(
          INVOICE_3_NUMBER, INVOICE_3_SERIES, INVOICE_3_ADVANCE_PAYMENT_DATE,
          INVOICE_3_EXPEDITION_DATE, INVOICE_3_TAX_EXEMPT, INVOICE_3_SELLER_NIF,
          INVOICE_3_BUYER_NIF
    );
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
    var invoiceResponse1 = new InvoiceResponse(
          INVOICE_1_NUMBER, INVOICE_1_SERIES, INVOICE_1_ADVANCE_PAYMENT_DATE,
          INVOICE_1_EXPEDITION_DATE, INVOICE_1_TAX_EXEMPT, INVOICE_1_SELLER_NIF,
          INVOICE_1_BUYER_NIF
    );
    var invoiceResponse2 = new InvoiceResponse(
          INVOICE_2_NUMBER, INVOICE_2_SERIES, INVOICE_2_ADVANCE_PAYMENT_DATE,
          INVOICE_2_EXPEDITION_DATE, INVOICE_2_TAX_EXEMPT, INVOICE_2_SELLER_NIF,
          INVOICE_2_BUYER_NIF
    );
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    var response = new InvoiceListResponse(invoices);
    var expectedToString = "InvoiceListResponse[invoicesList=" + invoices + "]";

    // Verificar que el método toString retorna el formato esperado
    assertEquals(
          expectedToString, response.toString(),
          "The toString method should return the expected "
                + "string format for InvoiceListResponse"
    );
  }

  @Test
  void testEqualsSameInstance() {
    var invoiceResponse1 = new InvoiceResponse(
          INVOICE_1_NUMBER, INVOICE_1_SERIES, INVOICE_1_ADVANCE_PAYMENT_DATE,
          INVOICE_1_EXPEDITION_DATE, INVOICE_1_TAX_EXEMPT, INVOICE_1_SELLER_NIF,
          INVOICE_1_BUYER_NIF
    );
    var invoiceResponse2 = new InvoiceResponse(
          INVOICE_2_NUMBER, INVOICE_2_SERIES, INVOICE_2_ADVANCE_PAYMENT_DATE,
          INVOICE_2_EXPEDITION_DATE, INVOICE_2_TAX_EXEMPT, INVOICE_2_SELLER_NIF,
          INVOICE_2_BUYER_NIF
    );
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    var response = new InvoiceListResponse(invoices);

    // Verificar que la instancia se compare con ella misma
    assertEquals(response, response, "The instance should be equal to itself");
  }

  @Test
  void testEqualsNullObject() {
    var invoiceResponse1 = new InvoiceResponse(
          INVOICE_1_NUMBER, INVOICE_1_SERIES, INVOICE_1_ADVANCE_PAYMENT_DATE,
          INVOICE_1_EXPEDITION_DATE, INVOICE_1_TAX_EXEMPT, INVOICE_1_SELLER_NIF,
          INVOICE_1_BUYER_NIF
    );
    var invoiceResponse2 = new InvoiceResponse(
          INVOICE_2_NUMBER, INVOICE_2_SERIES, INVOICE_2_ADVANCE_PAYMENT_DATE,
          INVOICE_2_EXPEDITION_DATE, INVOICE_2_TAX_EXEMPT, INVOICE_2_SELLER_NIF,
          INVOICE_2_BUYER_NIF
    );
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
    var invoiceResponse1 = new InvoiceResponse(
          INVOICE_1_NUMBER, INVOICE_1_SERIES, INVOICE_1_ADVANCE_PAYMENT_DATE,
          INVOICE_1_EXPEDITION_DATE, INVOICE_1_TAX_EXEMPT, INVOICE_1_SELLER_NIF,
          INVOICE_1_BUYER_NIF
    );
    var invoiceResponse2 = new InvoiceResponse(
          INVOICE_2_NUMBER, INVOICE_2_SERIES, INVOICE_2_ADVANCE_PAYMENT_DATE,
          INVOICE_2_EXPEDITION_DATE, INVOICE_2_TAX_EXEMPT, INVOICE_2_SELLER_NIF,
          INVOICE_2_BUYER_NIF
    );
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices =
          List.of(invoiceResponse1, invoiceResponse2);

    var response = new InvoiceListResponse(invoices);

    // Verifica que devuelve false cuando se compara con un objeto de otra clase
    Assertions.assertNotEquals("String", response, "No es igual.");
  }

  @Test
  void testEqualsDifferentInvoicesList() {
    var invoiceResponse1 = new InvoiceResponse(
          INVOICE_1_NUMBER, INVOICE_1_SERIES, INVOICE_1_ADVANCE_PAYMENT_DATE,
          INVOICE_1_EXPEDITION_DATE, INVOICE_1_TAX_EXEMPT, INVOICE_1_SELLER_NIF,
          INVOICE_1_BUYER_NIF
    );
    var invoiceResponse2 = new InvoiceResponse(
          INVOICE_2_NUMBER, INVOICE_2_SERIES, INVOICE_2_ADVANCE_PAYMENT_DATE,
          INVOICE_2_EXPEDITION_DATE, INVOICE_2_TAX_EXEMPT, INVOICE_2_SELLER_NIF,
          INVOICE_2_BUYER_NIF
    );
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices1 =
          List.of(invoiceResponse1, invoiceResponse2);

    var differentInvoiceResponse = new InvoiceResponse(
          INVOICE_3_NUMBER, INVOICE_3_SERIES, INVOICE_3_ADVANCE_PAYMENT_DATE,
          INVOICE_3_EXPEDITION_DATE, INVOICE_3_TAX_EXEMPT, INVOICE_3_SELLER_NIF,
          INVOICE_3_BUYER_NIF
    );
    // Asegúrate de inicializar correctamente
    List<InvoiceResponse> invoices2 = List.of(differentInvoiceResponse);

    var response1 = new InvoiceListResponse(invoices1);
    var response2 = new InvoiceListResponse(invoices2);

    // Verificar que se devuelve false cuando la lista de invoices es diferente
    Assertions.assertNotEquals(response1, response2, "responses not equals.");
  }

  @Test
  void testEqualsSameInvoicesList() {
    var invoiceResponse1 = new InvoiceResponse(
          INVOICE_1_NUMBER, INVOICE_1_SERIES, INVOICE_1_ADVANCE_PAYMENT_DATE,
          INVOICE_1_EXPEDITION_DATE, INVOICE_1_TAX_EXEMPT, INVOICE_1_SELLER_NIF,
          INVOICE_1_BUYER_NIF
    );
    var invoiceResponse2 = new InvoiceResponse(
          INVOICE_2_NUMBER, INVOICE_2_SERIES, INVOICE_2_ADVANCE_PAYMENT_DATE,
          INVOICE_2_EXPEDITION_DATE, INVOICE_2_TAX_EXEMPT, INVOICE_2_SELLER_NIF,
          INVOICE_2_BUYER_NIF
    );
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
