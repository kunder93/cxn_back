
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.requests.CreateInvoiceRequestForm;
import es.org.cxn.backapp.model.form.responses.InvoiceResponse;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link CreateInvoiceRequestForm} class.
 * This class tests the getter and setter methods, equality comparison,
 * hash code computation, and builder functionality of the
 * {@link CreateInvoiceRequestForm} class.
 */
class CreateInvoiceRequestTest {

  /**
   * The invoice number used for testing.
   */
  private static final int INVOICE_NUMBER = 123;

  /**
   * The invoice series used for testing.
   */
  private static final String INVOICE_SERIES = "A";

  /**
   * The advance payment date used for testing.
   */
  private static final LocalDate ADVANCE_PAYMENT_DATE =
        LocalDate.of(2022, 5, 1);

  /**
   * The expedition date used for testing.
   */
  private static final LocalDate EXPEDITION_DATE = LocalDate.of(2022, 5, 15);

  /**
   * Indicates whether the invoice is tax-exempt, used for testing.
   */
  private static final boolean TAX_EXEMPT = true;

  /**
   * The seller's NIF (tax identification number) used for testing.
   */
  private static final String SELLER_NIF = "123456789A";

  /**
   * The buyer's NIF (tax identification number) used for testing.
   */
  private static final String BUYER_NIF = "987654321B";

  /**
   * Tests getter and setter methods of the {@link CreateInvoiceRequestForm}
   * class.
   * This method verifies that values set using setters are correctly retrieved
   * using getters.
   */
  @Test
  void testGettersAndSetters() {
    var invoiceRequestForm = new InvoiceResponse(
          INVOICE_NUMBER, INVOICE_SERIES, ADVANCE_PAYMENT_DATE, EXPEDITION_DATE,
          TAX_EXEMPT, SELLER_NIF, BUYER_NIF
    );
    assertEquals(INVOICE_NUMBER, invoiceRequestForm.number(), "getter");
    assertEquals(INVOICE_SERIES, invoiceRequestForm.series(), "getter");
    assertEquals(
          ADVANCE_PAYMENT_DATE, invoiceRequestForm.advancePaymentDate(),
          "getter"
    );
    assertEquals(
          EXPEDITION_DATE, invoiceRequestForm.expeditionDate(), "getter"
    );
    assertTrue(invoiceRequestForm.taxExempt(), "getter");
    assertEquals(SELLER_NIF, invoiceRequestForm.sellerNif(), "getter");
    assertEquals(BUYER_NIF, invoiceRequestForm.buyerNif(), "getter");
  }

  /**
   * Tests the {@link CreateInvoiceRequestForm#equals(Object)} and
   * {@link CreateInvoiceRequestForm#hashCode()} methods.
   * This method verifies that two instances with the same values are considered
   * equal and have the same hash code.
   */
  @Test
  void testEqualsAndHashCode() {
    var invoiceRequestForm1 = new CreateInvoiceRequestForm(

          INVOICE_NUMBER, INVOICE_SERIES, ADVANCE_PAYMENT_DATE, EXPEDITION_DATE,
          TAX_EXEMPT, SELLER_NIF, BUYER_NIF
    );

    var invoiceRequestForm2 = new CreateInvoiceRequestForm(

          INVOICE_NUMBER, INVOICE_SERIES, ADVANCE_PAYMENT_DATE, EXPEDITION_DATE,
          TAX_EXEMPT, SELLER_NIF, BUYER_NIF
    );

    assertEquals(
          invoiceRequestForm1, invoiceRequestForm2,
          "Instances should be equal using equals"
    );
    assertEquals(
          invoiceRequestForm1.hashCode(), invoiceRequestForm2.hashCode(),
          "Hash codes should be equal"
    );

    CreateInvoiceRequestForm nullRequest = null;
    assertNotEquals(
          invoiceRequestForm1, nullRequest,
          "Instance should not be equal to null"
    );

    var otherObject = " ";
    assertNotEquals(
          invoiceRequestForm1, otherObject,
          "Instance should not be equal to a different object"
    );
  }

  /**
   * Tests the builder pattern of the {@link CreateInvoiceRequestForm} class.
   * This method verifies that values set using the builder are correctly
   * retrieved using getters.
   */
  @Test
  void testBuilder() {
    var invoiceRequestForm = new CreateInvoiceRequestForm(

          INVOICE_NUMBER, INVOICE_SERIES, ADVANCE_PAYMENT_DATE, EXPEDITION_DATE,
          TAX_EXEMPT, SELLER_NIF, BUYER_NIF
    );

    assertEquals(
          INVOICE_NUMBER, invoiceRequestForm.number(), "Values set by builder"
    );
    assertEquals(
          INVOICE_SERIES, invoiceRequestForm.series(), "Values set by builder"
    );
    assertEquals(
          ADVANCE_PAYMENT_DATE, invoiceRequestForm.advancePaymentDate(),
          "Values set by builder"
    );
    assertEquals(
          EXPEDITION_DATE, invoiceRequestForm.expeditionDate(),
          "Values set by builder"
    );
    assertTrue(invoiceRequestForm.taxExempt(), "Values set by builder");
    assertEquals(
          SELLER_NIF, invoiceRequestForm.sellerNif(), "Values set by builder"
    );
    assertEquals(
          BUYER_NIF, invoiceRequestForm.buyerNif(), "Values set by builder"
    );
  }
}
