
package es.org.cxn.backapp.controller.entity;

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

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.model.form.requests.UserChangeRoleRequest;
import es.org.cxn.backapp.model.form.responses.UserChangeRoleResponse;
import es.org.cxn.backapp.service.RoleService;
import es.org.cxn.backapp.service.exceptions.UserServiceException;

/**
 * Rest controller for the example entities.
 *
 * @author Santiago Paz Perez.
 */
@RestController
@RequestMapping("/api/user/role")
public class RoleController {

    /**
     * The admin role.
     */
    public static final String ADMIN_ROLE = "ADMIN";

    /**
     * The role service.
     */
    private final RoleService roleService;

    /**
     * Create a a controller with the specified dependencies.
     *
     * @param serv user service
     */
    public RoleController(final RoleService serv) {
        super();

        roleService = checkNotNull(serv, "Received a null pointer as user service");
    }

    /**
     * Changes role to user.
     *
     * @param userChangeRoleRequestForm form with data to change user role.
     * @return form with the deleted user data.
     */
    @PatchMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<UserChangeRoleResponse> changeUserRoles(
            @RequestBody final UserChangeRoleRequest userChangeRoleRequestForm) {

        final var roles = userChangeRoleRequestForm.userRoles();
        try {
            roleService.changeUserRoles(userChangeRoleRequestForm.email(), roles);
        } catch (UserServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return new ResponseEntity<>(
                new UserChangeRoleResponse(userChangeRoleRequestForm.email(), userChangeRoleRequestForm.userRoles()),
                HttpStatus.OK);
    }

}
