
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.form.responses.CountryListResponse;
import es.org.cxn.backapp.model.form.responses.CountryResponse;
import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

class CountryListResponseTest {
    @Test
    void testConstructorWithEmptyList() {
        // Arrange
        List<CountryResponse> emptyList = Collections.emptyList();

        // Act
        CountryListResponse response = new CountryListResponse(emptyList);

        // Assert
        assertNotNull(response.countryList());
        assertTrue(response.countryList().isEmpty());
    }

    @Test
    void testConstructorWithNullList() {
        // Act
        CountryListResponse response = new CountryListResponse(null);

        // Assert
        assertNotNull(response.countryList());
        assertTrue(response.countryList().isEmpty());
    }

    @Test
    void testConstructorWithValidList() {
        // Arrange
        List<CountryResponse> validList = new ArrayList<>();
        validList.add(new CountryResponse("US", "United States", 840, "US", "USA"));
        validList.add(new CountryResponse("ES", "Spain", 724, "ES", "ESP"));

        // Act
        CountryListResponse response = new CountryListResponse(validList);

        // Assert
        assertEquals(2, response.countryList().size());
        assertEquals("US", response.countryList().get(0).shortName());
        assertEquals("United States", response.countryList().get(0).fullName());
        assertEquals(840, response.countryList().get(0).numericCode());
        assertEquals("US", response.countryList().get(0).alpha2Code());
        assertEquals("USA", response.countryList().get(0).alpha3Code());
    }

    @Test
    void testDefensiveCopyInConstructor() {
        // Arrange
        List<CountryResponse> modifiableList = new ArrayList<>();
        modifiableList.add(new CountryResponse("US", "United States", 840, "US", "USA"));

        CountryListResponse response = new CountryListResponse(modifiableList);

        // Act
        modifiableList.add(new CountryResponse("ES", "Spain", 724, "ES", "ESP"));

        // Assert
        assertEquals(1, response.countryList().size()); // Original list remains unchanged
    }

    @Test
    void testDefensiveCopyInGetter() {
        // Arrange
        List<CountryResponse> validList = new ArrayList<>();
        validList.add(new CountryResponse("US", "United States", 840, "US", "USA"));

        CountryListResponse response = new CountryListResponse(validList);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            response.countryList().add(new CountryResponse("ES", "Spain", 724, "ES", "ESP"));
        });
    }

    @Test
    void testFromPersistentCountryEntity() {
        // Arrange
        PersistentCountryEntity entity1 = new PersistentCountryEntity();
        entity1.setShortName("US");
        entity1.setFullName("United States");
        entity1.setNumericCode(840);
        entity1.setAlpha2Code("US");
        entity1.setAlpha3Code("USA");

        PersistentCountryEntity entity2 = new PersistentCountryEntity();
        entity2.setShortName("ES");
        entity2.setFullName("Spain");
        entity2.setNumericCode(724);
        entity2.setAlpha2Code("ES");
        entity2.setAlpha3Code("ESP");

        List<PersistentCountryEntity> entities = List.of(entity1, entity2);

        // Act
        CountryListResponse response = CountryListResponse.from(entities);

        // Assert
        assertNotNull(response.countryList());
        assertEquals(2, response.countryList().size());

        assertEquals("US", response.countryList().get(0).shortName());
        assertEquals("United States", response.countryList().get(0).fullName());
        assertEquals(840, response.countryList().get(0).numericCode());
        assertEquals("US", response.countryList().get(0).alpha2Code());
        assertEquals("USA", response.countryList().get(0).alpha3Code());

        assertEquals("ES", response.countryList().get(1).shortName());
        assertEquals("Spain", response.countryList().get(1).fullName());
        assertEquals(724, response.countryList().get(1).numericCode());
        assertEquals("ES", response.countryList().get(1).alpha2Code());
        assertEquals("ESP", response.countryList().get(1).alpha3Code());
    }
}
