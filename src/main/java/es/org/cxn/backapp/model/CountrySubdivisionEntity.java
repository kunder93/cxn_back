
package es.org.cxn.backapp.model;

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

import java.io.Serializable;
import java.util.List;

import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

/**
 * The CountrySubdivisionEntity interface represents a subdivision within a
 * country, such as a state, province, or region. This interface provides
 * methods to access the details of the subdivision, including its identifier,
 * name, code, associated country, and related addresses.
 *
 * @author Santiago Paz Perez
 */
public interface CountrySubdivisionEntity extends Serializable {

    /**
     * Retrieves a list of addresses that use this country subdivision.
     *
     * @return A list of address entities associated with this country subdivision,
     *         or an empty list if none are found.
     */
    List<PersistentAddressEntity> getAddressList();

    /**
     * Retrieves the code associated with the country subdivision.
     *
     * @return The code of the country subdivision, or {@code null} if not set.
     */
    String getCode();

    /**
     * Retrieves the country to which the subdivision belongs.
     *
     * @return The country entity associated with this subdivision, or {@code null}
     *         if not set.
     */
    PersistentCountryEntity getCountry();

    /**
     * Retrieves the unique identifier for the country subdivision.
     *
     * @return The identifier of the country subdivision, or {@code null} if not
     *         set.
     */
    Integer getIdentifier();

    /**
     * Retrieves the name or type of the subdivision (e.g., state, province).
     *
     * @return The kind or type of subdivision, or {@code null} if not set.
     */
    String getKindSubdivisionName();

    /**
     * Retrieves the name of the country subdivision.
     *
     * @return The name of the country subdivision, or {@code null} if not set.
     */
    String getName();

}
