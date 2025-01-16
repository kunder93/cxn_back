package es.org.cxn.backapp.model.form.responses;

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

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import es.org.cxn.backapp.model.BookEntity;

/**
 * Represents the form used by the controller as a response for requesting one
 * book.
 * <p>
 * This is a Data Transfer Object (DTO), designed to facilitate communication
 * between the view and the controller.
 * </p>
 *
 * @param isbn        The book's ISBN.
 * @param title       The book's title.
 * @param gender      The book's genre.
 * @param language    The book's language.
 * @param publishYear The book's publish date.
 * @param authors     The set of book authors.
 *
 * @author Santiago Paz.
 */
public record BookResponse(long isbn, String title, String gender, String language, LocalDate publishYear,
        Set<AuthorResponse> authors) implements Comparable<BookResponse> {

    /**
     * Constructs a {@link BookResponse} from a {@link BookEntity}.
     *
     * @param book The book entity.
     */
    public BookResponse(final BookEntity book) {
        this(book.getIsbn(), book.getTitle(), book.getGender(), book.getLanguage(), book.getPublishYear(),
                // Defensive copy of the mutable set
                Collections
                        .unmodifiableSet(new HashSet<>(book.getAuthors().stream().map(AuthorResponse::new).toList())));
    }

    /**
     * Constructs a {@link BookResponse} with explicit values.
     *
     * @param isbn        The book's ISBN.
     * @param title       The book's title.
     * @param gender      The book's genre.
     * @param language    The book's language.
     * @param publishYear The book's publish year.
     * @param authors     A set of authors for the book.
     */
    public BookResponse(final long isbn, final String title, final String gender, final String language,
            final LocalDate publishYear, final Set<AuthorResponse> authors) {
        // Defensive copy to ensure immutability
        this.isbn = isbn;
        this.title = title;
        this.gender = gender;
        this.language = language;
        this.publishYear = publishYear;
        this.authors = Collections.unmodifiableSet(new HashSet<>(authors));
    }

    /**
     * Compare books using their title.
     */
    @Override
    public int compareTo(final BookResponse otherBook) {
        return this.title().compareTo(otherBook.title());
    }

}
