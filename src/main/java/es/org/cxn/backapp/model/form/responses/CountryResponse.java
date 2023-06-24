
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

import java.io.Serializable;

/**
 * Represents the form used by controller as response for requesting one country
 * data.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 *
 * @author Santiago Paz.
 */
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

  /**
   * @return The country short name.
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * @return The country full name.
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * @return The country numeric code.
   */
  public Integer getNumericCode() {
    return numericCode;
  }

  /**
   * @return The country alpha 2 code.
   */
  public String getAlpha2Code() {
    return alpha2Code;
  }

  /**
   * @return The country alpha 3 code.
   */
  public String getAlpha3Code() {
    return alpha3Code;
  }

  /**
   * @param value the country short name.
   */
  public void setShortName(final String value) {
    this.shortName = value;
  }

  /**
   * @param value The country full name.
   */
  public void setFullName(final String value) {
    this.fullName = value;
  }

  /**
   * @param numericCode The country numeric code.
   */
  public void setNumericCode(final Integer numericCode) {
    this.numericCode = numericCode;
  }

  /**
   * @param alpha2Code The country alpha 2 code.
   */
  public void setAlpha2Code(final String alpha2Code) {
    this.alpha2Code = alpha2Code;
  }

  /**
   * @param alpha3Code The country alpha 3 code.
   */
  public void setAlpha3Code(final String alpha3Code) {
    this.alpha3Code = alpha3Code;
  }

}
