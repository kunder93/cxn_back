
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import es.org.cxn.backapp.model.form.requests.CreateInvoiceRequestForm;

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
    var invoiceRequestForm = new CreateInvoiceRequestForm();

    invoiceRequestForm.setNumber(INVOICE_NUMBER);
    invoiceRequestForm.setSeries(INVOICE_SERIES);
    invoiceRequestForm.setAdvancePaymentDate(ADVANCE_PAYMENT_DATE);
    invoiceRequestForm.setExpeditionDate(EXPEDITION_DATE);
    invoiceRequestForm.setTaxExempt(TAX_EXEMPT);
    invoiceRequestForm.setSellerNif(SELLER_NIF);
    invoiceRequestForm.setBuyerNif(BUYER_NIF);

    assertEquals(INVOICE_NUMBER, invoiceRequestForm.getNumber(), "getter");
    assertEquals(INVOICE_SERIES, invoiceRequestForm.getSeries(), "getter");
    assertEquals(
          ADVANCE_PAYMENT_DATE, invoiceRequestForm.getAdvancePaymentDate(),
          "getter"
    );
    assertEquals(
          EXPEDITION_DATE, invoiceRequestForm.getExpeditionDate(), "getter"
    );
    assertTrue(invoiceRequestForm.getTaxExempt(), "getter");
    assertEquals(SELLER_NIF, invoiceRequestForm.getSellerNif(), "getter");
    assertEquals(BUYER_NIF, invoiceRequestForm.getBuyerNif(), "getter");
  }

  /**
   * Tests the {@link CreateInvoiceRequestForm#equals(Object)} and
   * {@link CreateInvoiceRequestForm#hashCode()} methods.
   * This method verifies that two instances with the same values are considered
   * equal and have the same hash code.
   */
  @Test
  void testEqualsAndHashCode() {
    var invoiceRequestForm1 =
          CreateInvoiceRequestForm.builder().number(INVOICE_NUMBER)
                .series(INVOICE_SERIES).advancePaymentDate(ADVANCE_PAYMENT_DATE)
                .expeditionDate(EXPEDITION_DATE).taxExempt(TAX_EXEMPT)
                .sellerNif(SELLER_NIF).buyerNif(BUYER_NIF).build();

    var invoiceRequestForm2 =
          CreateInvoiceRequestForm.builder().number(INVOICE_NUMBER)
                .series(INVOICE_SERIES).advancePaymentDate(ADVANCE_PAYMENT_DATE)
                .expeditionDate(EXPEDITION_DATE).taxExempt(TAX_EXEMPT)
                .sellerNif(SELLER_NIF).buyerNif(BUYER_NIF).build();

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
    var invoiceRequestForm =
          CreateInvoiceRequestForm.builder().number(INVOICE_NUMBER)
                .series(INVOICE_SERIES).advancePaymentDate(ADVANCE_PAYMENT_DATE)
                .expeditionDate(EXPEDITION_DATE).taxExempt(TAX_EXEMPT)
                .sellerNif(SELLER_NIF).buyerNif(BUYER_NIF).build();

    assertEquals(
          INVOICE_NUMBER, invoiceRequestForm.getNumber(),
          "Values set by builder"
    );
    assertEquals(
          INVOICE_SERIES, invoiceRequestForm.getSeries(),
          "Values set by builder"
    );
    assertEquals(
          ADVANCE_PAYMENT_DATE, invoiceRequestForm.getAdvancePaymentDate(),
          "Values set by builder"
    );
    assertEquals(
          EXPEDITION_DATE, invoiceRequestForm.getExpeditionDate(),
          "Values set by builder"
    );
    assertTrue(invoiceRequestForm.getTaxExempt(), "Values set by builder");
    assertEquals(
          SELLER_NIF, invoiceRequestForm.getSellerNif(), "Values set by builder"
    );
    assertEquals(
          BUYER_NIF, invoiceRequestForm.getBuyerNif(), "Values set by builder"
    );
  }
}
