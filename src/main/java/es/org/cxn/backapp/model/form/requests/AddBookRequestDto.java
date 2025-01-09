
package es.org.cxn.backapp.model.form.requests;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for adding a book to the library.
 * <p>
 * This class represents the form used by the controller to receive data for
 * adding a book. It includes Java validation annotations to ensure the
 * controller receives valid and complete data.
 * </p>
 * <p>
 * The DTO includes fields for various book attributes such as ISBN, title,
 * gender, publish year, language, and a list of authors. All fields are
 * required to ensure data integrity and completeness.
 * </p>
 *
 * @param isbn        The International Standard Book Number (ISBN) of the book.
 *                    Must not be {@code null}.
 * @param title       The title of the book. Must not be {@code null} and must
 *                    be between 1 and 100 characters.
 * @param gender      The genre or gender of the book. Can be {@code null} and
 *                    should be between 1 and 30 characters.
 * @param publishYear The year the book was published. Can be {@code null}.
 * @param language    The language in which the book is written. Can be
 *                    {@code null} and should be between 1 and 20 characters.
 * @param authorsList A list of authors associated with the book. Must not be
 *                    {@code null} and must contain at least one author.
 */
public record AddBookRequestDto(@NotNull(message = ValidationMessages.ISBN_NOT_NULL) Long isbn,

        @NotNull(message = ValidationMessages.TITLE_NOT_NULL)
        @Size(min = ValidationConstants.MIN_TITLE_LENGTH, max = ValidationConstants.MAX_TITLE_LENGTH,
                message = ValidationMessages.TITLE_SIZE) String title,

        @Size(min = ValidationConstants.MIN_GENDER_LENGTH, max = ValidationConstants.MAX_GENDER_LENGTH,
                message = ValidationMessages.GENDER_SIZE) String gender,

        LocalDate publishYear,

        @Size(min = ValidationConstants.MIN_LANGUAGE_LENGTH, max = ValidationConstants.MAX_LANGUAGE_LENGTH,
                message = ValidationMessages.LANGUAGE_SIZE) String language,

        @NotNull(message = ValidationMessages.AUTHORS_LIST_NOT_NULL)
        @Size(min = ValidationConstants.MIN_AUTHORS_LIST_SIZE,
                message = ValidationMessages.AUTHORS_LIST_SIZE) List<AuthorRequest> authorsList) {

    /**
     * Constructor for {@code AddBookRequestDto}.
     * <p>
     * This constructor creates a new instance of {@code AddBookRequestDto} with the
     * specified attributes. A defensive copy of the {@code authorsList} is made to
     * ensure immutability and prevent external modification of the internal state.
     * </p>
     *
     * @param isbn        The International Standard Book Number (ISBN) of the book.
     *                    Must not be {@code null}.
     * @param title       The title of the book. Must not be {@code null} and must
     *                    be between {@code ValidationConstants.MIN_TITLE_LENGTH}
     *                    and {@code ValidationConstants.MAX_TITLE_LENGTH}.
     * @param gender      The genre or gender of the book. Can be {@code null} and
     *                    should be between
     *                    {@code ValidationConstants.MIN_GENDER_LENGTH} and
     *                    {@code ValidationConstants.MAX_GENDER_LENGTH}.
     * @param publishYear The year the book was published. Can be {@code null}.
     * @param language    The language in which the book is written. Can be
     *                    {@code null} and should be between
     *                    {@code ValidationConstants.MIN_LANGUAGE_LENGTH} and
     *                    {@code ValidationConstants.MAX_LANGUAGE_LENGTH}.
     * @param authorsList A list of authors associated with the book. Must not be
     *                    {@code null} and must contain at least
     *                    {@code ValidationConstants.MIN_AUTHORS_LIST_SIZE} authors.
     *                    A defensive copy of this list is stored internally to
     *                    ensure immutability.
     * @throws NullPointerException if {@code authorsList} is {@code null}.
     */
    public AddBookRequestDto {
        // Create a defensive copy of the authorsList to avoid storing external mutable
        // references
        authorsList = List.copyOf(authorsList);
    }

    /**
     * Gets the list of authors associated with the book.
     * <p>
     * This method returns an unmodifiable view of the {@code authorsList} to ensure
     * that the internal list cannot be modified externally. Changes to the returned
     * list will throw an {@code UnsupportedOperationException}.
     * </p>
     *
     * @return An unmodifiable list of {@code AuthorRequest} objects representing
     *         the authors of the book.
     */
    @Override
    public List<AuthorRequest> authorsList() {
        // Return an unmodifiable view of the list
        return Collections.unmodifiableList(authorsList);
    }
}
