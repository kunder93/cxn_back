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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the form used for response change role user.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz Perez.
 */
public final class UserChangeRoleResponseForm implements Serializable {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 343376664044429081L;
    /**
     * userName field with user email.
     */
    private String userName;
    /**
     * userRoles field.
     */
    private List<String> userRoles;

    /**
     * Constructs a DTO for user info response.
     */
    public UserChangeRoleResponseForm() {
        super();
    }

    /**
     * Constructs a DTO for user info response with fields values.
     *
     * @param userNameValue the userName field.
     * @param userRolesList the userRoles field.
     */
    public UserChangeRoleResponseForm(
            String userNameValue, List<String> userRolesList
    ) {
        super();
        this.userName = userNameValue;
        this.userRoles = new ArrayList<>(userRolesList);
    }

    /**
     * Returns the value userName field with user email.
     *
     * @return the value userName field same as email.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns the value of the userRoles field.
     *
     * @return the value of the userRoles field.
     */
    public List<String> getUserRoles() {
        return new ArrayList<>(userRoles);
    }

    /**
     * Sets the value of the userName field.
     *
     * @param userName the new value for the userName field.
     */
    public void setUserName(final String userName) {
        this.userName = checkNotNull(
                userName, "Received a null pointer as name"
        );
    }

    /**
     * Sets the value of the userRoles field.
     *
     * @param userRoles the new value for the userName field.
     */
    public void setUserName(final List<String> userRoles) {
        this.userRoles = checkNotNull(
                userRoles, "Received a null pointer as name"
        );
    }

    /**
     * Hash code method.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userName, userRoles);
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
        var other = (UserChangeRoleResponseForm) obj;
        return Objects.equals(userName, other.userName)
                && Objects.equals(userRoles, other.userRoles);
    }

    /**
     * To string method.
     */
    @Override
    public String toString() {
        return "UserChangeRoleResponseForm [userName=" + userName
                + ", userRoles=" + userRoles + "]";
    }

}
