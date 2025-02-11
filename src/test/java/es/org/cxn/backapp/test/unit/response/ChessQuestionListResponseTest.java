
package es.org.cxn.backapp.test.unit.response;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.form.responses.ChessQuestionResponse;
import es.org.cxn.backapp.model.form.responses.ChessQuestionsListResponse;
import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;

class ChessQuestionListResponseTest {

    @Test
    void testConstructorWithNullCollection() {
        // When
        ChessQuestionsListResponse response = new ChessQuestionsListResponse(null);

        // Then
        assertEquals(Collections.emptyList(), response.chessQuestionList());
    }

    @Test
    void testConstructorWithValidCollection() {
        // Given
        ChessQuestionResponse question1 = new ChessQuestionResponse(1, "user1@example.com", "Tactics", "Forks",
                "How to spot forks?", LocalDateTime.now(), true);
        ChessQuestionResponse question2 = new ChessQuestionResponse(2, "user2@example.com", "Strategy",
                "Pawn Structure", "Best pawn structures?", LocalDateTime.now(), false);

        Collection<ChessQuestionResponse> chessQuestions = List.of(question1, question2);

        // When
        ChessQuestionsListResponse response = new ChessQuestionsListResponse(chessQuestions);

        // Then
        assertEquals(2, response.chessQuestionList().size());
        assertEquals(List.copyOf(chessQuestions), response.chessQuestionList());
    }

    @Test
    void testDefensiveCopy() {
        // Given
        ChessQuestionResponse question = new ChessQuestionResponse(1, "user@example.com", "Tactics", "Pins",
                "What is a pin?", LocalDateTime.now(), false);
        Collection<ChessQuestionResponse> chessQuestions = List.of(question);

        // When
        ChessQuestionsListResponse response = new ChessQuestionsListResponse(chessQuestions);

        // Attempt to modify the returned list
        assertThrows(UnsupportedOperationException.class, () -> {
            response.chessQuestionList().add(question);
        });
    }

    @Test
    void testStaticFactoryMethodWithEmptyEntities() {
        // When
        ChessQuestionsListResponse response = ChessQuestionsListResponse.from(Collections.emptyList());

        // Then
        assertEquals(0, response.chessQuestionList().size());
    }

    @Test
    void testStaticFactoryMethodWithValidEntities() {
        // Given
        PersistentChessQuestionEntity entity1 = new PersistentChessQuestionEntity(1, "Tactics", "Forks",
                "How to spot forks?", "user1@example.com", LocalDateTime.now(), true);
        PersistentChessQuestionEntity entity2 = new PersistentChessQuestionEntity(2, "Strategy", "Pawn Structure",
                "Best pawn structures?", "user2@example.com", LocalDateTime.now(), false);

        Collection<PersistentChessQuestionEntity> entities = List.of(entity1, entity2);

        // When
        ChessQuestionsListResponse response = ChessQuestionsListResponse.from(entities);

        // Then
        assertEquals(2, response.chessQuestionList().size());
        assertEquals("user1@example.com", response.chessQuestionList().iterator().next().email());
    }
}
