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

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.exceptions.RoleNameExistsException;
import es.org.cxn.backapp.exceptions.RoleNameNotFoundException;
import es.org.cxn.backapp.model.RoleEntity;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.repository.RoleEntityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Default implementation of the privilege entity service.
 *
 * @author Santiago Paz.
 *
 */
@Service
public final class DefaultRoleService implements RoleService {

  /**
   * Repository for the domain entities handled by the service.
   */
  private final RoleEntityRepository entityRepository;

  /**
   * Constructs an entities service with the specified repository.
   *
   * @param repository the repository for the entity instances.
   */
  public DefaultRoleService(final RoleEntityRepository repository) {
    super();

    entityRepository =
          checkNotNull(repository, "Received a null pointer as repository");
  }

  @Override
  public RoleEntity add(final String name) throws RoleNameExistsException {
    final PersistentRoleEntity save;
    final var nameNotNull =
          checkNotNull(name, "Received a null pointer as name");
    save = new PersistentRoleEntity();
    save.setName(nameNotNull);
    if (entityRepository.existsByName(nameNotNull)) {
      throw new RoleNameExistsException(nameNotNull);
    }

    return entityRepository.save(save);
  }

  /**
   * Returns an entity with the given id.
   * <p>
   * If no instance exists with that id then an entity with a negative id is
   * returned.
   *
   * @param identifier identifier of the entity to find
   * @return the entity for the given id
   */
  @Override
  public RoleEntity findById(final Integer identifier) {
    final Optional<PersistentRoleEntity> entityOpt;

    checkNotNull(identifier, "Received a null pointer as identifier");

    entityOpt = entityRepository.findById(identifier);

    if (entityOpt.isEmpty()) {
      return new PersistentRoleEntity();
    }
    return entityOpt.get();
  }

  @Override
  public RoleEntity findByName(final String name)
        throws RoleNameNotFoundException {
    checkNotNull(name, "Received a null pointer as identifier");
    final var entity = entityRepository.findByName(name);
    if (entity.isEmpty()) {
      throw new RoleNameNotFoundException(name);
    }
    return entity.get();
  }

  @Override
  public List<RoleEntity> getAllRoles() {
    var tmp = entityRepository.findAll();
    return new ArrayList<>(tmp);
  }

  @Override
  public void remove(final String name) throws RoleNameNotFoundException {
    final var delete = entityRepository.findByName(name);
    if (delete.isEmpty()) {
      throw new RoleNameNotFoundException(name);
    }
    entityRepository.delete(delete.get());
  }
}
