
package es.org.cxn.backapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.org.cxn.backapp.model.persistence.PersistentLichessProfileEntity;

/**
 * Spring-JPA repository for.
 * <p>
 * This repository provides methods for querying instances from the database.
 * </p>
 *
 * @author Santiago Paz Perez.
 */
@Repository
public interface LichessEntityRepository extends JpaRepository<PersistentLichessProfileEntity, String> {

}
