
package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the form used by controller as response of update company.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * </p>
 *
 * @author Santiago Paz.
 */
public final class CompanyUpdateResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3852186551148263014L;

  /**
   * The company nif.
   */
  private String nif;
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
   * @param nif     the company nif.
   * @param name    the company name.
   * @param address the company address.
   */
  public CompanyUpdateResponse(
        final String nif, final String name, final String address
  ) {
    super();
    this.name = name;
    this.address = address;
    this.nif = nif;
  }

  /**
   * Main empty constructor.
   */
  public CompanyUpdateResponse() {
    super();
  }

  /**
   * Get response company nif.
   *
   * @return The company nif.
   */
  public String getNif() {
    return nif;
  }

  /**
   * Set response company nif.
   *
   * @param value The new company nif.
   */
  public void setCifNif(final String value) {
    this.nif = value;
  }

  /**
   * Get response company name.
   *
   * @return The company name.
   */
  public String getName() {
    return name;
  }

  /**
   * Set response company name.
   *
   * @param value The company name.
   */
  public void setName(final String value) {
    this.name = value;
  }

  /**
   * Get response company address.
   *
   * @return The company address.
   */
  public String getAddress() {
    return address;
  }

  /**
   * Set response company address.
   *
   * @param value The company address.
   */
  public void setAddress(final String value) {
    this.address = value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, nif, name);
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
    var other = (CompanyUpdateResponse) obj;
    return Objects.equals(address, other.address)
          && Objects.equals(nif, other.nif) && Objects.equals(name, other.name);
  }

  @Override
  public String toString() {
    return "CompanyUpdateResponse [nif=" + nif + ", name=" + name + ", address="
          + address + "]";
  }

}
