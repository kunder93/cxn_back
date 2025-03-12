
package es.org.cxn.backapp.model.persistence.team;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.org.cxn.backapp.model.TeamEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * PersistentTeamEntity is a JPA entity that represents a team in the
 * application.
 *
 * <p>
 * This entity implements the {@link TeamEntity} interface and maps to the
 * "team" table in the database. Each team is uniquely identified by its name,
 * and it contains a category, a description, and a list of users that belong to
 * the team.
 * </p>
 *
 * @author Santiago
 */
@Entity(name = "Team")
@Table(name = "team")
public class PersistentTeamEntity implements TeamEntity {

    /**
     * Generated UID for serialization.
     */
    private static final long serialVersionUID = -2334928772024222275L;

    /**
     * The unique name of the team. This is the primary key.
     */
    @Id
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /**
     * The category of the team.
     */
    @Column(name = "category", length = 50, nullable = false)
    private String category;

    /**
     * The description of the team.
     */
    @Column(name = "description", length = 255, nullable = false)
    private String description;

    /**
     * The list of users that belong to this team.
     */
    @OneToMany(mappedBy = "team")
    private List<PersistentUserEntity> users = new ArrayList<>();

    /**
     * Default constructor.
     */
    public PersistentTeamEntity() {
        // For JPA
    }

    /**
     * Constructs a PersistentTeamEntity with the specified name, category, and
     * description.
     *
     * @param name        the unique name of the team.
     * @param category    the category of the team.
     * @param description the description of the team.
     */
    public PersistentTeamEntity(final String name, final String category, final String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    /**
     * Redefines equality based on {@code name}, {@code category}, and
     * {@code description}.
     *
     * @param o the object to compare with.
     * @return true if the given object is equal to this team, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersistentTeamEntity)) {
            return false;
        }
        PersistentTeamEntity that = (PersistentTeamEntity) o;
        return Objects.equals(name, that.name) && Objects.equals(category, that.category)
                && Objects.equals(description, that.description);
    }

    /**
     * Gets the category of the team.
     *
     * @return the team's category.
     */
    @Override
    public String getCategory() {
        return category;
    }

    /**
     * Gets the description of the team.
     *
     * @return the team's description.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Gets the name of the team.
     *
     * @return the team's name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the list of users that belong to the team.
     *
     * @return a list of {@link PersistentUserEntity} representing the team members.
     */
    @Override
    public List<PersistentUserEntity> getUsers() {
        return users;
    }

    /**
     * Generates the hash code using {@code name}, {@code category}, and
     * {@code description}.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, category, description);
    }

    /**
     * Sets the category of the team.
     *
     * @param category the category to set.
     */
    @Override
    public void setCategory(final String category) {
        this.category = category;
    }

    /**
     * Sets the description of the team.
     *
     * @param description the description to set.
     */
    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Sets the name of the team.
     *
     * @param name the name to set.
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the list of users associated with the team.
     *
     * @param users a list of {@link PersistentUserEntity} to associate with the
     *              team.
     */
    @Override
    public void setUsers(final List<PersistentUserEntity> users) {
        this.users = users;
    }
}
