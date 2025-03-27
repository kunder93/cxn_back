
package es.org.cxn.backapp.model.form.responses.user;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.org.cxn.backapp.model.UserRoleName;

/**
 * Represents the form used for responding to a user's role change.
 * <p>
 * This is a Data Transfer Object (DTO) designed to facilitate communication
 * between the view and the controller. It includes the user's name and the list
 * of roles assigned to the user after the role change operation.
 * </p>
 * <p>
 * The class ensures that the required data is correctly passed from the server
 * to the client and can be validated using Java's validation annotations. This
 * helps maintain data integrity and consistency in the application.
 * </p>
 *
 * @param userName  the username of the user whose roles are being changed. This
 *                  field represents the unique identifier of the user in the
 *                  system.
 * @param userRoles the list of {@link UserRoleName} objects representing the
 *                  roles assigned to the user after the change. This list
 *                  includes all the roles that the user currently holds,
 *                  providing a clear picture of the user's permissions.
 *
 * @author Santiago Paz Perez
 */
public record UserChangeRoleResponse(String userName, List<UserRoleName> userRoles) {

    /**
     * Constructor for creating a new {@link UserChangeRoleResponse}.
     * <p>
     * This constructor accepts a username and a list of user roles, creating an
     * unmodifiable list of roles to ensure that the roles cannot be modified after
     * the object is created.
     * </p>
     *
     * @param userName  the username of the user whose roles are being changed.
     * @param userRoles the list of {@link UserRoleName} objects representing the
     *                  roles assigned to the user after the change.
     */
    public UserChangeRoleResponse(final String userName, final List<UserRoleName> userRoles) {
        this.userName = userName;
        this.userRoles = Collections.unmodifiableList(new ArrayList<>(userRoles));
    }

    /**
     * Returns a defensive copy of the user roles list to ensure that the internal
     * list of roles cannot be modified by external code.
     * <p>
     * This method is designed to return a new mutable list that contains the same
     * user roles as the original list.
     * </p>
     *
     * @return a new {@link List} containing the roles assigned to the user.
     */
    public List<UserRoleName> userRole() {
        return new ArrayList<>(userRoles);
    }

}
