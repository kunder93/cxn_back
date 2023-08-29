
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentCompanyEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Represents the form used by controller as response for requesting all
 * invoices.
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
public final class CompanyListResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -6152905276653572886L;

  /**
   * List with all stored companies.
   */
  private List<CompanyResponse> companiesList = new ArrayList<>();

  /**
   * Constructor with provided parameters values.
   *
   * @param value The companies list.
   */
  public CompanyListResponse(final List<PersistentCompanyEntity> value) {
    super();
    value.forEach(
          (PersistentCompanyEntity e) -> this.companiesList
                .add(new CompanyResponse(e))
    );
  }
}
