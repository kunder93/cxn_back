
package es.org.cxn.backapp.service.impl;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;
import es.org.cxn.backapp.repository.CountryEntityRepository;
import es.org.cxn.backapp.service.AddressService;
import es.org.cxn.backapp.service.exceptions.AddressServiceException;

/**
 * Default implementation of the {@link AddressService}.
 *
 * @author Santiago Paz.
 *
 */
@Service
public final class DefaultAddressService implements AddressService {

    /**
     * Repository for the country entities handled by the service.
     */
    private final CountryEntityRepository countryRepository;

    /**
     * Constructs an entities service with the specified repositories.
     *
     * @param repoCountry The country repository{@link CountryEntityRepository}
     *
     */
    public DefaultAddressService(final CountryEntityRepository repoCountry) {
        super();

        countryRepository = Objects.requireNonNull(repoCountry, "Received a null pointer as country repository");
    }

    @Override
    public List<PersistentCountryEntity> getCountries() {
        return countryRepository.findAll();
    }

    @Override
    public PersistentCountryEntity getCountry(final Integer countryNumericCode) throws AddressServiceException {
        final var countryOptional = countryRepository.findById(countryNumericCode);
        if (countryOptional.isPresent()) {
            return countryOptional.get();
        } else {
            throw new AddressServiceException("Country with numeric code: " + countryNumericCode + " not found.");
        }
    }

}
