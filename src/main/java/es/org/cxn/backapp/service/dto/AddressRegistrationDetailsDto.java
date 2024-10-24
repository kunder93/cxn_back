
package es.org.cxn.backapp.service.dto;

/**
 * Data Transfer Object (DTO) representing the details of a user's address.
 * <p>
 * This DTO includes fields for the apartment number, building, city, postal
 * code, street, country numeric code, and country subdivision name.
 * It is designed to facilitate the transfer of address-related data within
 * the application.
 * </p>
 *
 * <p>
 * This class is implemented as a record, providing an immutable and concise
 * way to represent data. Each field in the record corresponds to a property
 *  of the user's address.
 * </p>
 *
 * @param apartmentNumber the apartment number associated with the address
 * @param building the building name or number associated with the address
 * @param city the city where the address is located
 * @param postalCode the postal code for the address
 * @param street the street name or number of the address
 * @param countryNumericCode the numeric code representing the country of
 * the address
 * @param countrySubdivisionName the name of the country's
 * subdivision (e.g., state or province).
 *
 * @author Santi
 */

public record AddressRegistrationDetailsDto(
      String apartmentNumber, String building, String city, String postalCode,
      String street, Integer countryNumericCode, String countrySubdivisionName
) {
}
