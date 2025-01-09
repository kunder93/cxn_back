package es.org.cxn.backapp.model.form.responses;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;

/**
 * Represents the form used by the controller as a response for requesting all
 * chess questions.
 * <p>
 * This is a Data Transfer Object (DTO), meant to facilitate communication
 * between the view and the controller.
 * </p>
 *
 * @param chessQuestionList A collection of chess questions.
 *
 * @author Santiago Paz.
 */
public record ChessQuestionsListResponse(Collection<ChessQuestionResponse> chessQuestionList) {

    /**
     * Constructs a ChessQuestionsListResponse with a defensive copy to ensure
     * immutability.
     *
     * @param chessQuestionList The collection of chess question responses.
     */
    public ChessQuestionsListResponse(final Collection<ChessQuestionResponse> chessQuestionList) {
        this.chessQuestionList = chessQuestionList == null ? Collections.emptyList() : List.copyOf(chessQuestionList);
    }

    /**
     * Static factory method to create an instance of ChessQuestionsListResponse.
     *
     * @param value The collection of persistent chess questions.
     * @return A new ChessQuestionsListResponse instance.
     */
    public static ChessQuestionsListResponse from(final Collection<PersistentChessQuestionEntity> value) {
        final Collection<ChessQuestionResponse> responses = value.stream()
                .map(e -> new ChessQuestionResponse(e.getIdentifier(), e.getEmail(), e.getCategory(), e.getTopic(),
                        e.getMessage(), e.getDate(), e.isSeen()))
                .toList();
        return new ChessQuestionsListResponse(responses);
    }

    /**
     * Returns a defensive copy of the chess question list.
     *
     * @return An unmodifiable view of the chess question list.
     */
    @Override
    public Collection<ChessQuestionResponse> chessQuestionList() {
        return List.copyOf(chessQuestionList);
    }
}
