
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

/**
 * Represents the response DTO used by the controller for requesting one
 * country data.
 * <p>
 * This record serves as a Data Transfer Object (DTO) to facilitate
 * communication between the view and the controller when requesting
 * country data.
 * </p>
 *
 * @param shortName   The country short name.
 * @param fullName    The country full name.
 * @param numericCode The country numeric code.
 * @param alpha2Code  The country alpha 2 code.
 * @param alpha3Code  The country alpha 3 code.
 * @author Santiago Paz
 */
public record CountryResponse(
      String shortName, String fullName, Integer numericCode, String alpha2Code,
      String alpha3Code
) {

  /**
   * Constructs a {@link CountryResponse} from a
   * {@link PersistentCountryEntity}.
   *
   * @param value The country entity to extract values from.
   */
  public CountryResponse(final PersistentCountryEntity value) {
    this(
          value.getShortName(), value.getFullName(), value.getNumericCode(),
          value.getAlpha2Code(), value.getAlpha3Code()
    );
  }
}
