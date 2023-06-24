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

package es.org.cxn.backapp.model.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.model.RoleEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Role entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez
 */
@Entity(name = "RoleEntity")
@Table(name = "roles")
public class PersistentRoleEntity implements RoleEntity {

  /**
   * Serialization ID.
   */
  @Transient
  private static final long serialVersionUID = 1328776999450853491L;

  /**
   * Role's ID.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, unique = true)
  private Integer id = -1;

  /**
   * Name of the entity.
   * <p>
   * This is to have additional data apart from the id, to be used on the tests.
   */
  @Column(name = "name", nullable = false, unique = true)
  private String name = "";

  /**
   * Role associated users.
   */
  @ManyToMany(mappedBy = "roles")
  private Set<PersistentUserEntity> users = new HashSet<>();

  /**
   * Constructs a role entity.
   */
  public PersistentRoleEntity() {
    super();
  }

  /**
   * Constructs a role entity with given name.
   *
   * @param value the role name.
   */
  public PersistentRoleEntity(final String value) {
    super();
    this.name = value;
  }

  /**
   * Returns the identifier assigned to this entity.
   * <p>
   * If no identifier has been assigned yet, then the value will be lower than
   * zero.
   *
   * @return the entity's identifier
   */
  @Override
  public Integer getId() {
    return id;
  }

  /**
   * Get role name.
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Get users from role.
   */
  @Override
  public Set<PersistentUserEntity> getUsers() {
    return new HashSet<>(users);
  }

  /**
   * Set role Id.
   */
  @Override
  public void setId(final Integer value) {
    id = checkNotNull(value, "Received a null pointer as identifier");
  }

  /**
   * Set role name.
   */
  @Override
  public void setName(final String roleName) {
    name = checkNotNull(roleName, "Received a null pointer as name");
  }

  /**
   * Set role users.
   */
  @Override
  public void setUsers(final Set<PersistentUserEntity> users) {
    this.users = new HashSet<>(users);
  }

  /**
   * Hash code method.
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  /**
   * Equals method.
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final var other = (PersistentRoleEntity) obj;
    return Objects.equals(id, other.id) && Objects.equals(name, other.name);
  }

  /**
   * To string method.
   */
  @Override
  public String toString() {
    return "PersistentRoleEntity [id=" + id + ", name=" + name + "]";
  }

}
