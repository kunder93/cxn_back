package es.org.cxn.backapp.model.form.responses;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import es.org.cxn.backapp.model.BookEntity;

/**
 * Represents the form used by the controller as a response for requesting all
 * books.
 * <p>
 * This is a Data Transfer Object (DTO), designed to facilitate communication
 * between the view and the controller.
 * </p>
 *
 * @param bookList An unmodifiable set of book responses.
 */
public record BookListResponse(Set<BookResponse> bookList) {

    /**
     * Constructs a {@link BookListResponse} from a list of {@link BookEntity}.
     *
     * @param bookEntities The list of book entities.
     */
    public BookListResponse(final List<BookEntity> bookEntities) {
        this(
                // Explicitly create a TreeSet
                Collections.unmodifiableSet(new TreeSet<>(bookEntities.stream().map(BookResponse::new).toList())));
    }

    /**
     * Constructs a {@link BookListResponse} with a custom set of book responses.
     *
     * @param bookList The set of book responses.
     */
    public BookListResponse(final Set<BookResponse> bookList) {
        // Defensive copy to ensure immutability
        this.bookList = Collections.unmodifiableSet(new TreeSet<>(bookList));
    }

}
