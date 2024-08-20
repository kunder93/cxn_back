
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents the form used by controller as request to add book into library.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @param isbn          The book ISBN.
 * @param title         The book title.
 * @param gender        The book gender.
 * @param publishYear   The book publish year.
 * @param language      The book language.
 * @param authorsList   The book authors list.
 *
 * @author Santiago Paz.
 */
public record AddBookRequestDto(@NotNull
Long isbn, @NotNull
String title, String gender, LocalDate publishYear, String language,
      List<AuthorRequestDto> authorsList
) {

}
