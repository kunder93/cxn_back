
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;

import java.io.Serializable;
import java.util.Objects;

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
public final class CompanyResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3133089826013337705L;

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
   * Constructor with provided Company entity.
   *
   * @param company the company entity for get values.
   */
  public CompanyResponse(final PersistentCompanyEntity company) {
    super();
    nif = company.getNif();
    name = company.getName();
    address = company.getAddress();
  }

  /**
   * Constructor with provided parameters values.
   *
   * @param nif     The nif.
   * @param name    The name.
   * @param address The address.
   */
  public CompanyResponse(
        final String nif, final String name, final String address
  ) {
    super();
    this.nif = nif;
    this.name = name;
    this.address = address;
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
   * @param value The company nif.
   */
  public void setNif(final String value) {
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
   * Get the response company address.
   *
   * @return The company address.
   */
  public String getAddress() {
    return address;
  }

  /**
   * Set the response company address.
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
    var other = (CompanyResponse) obj;
    return Objects.equals(address, other.address)
          && Objects.equals(name, other.name) && Objects.equals(nif, other.nif);
  }

  @Override
  public String toString() {
    return "CompanyResponse [nif=" + nif + ", name=" + name + ", address="
          + address + "]";
  }

}
