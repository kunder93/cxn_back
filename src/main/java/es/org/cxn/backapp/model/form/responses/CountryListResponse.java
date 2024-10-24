
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

import java.util.Collection;
import java.util.List;

/**
 * Represents the response DTO used by the controller for requesting all
 * countries.
 * <p>
 * This record serves as a Data Transfer Object (DTO) to facilitate
 * communication between the view and the controller when requesting a list of
 * all countries. It contains a list of country responses.
 * </p>
 *
 * @param countryList The list of {@link CountryResponse} representing all
 * countries.
 *
 * @author Santiago Paz
 */
public record CountryListResponse(List<CountryResponse> countryList) {

  /**
   * Creates a {@link CountryListResponse} from a collection of
   * {@link PersistentCountryEntity}.
   * <p>
   * This static factory method converts a collection of persistent country
   * entities into a {@link CountryListResponse}.
   * Each entity is mapped to a {@link CountryResponse} object, which is then
   * collected into a list.
   * </p>
   *
   * @param value The collection of {@link PersistentCountryEntity} to be
   * converted.
   * @return A new instance of {@link CountryListResponse} containing the list
   * of country responses.
   */
  public static CountryListResponse
        from(final Collection<PersistentCountryEntity> value) {
    final var countryResponses =
          value.stream().map(CountryResponse::new).toList();
    return new CountryListResponse(countryResponses);
  }
}
