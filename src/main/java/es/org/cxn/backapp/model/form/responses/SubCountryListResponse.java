
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.CountryEntity;

import java.util.List;

/**
 * Represents the response form used by the controller for requesting a list
 * of subcountries.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller by mapping a list of country subdivisions.
 * Each subdivision is represented by a {@link SubCountryResponse}.
 * <p>
 * This record provides an immutable representation of the list of subcountries,
 * ensuring that the data remains consistent.
 *
 * @param subCountryList a list of {@link SubCountryResponse} representing the
 * subdivisions of the country.
 *
 * @author Santiago Paz.
 */
public record SubCountryListResponse(List<SubCountryResponse> subCountryList) {

  /**
   * Creates a {@code SubCountryListResponse} from a {@code CountryEntity}.
   * <p>
   * This static factory method converts a {@code CountryEntity} into a
   * {@code SubCountryListResponse}.
   * It maps the subdivisions of the country entity into a list of
   * {@code SubCountryResponse}.
   *
   * @param country the {@code CountryEntity} containing subdivisions to be
   * converted.
   * @return a new {@code SubCountryListResponse} containing the list of
   * subcountry responses.
   */
  public static SubCountryListResponse fromEntity(final CountryEntity country) {
    final var subCountryResponses = country.getSubdivisions().stream()
          .map(SubCountryResponse::fromEntity).toList();

    return new SubCountryListResponse(subCountryResponses);
  }
}
