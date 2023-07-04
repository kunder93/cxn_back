
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

  /**
   * @return the payment sheets list.
   */
  public List<PaymentSheetResponse> getPaymentSheetsList() {
    return new ArrayList<>(paymentSheetsList);
  }

  /**
   * @param paymentSheetsList The payment sheets list.
   */
  public void setPaymentSheetsList(
        final List<PaymentSheetResponse> paymentSheetsList
  ) {
    this.paymentSheetsList = new ArrayList<>(paymentSheetsList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(paymentSheetsList);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    var other = (PaymentSheetListResponse) obj;
    return Objects.equals(paymentSheetsList, other.paymentSheetsList);
  }

  @Override
  public String toString() {
    return "PaymentSheetListResponse [paymentSheetsList=" + paymentSheetsList
          + "]";
  }

}
