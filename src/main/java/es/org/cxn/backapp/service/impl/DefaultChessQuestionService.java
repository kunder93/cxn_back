
package es.org.cxn.backapp.service.impl;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import es.org.cxn.backapp.model.ChessQuestionEntity;
import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;
import es.org.cxn.backapp.repository.ChessQuestionEntityRepository;
import es.org.cxn.backapp.service.ChessQuestionsService;
import es.org.cxn.backapp.service.exceptions.ChessQuestionServiceException;

/**
 * Default implementation of the {@link es.org.cxn.backapp.service.UserService}.
 *
 * @author Santiago Paz.
 *
 */
@Service
public final class DefaultChessQuestionService implements ChessQuestionsService {

    /**
     * Chess question not found exception message.
     */
    public static final String QUESTION_NOT_FOUND = "Question not found.";

    /**
     * Repository for the invoice entities handled by the service.
     */
    private final ChessQuestionEntityRepository chessQuestionRepository;

    /**
     * Constructs an entities service with the specified repositories.
     *
     * @param repo The chess questions repository.
     *             {@link ChessQuestionEntityRepository}
     */
    public DefaultChessQuestionService(final ChessQuestionEntityRepository repo) {
        super();
        chessQuestionRepository = Objects.requireNonNull(repo, "Received a null pointer as chess question repository");
    }

    @Override
    public ChessQuestionEntity add(final String email, final String category, final String topic,
            final String message) {
        final var chessQuestionEntity = PersistentChessQuestionEntity.builder().email(email).category(category)
                .topic(topic).message(message).seen(false).date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        return chessQuestionRepository.save(chessQuestionEntity);
    }

    @Override
    public ChessQuestionEntity changeChessQuestionSeen(final Integer identifier) throws ChessQuestionServiceException {
        final var question = chessQuestionRepository.findById(identifier);
        if (question.isPresent()) {
            final var entity = question.get();
            entity.setSeen(!entity.isSeen());
            return chessQuestionRepository.save(entity);
        } else {
            throw new ChessQuestionServiceException(QUESTION_NOT_FOUND);
        }
    }

    @Override
    public void delete(final int identifier) throws ChessQuestionServiceException {
        final var optionalQuestion = chessQuestionRepository.findById(identifier);
        if (optionalQuestion.isPresent()) {
            chessQuestionRepository.delete(optionalQuestion.get());
        } else {
            throw new ChessQuestionServiceException(QUESTION_NOT_FOUND);
        }
    }

    @Override
    public List<PersistentChessQuestionEntity> getAll() {
        return chessQuestionRepository.findAll();
    }

}
