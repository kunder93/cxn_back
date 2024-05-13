
package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.NonNull;

/**
 * The Chess question entity interface.
 *
 * @author Santi
 *
 */
public interface ChessQuestionEntity extends Serializable {
  /**
   * @return The chess question category.
   */
  @NonNull
  String getCategory();

  /**
   * @return The chess question email.
   */
  @NonNull
  String getEmail();

  /**
   * @return The chess question identifier number.
   */
  @NonNull
  Integer getIdentifier();

  /**
   * @return The chess question main message.
   */
  @NonNull
  String getMessage();

  /**
   * @return The chess question topic.
   */
  @NonNull
  String getTopic();

  /**
   * @return date when chess question was received.
   */
  @NonNull
  LocalDateTime getDate();

  /**
   * @param category The new chess question category.
   */
  void setCategory(@NonNull
  String category);

  /**
   * @param email The new chess question email.
   */
  void setEmail(@NonNull
  String email);

  /**
   * @param identifier The new chess question number identifier.
   */
  void setIdentifier(@NonNull
  Integer identifier);

  /**
   * @param message The new chess question message.
   */
  void setMessage(@NonNull
  String message);

  /**
   * @param topic The new chess question topic.
   */
  void setTopic(@NonNull
  String topic);

  /**
   * @param date The new chess question date.
   */
  void setDate(@NonNull
  LocalDateTime date);
}
