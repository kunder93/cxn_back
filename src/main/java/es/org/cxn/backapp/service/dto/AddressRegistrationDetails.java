
package es.org.cxn.backapp.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO with user Address fields.
 *
 * @author Santi
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddressRegistrationDetails {

  /**
   * The apartment number.
   */
  private String apartmentNumber;
  /**
   * The building.
   */
  private String building;
  /**
   * The city.
   */
  private String city;
  /**
   * The postal code.
   */
  private String postalCode;
  /**
   * The street.
   */
  private String street;
  /**
   * The country numeric code.
   */
  private Integer countryNumericCode;
  /**
   * The country subdivision name.
   */
  private String countrySubdivisionName;

}
