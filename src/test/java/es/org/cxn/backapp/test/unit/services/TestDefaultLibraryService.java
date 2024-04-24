/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package es.org.cxn.backapp.test.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.org.cxn.backapp.exceptions.LibraryServiceException;
import es.org.cxn.backapp.model.form.requests.AddBookRequestDto;
import es.org.cxn.backapp.model.persistence.PersistentBookEntity;
import es.org.cxn.backapp.repository.AuthorEntityRepository;
import es.org.cxn.backapp.repository.BookEntityRepository;
import es.org.cxn.backapp.service.DefaultLibraryService;
import es.org.cxn.backapp.service.LibraryService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Unit tests for {@link DefaultLibraryService}.
 * <p>
 * These tests verify that the bean applies the correct Java validation
 * annotations.
 *
 * @author Santiago Paz
 */
@SpringBootTest(
      classes = { BookEntityRepository.class, LibraryService.class,
          DefaultLibraryService.class }
)
final class TestDefaultLibraryService {

  @Autowired
  private LibraryService libraryService;

  @MockBean
  private BookEntityRepository libraryRepository;

  @MockBean
  private AuthorEntityRepository authorRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindAllBooks() {
    // Arrange
    var bookBuilder = PersistentBookEntity.builder();
    bookBuilder.isbn(235235234L).title("Book 1");
    var book1 = bookBuilder.build();
    bookBuilder.isbn(42341232134L).title("Book 2");
    var book2 = bookBuilder.build();
    bookBuilder.isbn(41523523352L).title("Book 3");
    var book3 = bookBuilder.build();

    List<PersistentBookEntity> expectedBooks =
          Arrays.asList(book1, book2, book3);

    // Mock the repository to return the expected books when findAll() is called
    when(libraryRepository.findAll()).thenReturn(expectedBooks);

    // Act
    var actualBooks = libraryService.getAllBooks();

    // Assert
    assertEquals(
          expectedBooks.size(), actualBooks.size(),
          "Number of books should match."
    );
    assertEquals(expectedBooks, actualBooks, "Books should be the same.");
  }

  @Test
  void testAddBookRetunrsBookSaved() throws LibraryServiceException {
    // Arrange
    var book1 = AddBookRequestDto.builder().isbn(21321321421L).title("Book 1")
          .build();
    var book1Entity = PersistentBookEntity.builder().isbn(21321321421L)
          .title("Book 1").build();

    // Mock the repository to return the expected books when save() is called
    when(libraryRepository.save(book1Entity)).thenReturn(book1Entity);
    var bookSaved = libraryService.addBook(book1);

    verify(libraryRepository, times(1)).save(book1Entity);
    Assertions.assertEquals(
          book1.getIsbn(), bookSaved.getIsbn(),
          "book isbn stored is same as book with data created."
    );
    Assertions.assertEquals(
          book1.getTitle(), bookSaved.getTitle(),
          "book isbn stored is same as book with data created."
    );
  }

  @Test
  void testAddNullBookRaiseServiceException() {
    // Arrange
    AddBookRequestDto book1 = null;
    Assertions.assertThrows(
          NullPointerException.class, () -> libraryService.addBook(book1),
          "book1 is null so exception is thrown."
    );
  }

  @Test
  void testFindBookByISBN() throws LibraryServiceException {
    var bookIsbn = 21321321421L;
    var book1 =
          PersistentBookEntity.builder().isbn(bookIsbn).title("Book 1").build();
    var bookOptional = Optional.of(book1);
    // Mock the repository to return the expected book when findById is called
    when(libraryRepository.findById(bookIsbn)).thenReturn(bookOptional);

    var bookFound = libraryService.findByIsbn(21321321421L);
    verify(libraryRepository, times(1)).findById(bookIsbn);
    Assertions.assertEquals(bookFound, book1, "return the correct book.");
  }

  @Test
  void testFindByIsbnWithNullValue() {
    // Arrange: Prepare the test scenario
    var isbn = 0L; // You can choose any valid ISBN value here

    // Act and Assert: Test the behavior when a null value is passed
    Assertions.assertThrows(LibraryServiceException.class, () -> {
      libraryService.findByIsbn(isbn);
    }, "null value raise exception.");
  }

  @Test
  void testRemoveBookWithIsbn() {
    // Arrange
    var bookIsbn = 21321321421L;
    when(libraryRepository.existsById(bookIsbn)).thenReturn(Boolean.TRUE);
    doNothing().when(libraryRepository).deleteById(bookIsbn);
    // Act
    try {
      libraryService.removeBookByIsbn(bookIsbn);
    } catch (LibraryServiceException e) {
      // Handle exceptions as needed in your test
    }
    // Assert
    verify(libraryRepository, times(1)).deleteById(bookIsbn);
  }

  @Test
  void testRemoveBookWithNullIsbn() {
    // Arrange
    Long nullIsbn = 0L;

    // Act and Assert
    Assertions.assertThrows(LibraryServiceException.class, () -> {
      libraryService.removeBookByIsbn(nullIsbn);
    }, "null identifier raises exception");

    // Ensure that libraryRepository.deleteById is not called
    verify(libraryRepository, never()).deleteById(anyLong());
  }

  @Test
  void testRemoveNonExistingBookByIsbn() {
    // Arrange
    var nonExistingIsbn = 1234567890L;

    // Mock the behavior of libraryRepository.existsById
    when(libraryRepository.existsById(nonExistingIsbn)).thenReturn(false);

    // Act and Assert
    Assertions.assertThrows(LibraryServiceException.class, () -> {
      libraryService.removeBookByIsbn(nonExistingIsbn);
    }, "non existing book by isbn raises service exception.");

    // Ensure that libraryRepository.deleteById is not called
    verify(libraryRepository, never()).deleteById(anyLong());
  }

}
