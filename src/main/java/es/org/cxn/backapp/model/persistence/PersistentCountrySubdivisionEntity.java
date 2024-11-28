/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.model.persistence;

import java.util.ArrayList;
import java.util.List;

import es.org.cxn.backapp.model.CountrySubdivisionEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Country subdivision Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "CountrySubdivision")
@Table(name = "country_subdivision")
@Data
@NoArgsConstructor
public class PersistentCountrySubdivisionEntity implements CountrySubdivisionEntity {

    /**
     * Serialization ID.
     */
    @Transient
    private static final long serialVersionUID = 1719788841150729211L;

    /**
     * Country subdivision identity Id PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Integer identifier;

    /**
     * Name of kind subdivision.
     */
    @Column(name = "kind_subdivision_name", nullable = false, unique = false)
    private String kindSubdivisionName = "";

    /**
     * Country subdivision name.
     */
    @Column(name = "name", nullable = false, unique = false)
    private String name = "";

    /**
     * Country subdivision name.
     */
    @Column(name = "code", nullable = false, unique = false)
    private String code = "";

    /**
     * Contry owns country subdivisions.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_numeric_code")
    private PersistentCountryEntity country;

    /**
     * Address have one country subdivisions.
     */
    @OneToMany(mappedBy = "countrySubdivision")
    private List<PersistentAddressEntity> addressList = new ArrayList<>();

}
