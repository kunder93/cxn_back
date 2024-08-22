
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;

import java.util.Collection;
import java.util.List;

/**
 * Represents the response DTO used by the controller for requesting all
 * payment sheets.
 * <p>
 * This record serves as a Data Transfer Object (DTO) to facilitate
 * communication between the view and the controller.
 * It contains an immutable list of payment sheet responses.
 * </p>
 *
 * @param paymentSheetsList An immutable list of {@link PaymentSheetResponse}
 * representing all stored payment sheets.
 *
 * @author Santiago Paz
 */
public record PaymentSheetListResponse(
      List<PaymentSheetResponse> paymentSheetsList
) {

  /**
   * Creates a {@link PaymentSheetListResponse} from a list of
   * {@link PersistentPaymentSheetEntity}.
   * <p>
   * This static factory method converts a list of persistent payment sheet
   * entities into a {@link PaymentSheetListResponse}.
   * </p>
   *
   * @param entities The list of {@link PersistentPaymentSheetEntity} to
   * be converted.
   * @return A new instance of {@link PaymentSheetListResponse}.
   */
  public static PaymentSheetListResponse
        fromEntities(final Collection<PersistentPaymentSheetEntity> entities) {
    final var responses =
          entities.stream().map(PaymentSheetResponse::fromEntity).toList();
    return new PaymentSheetListResponse(responses);
  }
}
