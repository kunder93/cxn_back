
package es.org.cxn.backapp.model.form.requests;

import es.org.cxn.backapp.model.persistence.PersistentUserEntity.UserType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Represents the form request used to add or remove roles from a user.
 * <p>
 * This is a DTO (Data Transfer Object) used for communication between the
 * view and the controller. The record is immutable and provides built-in
 * implementations for methods such as {@code equals()}, {@code hashCode()},
 * and {@code toString()}.
 * <p>
 * Includes fields for the user's email and their type (kind of member).
 *
 * @param email       The email address of the user.
 * @param kindMember  The type of member (user kind) to be set for the user.
 *
 * @author Santiago Paz Perez.
 */
public record UserChangeKindMemberRequest(@NotEmpty @Email
String email, @NotNull
UserType kindMember) {

}
