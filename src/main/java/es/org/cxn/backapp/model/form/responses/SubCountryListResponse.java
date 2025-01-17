
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.org.cxn.backapp.model.CountryEntity;

/**
 * Represents the response form used by the controller for requesting a list of
 * subcountries.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller by mapping a list of country subdivisions. Each
 * subdivision is represented by a {@link SubCountryResponse}.
 * <p>
 * This record provides an immutable representation of the list of subcountries,
 * ensuring that the data remains consistent.
 *
 * @param subCountryList a list of {@link SubCountryResponse} representing the
 *                       subdivisions of the country.
 *
 * @author Santiago Paz.
 */
public record SubCountryListResponse(List<SubCountryResponse> subCountryList) {

    /**
     * Constructor for the SubCountryListResponse class.
     * <p>
     * This constructor takes a list of {@link SubCountryResponse} objects and
     * ensures that the list is immutable by creating a defensive copy and wrapping
     * it in an unmodifiable list.
     * </p>
     *
     * @param subCountryList a list of {@link SubCountryResponse} objects
     *                       representing the subdivisions of a country.
     */
    public SubCountryListResponse(final List<SubCountryResponse> subCountryList) {
        this.subCountryList = Collections.unmodifiableList(new ArrayList<>(subCountryList));
    }

    /**
     * Creates a {@code SubCountryListResponse} from a {@code CountryEntity}.
     * <p>
     * This static factory method converts a {@code CountryEntity} into a
     * {@code SubCountryListResponse}. It maps the subdivisions of the country
     * entity into a list of {@code SubCountryResponse}.
     *
     * @param country the {@code CountryEntity} containing subdivisions to be
     *                converted.
     * @return a new {@code SubCountryListResponse} containing the list of
     *         subcountry responses.
     */
    public static SubCountryListResponse fromEntity(final CountryEntity country) {
        final var subCountryResponses = country.getSubdivisions().stream().map(SubCountryResponse::fromEntity).toList();

        return new SubCountryListResponse(Collections.unmodifiableList(subCountryResponses));
    }

    /**
     * Returns a defensive copy of the list of subcountries to avoid external
     * modification of the internal list.
     * <p>
     * This method ensures that external code cannot modify the internal list of
     * subcountry responses by returning a new copy of the list.
     * </p>
     *
     * @return a new {@link List} containing the same elements as the original
     *         {@code subCountryList}, but as a mutable list.
     */
    @Override
    public List<SubCountryResponse> subCountryList() {
        return Collections.unmodifiableList(new ArrayList<>(subCountryList));
    }

}
