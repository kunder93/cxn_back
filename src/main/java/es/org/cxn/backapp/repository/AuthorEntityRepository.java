
package es.org.cxn.backapp.repository;

import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring-JPA repository for {@link PersistentAuthorEntity}.
 * <p>
 * This repository provides methods for querying {@link PersistentAuthorEntity}
 * instances from the database.
 * </p>
 *
 * @author Santiago Paz Perez.
 */
@Repository
public interface AuthorEntityRepository
      extends JpaRepository<PersistentAuthorEntity, Long> {

  /**
   * Finds an author entity by its first name, last name, and nationality.
   * <p>
   * This method is used to retrieve an {@link PersistentAuthorEntity} instance
   * based on the provided first name, last name, and nationality. It assumes
   * that the combination of these fields is unique.
   * </p>
   *
   * @param firstName The author's first name.
   * @param lastName The author's last name.
   * @param nationality The author's nationality.
   * @return The {@link PersistentAuthorEntity} matching the provided criteria.
   *         Returns {@code null} if no matching author is found.
   */
  PersistentAuthorEntity findByFirstNameAndLastNameAndNationality(
        String firstName, String lastName, String nationality
  );
}
