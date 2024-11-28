
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.PersistentFoodHousingEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;
import es.org.cxn.backapp.model.persistence.PersistentSelfVehicleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;

/**
 * Unit tests for the {@link PersistentPaymentSheetEntity} class. This class
 * tests the getter and setter methods, equality, hash code, and other
 * functionality of the {@link PersistentPaymentSheetEntity} class.
 */
class PersistentPaymentSheetEntityTest {

    /**
     * Year value used for testing date-related fields. This constant represents the
     * year 2024.
     */
    private static final int TEST_YEAR = 2024;

    /**
     * Month value used for testing date-related fields. This constant represents
     * the month of May.
     */
    private static final int TEST_MONTH_MAY = 5;

    /**
     * Start day value used for testing date-related fields. This constant
     * represents the 1st day of the month.
     */
    private static final int TEST_DAY_START = 1;

    /**
     * End day value used for testing date-related fields. This constant represents
     * the 5th day of the month.
     */
    private static final int TEST_DAY_END = 5;

    /**
     * Alternative start day value used for testing date-related fields. This
     * constant represents the 10th day of the month.
     */
    private static final int TEST_DAY_ALT_START = 10;

    /**
     * Alternative end day value used for testing date-related fields. This constant
     * represents the 15th day of the month.
     */
    private static final int TEST_DAY_ALT_END = 15;

    /**
     * A sample reason used for testing purposes. This value represents the reason
     * for the payment sheet event.
     */
    private static final String TEST_REASON = "Event reason";

    /**
     * A sample place used for testing purposes. This value represents the location
     * associated with the payment sheet event.
     */
    private static final String TEST_PLACE = "Event place";

    /**
     * A sample start date used for testing purposes. This value represents the
     * beginning date of the payment sheet event.
     */
    private static final LocalDate TEST_START_DATE = LocalDate.of(TEST_YEAR, TEST_MONTH_MAY, TEST_DAY_START);

    /**
     * A sample end date used for testing purposes. This value represents the ending
     * date of the payment sheet event.
     */
    private static final LocalDate TEST_END_DATE = LocalDate.of(TEST_YEAR, TEST_MONTH_MAY, TEST_DAY_END);

    /**
     * A sample ID used for testing purposes. This value represents the identifier
     * for the payment sheet.
     */
    private static final Integer TEST_ID = 1;

    /**
     * Tests the equality of {@link PersistentPaymentSheetEntity}. Verifies that two
     * instances with different self vehicles are not considered equal.
     */
    @Test
    void testEquals() {
        var userOwner = mock(PersistentUserEntity.class);
        var selfVehicle1 = mock(PersistentSelfVehicleEntity.class);
        var selfVehicle2 = mock(PersistentSelfVehicleEntity.class);

        var paymentSheet1 = PersistentPaymentSheetEntity.builder().selfVehicle(selfVehicle1).reason(TEST_REASON)
                .place(TEST_PLACE).startDate(TEST_START_DATE).endDate(TEST_END_DATE).userOwner(userOwner).build();

        var paymentSheet2 = PersistentPaymentSheetEntity.builder().selfVehicle(selfVehicle2).reason(TEST_REASON)
                .place(TEST_PLACE).startDate(TEST_START_DATE).endDate(TEST_END_DATE).userOwner(userOwner).build();

        assertNotEquals(paymentSheet1, paymentSheet2,
                "Objects should be considered different due to different " + "self vehicles");
    }

    /**
     * Tests the {@link PersistentPaymentSheetEntity#getEndDate()} method. Verifies
     * that the end date returned is the same as the one set.
     */
    @Test
    void testGetEndDate() {
        var userOwner = mock(PersistentUserEntity.class);
        var paymentSheet = PersistentPaymentSheetEntity.builder().reason(TEST_REASON).place(TEST_PLACE)
                .startDate(TEST_START_DATE).endDate(TEST_END_DATE).userOwner(userOwner).build();

        assertEquals(TEST_END_DATE, paymentSheet.getEndDate(),
                "The end date returned should be the same as the one set");
    }

