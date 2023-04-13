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

package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.persistence.PersistentUserEntity;

/**
 * Represents the form used for response authenticating user.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller, and mapping all the values from the form. Each of field in the
 * DTO matches a field in the form.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz Perez.
 */
public final class UserListDataResponse implements Serializable {

    /**
     * UID Serial version.
     */
    private static final long serialVersionUID = -6421122331689999079L;

    private List<UserDataResponse> usersList = new ArrayList<UserDataResponse>();

    /**
     * Constructs a DTO for user data response with fields values.
     *
     * @param user the user entity with data.
     */
    public UserListDataResponse(final List<PersistentUserEntity> users) {
        super();
        users.forEach(
                (UserEntity user) -> usersList.add(new UserDataResponse(user))
        );
    }

    public List<UserDataResponse> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<UserDataResponse> usersList) {
        this.usersList = usersList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(usersList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        var other = (UserListDataResponse) obj;
        return Objects.equals(usersList, other.usersList);
    }

    @Override
    public String toString() {
        return "UserListDataResponse [usersList=" + usersList + "]";
    }

}