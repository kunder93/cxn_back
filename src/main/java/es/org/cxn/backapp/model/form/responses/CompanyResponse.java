
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
