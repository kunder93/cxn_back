
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentCountrySubdivisionEntity;

import java.io.Serializable;

/**
 * Represents the form used by controller as response for requesting one
 * company.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public final class SubCountryResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3133052826313337705L;

  /**
   * Subcountry name.
   */
  private String name;
  /**
   * Subcountry kind of subdivision.
   */
  private String kindSubdivisionName;
  /**
   * Subcountry code.
   */
  private String code;

  /**
   * Constructs a subCountry response.
   *
   * @param countrySubdivision country subdivision entity.
   */
  public SubCountryResponse(
        final PersistentCountrySubdivisionEntity countrySubdivision
  ) {
    super();
    this.name = countrySubdivision.getName();
    this.kindSubdivisionName = countrySubdivision.getKindSubdivisionName();
    this.code = countrySubdivision.getCode();
  }

  /**
   * @return The name.
   */
  public String getName() {
    return name;
  }

  /**
   * @return The kind subdivision name.
   */
  public String getKindSubdivisionName() {
    return kindSubdivisionName;
  }

  /**
   * @param name The name.
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * @return The code.
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code The code.
   */
  public void setCode(final String code) {
    this.code = code;
  }

  /**
   * @param kindSubdivisionName The kind subdivision name.
   */
  public void setKindSubdivisionName(final String kindSubdivisionName) {
    this.kindSubdivisionName = kindSubdivisionName;
  }

}
