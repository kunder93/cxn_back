
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as request of add food or housing to
 * existing payment sheet.
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
public final class AddFoodHousingToPaymentSheetRequestForm
      implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3100089900015947999L;

  /**
   * The amount of days that last the event.
   */
  private Integer amountDays;
  /**
   * Price for each day.
   */
  private float dayPrice;

  /**
   * True if sleep is included false if not.
   */
  private Boolean overnight;
}
