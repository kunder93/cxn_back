
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;

/**
 * Represents the form used by the controller as a response for requesting one
 * regular transport.
 * <p>
 * This record is a Data Transfer Object (DTO) used for communication between
 * the view and the controller, providing an immutable representation of the
 * regular transport details.
 * </p>
 * <p>
 * Includes a static factory method for creating instances from a
 * {@link PersistentRegularTransportEntity}.
 * </p>
 *
 * @param identifier The regular transport identifier.
 * @param category The regular transport category.
 * @param description The regular transport description.
 * @param invoiceResponse The invoice response associated with the regular
 * transport.
 *
 * @author Santiago Paz.
 */
public record RegularTransportResponse(
      int identifier, String category, String description,
      InvoiceResponse invoiceResponse
) {

  /**
   * Creates a {@link RegularTransportResponse} from a
   * {@link PersistentRegularTransportEntity}.
   * <p>
   * This static factory method initializes a new
   * {@code RegularTransportResponse} using the data from the provided
   * {@code PersistentRegularTransportEntity}.
   * </p>
   *
   * @param entity The {@code PersistentRegularTransportEntity} containing
   * the data.
   * @return A new {@code RegularTransportResponse} with values derived from
   * the entity.
   */
  public static RegularTransportResponse
        fromEntity(final PersistentRegularTransportEntity entity) {
    return new RegularTransportResponse(
          entity.getId(), entity.getCategory(), entity.getDescription(),
          InvoiceResponse.fromEntity(entity.getTransportInvoice())
    );
  }
}
