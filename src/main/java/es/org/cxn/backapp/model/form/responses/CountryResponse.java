
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as response for requesting one country
 * data.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 *
 * @author Santiago Paz.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class CountryResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3132389826561387705L;

  /**
   * The country short name.
   */
  private String shortName;
  /**
   * The country full name.
   */
  private String fullName;
  /**
   * The country numeric code.
   */
  private Integer numericCode;
  /**
   * The country alpha 2 code.
   */
  private String alpha2Code;
  /**
   * The country alpha 3 code.
   */
  private String alpha3Code;

  /**
   * Constructor with country entity.
   *
   * @param value The country entity for get values.
   */
  public CountryResponse(final PersistentCountryEntity value) {
    super();
    shortName = value.getShortName();
    fullName = value.getFullName();
    numericCode = value.getNumericCode();
    alpha2Code = value.getAlpha2Code();
    alpha3Code = value.getAlpha3Code();

  }
}
