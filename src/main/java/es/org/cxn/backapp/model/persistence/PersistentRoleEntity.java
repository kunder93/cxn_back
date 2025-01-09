package es.org.cxn.backapp.model.persistence;

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
