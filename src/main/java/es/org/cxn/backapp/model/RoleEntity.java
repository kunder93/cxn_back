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

package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.util.Set;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;

/**
 * A simple entity to be used as an example.
 *
 * @author Santiago Paz Perez
 */
public interface RoleEntity extends Serializable {

  /**
   * Returns the identifier assigned to this entity.
   * <p>
   * If no identifier has been assigned yet, then the value is expected to be
   * {@code null} or lower than zero.
   *
   * @return the entity's identifier
   */
  Integer getId();

  /**
   * Returns the name of role.
   *
   * @return the role name
   */
  String getName();

  /**
   * Returns the users associated to this role.
   *
   * @return the user set
   */
  Set<PersistentUserEntity> getUsers();

  /**
   * Sets the identifier assigned to this role.
   *
   * @param identifier the identifier for the role
   */
  void setId(Integer identifier);

  /**
   * Changes the name of the role.
   *
   * @param roleName the name to set on role
   */
  void setName(String roleName);

  /**
   * Changes the users associated to this role.
   *
   * @param users Set of users have relation
   */
  void setUsers(Set<PersistentUserEntity> users);

}
