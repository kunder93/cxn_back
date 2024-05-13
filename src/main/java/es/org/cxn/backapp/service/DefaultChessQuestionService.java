/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.service;

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.model.ChessQuestionEntity;
import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;
import es.org.cxn.backapp.repository.ChessQuestionEntityRepository;
import es.org.cxn.backapp.repository.CompanyEntityRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Default implementation of the {@link UserService}.
 *
 * @author Santiago Paz.
 *
 */
@Service
public final class DefaultChessQuestionService
      implements ChessQuestionsService {

  /**
   * Repository for the invoice entities handled by the service.
   */
  private final ChessQuestionEntityRepository chessQuestionRepository;

  /**
   * Constructs an entities service with the specified repositories.
   *
   * @param repo The company repository{@link CompanyEntityRepository}
   */
  public DefaultChessQuestionService(final ChessQuestionEntityRepository repo) {
    super();
    chessQuestionRepository = checkNotNull(
          repo, "Received a null pointer as chess question repository"
    );
  }

  @Override
  public ChessQuestionEntity add(
        final String email, final String category, final String topic,
        final String message
  ) {
    var chessQuestionEntity = PersistentChessQuestionEntity.builder()
          .email(email).category(category).topic(topic).message(message)
          .date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).build();
    return chessQuestionRepository.save(chessQuestionEntity);
  }

  @Override
  public List<PersistentChessQuestionEntity> getAll() {
    return chessQuestionRepository.findAll();
  }

}
