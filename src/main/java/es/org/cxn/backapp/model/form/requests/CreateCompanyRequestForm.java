
package es.org.cxn.backapp.model.form.requests;

import es.org.cxn.backapp.model.form.Constants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the form used by controller as request of create company.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public final class CreateCompanyRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3133389822212147705L;

  /**
   * The company nif.
   */
  private String nif;

  /**
   * The company name.
   */
  @NotBlank(message = Constants.NAME_NOT_BLANK_MESSAGE)
  @Size(
        max = Constants.NAME_MAX_LENGTH,
        message = Constants.NAME_MAX_LENGTH_MESSAGE
  )
  private String name;

  /**
   * The company address.
   */
  private String address;

  /**
   * Main arguments constructor.
   *
   * @param nif     the company nif.
   * @param name    the company name.
   * @param address the company address.
   */
  public CreateCompanyRequestForm(
        final String nif, final String name, final String address
  ) {
    super();
    this.nif = nif;
    this.name = name;
    this.address = address;
  }

  /**
   * Main empty constructor.
   */
  public CreateCompanyRequestForm() {
    super();
  }

  /**
   * Get company nif.
   *
   * @return the company nif.
   */
  public String getNif() {
    return nif;
  }

  /**
   * Set company nif.
   *
   * @param value The company nif.
   */
  public void setNifCif(final String value) {
    this.nif = value;
  }

  /**
   * Get company name.
   *
   * @return The company name.
   */
  public String getName() {
    return name;
  }

  /**
   * Set request company name.
   *
   * @param value The request company name.
   */
  public void setName(final String value) {
    this.name = value;
  }

  /**
   * Get request company address.
   *
   * @return The company address.
   */
  public String getAddress() {
    return address;
  }

  /**
   * Set request company address.
   *
   * @param value The company address.
   */
  public void setAddress(final String value) {
    this.address = value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, name, nif);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    var other = (CreateCompanyRequestForm) obj;
    return Objects.equals(address, other.address)
          && Objects.equals(name, other.name) && Objects.equals(nif, other.nif);
  }

  @Override
  public String toString() {
    return "CreateCompanyRequestForm [nif=" + nif + ", name=" + name
          + ", address=" + address + "]";
  }

}
