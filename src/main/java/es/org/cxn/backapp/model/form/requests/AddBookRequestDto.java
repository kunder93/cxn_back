
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as request to add book into library.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class AddBookRequestDto implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3112845649215975705L;

  /**
   * The book isbn.
   */
  @NotNull
  private Long isbn;

  /**
   * The book title.
   */
  @NotNull
  private String title;

  /**
   * The book gender.
   */

  private String gender;

  /**
   * The book publish year.
   */

  private LocalDate publishYear;

  /**
   * The book language.
   */
  private String language;

  /**
   * The book authors list.
   */
  private List<AuthorRequestDto> authorsList;
}
