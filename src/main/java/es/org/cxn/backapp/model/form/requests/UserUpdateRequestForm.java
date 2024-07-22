
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the request form used by controller for update user data.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz Perez.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class UserUpdateRequestForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 5587292614430493384L;

  /**
   * User name field.
   */
  @NotEmpty
  private String name;

  /**
   * User first surname field.
   */
  @NotEmpty
  private String firstSurname;

  /**
   * User second surname field.
   */
  @NotEmpty
  private String secondSurname;

  /**
   * User birth date field.
   */
  @NotNull
  @Past
  private LocalDate birthDate;

  /**
   * User gender field.
   */
  @NotEmpty
  private String gender;
}
