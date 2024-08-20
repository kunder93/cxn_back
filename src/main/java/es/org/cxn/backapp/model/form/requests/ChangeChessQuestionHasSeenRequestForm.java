
package es.org.cxn.backapp.model.form.requests;

/**
 * Represents the form used by the controller as a request to update the "seen"
 * status of a chess question.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller, capturing the necessary information required to change
 * the "seen" status of a specific chess question.
 * </p>
 *
 * <p>
 * The {@code ChangeChessQuestionHasSeenRequestForm} includes an identifier
 * field for the chess question, which is used to update its "seen" status in
 * the system.
 * </p>
 *
 * @param id The unique identifier of the chess question whose "seen" status
 * is to be updated.This field is mandatory and must be a valid integer
 * representing an existing chess question.
 *
 * <p>
 * Includes Java validation annotations for ensuring that the necessary data
 * is provided.
 * </p>
 *
 * @author Santiago Paz.
 */
public record ChangeChessQuestionHasSeenRequestForm(int id) {

}
