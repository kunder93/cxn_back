
package es.org.cxn.backapp.model.persistence;

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

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Book Entity.
 * <p>
 * This class represents a book entity in the system, utilizing JPA annotations
 * for persistence. It includes fields for ISBN, title, gender, publish year,
 * language, and authors.
 * The default constructor is provided by Lombok's @NoArgsConstructor
 * annotation and is required by JPA for entity instantiation.
 * </p>
 *
 * @author Santiago Paz Perez.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Book")
@Table(name = "book")
public class PersistentBookEntity
      implements BookEntity, Comparable<PersistentBookEntity> {

  /**
   * Serial UID for serialization.
   */
  private static final long serialVersionUID = 5749496625209170661L;

  /**
   * The book's ISBN, which acts as the unique identifier.
   */
  @Id
  @Column(name = "isbn", nullable = false, unique = true)
  @NonNull
  private Long isbn;

  /**
   * The title of the book.
   */
  @Column(name = "title", nullable = false, unique = false)
  @NonNull
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
   * The authors who wrote this book.
   * This is a many-to-many relationship with the PersistentAuthorEntity.
   */
  @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
  @JoinTable(
        name = "bookauthor", joinColumns = @JoinColumn(name = "book_isbn"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
  )
  @Builder.Default
  private Set<PersistentAuthorEntity> authors = new HashSet<>();

  /**
   * Compares this book to another book based on their titles.
   *
   * @param otherBook The other book to compare to.
   * @return A negative integer, zero, or a positive integer as this book's
   * title is less than, equal to, or greater than the specified book's title.
   */
  @Override
  public int compareTo(final PersistentBookEntity otherBook) {
    return this.title.compareTo(otherBook.title);
  }
}
