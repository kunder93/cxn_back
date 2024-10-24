
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.UserEntity;

import java.time.LocalDate;

/**
 * A record representing the response form used for updating a user's details
 * in the controller.
 * This record encapsulates the essential fields that represent a user in the
 * update operation.
 * <p>
 * The record is immutable and provides an automatic implementation of equals,
 * hashCode,
 * and toString methods. The fields include the user's name, surnames,
 * birth date, and gender.
 * </p>
 *
 * @param name         The user's first name.
 * @param firstSurname The user's first surname.
 * @param secondSurname The user's second surname.
 * @param birthDate    The user's birth date.
 * @param gender       The user's gender.
 *
 * @author Santiago Paz Perez
 */
public record UserUpdateResponseForm(
      String name, String firstSurname, String secondSurname,
      LocalDate birthDate, String gender
) {

  /**
   * Constructs a {@code UserUpdateResponseForm} from a {@link UserEntity}.
   *
   * @param user The {@code UserEntity} from which to create the response form.
   *             The entity's properties are used to initialize the record
   *             fields.
   */
  public UserUpdateResponseForm(final UserEntity user) {
    this(
          user.getName(), user.getFirstSurname(), user.getSecondSurname(),
          user.getBirthDate(), user.getGender()
    );
  }
}
