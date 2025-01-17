package es.org.cxn.backapp.model.form.responses;

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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

/**
 * Represents the response DTO used by the controller for requesting all
 * countries.
 * <p>
 * This record serves as a Data Transfer Object (DTO) to facilitate
 * communication between the view and the controller when requesting a list of
 * all countries. It contains a list of country responses.
 * </p>
 *
 * @param countryList The list of {@link CountryResponse} representing all
 *                    countries.
 *
 * @author Santiago Paz
 */
public record CountryListResponse(List<CountryResponse> countryList) {

    /**
     * Constructs a CountryListResponse with a defensive copy to ensure
     * immutability.
     *
     * @param countryList The list of country responses.
     */
    public CountryListResponse(final List<CountryResponse> countryList) {
        this.countryList = countryList == null ? Collections.emptyList() : List.copyOf(countryList);
    }

    /**
     * Creates a {@link CountryListResponse} from a collection of
     * {@link PersistentCountryEntity}.
     * <p>
     * This static factory method converts a collection of persistent country
     * entities into a {@link CountryListResponse}. Each entity is mapped to a
     * {@link CountryResponse} object, which is then collected into a list.
     * </p>
     *
     * @param value The collection of {@link PersistentCountryEntity} to be
     *              converted.
     * @return A new instance of {@link CountryListResponse} containing the list of
     *         country responses.
     */
    public static CountryListResponse from(final Collection<PersistentCountryEntity> value) {
        final var countryResponses = value.stream().map(CountryResponse::new).toList();
        return new CountryListResponse(countryResponses);
    }

    /**
     * Returns an unmodifiable view of the country list.
     *
     * @return An immutable list of country responses.
     */
    @Override
    public List<CountryResponse> countryList() {
        // Explicitly return an unmodifiable view to ensure safety
        return List.copyOf(countryList);
    }

}
