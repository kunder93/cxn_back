
package es.org.cxn.backapp.controller.entity.member_resources;

import java.util.List;

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

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.model.form.requests.member_resources.AddBookRequestDto;
import es.org.cxn.backapp.model.form.responses.member_resources.BookResponse;
import es.org.cxn.backapp.service.BookService;
import es.org.cxn.backapp.service.dto.BookDataImageDto;
import es.org.cxn.backapp.service.exceptions.BookServiceException;
import jakarta.validation.Valid;

/**
 * Controller for handle request related to library books.
 *
 * @author Santiago Paz.
 *
 */
@RestController
@RequestMapping("/api/resources/book")
public class BookController {

    /**
     * The book service.
     */
    private final BookService bookService;

    /**
     * Constructs a controller with the specified dependencies.
     *
     * @param service books service.
     */
    public BookController(final BookService service) {
        bookService = Objects.requireNonNull(service, "Books service must not be null.");
    }

    /**
     * Add new book.
     *
     * @param bookData  form with data to add book. {@link AddBookRequestDto}.
     * @param imageFile the book image cover file.
     * @return book's data created.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('SECRETARIO')")
    @PostMapping()
    public ResponseEntity<String> addBook(@RequestPart("data")
    @Valid final AddBookRequestDto bookData, @RequestPart(value = "imageFile", required = false)
    /* @ValidImageFile */ final MultipartFile imageFile) {
        try {
            // Call the libraryService to add the book
            bookService.add(bookData, imageFile);
            return new ResponseEntity<>("Created.", HttpStatus.CREATED);
        } catch (BookServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Returns all books with their authors.
     *
     * @return Book list.
     */
    @GetMapping()
    public ResponseEntity<List<BookDataImageDto>> getAllBooks() {
        final var bookList = bookService.getAll();
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }

    /**
     * Get book using isbn number.
     *
     * @param isbn The isbn number,
     * @return Http Ok or Bad Request.
     */
    @GetMapping("/{isbn}")
    public ResponseEntity<BookResponse> getBook(@PathVariable final String isbn) {
        try {
            // Call the libraryService to remove the book
            final var book = bookService.find(isbn);
            return new ResponseEntity<>(new BookResponse(book), HttpStatus.OK);
        } catch (BookServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Endpoint to retrieve the cover image of a book by its ISBN.
     * <p>
     * This method accepts a book's ISBN as a path variable, fetches the cover image
     * for that book from the service layer, and returns the image as a byte array
     * in the HTTP response. If the image cannot be found or an error occurs, it
     * throws a {@link ResponseStatusException} with a
     * {@link HttpStatus#BAD_REQUEST} status.
     * </p>
     *
     * @param isbn The ISBN of the book whose cover image is to be retrieved.
     * @return A {@link ResponseEntity} containing the cover image as a byte array
     *         in the response body with an {@link HttpStatus#OK} status.
     * @throws ResponseStatusException if there is an error fetching the image
     *                                 (e.g., book not found or other service
     *                                 exceptions).
     */
    @GetMapping("/{isbn}/coverImage")
    public ResponseEntity<byte[]> getBookImage(@PathVariable final String isbn) {
        try {
            final var image = bookService.findImage(isbn);
            return new ResponseEntity<>(image, HttpStatus.OK);
        } catch (BookServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Remove a book using isbn number.
     *
     * @param isbn The isbn number,
     * @return Http Ok or Bad Request.
     */
    @DeleteMapping("/{isbn}")
    public ResponseEntity<String> removeBook(@PathVariable final String isbn) {
        try {
            // Call the libraryService to remove the book
            bookService.remove(isbn);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BookServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
