
package es.org.cxn.backapp.service.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing the user update form used by the
 * service.
 * <p>
 * This record contains the fields necessary for updating user information,
 *  including personal details such as name, surname, birth date, and gender.
 * </p>
 *
 * @param name the name of the user
 * @param firstSurname the first surname of the user
 * @param secondSurname the second surname of the user
 * @param birthDate the birth date of the user
 * @param gender the gender of the user
 *
 * @author Santiago Paz
 */
public record UserServiceUpdateDto(
      String name, String firstSurname, String secondSurname,
      LocalDate birthDate, String gender
) {
}
