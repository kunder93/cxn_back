
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;

import java.util.Collection;
import java.util.List;

/**
 * Represents the form used by the controller as a response for requesting all
 * regular transports.
 * <p>
 * This is a DTO meant to facilitate communication between the view and the
 * controller by providing an immutable representation of a list of regular
 * transports.
 * </p>
 *
 * @param regularTransportList A list of {@link RegularTransportResponse}
 * objects representing the regular transports.
 *
 * @author Santiago Paz.
 */
public record RegularTransportListResponse(
      List<RegularTransportResponse> regularTransportList
) {

  /**
   * Creates a {@link RegularTransportListResponse} from a collection of
   * {@link PersistentRegularTransportEntity}.
   * <p>
   * This static factory method initializes a new
   * {@code RegularTransportListResponse} using the data from the provided
   * collection of entities.
   * </p>
   *
   * @param entities A collection of {@code PersistentRegularTransportEntity}
   * objects.
   * @return A new {@code RegularTransportListResponse} containing the list of
   * regular transport responses.
   */
  public static RegularTransportListResponse fromEntities(
        final Collection<PersistentRegularTransportEntity> entities
  ) {
    final var responses =
          entities.stream().map(RegularTransportResponse::fromEntity).toList();
    return new RegularTransportListResponse(responses);
  }
}
