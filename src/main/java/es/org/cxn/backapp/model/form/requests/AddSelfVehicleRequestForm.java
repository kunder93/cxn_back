
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as request of add self vehicle to
 * payment sheet.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class AddSelfVehicleRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3112385111515947705L;

  /**
   * The self vehicle places visited.
   */
  private String places;
  /**
   * The total distance traveled.
   */
  private float distance;

  /**
   * Price per kilometer traveled.
   */
  private double kmPrice;

}
