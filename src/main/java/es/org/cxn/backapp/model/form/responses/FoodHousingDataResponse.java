
package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as request of Food Housing data.
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
public final class FoodHousingDataResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3110452922215007099L;

  /**
   * The food housing amount of days that last.
   */
  private Integer amountDays;
  /**
   * The food housing price per day.
   */
  private double dayPrice;

  /**
   * True if sleeps else false.
   */
  private boolean overnight;

}
