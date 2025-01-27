package es.org.cxn.backapp.service.dto;

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

/**
 * Represents a Data Transfer Object (DTO) for transferring book data between
 * application layers.
 * <p>
 * This DTO encapsulates core book information along with associated authors,
 * while avoiding direct exposure of persistence entities. It is typically used
 * in API responses or service-layer communication.
 * </p>
 *
 * @param isbn        The International Standard Book Number (ISBN) uniquely
 *                    identifying the book
 * @param title       The title of the book
 * @param description A brief summary describing the book's content
 * @param publishDate The publication date of the book (format as String, e.g.,
 *                    "YYYY-MM-DD")
 * @param language    The language in which the book is written (e.g.,
 *                    "English", "Spanish")
 * @param genre       The literary genre/category of the book (e.g., "Science
 *                    Fiction", "Biography")
 * @param authors     A set of {@link AuthorDataDto} objects representing the
 *                    book's authors
 */
public record BookDataImageDto(String isbn, String title, String description, String publishDate, String language,
        String genre, Set<AuthorDataDto> authors) {

}
