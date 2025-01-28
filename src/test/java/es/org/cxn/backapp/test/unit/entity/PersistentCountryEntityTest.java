
package es.org.cxn.backapp.test.unit.entity;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

/**
 * Unit tests for the {@link PersistentCountryEntity} class. This class tests
 * the getter methods, equality, and hash code implementation of the
 * {@link PersistentCountryEntity} class.
 */
class PersistentCountryEntityTest {

    /**
     * A sample numeric code used for testing purposes. This value represents the
     * numeric code of the country.
     */
    private static final int TEST_NUMERIC_CODE = 123;

    /**
     * A sample full name used for testing purposes. This value represents the full
     * name of the country.
     */
    private static final String TEST_FULL_NAME = "Example Full Name";

    /**
     * A sample short name used for testing purposes. This value represents the
     * short name or abbreviation of the country.
     */
    private static final String TEST_SHORT_NAME = "Example Short Name";

    /**
     * A sample alpha-2 code used for testing purposes. This value represents the
     * two-letter country code.
     */
    private static final String TEST_ALPHA2_CODE = "AB";

    /**
     * A sample alpha-3 code used for testing purposes. This value represents the
     * three-letter country code.
     */
    private static final String TEST_ALPHA3_CODE = "ABC";

    /**
     * A different full name used to test the inequality of
     * {@link PersistentCountryEntity}. This value is used to verify that instances
     * with different full names are not considered equal.
     */
    private static final String TEST_DIFFERENT_FULL_NAME = "Different Full Name";

    /**
     * Tests the {@link PersistentCountryEntity#equals(Object)} and
     * {@link PersistentCountryEntity#hashCode()} methods. Verifies that two
     * instances with the same attributes are considered equal and have the same
     * hash code. Also tests cases where instances differ and with null and
     * different object types.
     */
    @Test
    void testEqualsAndHashCode() {
        // Create two instances of PersistentCountryEntity with the same attributes
        var country1 = new PersistentCountryEntity();
        country1.setNumericCode(TEST_NUMERIC_CODE);
        country1.setFullName(TEST_FULL_NAME);
        country1.setShortName(TEST_SHORT_NAME);
        country1.setAlpha2Code(TEST_ALPHA2_CODE);
        country1.setAlpha3Code(TEST_ALPHA3_CODE);

        var country2 = new PersistentCountryEntity();
        country2.setNumericCode(TEST_NUMERIC_CODE);
        country2.setFullName(TEST_FULL_NAME);
        country2.setShortName(TEST_SHORT_NAME);
        country2.setAlpha2Code(TEST_ALPHA2_CODE);
        country2.setAlpha3Code(TEST_ALPHA3_CODE);

        // Check equality and hash code for instances with identical attributes
        assertEquals(country1, country2, "country1 and country2 should be equal according to equals()");
        assertEquals(country2, country1, "country1 and country2 should be equal according to equals()");

        assertEquals(country1.hashCode(), country2.hashCode(),
                "Hash codes of country1 and country2 should be the same");

        // Modify an attribute in country2 and test inequality
        country2.setFullName(TEST_DIFFERENT_FULL_NAME);

        assertNotEquals(country1, country2,
                "country1 and country2 should no longer be equal after" + " modifying country2");
        assertNotEquals(country2, country1,
                "country1 and country2 should no longer be equal after " + "modifying country2");

        assertNotEquals(country1.hashCode(), country2.hashCode(),
                "Hash codes of country1 and country2 should be different " + "after modifying country2");

        // Test equality with a null value and an object of a different type
        PersistentCountryEntity country3 = null;
        assertNotEquals(country1, country3, "country1 should not be equal to null");

        var otherObject = "This is not a PersistentCountryEntity";
        assertNotEquals(country1, otherObject, "country1 should not be equal to an object of a different type");
    }

    /**
     * Tests the getter methods of {@link PersistentCountryEntity}. Verifies that
     * the values set using setters are correctly retrieved using getters.
     */
    @Test
    void testGetters() {
        // Create an instance of PersistentCountryEntity
        var country = new PersistentCountryEntity();

        // Set values using setter methods
        country.setNumericCode(TEST_NUMERIC_CODE);
        country.setFullName(TEST_FULL_NAME);
        country.setShortName(TEST_SHORT_NAME);
        country.setAlpha2Code(TEST_ALPHA2_CODE);
        country.setAlpha3Code(TEST_ALPHA3_CODE);

        // Verify that the values were correctly set and retrieved
        assertEquals(TEST_NUMERIC_CODE, country.getNumericCode(),
                "The numeric code getter should return the correct value");
        assertEquals(TEST_FULL_NAME, country.getFullName(), "The full name getter should return the correct value");
        assertEquals(TEST_SHORT_NAME, country.getShortName(), "The short name getter should return the correct value");
        assertEquals(TEST_ALPHA2_CODE, country.getAlpha2Code(),
                "The alpha-2 code getter should return the correct value");
        assertEquals(TEST_ALPHA3_CODE, country.getAlpha3Code(),
                "The alpha-3 code getter should return the correct value");
    }

    @Test
    void testSetAddressListImmutable() {
        // Arrange
        PersistentCountryEntity countryEntity = new PersistentCountryEntity();
        PersistentAddressEntity address1 = new PersistentAddressEntity();
        address1.setUserDni("11111111N");

        PersistentAddressEntity address2 = new PersistentAddressEntity();
        address2.setUserDni("22222222J");
        Set<PersistentAddressEntity> addressSet = new HashSet<>();
        addressSet.add(address1);
        addressSet.add(address2);

        // Act
        countryEntity.setAddressList(addressSet);

        // Assert
        Set<PersistentAddressEntity> result = countryEntity.getAddressList();
        assertEquals(2, result.size(), "The address list should contain 2 elements.");
        assertTrue(result.contains(address1), "The address list should contain address1.");
        assertTrue(result.contains(address2), "The address list should contain address2.");
        assertThrows(UnsupportedOperationException.class, () -> result.add(new PersistentAddressEntity()),
                "The address list should be immutable.");
    }

    @Test
    void testSetAddressListNullValue() {
        PersistentCountryEntity countryEntity = new PersistentCountryEntity();
        // Act & Assert
        assertThrows(NullPointerException.class, () -> countryEntity.setAddressList(null),
                "Setting a null address list should throw a NullPointerException.");
    }

    @Test
    void testSetAddressListOverridesPreviousValue() {
        // Arrange
        PersistentCountryEntity countryEntity = new PersistentCountryEntity();
        PersistentAddressEntity address1 = new PersistentAddressEntity();
        address1.setUserDni("11111111K");
        Set<PersistentAddressEntity> initialSet = new HashSet<>();
        initialSet.add(address1);

        PersistentAddressEntity address2 = new PersistentAddressEntity();
        address2.setUserDni("22222222K");
        Set<PersistentAddressEntity> newSet = new HashSet<>();
        newSet.add(address2);

        // Act
        countryEntity.setAddressList(initialSet);
        countryEntity.setAddressList(newSet);

        // Assert
        Set<PersistentAddressEntity> result = countryEntity.getAddressList();
        assertEquals(1, result.size(), "The address list should contain 1 element after being overridden.");
        assertTrue(result.contains(address2), "The address list should contain the new address2.");
        assertFalse(result.contains(address1), "The address list should no longer contain the old address1.");
    }

}
