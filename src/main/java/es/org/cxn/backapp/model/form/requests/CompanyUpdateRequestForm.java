
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the form used by controller as request of update company.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public final class CompanyUpdateRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 7447202297370722033L;

  /**
   * The company name.
   */
  private String name;
  /**
   * The company address.
   */
  private String address;

  /**
   * Constructor with provided params.
   *
   * @param name    the new company name.
   * @param address the new company address.
   */
  public CompanyUpdateRequestForm(final String name, final String address) {
    super();
    this.name = name;
    this.address = address;
  }

  /**
   * Main empty constructor.
   */
  public CompanyUpdateRequestForm() {
    super();
  }

  /**
   * Get request company name.
   *
   * @return The company name.
   */
  public String getName() {
    return name;
  }

  /**
   * Set request company name.
   *
   * @param value The company name.
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
    return Objects.hash(address, name);
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
    var other = (CompanyUpdateRequestForm) obj;
    return Objects.equals(address, other.address)
          && Objects.equals(name, other.name);
  }

  @Override
  public String toString() {
    return "CompanyUpdateRequestForm [name=" + name + ", address=" + address
          + "]";
  }

}
