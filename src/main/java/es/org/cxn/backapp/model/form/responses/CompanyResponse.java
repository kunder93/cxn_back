
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;

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
 * @param nif The company nif.
 * @param name The company name.
 * @param address The company address.
 * @author Santiago Paz.
 */
public record CompanyResponse(String nif, String name, String address)
      implements Comparable<CompanyResponse> {

  /**
   * Constructs a {@link CompanyResponse} from a
   * {@link PersistentCompanyEntity}.
   *
   * @param company The company entity to get values from.
   */
  public CompanyResponse(final PersistentCompanyEntity company) {
    this(company.getNif(), company.getName(), company.getAddress());
  }

  @Override
  public int compareTo(CompanyResponse other) {
    // Example: Compare by company name
    return this.name.compareTo(other.name);
  }
}
