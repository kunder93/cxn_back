/**
 * The MIT License (MIT)
 *
 * <p>Copyright (c) 2020 the original author or authors.
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.exceptions.AddressServiceException;
import es.org.cxn.backapp.model.form.responses.CountryListResponse;
import es.org.cxn.backapp.model.form.responses.SubCountryListResponse;
import es.org.cxn.backapp.service.AddressService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    addressService =
          checkNotNull(service, "Received a null pointer as service");
  }

  /**
   * Return all stored countries with their data.
   *
   *
   * @return all stored countries.
   */
  @CrossOrigin
  @GetMapping(path = "/getCountries")
  public ResponseEntity<CountryListResponse> getAllCountries() {
    final var countryList = addressService.getCountries();
    return new ResponseEntity<>(
          new CountryListResponse(countryList), HttpStatus.OK
    );
  }

  /**
   * Return country data and sub-countries from one country.
   *
   * @param countryCode The country code a.k.a. country identifier for getting
   *                    related sub-countries.
   *
   * @return country data and his sub-countries.
   */
  @CrossOrigin
  @GetMapping(path = "/country/{countryCode}")
  public ResponseEntity<SubCountryListResponse>
        getAllSubCountriesFromCountry(@PathVariable
  final Integer countryCode) {
    try {
      final var country = addressService.getCountry(countryCode);
      return new ResponseEntity<>(
            new SubCountryListResponse(country), HttpStatus.OK
      );
    } catch (AddressServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e

      );
    }
  }
}
