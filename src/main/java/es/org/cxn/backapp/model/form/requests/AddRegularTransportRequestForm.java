
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as request of add regular transport to
 * paymentSheet.
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
public final class AddRegularTransportRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3112385622215947705L;

  /**
   * The regular transport Category.
   */
  private String category;
  /**
   * The regular transport description.
   */
  private String description;

  /**
   * The regular transport invoice if it have.
   */
  private Integer invoiceNumber;

  /**
   * The regular transport invoice series.
   */
  private String invoiceSeries;

}
