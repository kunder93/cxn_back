
package es.org.cxn.backapp.model;

import es.org.cxn.backapp.model.persistence.PersistentAddressEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

import java.io.Serializable;
import java.util.List;

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
   * Retrieves the unique identifier for the country subdivision.
   *
   * @return The identifier of the country subdivision, or {@code null} if
   * not set.
   */
  Integer getId();

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

  /**
   * Retrieves the code associated with the country subdivision.
   *
   * @return The code of the country subdivision, or {@code null} if not set.
   */
  String getCode();

  /**
   * Retrieves the country to which the subdivision belongs.
   *
   * @return The country entity associated with this subdivision, or
   * {@code null} if not set.
   */
  PersistentCountryEntity getCountry();

  /**
   * Retrieves a list of addresses that use this country subdivision.
   *
   * @return A list of address entities associated with this country
   * subdivision, or an empty list if none are found.
   */
  List<PersistentAddressEntity> getAddressList();

}
