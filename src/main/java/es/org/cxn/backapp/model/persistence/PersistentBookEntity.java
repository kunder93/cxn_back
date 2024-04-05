
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
 * This makes use of JPA annotations for the persistence configuration.
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
   * Serial UID.
   */
  private static final long serialVersionUID = 5749496625209170661L;

  /**
   *  The book's isbn aka identifier.
   */
  @Id
  @Column(name = "isbn", nullable = false, unique = true)
  @NonNull
  private Long isbn;

  /**
   * Book's title.
   */
  @Column(name = "title", nullable = false, unique = false)
  @NonNull
  private String title;

  /**
   * The book gender.
   */
  @Column(name = "gender", nullable = true, unique = false)
  private String gender;

  /**
   * The book publish year date.
   */
  @Column(name = "publish_year", nullable = true, unique = false)
  private LocalDate publishYear;

  /**
   * The book language.
   */
  @Column(name = "language", nullable = true, unique = false)
  private String language;

  /**
   * Authors who wrote this book.
   */
  @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
  @JoinTable(
        name = "bookauthor", joinColumns = @JoinColumn(name = "book_isbn"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
  )
  @Builder.Default
  private Set<PersistentAuthorEntity> authors = new HashSet<>();

  /**
   * Compare books with their title.
   */
  @Override
  public int compareTo(final PersistentBookEntity otherBook) {
    return this.title.compareTo(otherBook.title);
  }
}