    /**
     * Tests the {@link PersistentPaymentSheetEntity#getFoodHousing()} method.
     * Verifies that the food housing returned is the same as the one set.
     */
    @Test
    void testGetFoodHousing() {
        var userOwner = mock(PersistentUserEntity.class);
        var foodHousing = mock(PersistentFoodHousingEntity.class);

        var paymentSheet = PersistentPaymentSheetEntity.builder().foodHousing(foodHousing).reason(TEST_REASON)
                .place(TEST_PLACE).startDate(TEST_START_DATE).endDate(TEST_END_DATE).userOwner(userOwner).build();

        assertEquals(foodHousing, paymentSheet.getFoodHousing(),
                "The food housing returned should be the same as the one set");
    }

    /**
     * Tests the {@link PersistentPaymentSheetEntity#getPlace()} method. Verifies
     * that the place returned is the same as the one set.
     */
    @Test
    void testGetPlace() {
        var userOwner = mock(PersistentUserEntity.class);
        var paymentSheet = PersistentPaymentSheetEntity.builder().reason(TEST_REASON).place(TEST_PLACE)
                .startDate(TEST_START_DATE).endDate(TEST_END_DATE).userOwner(userOwner).build();

        assertEquals(TEST_PLACE, paymentSheet.getPlace(), "The place returned should be the same as the one set");
    }

    /**
     * Tests the {@link PersistentPaymentSheetEntity#getReason()} method. Verifies
     * that the reason returned is the same as the one set.
     */
    @Test
    void testGetReason() {
        var userOwner = mock(PersistentUserEntity.class);
        var paymentSheet = PersistentPaymentSheetEntity.builder().reason(TEST_REASON).place(TEST_PLACE)
                .startDate(TEST_START_DATE).endDate(TEST_END_DATE).userOwner(userOwner).build();

        assertEquals(TEST_REASON, paymentSheet.getReason(), "The reason returned should be the same as the one set");
    }

    /**
     * Tests the {@link PersistentPaymentSheetEntity#getRegularTransports()} method.
     * Verifies that the set of regular transports contains the expected items.
     */
    @Test
    void testGetRegularTransports() {
        var userOwner = mock(PersistentUserEntity.class);
        var transport1 = mock(PersistentRegularTransportEntity.class);
        var transport2 = mock(PersistentRegularTransportEntity.class);

        Set<PersistentRegularTransportEntity> regularTransports = new HashSet<>();
        regularTransports.add(transport1);
        regularTransports.add(transport2);

        var paymentSheet = PersistentPaymentSheetEntity.builder().userOwner(userOwner)
                .regularTransports(new HashSet<>(regularTransports)).reason(TEST_REASON).place(TEST_PLACE)
                .startDate(TEST_START_DATE).endDate(TEST_END_DATE).build();

        assertTrue(paymentSheet.getRegularTransports().contains(transport1),
                "The set should contain the expected regular transport");
        assertTrue(paymentSheet.getRegularTransports().contains(transport2),
                "The set should contain the expected regular transport");
    }

    /**
     * Tests the {@link PersistentPaymentSheetEntity#getSelfVehicle()} method.
     * Verifies that the self vehicle returned is the same as the one set.
     */
    @Test
    void testGetSelfVehicle() {
        var userOwner = mock(PersistentUserEntity.class);
        var selfVehicle = mock(PersistentSelfVehicleEntity.class);

        var paymentSheet = PersistentPaymentSheetEntity.builder().selfVehicle(selfVehicle).reason(TEST_REASON)
                .place(TEST_PLACE).startDate(TEST_START_DATE).endDate(TEST_END_DATE).userOwner(userOwner).build();

        assertEquals(selfVehicle, paymentSheet.getSelfVehicle(),
                "The self vehicle returned should be the same as the one set");
    }

    /**
     * Tests the {@link PersistentPaymentSheetEntity#getStartDate()} method.
     * Verifies that the start date returned is the same as the one set.
     */
    @Test
    void testGetStartDate() {
        var userOwner = mock(PersistentUserEntity.class);
        var paymentSheet = PersistentPaymentSheetEntity.builder().reason(TEST_REASON).place(TEST_PLACE)
                .startDate(TEST_START_DATE).endDate(TEST_END_DATE).userOwner(userOwner).build();

        assertEquals(TEST_START_DATE, paymentSheet.getStartDate(),
                "The start date returned should be the same as the one set");
    }

