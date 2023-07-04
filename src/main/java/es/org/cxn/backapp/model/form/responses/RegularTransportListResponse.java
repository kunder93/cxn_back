
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public final class RegularTransportListResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -6122005225553512886L;

  /**
   * List with all individual invoices responses.
   */
  private List<RegularTransportResponse> regularTransportList =
        new ArrayList<>();

  /**
   * Response constructor with parameters provided.
   *
   * @param value the invoices entity list.
   */
  public RegularTransportListResponse(
        final List<PersistentRegularTransportEntity> value
  ) {
    super();
    value.forEach(
          (PersistentRegularTransportEntity e) -> this.regularTransportList
                .add(new RegularTransportResponse(e))
    );
  }

  /**
   * Empty constructor.
   */
  public RegularTransportListResponse() {
    super();
  }

  /**
   * @return The regular transport list.
   */
  public List<RegularTransportResponse> getRegularTransportList() {
    return new ArrayList<>(regularTransportList);
  }

  /**
   * @param regularTransportList The regular transport list.
   */
  public void setRegularTransportList(
        final List<RegularTransportResponse> regularTransportList
  ) {
    this.regularTransportList = new ArrayList<>(regularTransportList);
  }

}
