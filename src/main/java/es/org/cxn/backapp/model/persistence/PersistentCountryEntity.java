/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package es.org.cxn.backapp.model.persistence;

import java.util.ArrayList;
import java.util.List;

import es.org.cxn.backapp.model.CountryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * User Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "CountryEntity")
@Table(name = "country")
public class PersistentCountryEntity implements CountryEntity {

    /**
     * Serialization ID.
     */
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

    @OneToMany(mappedBy = "country")
    private List<PersistentCountrySubdivisionEntity> subdivisions = new ArrayList<>();

    @OneToMany(mappedBy = "country")
    private List<PersistentAddressEntity> addressList = new ArrayList<>();

    public Integer getNumericCode() {
        return numericCode;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    public List<PersistentCountrySubdivisionEntity> getSubdivisions() {
        return subdivisions;
    }

    public List<PersistentAddressEntity> getAddressList() {
        return addressList;
    }

    public void setNumericCode(Integer numericCode) {
        this.numericCode = numericCode;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    public void setAlpha3Code(String alpha3Code) {
        this.alpha3Code = alpha3Code;
    }

    public void setSubdivisions(
            List<PersistentCountrySubdivisionEntity> subdivisions
    ) {
        this.subdivisions = subdivisions;
    }

    public void setAddressList(List<PersistentAddressEntity> addressList) {
        this.addressList = addressList;
    }

    /**
     * Constructs a country entity.
     */
    public PersistentCountryEntity() {
        super();
    }

}
