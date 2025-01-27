package es.org.cxn.backapp.model.form.responses.member_resources;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import java.util.Set;
import java.util.stream.Collectors;

import es.org.cxn.backapp.model.BookEntity;

/**
 * Represents the response DTO (Data Transfer Object) for a book entity. This
 * record is used to transfer book-related data between the backend and frontend
 * or other services.
 * <p>
 * The {@link BookResponse} includes information about a book, such as its ISBN,
 * title, genre, publication year, language, description, and associated
 * authors.
 * </p>
 *
 * @param isbn        The ISBN number of the book, represented as a string.
 * @param title       The title of the book.
 * @param genre       The genre of the book.
 * @param publishYear The publication year of the book, represented as a string.
 * @param language    The language in which the book is written.
 * @param description The description of the book.
 * @param authors     A set of {@link AuthorResponse} representing the authors
 *                    of the book.
 *
 * @author Santiago Paz
 */
public record BookResponse(String isbn, String title, String genre, String publishYear, String language,
        String description, Set<AuthorResponse> authors) {

    /**
     * Constructs a {@link BookResponse} from a {@link BookEntity}.
     * <p>
     * This constructor maps the properties of the {@link BookEntity} to the
     * corresponding fields of the {@link BookResponse}.
     * </p>
     *
     * @param book The {@link BookEntity} containing the book data.
     */
    public BookResponse(final BookEntity book) {
        this(book.getIsbn().toString(), book.getTitle(), book.getGender(), book.getPublishYear().toString(),
                book.getLanguage(), book.getDescription(),
                book.getAuthors().stream().map(AuthorResponse::new).collect(Collectors.toSet()));
    }
}
