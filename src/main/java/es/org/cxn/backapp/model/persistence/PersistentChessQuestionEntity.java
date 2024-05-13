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

package es.org.cxn.backapp.model.persistence;

import es.org.cxn.backapp.model.ChessQuestionEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Self vehicle entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "ChessQuestion")
@Table(name = "chess_questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersistentChessQuestionEntity implements ChessQuestionEntity {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = 2897644409670798725L;

  /**
   * Entity's identifier.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, unique = true)
  private Integer identifier;

  /**
   * Chess question topic.
   *
   */
  @Column(name = "topic", nullable = false, unique = false)
  @NonNull
  private String topic;

  /**
   * Chess question message.
   *
   */
  @Column(name = "message", nullable = false, unique = false)
  @NonNull
  private String message;

  /**
   * Chess question category.
   *
   */
  @Column(name = "category", nullable = false, unique = false)
  @NonNull
  private String category;

  /**
   * Chess question email.
   *
   */
  @Column(name = "email", nullable = false, unique = false)
  @NonNull
  private String email;

  /**
   * Chess question date.
   *
   */
  @Column(name = "date", nullable = false, unique = false)
  @NonNull
  private LocalDateTime date;

}
