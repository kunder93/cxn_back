
package es.org.cxn.backapp.model;

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

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.NonNull;

/**
 * The ChessQuestionEntity interface represents a chess-related inquiry with
 * fields like category, email, message, and other metadata associated with
 * the question.
 *
 * Each chess question includes a unique identifier, a topic, and a status flag
 * indicating whether it has been seen.
 *
 * @author Santi
 */
public interface ChessQuestionEntity extends Serializable {

  /**
   * Retrieves the category of the chess question.
   *
   * @return The category of the chess question, never null.
   */
  @NonNull
  String getCategory();

  /**
   * Retrieves the email associated with the chess question.
   *
   * @return The email of the person asking the question, never null.
   */
  @NonNull
  String getEmail();

  /**
   * Retrieves the unique identifier for the chess question.
   *
   * @return The identifier number for the chess question, never null.
   */
  @NonNull
  Integer getIdentifier();

  /**
   * Retrieves the main message or content of the chess question.
   *
   * @return The main message of the chess question, never null.
   */
  @NonNull
  String getMessage();

  /**
   * Retrieves the topic of the chess question.
   *
   * @return The topic of the chess question, never null.
   */
  @NonNull
  String getTopic();

  /**
   * Indicates whether the chess question has been marked as seen.
   *
   * @return {@code true} if the chess question has been seen, {@code false}
   * otherwise.
   */
  boolean isSeen();

  /**
   * Retrieves the date and time when the chess question was received.
   *
   * @return The date when the chess question was received, never null.
   */
  @NonNull
  LocalDateTime getDate();

  /**
   * Sets a new identifier for the chess question.
   *
   * @param identifier The new identifier for the chess question, never null.
   */
  void setIdentifier(@NonNull
  Integer identifier);

  /**
   * Sets a new category for the chess question.
   *
   * @param category The new category for the chess question, never null.
   */
  void setCategory(@NonNull
  String category);

  /**
   * Sets a new email for the chess question.
   *
   * @param email The new email associated with the chess question, never null.
   */
  void setEmail(@NonNull
  String email);

  /**
   * Sets a new message for the chess question.
   *
   * @param message The new main message for the chess question, never null.
   */
  void setMessage(@NonNull
  String message);

  /**
   * Sets a new topic for the chess question.
   *
   * @param topic The new topic for the chess question, never null.
   */
  void setTopic(@NonNull
  String topic);

  /**
   * Sets a new date for when the chess question was received.
   *
   * @param date The new date for the chess question, never null.
   */
  void setDate(@NonNull
  LocalDateTime date);

  /**
   * Sets the seen status of the chess question.
   *
   * @param value {@code true} if the chess question has been seen,
   * {@code false} otherwise.
   */
  void setSeen(boolean value);
}
