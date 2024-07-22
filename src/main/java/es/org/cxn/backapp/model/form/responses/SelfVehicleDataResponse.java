
package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as request of create company.
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
public final class SelfVehicleDataResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3110389922215947999L;

  /**
   * The regular transport Category.
   */
  private String places;
  /**
   * The regular transport description.
   */
  private float distance;

  /**
   * The regular transport price per kilometer.
   */
  private double kmPrice;

}
