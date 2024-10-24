
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import es.org.cxn.backapp.model.persistence.PersistentFoodHousingEntity;
import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link PersistentFoodHousingEntity} class.
 * This class tests the getters, setters, equality, hash code, and
 * specific functionality of the {@link PersistentFoodHousingEntity} class.
 */
class PersistentFoodHousingEntityTest {

  /**
   * The amount of days used for testing the
   * {@link PersistentFoodHousingEntity}.
   * This value is used to validate the getter and setter methods for the
   * number of days in food and housing.
   */
  private static final int TEST_AMOUNT_DAYS = 5;

  /**
   * The price per day used for testing the {@link PersistentFoodHousingEntity}.
   * This value is used to validate the getter and setter methods for the
   * daily price in food and housing.
   */
  private static final BigDecimal TEST_DAY_PRICE = BigDecimal.valueOf(50.0);

  /**
   * Indicates whether the overnight option is enabled, used for testing the
   * {@link PersistentFoodHousingEntity}. This boolean value is used to verify
   * the getter and setter methods for the overnight stay option.
   */
  private static final boolean TEST_OVERNIGHT = true;

  /**
   * Tests the getter and setter methods of {@link PersistentFoodHousingEntity}.
   * Verifies that the values set using setters are correctly retrieved
   * using getters.
   */
  @Test
  void testGettersAndSetters() {
    var foodHousing = new PersistentFoodHousingEntity();

    foodHousing.setAmountDays(TEST_AMOUNT_DAYS);
    foodHousing.setDayPrice(TEST_DAY_PRICE);
    foodHousing.setOvernight(TEST_OVERNIGHT);

    assertEquals(
          TEST_AMOUNT_DAYS, foodHousing.getAmountDays(),
          "The amount days getter/setter"
    );
    assertEquals(
          TEST_DAY_PRICE, foodHousing.getDayPrice(),
          "The day price getter/setter"
    );
    assertTrue(foodHousing.getOvernight(), "The overnight getter/setter");
  }

  /**
   * Tests the hash code implementation of {@link PersistentFoodHousingEntity}.
   * Verifies that two instances with the same values have the same hash code.
   */
  @Test
  void testHashCode() {
    var paymentSheet = mock(PersistentPaymentSheetEntity.class);
    var foodHousing1 = PersistentFoodHousingEntity.builder()
          .amountDays(TEST_AMOUNT_DAYS).dayPrice(TEST_DAY_PRICE)
          .overnight(TEST_OVERNIGHT).paymentSheet(paymentSheet).build();

    var foodHousing2 = PersistentFoodHousingEntity.builder()
          .amountDays(TEST_AMOUNT_DAYS).dayPrice(TEST_DAY_PRICE)
          .overnight(TEST_OVERNIGHT).paymentSheet(paymentSheet).build();

    assertEquals(
          foodHousing1.hashCode(), foodHousing2.hashCode(),
          "Hash codes should be equal for objects with the same values"
    );
  }

  /**
   * Tests the equality implementation of {@link PersistentFoodHousingEntity}.
   * Verifies that two instances with the same values are considered equal,
   * and instances are not equal to null or objects of different classes.
   */
  @Test
  void testEquals() {
    var paymentSheet = mock(PersistentPaymentSheetEntity.class);
    var foodHousing1 = PersistentFoodHousingEntity.builder()
          .amountDays(TEST_AMOUNT_DAYS).dayPrice(TEST_DAY_PRICE)
          .overnight(TEST_OVERNIGHT).paymentSheet(paymentSheet).build();

    var foodHousing2 = PersistentFoodHousingEntity.builder()
          .amountDays(TEST_AMOUNT_DAYS).dayPrice(TEST_DAY_PRICE)
          .overnight(TEST_OVERNIGHT).paymentSheet(paymentSheet).build();

    assertEquals(
          foodHousing1, foodHousing2,
          "Instances with the same values should be equal"
    );
    assertEquals(foodHousing2, foodHousing1, "Equality should be symmetric");

    assertEquals(
          foodHousing1, foodHousing1, "An instance should be equal to itself"
    );

    PersistentFoodHousingEntity foodHousingNull = null;
    assertNotEquals(
          foodHousing1, foodHousingNull,
          "An instance should not be equal to null"
    );

    var otherObject = "other";
    assertNotEquals(
          foodHousing1, otherObject,
          "An instance should not be equal to an object of a different class"
    );
  }

  /**
   * Tests the {@link PersistentFoodHousingEntity#getPaymentSheet()} method.
   * Verifies that the payment sheet returned is the same as the one set.
   */
  @Test
  void testGetPaymentSheet() {
    var paymentSheet = mock(PersistentPaymentSheetEntity.class);

    var foodHousing = PersistentFoodHousingEntity.builder()
          .paymentSheet(paymentSheet).amountDays(TEST_AMOUNT_DAYS)
          .dayPrice(TEST_DAY_PRICE).overnight(TEST_OVERNIGHT).build();

    assertEquals(
          paymentSheet, foodHousing.getPaymentSheet(),
          "The payment sheet returned should be the same as the one set"
    );
  }

  /**
   * Tests the
   * {@link PersistentFoodHousingEntity#setPaymentSheet
   * (PersistentPaymentSheetEntity)}
   * method.
   * Verifies that the payment sheet set is correctly retrieved.
   */
  @Test
  void testSetPaymentSheet() {
    var paymentSheet = mock(PersistentPaymentSheetEntity.class);

    var foodHousing = new PersistentFoodHousingEntity();
    foodHousing.setPaymentSheet(paymentSheet);

    assertEquals(
          paymentSheet, foodHousing.getPaymentSheet(),
          "The payment sheet set should be the same as the one retrieved"
    );
  }

  /**
   * Tests the
   * {@link PersistentFoodHousingEntity#getInvoices()} method.
   * Verifies that the set of invoices contains the expected items.
   */
  @Test
  void testGetInvoices() {
    var invoice1 = mock(PersistentInvoiceEntity.class);
    var invoice2 = mock(PersistentInvoiceEntity.class);
    var paymentSheet = mock(PersistentPaymentSheetEntity.class);

    Set<PersistentInvoiceEntity> invoices = new HashSet<>();
    invoices.add(invoice1);
    invoices.add(invoice2);

    var foodHousing = PersistentFoodHousingEntity.builder().invoices(invoices)
          .amountDays(TEST_AMOUNT_DAYS).dayPrice(TEST_DAY_PRICE)
          .overnight(TEST_OVERNIGHT).paymentSheet(paymentSheet).build();

    assertTrue(
          foodHousing.getInvoices().contains(invoice1),
          "The set of invoices should contain the mock invoice1"
    );
    assertTrue(
          foodHousing.getInvoices().contains(invoice2),
          "The set of invoices should contain the mock invoice2"
    );
  }
}
