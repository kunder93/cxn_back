
package es.org.cxn.backapp.service.dto;

import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Define a container class to hold user details
/**
 * DTO with user registration fields.
 *
 * @author Santi
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserRegistrationDetails {
  /**
   * The user dni.
   */
  private String dni;
  /**
   * The user name.
   */
  private String name;
  /**
   * The user first surname.
   */
  private String firstSurname;
  /**
   * The user second surname.
   */
  private String secondSurname;
  /**
   * The user birth date.
   */
  private LocalDate birthDate;
  /**
   * The user gender.
   */
  private String gender;
  /**
   * The user password.
   */
  private String password;
  /**
   * The user email.
   */
  private String email;
  /**
   * Address details object that contains address user fields.
   */
  private AddressRegistrationDetails addressDetails;
  /**
   * The user kind of member.
   */
  private UserType kindMember;

}
