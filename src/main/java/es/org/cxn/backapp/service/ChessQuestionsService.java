
package es.org.cxn.backapp.service;

import es.org.cxn.backapp.model.ChessQuestionEntity;
import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;
import es.org.cxn.backapp.service.exceptions.ChessQuestionServiceException;

import java.util.List;

/**
 * Service interface for managing chess questions.
 * <p>
 * This service provides methods for creating, retrieving, updating, and deleting
 * chess questions.
 * It is intended for use by endpoints that need to interact with chess
 * question entities.
 * </p>
 *
 * @author Santiago Paz Perez
 */
public interface ChessQuestionsService {

  /**
   * Creates a new chess question entity.
   *
   * @param email    The email of the user who submitted the question.
   * @param category The category of the chess question.
   * @param topic    The topic of the chess question.
   * @param message  The message content of the chess question.
   * @return The created and persisted chess question entity.
   */
  ChessQuestionEntity
        add(String email, String category, String topic, String message);

  /**
   * Retrieves all chess question entities.
   *
   * @return A list of all chess question entities.
   */
  List<PersistentChessQuestionEntity> getAll();

  /**
   * Updates the "seen" status of a chess question.
   *
   * @param identifier The identifier of the chess question to update.
   * @return The updated chess question entity with the "seen" status changed.
   * @throws ChessQuestionServiceException If the chess question with the given
   * identifier is not found.
   */
  ChessQuestionEntity changeChessQuestionSeen(Integer identifier)
        throws ChessQuestionServiceException;

  /**
   * Deletes a chess question by its identifier.
   *
   * @param identifier The identifier of the chess question to delete.
   * @throws ChessQuestionServiceException If the chess question cannot be
   * deleted or an error occurs during the deletion.
   */
  void delete(int identifier) throws ChessQuestionServiceException;

}
