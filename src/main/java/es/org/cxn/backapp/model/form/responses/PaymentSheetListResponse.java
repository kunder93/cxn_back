
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Represents the form used by controller as response for requesting all payment
 * sheets.
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
public final class PaymentSheetListResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -2152905244656672886L;

  /**
   * List with all stored payment sheets.
   */
  private List<PaymentSheetResponse> paymentSheetsList = new ArrayList<>();

  /**
   * Constructor with provided parameters values.
   *
   * @param value The payment sheets list.
   */
  public PaymentSheetListResponse(
        final List<PersistentPaymentSheetEntity> value
  ) {
    super();
    value.forEach(
          (PersistentPaymentSheetEntity entity) -> this.paymentSheetsList
                .add(new PaymentSheetResponse(entity))
    );
  }
}
