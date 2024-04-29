
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.AddressEntity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as response for requesting one
 * book.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class AddressResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3160023841222437705L;

  /**
   * The address postal code.
   */
  private String postalCode;

  /**
   * The address apartment number.
   */
  private String apartmentNumber;

  /**
   * The address building.
   */
  private String building;

  /**
   * The address street.
   */
  private String street;

  /**
   * The address city name.
   */
  private String city;

  /**
   * The address country name.
   */
  private String countryName;

  /**
   * The address subCountry name.
   */
  private String subCountryName;

  /**
   * Build response using author entity.
   *
   * @param author The author entity.
   */
  public AddressResponse(final AddressEntity address) {
    postalCode = address.getPostalCode();
    apartmentNumber = address.getApartmentNumber();
    building = address.getBuilding();
    street = address.getStreet();
    city = address.getCity();
    countryName = address.getCountry().getFullName();
    subCountryName = address.getCountrySubdivision().getName();
  }
}
