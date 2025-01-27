
package es.org.cxn.backapp.service;

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

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.BookEntity;
import es.org.cxn.backapp.model.form.requests.member_resources.AddBookRequestDto;
import es.org.cxn.backapp.service.dto.BookDataImageDto;
import es.org.cxn.backapp.service.exceptions.BookServiceException;

/**
 * Interface for the service that handles books operations.
 *
 * @author Santiago Paz.
 */
public interface BookService {

    /**
     * Adds a new book.
     *
     * @param bookRequest The {@link AddBookRequestDto} containing the new book's
     *                    data.
     * @param imageCover  The book image cover file.
     * @return The newly created {@link BookEntity}.
     * @throws BookServiceException If the book cannot be added.
     */
    BookEntity add(AddBookRequestDto bookRequest, MultipartFile imageCover) throws BookServiceException;

    /**
     * Finds a book using its ISBN number.
     *
     * @param isbn The ISBN of the book to find.
     * @return The {@link BookEntity} corresponding to the provided ISBN.
     * @throws BookServiceException If the book cannot be found.
     */
    BookEntity find(String isbn) throws BookServiceException;

    /**
     * Find book's image.
     *
     * @param isbn The book isbn aka identifier.
     * @return The book image that matched provided isbn.
     * @throws BookServiceException When book image cannot be loaded.
     */
    byte[] findImage(String isbn) throws BookServiceException;

    /**
     * Retrieves a list of all books.
     *
     * @return A list of all {@link BookDataImageDto} objects representing the books
     *         in the library.
     */
    List<BookDataImageDto> getAll();

    /**
     * Removes a book using its ISBN number.
     *
     * @param val The ISBN of the book to be removed.
     * @throws BookServiceException If the book cannot be found or removed.
     */
    void remove(String val) throws BookServiceException;

}
