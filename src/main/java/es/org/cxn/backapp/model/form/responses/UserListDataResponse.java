/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
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

package es.org.cxn.backapp.model.form.responses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import es.org.cxn.backapp.model.UserEntity;

/**
 * Represents the form used for response authenticating user.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller, and mapping all the values from the form. Each field in the DTO
 * matches a field in the form.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @param usersList The users list with UserDataResponse.
 * @author Santiago Paz Perez.
 */
public record UserListDataResponse(List<UserDataResponse> usersList) {

    /**
     * Constructs a {@code UserListDataResponse} object with a list of user data
     * responses. The list is made immutable to prevent modification of the data
     * once the object is created.
     *
     * @param usersList the list of user data response objects to initialize the
     *                  DTO.
     */
    public UserListDataResponse(final List<UserDataResponse> usersList) {
        this.usersList = Collections.unmodifiableList(usersList);
    }

    /**
     * Constructs a DTO for user data response with fields values.
     *
     * @param users the collection of user entities with data.
     * @return UserListDataResponse object.
     */
    public static UserListDataResponse fromUserEntities(final Collection<UserEntity> users) {
        return new UserListDataResponse(users.stream().map(UserDataResponse::new).toList());
    }

    /**
     * Returns a copy of the list of user data responses.
     *
     * @return A new list containing all the user data responses in the original
     *         list.
     */
    public List<UserDataResponse> usersList() {
        return new ArrayList<>(usersList);
    }

}
