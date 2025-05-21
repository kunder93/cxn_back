
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertAll;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;
import es.org.cxn.backapp.model.persistence.PersistentBookEntity;

/**
 * Unit test class for {@link PersistentBookEntity}.
 * <p>
 * This class contains tests for the methods and behavior of the
 * {@link PersistentBookEntity} class. It includes tests for various
 * functionalities such as:
 * <ul>
 * <li>Constructor behavior.</li>
 * <li>Equality and hash code consistency.</li>
 * <li>Getter and setter methods.</li>
 * <li>Manipulating the authors collection (e.g., adding and removing
 * authors).</li>
 * </ul>
 * The goal is to ensure that the {@link PersistentBookEntity} class works as
 * expected in different scenarios.
 */
class PersistentBookEntityTest {

    /**
     * The book entity.
     */
    private PersistentBookEntity book;
    /**
     * Mocked author entity.
     */
    private PersistentAuthorEntity authorMock1;

    /**
     * The book isb string number.
     */
    private final String isbn = "123456789";

    /**
     * The book title.
     */
    private final String title = "Kovic chess problems";

    /**
     * The book genre.
     */
    private final String genre = "Fiction";

    /**
     * The book publish date.
     */
    private final LocalDate publishDate = LocalDate.of(2023, 1, 1);

    /**
     * The book language.
     */
    private final String language = "English";

    /**
     * The book description.
     */
    private final String description = "A test book description.";

    /**
     * The book cover image source.
     */
    private final String coverSrc = "/images/test-cover.jpg";

    @BeforeEach
    void setUp() {
        final String authorFirstName = "Javier";
        final String authorLastName = "Ochoa";
        final Long authorId = 1L;
        book = PersistentBookEntity.builder().isbn(isbn).title(title).genre(genre).publishYear(publishDate)
                .language(language).description(description).coverSrc(coverSrc).authors(new HashSet<>()).build();

        authorMock1 = mock(PersistentAuthorEntity.class);

        when(authorMock1.getFirstName()).thenReturn(authorFirstName);
        when(authorMock1.getLastName()).thenReturn(authorLastName);
        when(authorMock1.getId()).thenReturn(authorId);

    }

    /**
     * Tests the functionality of adding an author to the
     * {@link PersistentBookEntity}.
     * <p>
     * This test verifies that when an author is added to the book's set of authors,
     * it is correctly included in the collection. The test also checks that the
     * size of the authors' collection reflects the addition.
     * </p>
     */
    @Test
    void testAddAuthor() {
        // Add an author to the book
        book.getAuthors().add(authorMock1);

        // Verify that the author was added
        assertEquals(1, book.getAuthors().size());
        assertTrue(book.getAuthors().contains(authorMock1));
    }

    /**
     * Tests the functionality of the all-args constructor in
     * {@link PersistentBookEntity}.
     * <p>
     * This test ensures that a {@code PersistentBookEntity} instance created using
     * the all-args constructor correctly initializes all its fields with the
     * provided values.
     * </p>
     */
    @Test
    void testAllArgsConstructor() {
        Set<PersistentAuthorEntity> authors = new HashSet<>();
        PersistentBookEntity bookWithAuthors = new PersistentBookEntity(isbn, title, genre, publishDate, language,
                description, coverSrc, authors);
        assertEquals(isbn, bookWithAuthors.getIsbn());
        assertEquals(title, bookWithAuthors.getTitle());
        assertEquals(genre, bookWithAuthors.getGenre());
        assertEquals(publishDate, bookWithAuthors.getPublishYear());
        assertEquals(language, bookWithAuthors.getLanguage());
        assertEquals(description, bookWithAuthors.getDescription());
        assertEquals(coverSrc, bookWithAuthors.getCoverSrc());
        assertEquals(authors, bookWithAuthors.getAuthors());
    }

    /**
     * Tests the builder functionality of {@link PersistentBookEntity}. Ensures that
     * all fields are correctly set using the builder pattern.
     */
    @Test
    void testBuilder() {
        assertEquals(isbn, book.getIsbn(), "isbn");
        assertEquals(title, book.getTitle(), "title");
        assertEquals(genre, book.getGenre(), "genre");
        assertEquals(publishDate, book.getPublishYear(), "publish year");
        assertEquals(language, book.getLanguage(), "language");
        assertEquals(description, book.getDescription(), "description");
        assertEquals(coverSrc, book.getCoverSrc(), "cover src");
        assertNotNull(book.getAuthors(), "authors");
    }

    /**
     * Tests the compareTo method of {@link PersistentBookEntity}. Verifies that
     * books are compared correctly based on their titles.
     */
    @Test
    void testCompareTo() {
        PersistentBookEntity anotherBook = PersistentBookEntity.builder().title("Another Book").build();

        assertAll(() -> assertTrue(book.compareTo(anotherBook) > 0, "Book should be greater than anotherBook"),
                () -> assertTrue(anotherBook.compareTo(book) < 0, "AnotherBook should be less than book"), () -> {
                    anotherBook.setTitle(title);
                    assertEquals(0, book.compareTo(anotherBook), "Books with the same title should be equal");
                });
    }