    /**
     * Tests the getter and setter methods of {@link PersistentPaymentSheetEntity}.
     * Verifies that values set using setters are correctly retrieved using getters.
     */
    @Test
    void testGettersAndSetters() {
        var paymentSheet = new PersistentPaymentSheetEntity();

        paymentSheet.setIdentifier(TEST_ID);
        paymentSheet.setReason("Test reason");
        paymentSheet.setPlace("Test place");
        paymentSheet.setStartDate(LocalDate.of(TEST_YEAR, TEST_MONTH_MAY, TEST_DAY_ALT_START));
        paymentSheet.setEndDate(LocalDate.of(TEST_YEAR, TEST_MONTH_MAY, TEST_DAY_ALT_END));

        assertEquals(TEST_ID, paymentSheet.getIdentifier(), "The ID getter/setter");
        assertEquals("Test reason", paymentSheet.getReason(), "The reason getter/setter");
        assertEquals("Test place", paymentSheet.getPlace(), "The place getter/setter");
        assertEquals(LocalDate.of(TEST_YEAR, TEST_MONTH_MAY, TEST_DAY_ALT_START), paymentSheet.getStartDate(),
                "The start date getter/setter");
        assertEquals(LocalDate.of(TEST_YEAR, TEST_MONTH_MAY, TEST_DAY_ALT_END), paymentSheet.getEndDate(),
                "The end date getter/setter");
    }

    /**
     * Tests the getter methods with mock objects. Verifies that mocks set using
     * setters are correctly retrieved using getters.
     */
    @Test
    void testGettersAndSettersWithMocks() {
        var regularTransport = mock(PersistentRegularTransportEntity.class);
        var selfVehicle = mock(PersistentSelfVehicleEntity.class);
        var foodHousing = mock(PersistentFoodHousingEntity.class);

        var paymentSheet = new PersistentPaymentSheetEntity();
        paymentSheet.getRegularTransports().add(regularTransport);
        paymentSheet.setSelfVehicle(selfVehicle);
        paymentSheet.setFoodHousing(foodHousing);

        assertTrue(paymentSheet.getRegularTransports().contains(regularTransport),
                "The regular transports set should contain the mock");
        assertEquals(selfVehicle, paymentSheet.getSelfVehicle(),
                "The self vehicle getter/setter should return the mock");
        assertEquals(foodHousing, paymentSheet.getFoodHousing(),
                "The food housing getter/setter should return the mock");
    }

    /**
     * Tests the {@link PersistentPaymentSheetEntity#getUserOwner()} method.
     * Verifies that the user owner returned is the same as the one set.
     */
    @Test
    void testGetUserOwner() {
        var userOwner = mock(PersistentUserEntity.class);
        var paymentSheet = PersistentPaymentSheetEntity.builder().userOwner(userOwner).reason(TEST_REASON)
                .place(TEST_PLACE).startDate(TEST_START_DATE).endDate(TEST_END_DATE).build();

        assertEquals(userOwner, paymentSheet.getUserOwner(),
                "The user owner returned should be the same as the one set");
    }

    /**
     * Tests the hash code implementation of {@link PersistentPaymentSheetEntity}.
     * Verifies that the hash codes differ when self vehicle instances are
     * different.
     */
    @Test
    void testHashCode() {
        var userOwner = mock(PersistentUserEntity.class);
        var selfVehicle1 = mock(PersistentSelfVehicleEntity.class);
        var selfVehicle2 = mock(PersistentSelfVehicleEntity.class);

        var paymentSheet1 = PersistentPaymentSheetEntity.builder().selfVehicle(selfVehicle1).reason(TEST_REASON)
                .place(TEST_PLACE).startDate(TEST_START_DATE).endDate(TEST_END_DATE).userOwner(userOwner).build();

        var paymentSheet2 = PersistentPaymentSheetEntity.builder().selfVehicle(selfVehicle2).reason(TEST_REASON)
                .place(TEST_PLACE).startDate(TEST_START_DATE).endDate(TEST_END_DATE).userOwner(userOwner).build();

        assertNotEquals(paymentSheet1.hashCode(), paymentSheet2.hashCode(),
                "Hash codes should be different for objects with different " + "self vehicles");
    }
}
