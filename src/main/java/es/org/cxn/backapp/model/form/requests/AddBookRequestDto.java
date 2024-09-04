
package es.org.cxn.backapp.model.form.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) for adding a book to the library.
 * <p>
 * This class represents the form used by the controller to receive data for
 * adding a book.
 * It includes Java validation annotations to ensure the controller receives
 * valid and complete data.
 * </p>
 * <p>
 * The DTO includes fields for various book attributes such as ISBN, title,
 * gender, publish year, language, and a list of authors.
 * All fields are required to ensure data integrity and completeness.
 * </p>
 *
 * @param isbn          The International Standard Book Number (ISBN) of the
 * book. Must not be {@code null}.
 * @param title         The title of the book. Must not be {@code null} and
 * must be between 1 and 100 characters.
 * @param gender        The genre or gender of the book. Can be {@code null}
 * and should be between 1 and 30 characters.
 * @param publishYear   The year the book was published. Can be {@code null}.
 * @param language      The language in which the book is written. Can be
 * {@code null} and should be between 1 and 20 characters.
 * @param authorsList   A list of authors associated with the book. Must not be
 * {@code null} and must contain at least one author.
 */
public record AddBookRequestDto(
      @NotNull(message = ValidationMessages.ISBN_NOT_NULL)
      Long isbn,

      @NotNull(message = ValidationMessages.TITLE_NOT_NULL) @Size(
            min = ValidationConstants.MIN_TITLE_LENGTH,
            max = ValidationConstants.MAX_TITLE_LENGTH,
            message = ValidationMessages.TITLE_SIZE
      )
      String title,

      @Size(
            min = ValidationConstants.MIN_GENDER_LENGTH,
            max = ValidationConstants.MAX_GENDER_LENGTH,
            message = ValidationMessages.GENDER_SIZE
      )
      String gender,

      LocalDate publishYear,

      @Size(
            min = ValidationConstants.MIN_LANGUAGE_LENGTH,
            max = ValidationConstants.MAX_LANGUAGE_LENGTH,
            message = ValidationMessages.LANGUAGE_SIZE
      )
      String language,

      @NotNull(message = ValidationMessages.AUTHORS_LIST_NOT_NULL) @Size(
            min = ValidationConstants.MIN_AUTHORS_LIST_SIZE,
            message = ValidationMessages.AUTHORS_LIST_SIZE
      )
      List<AuthorRequest> authorsList
) {

}
