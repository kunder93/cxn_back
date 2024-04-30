/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.repository;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring-JPA repository for {@link PersistentRoleEntity}.
 * <p>
 * This is a simple repository just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago Paz Perez.
 */
@Repository
public interface RoleEntityRepository
      extends JpaRepository<PersistentRoleEntity, Integer> {

  /**
   * Returns all entities with a partial match to the name.
   *
   * @param name name for searching.
   * @param page pagination to apply.
   * @return all entities at least partially matching the name.
   */
  Page<PersistentRoleEntity>
        findByNameContaining(UserRoleName name, Pageable page);

  /**
   * Find privilege entity with provided name.
   *
   * @param name the privilege name.
   * @return privilege entity with name.
   */
  Optional<PersistentRoleEntity> findByName(UserRoleName name);

  /**
   * Check privilege entity with provided name.
   *
   * @param name the privilege name.
   * @return true if exists.
   */
  boolean existsByName(UserRoleName name);

}
