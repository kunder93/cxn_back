
package es.org.cxn.backapp.controller.entity;

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

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.model.CountryEntity;
import es.org.cxn.backapp.model.form.responses.CountryListResponse;
import es.org.cxn.backapp.model.form.responses.SubCountryListResponse;
import es.org.cxn.backapp.service.AddressService;
import es.org.cxn.backapp.service.exceptions.AddressServiceException;

/**
 * Rest controller for user address, countries and countries subdivisions.
 *
 * @author Santiago Paz
 */
@RestController
@RequestMapping("/api/address")
public class AddressController {

    /**
     * The address service.
     */
    private final AddressService addressService;

    /**
     * Constructs a controller with the specified dependencies.
     *
     * @param service address service.
     */
    public AddressController(final AddressService service) {
        super();

        addressService = checkNotNull(service, "Received a null pointer as service");
    }

    /**
     * Return all stored countries with their data.
     *
     *
     * @return all stored countries.
     */
    @GetMapping(path = "/getCountries")
    public ResponseEntity<CountryListResponse> getAllCountries() {
        // Retrieve the list of PersistentCountryEntity objects
        final var countryEntities = addressService.getCountries();

        // Use the static factory method to create a CountryListResponse
        final var response = CountryListResponse.from(countryEntities);

        // Return the response entity with the created CountryListResponse and
        // HTTP status OK
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Returns country data and sub-countries for a given country.
     * <p>
     * This endpoint retrieves information about a country and its associated
     * sub-countries based on the provided country code.
     *
     * @param countryCode The country code (identifier) for retrieving related
     *                    sub-countries.
     * @return a {@code ResponseEntity} containing a {@code SubCountryListResponse}
     *         with the country data and its sub-countries, or an error response if
     *         the country cannot be found.
     */
    @GetMapping(path = "/country/{countryCode}")
    public ResponseEntity<SubCountryListResponse> getAllSubCountriesFromCountry(
            @PathVariable final Integer countryCode) {
        try {
            // Fetch the country entity using the service
            final CountryEntity country = addressService.getCountry(countryCode);

            // Create the response using the static factory method
            final var response = SubCountryListResponse.fromEntity(country);

            // Return the response with HTTP status OK
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AddressServiceException e) {
            // Handle the exception and return a BAD_REQUEST response
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
