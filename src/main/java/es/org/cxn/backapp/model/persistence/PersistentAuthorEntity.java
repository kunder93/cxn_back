
package es.org.cxn.backapp.model.persistence;

import java.util.HashSet;
import java.util.Set;

import es.org.cxn.backapp.model.AuthorEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Book Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 * <p>
 * The default constructor is provided by Lombok's @NoArgsConstructor
 * annotation. This default constructor is required by JPA for entity
 * instantiation.
 *
 * @author Santiago Paz Perez.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Author")
@Table(name = "author")
public class PersistentAuthorEntity implements AuthorEntity {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 5777796129999170661L;

    /**
     * Author identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifier;

    /**
     * Author first name.
     */
    @Column(name = "first_name", nullable = false, unique = false)
    private String firstName;

    /**
     * Author last name.
     */
    @Column(name = "last_name", nullable = false, unique = false)
    private String lastName;

    /**
     * Author nationality.
     */
    @Column(name = "nationality", nullable = true, unique = false)
    private String nationality;

    /**
     * Books written by this author.
     */
    @ManyToMany(mappedBy = "authors")
    @Builder.Default
    private Set<PersistentBookEntity> books = new HashSet<>();
}
