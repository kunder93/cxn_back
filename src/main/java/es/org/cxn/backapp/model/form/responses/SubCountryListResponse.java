
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;
import es.org.cxn.backapp.model.persistence.PersistentCountrySubdivisionEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

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
public final class SubCountryListResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3133052899993337705L;

  /**
   * List with subcountries.
   */
  private List<SubCountryResponse> subCountryList = new ArrayList<>();

  /**
   * Constructs subcountry list response.
   *
   * @param country The country entity.
   */
  public SubCountryListResponse(final PersistentCountryEntity country) {
    super();
    if (!country.getSubdivisions().isEmpty()) {
      country.getSubdivisions().forEach(
            (PersistentCountrySubdivisionEntity cs) -> subCountryList
                  .add(new SubCountryResponse(cs))
      );
    }
  }
}