    /**
     * Tests the default constructor of the {@link PersistentBookEntity} class.
     * <p>
     * This test verifies that when a {@link PersistentBookEntity} object is created
     * using the default constructor:
     * <ul>
     * <li>The object is not null.</li>
     * <li>The authors list is initialized as an empty list.</li>
     * </ul>
     */
    @Test
    void testDefaultConstructor() {
        PersistentBookEntity defaultBook = new PersistentBookEntity();
        assertNotNull(defaultBook);
        assertNotNull(defaultBook.getAuthors());
        assertTrue(defaultBook.getAuthors().isEmpty());
    }

    /**
     * Tests the {@link PersistentBookEntity#equals(Object)} and
     * {@link PersistentBookEntity#hashCode()} methods.
     * <p>
     * This test verifies the following:
     * <ul>
     * <li>{@link PersistentBookEntity#equals(Object)} correctly identifies two
     * objects as equal if their fields are identical.</li>
     * <li>{@link PersistentBookEntity#equals(Object)} correctly identifies two
     * objects as different if their fields differ.</li>
     * <li>{@link PersistentBookEntity#hashCode()} returns the same hash code for
     * two objects that are considered equal.</li>
     * <li>{@link PersistentBookEntity#hashCode()} returns different hash codes for
     * objects that are considered different.</li>
     * </ul>
     */
    @Test
    void testEqualsAndHashCode() {
        // Create the sameBook object with identical fields as the book
        PersistentBookEntity sameBook = PersistentBookEntity.builder().isbn(isbn).title(title).genre(genre)
                .publishYear(publishDate).language(language).description(description).coverSrc(coverSrc)
                .authors(new HashSet<>()).build();

        PersistentBookEntity differentBook = PersistentBookEntity.builder().isbn("987-654-321").title("Different Book")
                .build();

        assertEquals(book, sameBook);
        assertNotEquals(book, differentBook);
        assertEquals(book.hashCode(), sameBook.hashCode());
        assertNotEquals(book.hashCode(), differentBook.hashCode());
    }

    /**
     * Tests the getter and setter methods of the {@link PersistentBookEntity}
     * class.
     * <p>
     * This test verifies that the setter methods correctly set the fields of a
     * {@link PersistentBookEntity} object and that the corresponding getter methods
     * return the expected values.
     * <ul>
     * <li>{@link PersistentBookEntity#setIsbn(String)} and
     * {@link PersistentBookEntity#getIsbn()}.</li>
     * <li>{@link PersistentBookEntity#setTitle(String)} and
     * {@link PersistentBookEntity#getTitle()}.</li>
     * <li>{@link PersistentBookEntity#setLanguage(String)} and
     * {@link PersistentBookEntity#getLanguage()}.</li>
     * <li>{@link PersistentBookEntity#setGenre(String)} and
     * {@link PersistentBookEntity#getGenre()}.</li>
     * <li>{@link PersistentBookEntity#setDescription(String)} and
     * {@link PersistentBookEntity#getDescription()}.</li>
     * <li>{@link PersistentBookEntity#setPublishYear(String)} and
     * {@link PersistentBookEntity#getPublishYear()}.</li>
     * <li>{@link PersistentBookEntity#setCoverSrc(String)} and
     * {@link PersistentBookEntity#getCoverSrc()}.</li>
     * </ul>
     * Each setter is followed by a corresponding getter assertion to verify that
     * the value set is correctly retrieved.
     */
    @Test
    void testGettersAndSetters() {

        book.setIsbn(isbn);
        book.setTitle(title);
        book.setLanguage(language);
        book.setGenre(genre);
        book.setDescription(description);
        book.setPublishYear(publishDate);
        book.setCoverSrc(coverSrc);

        assertEquals(isbn, book.getIsbn(), "isbn getter");
        assertEquals(title, book.getTitle(), "title getter");
        assertEquals(language, book.getLanguage(), "language getter");
        assertEquals(genre, book.getGenre(), "genre getter");
        assertEquals(description, book.getDescription(), "description getter");
        assertEquals(publishDate, book.getPublishYear(), "publishDate getter");
        assertEquals(coverSrc, book.getCoverSrc(), "coverSrc getter");
    }

    /**
     * Tests the removal of an author from the {@link PersistentBookEntity}'s
     * authors collection.
     * <p>
     * This test verifies that when an author is added to the book's authors
     * collection and then removed:
     * <ul>
     * <li>The author's removal is reflected by the size of the authors collection
     * being zero.</li>
     * <li>The removed author is no longer contained in the authors collection.</li>
     * </ul>
     */
    @Test
    void testRemoveAuthor() {
        // Add and then remove an author
        book.getAuthors().add(authorMock1);
        book.getAuthors().remove(authorMock1);

        // Verify that the author was removed
        assertEquals(0, book.getAuthors().size());
        assertFalse(book.getAuthors().contains(authorMock1));
    }

}
