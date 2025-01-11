package es.org.cxn.backapp.model.persistence;

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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import es.org.cxn.backapp.model.RoleEntity;
import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Role entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez
 */
@Entity(name = "RoleEntity")
@Table(name = "roles")
@Data
@NoArgsConstructor
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
    private Integer identifier = -1;

    /**
     * Name of the entity.
     * <p>
     * This is to have additional data apart from the id, to be used on the tests.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private UserRoleName name = UserRoleName.ROLE_CANDIDATO_SOCIO;

    /**
     * Role associated users.
     */
    @ManyToMany(mappedBy = "roles")
    private Set<PersistentUserEntity> users = new HashSet<>();

    /**
     * Constructs a role entity with given name.
     *
     * @param value the role name.
     */
    public PersistentRoleEntity(final UserRoleName value) {
        super();
        name = value;
    }

    /**
     * Returns an unmodifiable set of associated users to prevent external
     * modification.
     *
     * @return An unmodifiable set of associated users.
     */
    @Override
    public Set<PersistentUserEntity> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    /**
     * Sets the associated users with a defensive copy to prevent direct
     * modification of the internal set.
     *
     * @param users The set of associated users.
     */
    @Override
    public void setUsers(final Set<PersistentUserEntity> users) {
        this.users = new HashSet<>(users);
    }
}
