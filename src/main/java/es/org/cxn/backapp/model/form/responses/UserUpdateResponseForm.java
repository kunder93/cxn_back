
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.UserEntity;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response form for Controller update user.
 *
 * @author Santiago Paz Perez.
 *
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public final class UserUpdateResponseForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 3698024843709681859L;

  /**
   * Name field.
   */
  private String name;
  /**
   * First surname field.
   */
  private String firstSurname;
  /**
   * Second surname field.
   */
  private String secondSurname;
  /**
   * Birth date field.
   */
  private LocalDate birthDate;
  /**
   * Gender field.
   */
  private String gender;

  /**
   * Constructor with user entity param.
   *
   * @param user The user entity.
   */
  public UserUpdateResponseForm(final UserEntity user) {
    name = user.getName();
    firstSurname = user.getFirstSurname();
    secondSurname = user.getSecondSurname();
    birthDate = user.getBirthDate();
    gender = user.getGender();
  }

}
