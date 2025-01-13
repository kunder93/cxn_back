
package es.org.cxn.backapp.model.persistence;

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

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.org.cxn.backapp.model.CountryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Country Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "CountryEntity")
@Table(name = "country")
@NoArgsConstructor
@Data
public class PersistentCountryEntity implements CountryEntity {

    /**
     * Serialization ID.
     */
    @Serial
    @Transient
    private static final long serialVersionUID = 1419348811150111291L;

    /**
     * Numeric code use as Country PK.
     */
    @Id
    @Column(name = "numeric_code", nullable = false, unique = true)
    private Integer numericCode;

    /**
     * Country full name.
     */
    @Column(name = "full_name", nullable = false, unique = false)
    private String fullName = "";

    /**
     * Country short name.
     */
    @Column(name = "short_name", nullable = false, unique = false)
    private String shortName = "";

    /**
     * Country 2 characters code.
     */
    @Column(name = "alpha_2_code", nullable = false, unique = false)
    private String alpha2Code = "";

    /**
     * Country 3 characters code.
     */
    @Column(name = "alpha_3_code", nullable = false, unique = false)
    private String alpha3Code = "";

    /**
     * Country subdivisions.
     */
    @OneToMany(mappedBy = "country")
    private List<PersistentCountrySubdivisionEntity> subdivisions = new ArrayList<>();

    /**
     * Set of addresses with this country.
     */
    @OneToMany(mappedBy = "country")
    private Set<PersistentAddressEntity> addressList = new HashSet<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<PersistentAddressEntity> getAddressList() {
        return Collections.unmodifiableSet(addressList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAddressList(final Set<PersistentAddressEntity> value) {
        addressList = Collections.unmodifiableSet(value);
    }

}
