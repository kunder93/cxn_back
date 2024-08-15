
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentSelfVehicleEntity;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link PersistentSelfVehicleEntity} class.
 * This class tests the getter and setter methods, as well as
 * the equality and hashCode methods of the
 * {@link PersistentSelfVehicleEntity} class.
 */
class PersistentSelfVehicleEntityTest {

  /**
   * A test value for the places property.
   * Used to verify that the getter and setter methods work correctly for
   * the places attribute.
   */
  private static final String TEST_PLACES = "Example Places";

  /**
   * A test value for the distance property.
   * Used to verify that the getter and setter methods work correctly for the
   * distance attribute.
   */
  private static final float TEST_DISTANCE = 100.0f;

  /**
   * A test value for the kmPrice property.
   * Used to verify that the getter and setter methods work correctly for the
   * kmPrice attribute.
   */
  private static final double TEST_KM_PRICE = 1.5;

  /**
   * An updated test value for the kmPrice property.
   * Used to verify that changes in the kmPrice attribute are correctly
   * reflected and that the equals and hashCode methods handle updates properly.
   */
  private static final double UPDATED_KM_PRICE = 2.0;

  /**
   * A test value for the identifier property.
   * Used to verify that the getter and setter methods work correctly for the
   * identifier attribute.
   */
  private static final int TEST_IDENTIFIER = 1;

  /**
   * Tests getter and setter methods of the
   * {@link PersistentSelfVehicleEntity} class.
   * Verifies that values set using setters are correctly
   *  retrieved using getters.
   */
  @Test
  void testGettersAndSetters() {
    // Create a mock for PersistentPaymentSheetEntity
    var paymentSheet = mock(PersistentPaymentSheetEntity.class);

    // Create an instance of PersistentSelfVehicleEntity
    var selfVehicle = new PersistentSelfVehicleEntity();

    // Set values using setter methods
    selfVehicle.setPlaces(TEST_PLACES);
    selfVehicle.setDistance(TEST_DISTANCE);
    selfVehicle.setKmPrice(TEST_KM_PRICE);
    selfVehicle.setPaymentSheet(paymentSheet);

    // Verify that the values are retrieved correctly using getter methods
    assertEquals(TEST_PLACES, selfVehicle.getPlaces(), "Places getter/setter");
    assertEquals(
          TEST_DISTANCE, selfVehicle.getDistance(), "Distance getter/setter"
    );
    assertEquals(
          TEST_KM_PRICE, selfVehicle.getKmPrice(), "KM Price getter/setter"
    );
    assertEquals(
          paymentSheet, selfVehicle.getPaymentSheet(),
          "Payment Sheet getter/setter"
    );
  }

  /**
   * Tests the {@link PersistentSelfVehicleEntity#equals(Object)} and
   * {@link PersistentSelfVehicleEntity#hashCode()} methods.
   * Verifies that two instances with the same values are considered equal
   * and have the same hash code. Also checks that an object is not equal
   * to null or an object of a different class.
   */
  @Test
  void testEqualsAndHashCode() {
    // Create two instances of PersistentSelfVehicleEntity with same attributes
    var selfVehicle1 = new PersistentSelfVehicleEntity();
    selfVehicle1.setIdentifier(TEST_IDENTIFIER);
    selfVehicle1.setPlaces(TEST_PLACES);
    selfVehicle1.setDistance(TEST_DISTANCE);
    selfVehicle1.setKmPrice(TEST_KM_PRICE);
    var paymentSheet1 = mock(PersistentPaymentSheetEntity.class);
    selfVehicle1.setPaymentSheet(paymentSheet1);

    var selfVehicle2 = new PersistentSelfVehicleEntity();
    selfVehicle2.setIdentifier(TEST_IDENTIFIER);
    selfVehicle2.setPlaces(TEST_PLACES);
    selfVehicle2.setDistance(TEST_DISTANCE);
    selfVehicle2.setKmPrice(TEST_KM_PRICE);
    selfVehicle2.setPaymentSheet(paymentSheet1);

    // Verify that selfVehicle1 is equal to itself
    assertEquals(
          selfVehicle1, selfVehicle1, "An object should be equal to itself"
    );

    // Verify that two objects with the same attributes are equal
    assertEquals(
          selfVehicle1, selfVehicle2,
          "selfVehicle1 should be equal to selfVehicle2"
    );
    assertEquals(
          selfVehicle2, selfVehicle1,
          "selfVehicle2 should be equal to selfVehicle1"
    );

    // Verify that hash codes of equal objects are the same
    assertEquals(
          selfVehicle1.hashCode(), selfVehicle2.hashCode(),
          "Hash codes of equal objects should be equal"
    );

    // Modify an attribute in selfVehicle2
    selfVehicle2.setKmPrice(UPDATED_KM_PRICE);

    // Verify that two objects with different attributes are not equal
    assertNotEquals(
          selfVehicle1, selfVehicle2,
          "selfVehicle1 should not be equal to selfVehicle2 after modification"
    );
    assertNotEquals(
          selfVehicle2, selfVehicle1,
          "selfVehicle2 should not be equal to selfVehicle1 after modification"
    );

    // Verify that hash codes of objects with different attributes are different
    assertNotEquals(
          selfVehicle1.hashCode(), selfVehicle2.hashCode(),
          "Hash codes of unequal objects should be different"
    );

    // Verify that an object is not equal to null
    PersistentSelfVehicleEntity nullVehicle = null;
    assertNotEquals(
          selfVehicle1, nullVehicle, "An object should not be equal to null"
    );

    // Verify that an object is not equal to an object of a different class
    var otherObject = "This is not a PersistentSelfVehicleEntity";
    assertNotEquals(
          selfVehicle1, otherObject,
          "An object should not be equal to an object of a different class"
    );
  }
}
