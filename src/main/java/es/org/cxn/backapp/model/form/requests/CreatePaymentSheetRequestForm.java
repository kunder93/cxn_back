
package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as request of create payment sheet.
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
public final class CreatePaymentSheetRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3313389555214347185L;

  /**
   * The email of user who owns payment sheet.
   */
  private String userEmail;

  /**
   * The payment sheet event reason.
   */
  private String reason;

  /**
   * The payment sheet event place.
   */
  private String place;

  /**
   * The payment sheet event start date.
   */
  private LocalDate startDate;

  /**
   * The payment sheet event end date.
   */
  private LocalDate endDate;

}
