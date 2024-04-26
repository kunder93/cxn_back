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

package es.org.cxn.backapp.service;

import es.org.cxn.backapp.exceptions.RoleNameExistsException;
import es.org.cxn.backapp.exceptions.RoleNameNotFoundException;
import es.org.cxn.backapp.model.RoleEntity;
import es.org.cxn.backapp.model.UserRoleName;

/**
 * Service for the Role entity domain. {@link RoleEntity}
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago Paz Perez.
 */
public interface RoleService {

  /**
   * Creates a new role entity.
   *
   * @param name the role name to persist
   * @return the persisted role entity
   * @throws RoleNameExistsException when role with this name already exists
   */
  RoleEntity add(UserRoleName name) throws RoleNameExistsException;

  /**
   * Returns a role with the given id.
   * <p>
   * If no instance exists with that id then an entity with a negative id is
   * expected to be returned. Avoid returning nulls.
   *
   * @param identifier identifier of the role entity to find.
   * @return the role entity for the given id.
   */
  RoleEntity findById(Integer identifier);

  /**
   * Returns a role with the given name.
   *
   * @param name role name used to find
   * @return the role entity for the given name
   * @throws RoleNameNotFoundException when cannot find a Role with provided
   *                                   name
   */
  RoleEntity findByName(UserRoleName name) throws RoleNameNotFoundException;

  /**
   * Returns all the roles entities from the DB.
   *
   * @return the persisted role entities.
   */
  Iterable<RoleEntity> getAllRoles();

  /**
   * Removes a role entity from persistence.
   *
   * @param name name of the role entity to remove.
   * @throws RoleNameNotFoundException when a Role entity with provided name not
   *                                   found.
   */
  void remove(UserRoleName name) throws RoleNameNotFoundException;

}
