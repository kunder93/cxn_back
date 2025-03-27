
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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import es.org.cxn.backapp.model.CountryEntity;
import es.org.cxn.backapp.model.form.responses.user.address.SubCountryListResponse;
import es.org.cxn.backapp.model.form.responses.user.address.SubCountryResponse;
import es.org.cxn.backapp.model.persistence.PersistentCountrySubdivisionEntity;

class SubCountryListResponseTest {
    @Test
    void testConstructorAndImmutability() {
        // Arrange
        List<SubCountryResponse> subCountries = List.of(new SubCountryResponse("Name1", "Kind1", "Code1"),
                new SubCountryResponse("Name2", "Kind2", "Code2"));

        // Act
        SubCountryListResponse response = new SubCountryListResponse(subCountries);

        // Assert
        assertEquals(subCountries, response.subCountryList());
        assertThrows(UnsupportedOperationException.class,
                () -> response.subCountryList().add(new SubCountryResponse("Invalid", "InvalidKind", "InvalidCode")));
    }

    @Test
    void testFromEntity() {
        // Arrange
        PersistentCountrySubdivisionEntity subCountry1 = Mockito.mock(PersistentCountrySubdivisionEntity.class);
        PersistentCountrySubdivisionEntity subCountry2 = Mockito.mock(PersistentCountrySubdivisionEntity.class);

        Mockito.when(subCountry1.getName()).thenReturn("Name1");
        Mockito.when(subCountry1.getKindSubdivisionName()).thenReturn("Kind1");
        Mockito.when(subCountry1.getCode()).thenReturn("Code1");

        Mockito.when(subCountry2.getName()).thenReturn("Name2");
        Mockito.when(subCountry2.getKindSubdivisionName()).thenReturn("Kind2");
        Mockito.when(subCountry2.getCode()).thenReturn("Code2");

        CountryEntity countryEntity = Mockito.mock(CountryEntity.class);
        Mockito.when(countryEntity.getSubdivisions()).thenReturn(List.of(subCountry1, subCountry2));

        // Act
        SubCountryListResponse response = SubCountryListResponse.fromEntity(countryEntity);

        // Assert
        List<SubCountryResponse> expected = List.of(new SubCountryResponse("Name1", "Kind1", "Code1"),
                new SubCountryResponse("Name2", "Kind2", "Code2"));
        assertEquals(expected, response.subCountryList());
    }

    @Test
    void testFromEntityWithEmptyList() {
        // Arrange
        CountryEntity countryEntity = Mockito.mock(CountryEntity.class);
        Mockito.when(countryEntity.getSubdivisions()).thenReturn(List.of());

        // Act
        SubCountryListResponse response = SubCountryListResponse.fromEntity(countryEntity);

        // Assert
        assertEquals(0, response.subCountryList().size());
    }
}
