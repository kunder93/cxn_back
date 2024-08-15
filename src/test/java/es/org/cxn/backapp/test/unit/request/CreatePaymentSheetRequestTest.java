
package es.org.cxn.backapp.test.unit.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import es.org.cxn.backapp.model.form.requests.CreatePaymentSheetRequestForm;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link CreatePaymentSheetRequestForm} class.
 * This class tests the getter and setter methods, equality comparison,
 * and hash code computation of the {@link CreatePaymentSheetRequestForm} class.
 */
class CreatePaymentSheetRequestTest {

  /**
   * Constant for the user email value used in tests.
   */
  private static final String USER_EMAIL = "user@example.com";

  /**
   * Constant for the event reason value used in tests.
   */
  private static final String EVENT_REASON = "Event reason";

  /**
   * Constant for the event place value used in tests.
   */
  private static final String EVENT_PLACE = "Event place";

  /**
   * Constant for the start date value used in tests.
   */
  private static final LocalDate START_DATE = LocalDate.now();

  /**
   * Constant for the end date value used in tests.
   */
  private static final LocalDate END_DATE = LocalDate.now().plusDays(7);

  /**
   * Constant for a different user email value used in tests.
   */
  private static final String DIFFERENT_USER_EMAIL = "user1@example.com";

  /**
   * Constant for a different event reason value used in tests.
   */
  private static final String DIFFERENT_EVENT_REASON = "Event reason 1";

  /**
   * Constant for a different event place value used in tests.
   */
  private static final String DIFFERENT_EVENT_PLACE = "Event place 1";

  /**
   * Constant for a different start date value used in tests.
   */
  private static final LocalDate DIFFERENT_START_DATE =
        LocalDate.now().plusDays(1);

  /**
   * Constant for a different end date value used in tests.
   */
  private static final LocalDate DIFFERENT_END_DATE =
        LocalDate.now().plusDays(8);

  /**
   * Tests getter and setter methods of the
   * {@link CreatePaymentSheetRequestForm} class.
   * This method verifies that values set using setters are correctly retrieved
   * using getters.
   */
  @Test
  void testGettersAndSetters() {
    var paymentSheetRequestForm = new CreatePaymentSheetRequestForm();

    paymentSheetRequestForm.setUserEmail(USER_EMAIL);
    paymentSheetRequestForm.setReason(EVENT_REASON);
    paymentSheetRequestForm.setPlace(EVENT_PLACE);
    paymentSheetRequestForm.setStartDate(START_DATE);
    paymentSheetRequestForm.setEndDate(END_DATE);

    assertEquals(
          USER_EMAIL, paymentSheetRequestForm.getUserEmail(),
          "Verify values using getters"
    );
    assertEquals(
          EVENT_REASON, paymentSheetRequestForm.getReason(),
          "Verify values using getters"
    );
    assertEquals(
          EVENT_PLACE, paymentSheetRequestForm.getPlace(),
          "Verify values using getters"
    );
    assertEquals(
          START_DATE, paymentSheetRequestForm.getStartDate(),
          "Verify values using getters"
    );
    assertEquals(
          END_DATE, paymentSheetRequestForm.getEndDate(),
          "Verify values using getters"
    );
  }

  /**
   * Tests the {@link CreatePaymentSheetRequestForm#equals(Object)} method.
   * This method checks that two instances with the same values
   * are considered equal.
   */
  @Test
  void testEquals() {
    var paymentSheetRequestForm1 = new CreatePaymentSheetRequestForm(
          USER_EMAIL, EVENT_REASON, EVENT_PLACE, START_DATE, END_DATE
    );
    var paymentSheetRequestForm2 = new CreatePaymentSheetRequestForm(
          USER_EMAIL, EVENT_REASON, EVENT_PLACE, START_DATE, END_DATE
    );

    assertEquals(
          paymentSheetRequestForm1, paymentSheetRequestForm2,
          "Instances should be equal using equals"
    );
  }

  /**
   * Tests the inequality comparison of the
   * {@link CreatePaymentSheetRequestForm#equals(Object)} method.
   * This method verifies that two instances with different values are
   *  not considered equal.
   */
  @Test
  void testNotEquals() {
    var paymentSheetRequestForm1 = new CreatePaymentSheetRequestForm(
          DIFFERENT_USER_EMAIL, DIFFERENT_EVENT_REASON, DIFFERENT_EVENT_PLACE,
          DIFFERENT_START_DATE, DIFFERENT_END_DATE
    );
    var paymentSheetRequestForm2 = new CreatePaymentSheetRequestForm(
          USER_EMAIL, EVENT_REASON, EVENT_PLACE, START_DATE, END_DATE
    );

    assertNotEquals(
          paymentSheetRequestForm1, paymentSheetRequestForm2,
          "Instances should not be equal using equals"
    );
    assertNotEquals(
          paymentSheetRequestForm2, paymentSheetRequestForm1,
          "Instances should not be equal using equals"
    );
  }

  /**
   * Tests the {@link CreatePaymentSheetRequestForm#hashCode()} method.
   * This method verifies that two instances with the same values
   *  have the same hash code.
   */
  @Test
  void testHashCode() {
    var paymentSheetRequestForm1 = new CreatePaymentSheetRequestForm(
          USER_EMAIL, EVENT_REASON, EVENT_PLACE, START_DATE, END_DATE
    );
    var paymentSheetRequestForm2 = new CreatePaymentSheetRequestForm(
          USER_EMAIL, EVENT_REASON, EVENT_PLACE, START_DATE, END_DATE
    );

    assertEquals(
          paymentSheetRequestForm1.hashCode(),
          paymentSheetRequestForm2.hashCode(), "Hash codes should be equal"
    );
  }
}
