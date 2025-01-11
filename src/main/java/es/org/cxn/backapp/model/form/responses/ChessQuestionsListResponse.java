package es.org.cxn.backapp.model.form.responses;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
