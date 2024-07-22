/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package es.org.cxn.backapp.test.integration.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import es.org.cxn.backapp.exceptions.RoleNameExistsException;
import es.org.cxn.backapp.exceptions.RoleNameNotFoundException;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.PersistentRoleEntity;
import es.org.cxn.backapp.repository.RoleEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.DefaultRoleService;
import es.org.cxn.backapp.service.RoleService;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Unit tests for {@link DefaultRoleService}.
 * <p>
 * These tests verify that the bean applies the correct Java validation
 * annotations.
 *
 * @author Santiago Paz
 */
@SpringBootTest(
      classes = { UserEntityRepository.class, RoleEntityRepository.class,
          RoleService.class, DefaultRoleService.class }
)
final class TestDefaultRoleService {

  @Autowired
  private RoleService roleService;

  @MockBean
  private RoleEntityRepository roleEntityRepository;

  UserRoleName roleName = UserRoleName.ROLE_CANDIDATO_SOCIO;
  Integer roleId = Integer.valueOf(79);

  /**
   * Sets up the validator for the tests.
   */
  @BeforeEach
  public final void setUpValidator() {
  }

  /**
   * Default constructor.
   */
  public TestDefaultRoleService() {
    super();
  }

  /**
   * Verifies that tow roles are equals or not
   *
   */
  @DisplayName("Assert equals")
  @Test
  void testTwoRolesEquals() {
    var roleEntityA = new PersistentRoleEntity();
    roleEntityA.setName(roleName);
    roleEntityA.setId(roleId);
    var diferentRoleId = 77;
    var roleEntityB = new PersistentRoleEntity();
    roleEntityB.setName(roleEntityA.getName());
    roleEntityB.setId(roleEntityA.getId());

    Assertions.assertEquals(roleEntityA, roleEntityB, "same id and roleName");
    roleEntityA.setId(diferentRoleId);
    Assertions
          .assertNotEquals(roleEntityA, roleEntityB, "same id and roleName");
    PersistentRoleEntity nullEntity = null;
    Assertions.assertNotEquals(
          nullEntity, roleEntityA, "equals with null is false"
    );
  }

  /**
   * Verifies that service throw exception when cannot find user with provided
   * id
   *
   * @throws RoleNameExistsException when role name already exists, goal of
   *                                 this test
   */
  @DisplayName("Add role with name that exists throw exception")
  @Test
  void testAddRoleRoleNameThatExistsThrowException() {
    Mockito.when(roleEntityRepository.existsByName(roleName)).thenReturn(true);
    Assertions.assertThrows(RoleNameExistsException.class, () -> {
      roleService.add(roleName);
    }, "The RoleNameExistsException is catched");

  }

  /**
   * Verifies that service call repository save if data is correct
   *
   * @throws RoleNameExistsException when role name already exists, not should
   *                                 happen
   */
  @DisplayName("Add role with name that not exists persist it")
  @Test
  final void testAddRoleNameReturnRole() throws RoleNameExistsException {
    var roleEntity = new PersistentRoleEntity();

    Mockito.when(roleEntityRepository.existsByName(roleName)).thenReturn(false);
    Mockito.when(roleEntityRepository.save(any(PersistentRoleEntity.class)))
          .thenReturn(roleEntity);
    var result = (PersistentRoleEntity) roleService.add(roleName);
    Assertions
          .assertEquals(result, roleEntity, "persisted is same as provided");
    Mockito.verify(roleEntityRepository, times(1))
          .save(any(PersistentRoleEntity.class));
  }

  /**
   * Verifies that service return role when finds it by id
   *
   * @throws RoleNameExistsException when role name already exists, goal of
   *                                 this test
   */
  @DisplayName("Find role with id that exists")
  @Test
  final void testFindRoleByIdRoleExists() {
    var roleEntity = new PersistentRoleEntity(roleName);
    Optional<PersistentRoleEntity> roleOptional = Optional.of(roleEntity);

    Mockito.when(roleEntityRepository.findById(roleId))
          .thenReturn(roleOptional);
    var roleResult = roleService.findById(roleId);
    Assertions.assertEquals(roleEntity, roleResult, "Check role found");
    Mockito.verify(roleEntityRepository, times(1)).findById(roleId);
  }

  /**
   * Verifies that service return role when finds it by name
   *
   * @throws RoleNameNotFoundException when role name not found
   */
  @DisplayName("Find role with name provided")
  @Test
  final void testFindRoleByNameRoleExists() throws RoleNameNotFoundException {
    var roleEntity = new PersistentRoleEntity(roleName);
    Optional<PersistentRoleEntity> roleOptional = Optional.of(roleEntity);

    Mockito.when(roleEntityRepository.findByName(roleName))
          .thenReturn(roleOptional);
    var roleResult = roleService.findByName(roleName);
    Assertions.assertEquals(roleEntity, roleResult, "Check role found");
    Mockito.verify(roleEntityRepository, times(1)).findByName(roleName);
  }

  /**
   * Verifies that service throw exception when cannot find role with provided
   * name
   *
   * @throws RoleNameNotFoundException when role name not found
   */
  @DisplayName("Find role with name that not exists throw exception")
  @Test
  final void testFindRoleRoleNameNotExists() {

    Mockito.when(roleEntityRepository.findByName(roleName))
          .thenReturn(Optional.empty());
    ;
    Assertions.assertThrows(RoleNameNotFoundException.class, () -> {
      roleService.findByName(roleName);
    }, "Role name not found throw exception");

  }

  /**
   * Verifies that service throw exception when cannot remove role with
   * provided name.
   *
   * @throws RoleNameNotFoundException when role name not found.
   */
  @DisplayName("Remove role with name that not exists throw exception")
  @Test
  final void testRemoveRoleRoleNameNotExists() {

    Mockito.when(roleEntityRepository.findByName(roleName))
          .thenReturn(Optional.empty());
    ;
    Assertions.assertThrows(RoleNameNotFoundException.class, () -> {
      roleService.remove(roleName);
    }, "Role name not found throw exception");

  }

  /**
   * Verifies that service remove role call to repository
   *
   * @throws RoleNameNotFoundException when role name not found
   */
  @DisplayName("Remove role with name that exists n")
  @Test
  final void testRemoveRoleCallRepository() throws RoleNameNotFoundException {
    var roleEntity = new PersistentRoleEntity(roleName);
    Optional<PersistentRoleEntity> roleOptional = Optional.of(roleEntity);

    Mockito.when(roleEntityRepository.findByName(roleName))
          .thenReturn(roleOptional);
    ;

    Mockito.doNothing().when(roleEntityRepository).delete(roleEntity);
    roleService.remove(roleName);
    Mockito.verify(roleEntityRepository, times(1)).delete(roleEntity);
  }

}
