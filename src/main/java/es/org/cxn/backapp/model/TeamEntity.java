package es.org.cxn.backapp.model;

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

import java.util.List;

import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;

/**
 * Represents a Team Entity in the application.
 *
 * <p>
 * This interface defines the contract for a team entity, which includes a name,
 * category, description, and a list of users associated with the team.
 * </p>
 *
 * @see es.org.cxn.backapp.model.persistence.user.PersistentUserEntity
 */
public interface TeamEntity extends java.io.Serializable {

    /**
     * Retrieves the category of the team.
     *
     * @return the team's category as a String.
     */
    String getCategory();

    /**
     * Retrieves the description of the team.
     *
     * @return the team's description as a String.
     */
    String getDescription();

    /**
     * Retrieves the name of the team.
     *
     * @return the team's name as a String.
     */
    String getName();

    /**
     * Retrieves the list of users associated with the team.
     *
     * @return a list of {@link PersistentUserEntity} objects representing the team
     *         members.
     */
    List<PersistentUserEntity> getUsersAssigned();

    /**
     * Retrieves the list of users that have this team as preferred.
     *
     * @return a list of {@link PersistentUserEntity} objects representing the team
     *         members.
     */
    List<PersistentUserEntity> getUsersPreferred();

    /**
     * Sets the category of the team.
     *
     * @param category the new category to be assigned to the team.
     */
    void setCategory(String category);

    /**
     * Sets the description of the team.
     *
     * @param description the new description to be assigned to the team.
     */
    void setDescription(String description);

    /**
     * Sets the name of the team.
     *
     * @param name the new name to be assigned to the team.
     */
    void setName(String name);

    /**
     * Sets the list of users associated with the team.
     *
     * @param users a list of {@link PersistentUserEntity} objects to be associated
     *              with the team.
     */
    void setUsersAssigned(List<PersistentUserEntity> users);

    /**
     * Sets the list of users that have this team as preferred.
     *
     * @param users a list of {@link PersistentUserEntity} objects to be associated
     *              with the team.
     */
    void setUsersPreferred(List<PersistentUserEntity> users);
}
