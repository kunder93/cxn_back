package es.org.cxn.backapp.model.form.responses;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import es.org.cxn.backapp.model.persistence.PersistentCountryEntity;

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
 *                    countries.
 *
 * @author Santiago Paz
 */
public record CountryListResponse(List<CountryResponse> countryList) {

    /**
     * Constructs a CountryListResponse with a defensive copy to ensure
     * immutability.
     *
     * @param countryList The list of country responses.
     */
    public CountryListResponse(final List<CountryResponse> countryList) {
        this.countryList = countryList == null ? Collections.emptyList() : List.copyOf(countryList);
    }

    /**
     * Creates a {@link CountryListResponse} from a collection of
     * {@link PersistentCountryEntity}.
     * <p>
     * This static factory method converts a collection of persistent country
     * entities into a {@link CountryListResponse}. Each entity is mapped to a
     * {@link CountryResponse} object, which is then collected into a list.
     * </p>
     *
     * @param value The collection of {@link PersistentCountryEntity} to be
     *              converted.
     * @return A new instance of {@link CountryListResponse} containing the list of
     *         country responses.
     */
    public static CountryListResponse from(final Collection<PersistentCountryEntity> value) {
        final var countryResponses = value.stream().map(CountryResponse::new).toList();
        return new CountryListResponse(countryResponses);
    }

    /**
     * Returns an unmodifiable view of the country list.
     *
     * @return An immutable list of country responses.
     */
    @Override
    public List<CountryResponse> countryList() {
        // Explicitly return an unmodifiable view to ensure safety
        return List.copyOf(countryList);
    }

}
