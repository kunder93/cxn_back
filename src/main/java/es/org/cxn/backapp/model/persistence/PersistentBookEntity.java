
package es.org.cxn.backapp.model.persistence;

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

import java.io.Serial;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import es.org.cxn.backapp.model.BookEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Book Entity.
 * <p>
 * This class represents a book entity in the system, utilizing JPA annotations
 * for persistence. It includes fields for ISBN, title, gender, publish year,
 * language, and authors. The default constructor is provided by
 * Lombok's @NoArgsConstructor annotation and is required by JPA for entity
 * instantiation.
 * </p>
 *
 * @author Santiago Paz Perez.
 */
@Data
@AllArgsConstructor
@Builder
@Entity(name = "Book")
@Table(name = "book")
public class PersistentBookEntity implements BookEntity, Comparable<PersistentBookEntity> {

    /**
     * Serial UID for serialization.
     */
    @Serial
    private static final long serialVersionUID = 5749496625209170661L;

    /**
     * The book's ISBN, which acts as the unique identifier.
     */
    @Id
    @Column(name = "isbn", nullable = false, unique = true)
    private Long isbn;

    /**
     * The title of the book.
     */
    @Column(name = "title", nullable = false, unique = false)
    private String title;

    /**
     * The genre or category of the book.
     */
    @Column(name = "gender", nullable = true, unique = false)
    private String gender;

    /**
     * The year the book was published.
     */
    @Column(name = "publish_year", nullable = true, unique = false)
    private LocalDate publishYear;

    /**
     * The language in which the book is written.
     */
    @Column(name = "language", nullable = true, unique = false)
    private String language;

    /**
     * The authors who wrote this book. This is a many-to-many relationship with the
     * PersistentAuthorEntity.
     */
    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(name = "bookauthor", joinColumns = @JoinColumn(name = "book_isbn"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @Builder.Default
    private Set<PersistentAuthorEntity> authors = new HashSet<>();

    /**
     * Default constructor for the PersistentBookEntity class.
     * <p>
     * This constructor initializes a new instance of the PersistentBookEntity
     * class. It is required for frameworks like JPA that rely on reflection to
     * create objects.
     * </p>
     */
    public PersistentBookEntity() {
        authors = new HashSet<>();
        // Default constructor
    }

    /**
     * Compares this book to another book based on their titles.
     *
     * @param otherBook The other book to compare to.
     * @return A negative integer, zero, or a positive integer as this book's title
     *         is less than, equal to, or greater than the specified book's title.
     */
    @Override
    public int compareTo(final PersistentBookEntity otherBook) {
        return this.title.compareTo(otherBook.title);
    }
}
