
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the user address data in requests.
 * <p>
 * This is part of DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public final class UserAddressData implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3100381907715947999L;

  /**
   * The postal code.
   */
  private String postalCode;
  /**
   * The apartment number.
   */
  private String apartmentNumber;
  /**
   * The building.
   */
  private String building;
  /**
   * The street.
   */
  private String street;
  /**
   * The city.
   */
  private String city;
  /**
   * The country numeric code.
   */
  private Integer countryNumericCode;
  /**
   * The country subdivision name.
   */
  private String countrySubdivisionName;

  /**
   * Main constructor.
   *
   * @param postalCode The postal code.
   * @param apartmentNumber The apartment number.
   * @param building The building.
   * @param street The street.
   * @param city The city.
   * @param countryNumericCode The country numeric code.
   * @param countrySubdivisionName The country subdivision name.
   */
  public UserAddressData(
        String postalCode, String apartmentNumber, String building,
        String street, String city, Integer countryNumericCode,
        String countrySubdivisionName
  ) {
    super();
    this.postalCode = postalCode;
    this.apartmentNumber = apartmentNumber;
    this.building = building;
    this.street = street;
    this.city = city;
    this.countryNumericCode = countryNumericCode;
    this.countrySubdivisionName = countrySubdivisionName;
  }

  /**
   * @return The postal code.
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * @param postalCode The postal code.
   */
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * @return The apartment number.
   */
  public String getApartmentNumber() {
    return apartmentNumber;
  }

  /**
   * @param apartmentNumber The apartment number.
   */
  public void setApartmentNumber(String apartmentNumber) {
    this.apartmentNumber = apartmentNumber;
  }

  /**
   * @return The building.
   */
  public String getBuilding() {
    return building;
  }

  /**
   * @param building The building.
   */
  public void setBuilding(String building) {
    this.building = building;
  }

  /**
   * @return The street.
   */
  public String getStreet() {
    return street;
  }

  /**
   * @param street The street.
   */
  public void setStreet(String street) {
    this.street = street;
  }

  /**
   * @return The city.
   */
  public String getCity() {
    return city;
  }

  /**
   * @param city The city.
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * @return The country numeric code.
   */
  public Integer getCountryNumericCode() {
    return countryNumericCode;
  }

  /**
   * @param countryNumericCode The country numeric code.
   */
  public void setCountryNumericCode(Integer countryNumericCode) {
    this.countryNumericCode = countryNumericCode;
  }

  /**
   * @return The country subdivision name.
   */
  public String getCountrySubdivisionName() {
    return countrySubdivisionName;
  }

  /**
   * @param countrySubdivisionName The country subdivision name.
   */
  public void setCountrySubdivisionName(String countrySubdivisionName) {
    this.countrySubdivisionName = countrySubdivisionName;
  }

  /**
   * The hash code method.
   */
  @Override
  public int hashCode() {
    return Objects.hash(
          apartmentNumber, building, city, countryNumericCode,
          countrySubdivisionName, postalCode, street
    );
  }

  /**
   * The equals method.
   */
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
    var other = (UserAddressData) obj;
    return Objects.equals(apartmentNumber, other.apartmentNumber)
          && Objects.equals(building, other.building)
          && Objects.equals(city, other.city)
          && Objects.equals(countryNumericCode, other.countryNumericCode)
          && Objects
                .equals(countrySubdivisionName, other.countrySubdivisionName)
          && Objects.equals(postalCode, other.postalCode)
          && Objects.equals(street, other.street);
  }

  /**
   * The to string method.
   */
  @Override
  public String toString() {
    return "UserAddressData [postalCode=" + postalCode + ", apartmentNumber="
          + apartmentNumber + ", building=" + building + ", street=" + street
          + ", city=" + city + ", countryNumericCode=" + countryNumericCode
          + ", countrySubdivisionName=" + countrySubdivisionName + "]";
  }

}
