
package es.org.cxn.backapp.repository;

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

import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring-JPA repository for {@link PersistentAuthorEntity}.
 * <p>
 * This repository provides methods for querying {@link PersistentAuthorEntity}
 * instances from the database.
 * </p>
 *
 * @author Santiago Paz Perez.
 */
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
