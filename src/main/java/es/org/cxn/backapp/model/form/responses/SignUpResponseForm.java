
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as response for the creating user.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
@NoArgsConstructor
@Data
public final class SignUpResponseForm implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3214940499061435783L;

  /**
   * Dni field.
   */
  private String dni;

  /**
   * Name field.
   */
  private String name;

  /**
   * User first surname field.
   */
  private String firstSurname;

  /**
   * User second surname field.
   */
  private String secondSurname;

  /**
   * User birth date field.
   */
  private LocalDate birthDate;

  /**
   * User gender field.
   */
  private String gender;

  /**
   * User email field.
   */
  private String email;

  /**
   * User kind of member.
   */
  private UserType kindMember;

  /**
   * User roles set field.
   */
  private Set<String> userRoles = new HashSet<>();

  /**
   * @param user User entity with data for construct DTO.
   */
  public SignUpResponseForm(final UserEntity user) {
    dni = user.getDni();
    name = user.getName();
    firstSurname = user.getFirstSurname();
    secondSurname = user.getSecondSurname();
    birthDate = user.getBirthDate();
    gender = user.getGender();
    email = user.getEmail();
    kindMember = user.getKindMember();
    final var userEntityRoles = user.getRoles();
    userEntityRoles.forEach(role -> this.userRoles.add(role.getName()));
  }

  /**
   * Returns the value of the userRoles field.
   *
   * @return the value of the userRoles field.
   */
  public Set<String> getUserRoles() {
    return new HashSet<>(userRoles);
  }

  /**
   * Sets the value of the userRoles field.
   *
   * @param value the new value for the userRoles field.
   */
  public void setUserRoles(final Iterable<PersistentRoleEntity> value) {
    value.forEach(role -> this.userRoles.add(role.getName()));
  }

}
