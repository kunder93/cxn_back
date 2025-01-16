
package es.org.cxn.backapp.controller.entity;

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

import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;
import es.org.cxn.backapp.model.form.responses.AuthorListResponse;
import es.org.cxn.backapp.model.form.responses.BookListResponse;
import es.org.cxn.backapp.model.form.responses.BookResponse;
import es.org.cxn.backapp.service.LibraryService;
import es.org.cxn.backapp.service.exceptions.LibraryServiceException;
import jakarta.validation.Valid;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for handle request related to library.
 *
 * @author Santiago Paz.
 *
 */
@RestController
@RequestMapping("/api/library")
public class LibraryController {

  /**
   * The library service.
   */
  private final LibraryService libraryService;

  /**
   * Constructs a controller with the specified dependencies.
   *
   * @param service library service.
   */
  public LibraryController(final LibraryService service) {
    super();
    libraryService =
          Objects.requireNonNull(service, "Library service must not be null.");
  }

  /**
   * Returns all books with their authors.
   *
   * @return Book list.
   */
  @GetMapping()
  @CrossOrigin
  public ResponseEntity<BookListResponse> getAllBooks() {
    final var bookList = libraryService.getAllBooks();
    final var bookListResponse = new BookListResponse(bookList);
    return new ResponseEntity<>(bookListResponse, HttpStatus.OK);
  }

  /**
   * Add new book.
   *
   * @param bookRequest form with data to add book.
   *                                 {@link AddBookRequestDto}.
   * @return book's data created.
   */
  @PostMapping()
  @CrossOrigin
  public ResponseEntity<BookResponse> addBook(@RequestBody @Valid
  final AddBookRequestDto bookRequest) {
    try {
      // Call the libraryService to add the book
      final var bookAdded = libraryService.addBook(bookRequest);
      final var response = new BookResponse(bookAdded);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (LibraryServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }
  }

  /**
   * Remove a book using isbn number.
   *
   * @param isbn The isbn number,
   * @return Http Ok or Bad Request.
   */
  @DeleteMapping("/{isbn}")
  @CrossOrigin
  public ResponseEntity<String> removeBook(@PathVariable
  final Long isbn) {
    try {
      // Call the libraryService to remove the book
      libraryService.removeBookByIsbn(isbn);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (LibraryServiceException e) {
      throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, e.getMessage(), e
      );
    }
  }

  /**
   * Returns all authors.
   *
   * @return The authors list wrapped in a ResponseEntity.
   */
  @GetMapping("/authors")
  @CrossOrigin
  public ResponseEntity<AuthorListResponse> getAllAuthors() {
    final var authorList = libraryService.getAllAuthors();
    final var authorListResponse = AuthorListResponse.from(authorList);
    return new ResponseEntity<>(authorListResponse, HttpStatus.OK);
  }
}
