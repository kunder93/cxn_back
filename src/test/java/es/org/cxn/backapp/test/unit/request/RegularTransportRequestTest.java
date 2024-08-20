
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.AddRegularTransportRequestForm;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link AddRegularTransportRequestForm} class.
 * This class tests the getter and setter methods, equality comparison,
 * and hash code computation of the {@link AddRegularTransportRequestForm}
 * class.
 */
class RegularTransportRequestTest {

  /** Constant for category value used in tests. */
  private static final String CATEGORY = "Category";

  /** Constant for description value used in tests. */
  private static final String DESCRIPTION = "Description";

  /** Constant for invoice number used in tests. */
  private static final int INVOICE_NUMBER = 123;

  /** Constant for invoice series used in tests. */
  private static final String INVOICE_SERIES = "Series";

  /** Constant for a different category value used in tests. */
  private static final String DIFFERENT_CATEGORY = "Category1";

  /** Constant for a different description value used in tests. */
  private static final String DIFFERENT_DESCRIPTION = "Description1";

  /** Constant for a different invoice number used in tests. */
  private static final int DIFFERENT_INVOICE_NUMBER = 456;

  /** Constant for a different invoice series used in tests. */
  private static final String DIFFERENT_INVOICE_SERIES = "Series1";

  /**
   * Tests getter and setter methods of the
   * {@link AddRegularTransportRequestForm} class.
   * This method verifies that values set using setters are correctly retrieved
   * using getters.
   */
  @Test
  void testGettersAndSetters() {
    var request = new AddRegularTransportRequestForm(
          CATEGORY, DESCRIPTION, INVOICE_NUMBER, INVOICE_SERIES
    );

    assertEquals(CATEGORY, request.category(), "values using getters");
    assertEquals(DESCRIPTION, request.description(), "values using getters");
    assertEquals(
          INVOICE_NUMBER, request.invoiceNumber(), "values using getters"
    );
    assertEquals(
          INVOICE_SERIES, request.invoiceSeries(), "values using getters"
    );
  }

  /**
   * Tests the {@link AddRegularTransportRequestForm#equals(Object)} method.
   * This method checks that two instances with the same values are
   * considered equal.
   */
  @Test
  void testEquals() {
    var request1 = new AddRegularTransportRequestForm(
          CATEGORY, DESCRIPTION, INVOICE_NUMBER, INVOICE_SERIES
    );
    var request2 = new AddRegularTransportRequestForm(
          CATEGORY, DESCRIPTION, INVOICE_NUMBER, INVOICE_SERIES
    );

    assertEquals(request1, request2, "instances should be equal using equals");
    assertEquals(request2, request1, "instances should be equal using equals");
  }

  /**
   * Tests the inequality comparison of the
   * {@link AddRegularTransportRequestForm#equals(Object)} method.
   * This method verifies that two instances with different values are
   * not considered equal.
   */
  @Test
  void testNotEquals() {
    var request1 = new AddRegularTransportRequestForm(
          DIFFERENT_CATEGORY, DIFFERENT_DESCRIPTION, INVOICE_NUMBER,
          DIFFERENT_INVOICE_SERIES
    );
    var request2 = new AddRegularTransportRequestForm(
          CATEGORY, DESCRIPTION, DIFFERENT_INVOICE_NUMBER,
          DIFFERENT_INVOICE_SERIES
    );

    assertNotEquals(
          request1, request2, "instances should not be equal using equals"
    );
    assertNotEquals(
          request2, request1, "instances should not be equal using equals"
    );
  }

  /**
   * Tests the {@link AddRegularTransportRequestForm#hashCode()} method.
   * This method verifies that two instances with the same values
   *  have the same hash code.
   */
  @Test
  void testHashCode() {
    var request1 = new AddRegularTransportRequestForm(
          CATEGORY, DESCRIPTION, INVOICE_NUMBER, INVOICE_SERIES
    );
    var request2 = new AddRegularTransportRequestForm(
          CATEGORY, DESCRIPTION, INVOICE_NUMBER, INVOICE_SERIES
    );

    assertEquals(
          request1.hashCode(), request2.hashCode(), "hashCodes should be equal"
    );
  }
}
