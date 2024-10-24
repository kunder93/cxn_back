
package es.org.cxn.backapp.model;

/**
 * Represents an Address entity with various details about a user's address.
 * This includes the user's DNI, postal code, apartment number, building,
 * street, city, user entity, country, and country subdivision.
 * <p>
 * It provides methods to access these fields.
 * </p>
 *
 * @author Santiago Paz Perez
 */
public interface AddressEntity extends java.io.Serializable {

  /**
   * Gets the user's DNI associated with the address.
   *
   * @return The Address user's DNI.
   */
  String getUserDni();

  /**
   * Gets the postal code of the address.
   *
   * @return The address postal code.
   */
  String getPostalCode();

  /**
   * Gets the apartment number of the address.
   *
   * @return The address apartment number.
   */
  String getApartmentNumber();

  /**
   * Gets the building information of the address.
   *
   * @return The address building.
   */
  String getBuilding();

  /**
   * Gets the street name of the address.
   *
   * @return The address street.
   */
  String getStreet();

  /**
   * Gets the city where the address is located.
   *
   * @return The address city.
   */
  String getCity();

  /**
   * Gets the user entity associated with the address.
   *
   * @return The address user entity.
   */
  UserEntity getUser();

  /**
   * Gets the country associated with the address.
   *
   * @return The address country.
   */
  CountryEntity getCountry();

  /**
   * Gets the country subdivision (state, province, etc.) associated with the
   * address.
   *
   * @return The address country subdivision.
   */
  CountrySubdivisionEntity getCountrySubdivision();

}
