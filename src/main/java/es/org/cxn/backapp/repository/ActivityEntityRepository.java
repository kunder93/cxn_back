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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;

/**
 * Repository interface for {@link PersistentActivityEntity} entities. This
 * interface extends {@link JpaRepository}, providing CRUD operations and
 * database access methods for the activities table.
 *
 * <p>
 * Additional query methods can be defined here, allowing for custom queries and
 * operations on the activity data.
 *
 * <p>
 * This repository is marked with the {@code @Repository} annotation, which
 * makes it a Spring Data JPA repository and enables exception translation to
 * Spring's DataAccessException.
 *
 * @see PersistentActivityEntity
 */
@Repository
public interface ActivityEntityRepository extends JpaRepository<PersistentActivityEntity, Integer> {

}
