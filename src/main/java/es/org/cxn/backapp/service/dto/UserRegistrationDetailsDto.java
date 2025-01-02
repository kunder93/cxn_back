
package es.org.cxn.backapp.service.dto;

import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing the details required for user
 * registration.
 * <p>
 * This record encapsulates the user details necessary for registration,
 * including personal information, contact details, and address information.
 * </p>
 *
 * @param dni the unique identifier (DNI) of the user
 * @param name the name of the user
 * @param firstSurname the first surname of the user
 * @param secondSurname the second surname of the user
 * @param birthDate the birth date of the user
 * @param gender the gender of the user
 * @param password the password for the user's account
 * @param email the email address of the user
 * @param addressDetails an instance of {@link AddressRegistrationDetailsDto}
 * containing the user's address details
 * @param kindMember the type of membership the user holds, represented by
 * {@link UserType}
 *
 * @author Santi
 */
public record UserRegistrationDetailsDto(
      String dni, String name, String firstSurname, String secondSurname,
      LocalDate birthDate, String gender, String password, String email,
      AddressRegistrationDetailsDto addressDetails, UserType kindMember
) {
}
