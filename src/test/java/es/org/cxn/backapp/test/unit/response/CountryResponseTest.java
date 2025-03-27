
package es.org.cxn.backapp.test.unit.response;

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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.form.responses.user.address.CountryResponse;
import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

/**
 * Unit tests for the CountryResponse class. This test class ensures that the
 * CountryResponse object accurately represents the country data retrieved from
 * the persistence layer.
 */
class CountryResponseTest {

    /**
     * Constant representing the numeric code for Spain. Used in tests to validate
     * the proper handling of country numeric codes in the response.
     */
    private static final int SPAIN_COUNTRY_NUMERIC_CODE = 724;

    /**
     * Mock object for the PersistentCountryEntity class. Used to simulate the
     * behavior of the persistence layer and to verify response creation.
     */
    private PersistentCountryEntity mockCountryEntity;

    @BeforeEach
    void setUp() {
        mockCountryEntity = mock(PersistentCountryEntity.class);

        when(mockCountryEntity.getShortName()).thenReturn("ES");
        when(mockCountryEntity.getFullName()).thenReturn("Spain");
        when(mockCountryEntity.getNumericCode()).thenReturn(SPAIN_COUNTRY_NUMERIC_CODE);
        when(mockCountryEntity.getAlpha2Code()).thenReturn("ES");
        when(mockCountryEntity.getAlpha3Code()).thenReturn("ESP");
    }

    @Test
    void testCountryResponseEqualsAndHashCode() {
        // Arrange
        CountryResponse response1 = new CountryResponse(mockCountryEntity);
        CountryResponse response2 = new CountryResponse(mockCountryEntity);

        // Act & Assert
        assertEquals(response1, response2, "Two CountryResponse instances with the same data should be equal");
        assertEquals(response1.hashCode(), response2.hashCode(), "Hash codes should match for equal instances");
    }

    @Test
    void testCountryResponseFromEntity() {
        // Act
        CountryResponse response = new CountryResponse(mockCountryEntity);

        // Assert
        assertNotNull(response, "CountryResponse should not be null");
        assertEquals("ES", response.shortName(), "Short name should match");
        assertEquals("Spain", response.fullName(), "Full name should match");
        assertEquals(SPAIN_COUNTRY_NUMERIC_CODE, response.numericCode(), "Numeric code should match");
        assertEquals("ES", response.alpha2Code(), "Alpha2 code should match");
        assertEquals("ESP", response.alpha3Code(), "Alpha3 code should match");
    }

    @Test
    void testCountryResponseHandlesNullValues() {
        // Arrange
        when(mockCountryEntity.getShortName()).thenReturn(null);
        when(mockCountryEntity.getFullName()).thenReturn(null);
        when(mockCountryEntity.getNumericCode()).thenReturn(null);
        when(mockCountryEntity.getAlpha2Code()).thenReturn(null);
        when(mockCountryEntity.getAlpha3Code()).thenReturn(null);

        // Act
        CountryResponse response = new CountryResponse(mockCountryEntity);

        // Assert
        assertNotNull(response, "CountryResponse should not be null");
        assertNull(response.shortName(), "Short name should be null");
        assertNull(response.fullName(), "Full name should be null");
        assertNull(response.numericCode(), "Numeric code should be null");
        assertNull(response.alpha2Code(), "Alpha2 code should be null");
        assertNull(response.alpha3Code(), "Alpha3 code should be null");
    }

    @Test
    void testCountryResponseToString() {
        // Act
        CountryResponse response = new CountryResponse(mockCountryEntity);

        // Assert
        String expectedString = "CountryResponse[shortName=ES, fullName=Spain, numericCode=724,"
                + " alpha2Code=ES, alpha3Code=ESP]";
        assertEquals(expectedString, response.toString(),
                "The toString() representation should match the expected format");
    }

    @Test
    void testCountryResponseWithEmptyEntity() {
        // Arrange
        PersistentCountryEntity emptyEntity = mock(PersistentCountryEntity.class);

        when(emptyEntity.getShortName()).thenReturn("");
        when(emptyEntity.getFullName()).thenReturn("");
        when(emptyEntity.getNumericCode()).thenReturn(0);
        when(emptyEntity.getAlpha2Code()).thenReturn("");
        when(emptyEntity.getAlpha3Code()).thenReturn("");

        // Act
        CountryResponse response = new CountryResponse(emptyEntity);

        // Assert
        assertEquals("", response.shortName(), "Short name should be empty");
        assertEquals("", response.fullName(), "Full name should be empty");
        assertEquals(0, response.numericCode(), "Numeric code should be zero");
        assertEquals("", response.alpha2Code(), "Alpha2 code should be empty");
        assertEquals("", response.alpha3Code(), "Alpha3 code should be empty");
    }
}
