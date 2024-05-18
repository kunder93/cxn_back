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

import es.org.cxn.backapp.exceptions.ChessQuestionServiceException;
import es.org.cxn.backapp.model.ChessQuestionEntity;
import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;

import java.util.List;

/**
 * Service for the Company entity domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago Paz Perez.
 */
public interface ChessQuestionsService {

  /**
   * Creates a new chess questions entity.
   *
   * @param email   The user who made question email.
   * @param category    The chess question category.
   * @param topic The chess question topic.
   * @param message The chess question message.
   * @return The chess question entity persisted.
   *
   */
  ChessQuestionEntity
        add(String email, String category, String topic, String message);

  /**
   * @return List with all chess questions.
   */
  List<PersistentChessQuestionEntity> getAll();

  /**
   * Change chess question seen value.
   *
   * @param id The chess question identifier.
   * @return The chess question entity with senn value changed.
   * @throws ChessQuestionServiceException When chess question not found.
   */
  ChessQuestionEntity changeChessQuestionSeen(Integer id)
        throws ChessQuestionServiceException;

}
