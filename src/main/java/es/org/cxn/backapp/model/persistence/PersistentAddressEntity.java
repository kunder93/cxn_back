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

import java.util.Objects;

import es.org.cxn.backapp.model.AddressEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * User Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "AddressEntity")
@Table(name = "address")
public class PersistentAddressEntity implements AddressEntity {

    @Id
    private String userDni;

    /**
     * Serialization ID.
     */
    @Transient
    private static final long serialVersionUID = 1396772919450111291L;

    /**
     * Address postal code
     */
    @Column(name = "postal_code", nullable = false, unique = false)
    private String postalCode = "";

    /**
     * Address apartment or number.
     */
    @Column(name = "apartment_number", nullable = false, unique = false)
    private String apartmentNumber = "";

    /**
     * Address building.
     */
    @Column(name = "building", nullable = false, unique = false)
    private String building = "";

    /**
     * Address apartment or number.
     */
    @Column(name = "street", nullable = false, unique = false)
    private String street = "";

    /**
     * Address apartment or number.
     */
    @Column(name = "city", nullable = false, unique = false)
    private String city = "";

    public String getUserDni() {
        return userDni;
    }

    public void setUserDni(String userDni) {
        this.userDni = userDni;
    }

    // RELATIONS
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_dni")
    private PersistentUserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_numeric_code")
    private PersistentCountryEntity country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_subdivision_id")
    private PersistentCountrySubdivisionEntity countrySubdivision;

    /**
     * Constructs an address entity.
     */
    public PersistentAddressEntity() {
        super();
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public String getBuilding() {
        return building;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public PersistentUserEntity getUser() {
        return user;
    }

    public PersistentCountryEntity getCountry() {
        return country;
    }

    public PersistentCountrySubdivisionEntity getCountrySubdivision() {
        return countrySubdivision;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setUser(PersistentUserEntity user) {
        this.user = user;
    }

    public void setCountry(PersistentCountryEntity country) {
        this.country = country;
    }

    public void setCountrySubdivision(
            PersistentCountrySubdivisionEntity countrySubdivision
    ) {
        this.countrySubdivision = countrySubdivision;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                apartmentNumber, building, city, postalCode, street, userDni
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        var other = (PersistentAddressEntity) obj;
        return Objects.equals(apartmentNumber, other.apartmentNumber)
                && Objects.equals(building, other.building)
                && Objects.equals(city, other.city)
                && Objects.equals(postalCode, other.postalCode)
                && Objects.equals(street, other.street)
                && Objects.equals(userDni, other.userDni);
    }

    @Override
    public String toString() {
        return "PersistentAddressEntity [postalCode=" + postalCode
                + ", apartmentNumber=" + apartmentNumber + ", building="
                + building + ", street=" + street + ", city=" + city + "]";
    }
}
