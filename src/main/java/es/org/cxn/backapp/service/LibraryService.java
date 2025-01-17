
package es.org.cxn.backapp.service;

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

import es.org.cxn.backapp.model.AuthorEntity;
import es.org.cxn.backapp.model.BookEntity;
import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;
import es.org.cxn.backapp.service.exceptions.LibraryServiceException;

import java.util.List;

/**
 * Interface for the service that handles library operations such as managing
 * books and authors.
 *
 * @author Santiago Paz.
 */
public interface LibraryService {

  /**
   * Retrieves a list of all books in the library.
   *
   * @return A list of all {@link BookEntity} objects representing the books
   * in the library.
   */
  List<BookEntity> getAllBooks();

  /**
   * Finds a book using its ISBN number.
   *
   * @param isbn The ISBN of the book to find.
   * @return The {@link BookEntity} corresponding to the provided ISBN.
   * @throws LibraryServiceException If the book cannot be found.
   */
  BookEntity findByIsbn(long isbn) throws LibraryServiceException;

  /**
   * Removes a book from the library using its ISBN number.
   *
   * @param val The ISBN of the book to be removed.
   * @throws LibraryServiceException If the book cannot be found or removed.
   */
  void removeBookByIsbn(long val) throws LibraryServiceException;

  /**
   * Adds a new book to the library based on the provided request data.
   *
   * @param bookRequest The {@link AddBookRequestDto} containing the new book's
   * data.
   * @return The newly created {@link BookEntity}.
   * @throws LibraryServiceException If the book cannot be added.
   */
  BookEntity addBook(AddBookRequestDto bookRequest)
        throws LibraryServiceException;

  /**
   * Retrieves a list of all authors in the library.
   *
   * @return A list of all {@link AuthorEntity} objects representing the
   * authors.
   */
  List<AuthorEntity> getAllAuthors();

}
