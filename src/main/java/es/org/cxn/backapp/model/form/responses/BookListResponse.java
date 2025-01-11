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
