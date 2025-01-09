
package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountrySubdivisionEntity;

/**
 * Represents a country with its associated details such as numeric and alpha
 * codes, full and short names, as well as related subdivisions and addresses.
 * This interface provides methods to access the country's metadata and
 * associated entities.
 *
 * @author Santiago Paz Perez
 */
public interface CountryEntity extends Serializable {

    /**
     * Retrieves a set of addresses that are associated with this country.
     *
     * @return A set of address entities associated with this country, or an empty
     *         list if none are found.
     */
    Set<PersistentAddressEntity> getAddressList();

    /**
     * Retrieves the alpha-2 code for the country, which is a two-letter country
     * code.
     *
     * @return The alpha-2 code of the country, or {@code null} if not set.
     */
    String getAlpha2Code();

    /**
     * Retrieves the alpha-3 code for the country, which is a three-letter country
     * code.
     *
     * @return The alpha-3 code of the country, or {@code null} if not set.
     */
    String getAlpha3Code();

    /**
     * Retrieves the full name of the country.
     *
     * @return The full name of the country, or {@code null} if not set.
     */
    String getFullName();

    /**
     * Retrieves the numeric code assigned to the country.
     *
     * @return The numeric code of the country, or {@code null} if not set.
     */
    Integer getNumericCode();

    /**
     * Retrieves the short name or abbreviation of the country.
     *
     * @return The short name of the country, or {@code null} if not set.
     */
    String getShortName();

    /**
     * Retrieves a list of subdivisions associated with the country. Subdivisions
     * could include states, provinces, or regions.
     *
     * @return A list of country subdivision entities associated with this country,
     *         or an empty list if none are found.
     */
    List<PersistentCountrySubdivisionEntity> getSubdivisions();

    /**
     * Set the new address list.
     *
     * @param value The new set of address values.
     */
    void setAddressList(Set<PersistentAddressEntity> value);

}
