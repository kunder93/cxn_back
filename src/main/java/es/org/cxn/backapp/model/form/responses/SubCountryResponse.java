
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.CountrySubdivisionEntity;

/**
 * Represents the response form used by the controller for requesting details
 * of a country subdivision.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller by mapping the necessary fields. Each field in this
 * record corresponds to a piece of information about the country subdivision.
 * <p>
 * This record provides a concise and immutable way to model data, ensuring that
 * the data remains consistent and is not modified unintentionally.
 *
 * @param name                  the name of the country subdivision.
 * @param kindSubdivisionName   the kind of subdivision.
 * @param code                  the code of the subdivision.
 *
 * @author Santiago Paz.
 */
public record SubCountryResponse(
      String name, String kindSubdivisionName, String code
) {

  /**
   * Creates a {@code SubCountryResponse} instance from a
   * {@code PersistentCountrySubdivisionEntity}.
   * <p>
   * This static factory method is used to convert a
   * {@code PersistentCountrySubdivisionEntity} into a
   * {@code SubCountryResponse}. It extracts relevant fields from the entity
   * to populate the fields of the response record.
   *
   * @param countrySubdivision the {@code PersistentCountrySubdivisionEntity}
   * from which to create the {@code SubCountryResponse}.
   * @return a new {@code SubCountryResponse} containing data from the
   * provided entity.
   */
  public static SubCountryResponse
        fromEntity(final CountrySubdivisionEntity countrySubdivision) {
    return new SubCountryResponse(
          countrySubdivision.getName(),
          countrySubdivision.getKindSubdivisionName(),
          countrySubdivision.getCode()
    );
  }
}
