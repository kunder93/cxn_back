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

package es.org.cxn.backapp.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.exceptions.RoleNameNotFoundException;
import es.org.cxn.backapp.exceptions.UserEmailNotFoundException;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.form.UserChangeRoleRequestForm;
import es.org.cxn.backapp.model.form.UserChangeRoleResponseForm;
import es.org.cxn.backapp.service.RoleService;
import es.org.cxn.backapp.service.UserService;

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
    final private String adminRole = "ADMIN";

    /**
     * The role service.
     */
    private final RoleService roleService;

    /**
     * The user service.
     */
    private final UserService userService;

    /**
     * Create a a controller with the specified dependencies.
     *
     * @param service role service
     * @param serv    user service
     */
    public RoleController(final RoleService service, final UserService serv) {
        super();

        roleService = checkNotNull(
                service, "Received a null pointer as role service"
        );
        userService = checkNotNull(
                serv, "Received a null pointer as user service"
        );
    }

    /**
     * Changes role to user.
     *
     * @param userChangeRoleRequestForm form with data to change user role.
     * @return form with the deleted user data.
     */
    @CrossOrigin
    @PostMapping()
    public ResponseEntity<UserChangeRoleResponseForm> addRoleToUser(
            @RequestBody final UserChangeRoleRequestForm userChangeRoleRequestForm
    ) {
        var user = SecurityContextHolder.getContext().getAuthentication();
        List<String> roleList = new ArrayList<>();

        if (user.getAuthorities()
                .contains(new SimpleGrantedAuthority(adminRole))) {
            UserEntity userWithAddedRole;
            try {
                userWithAddedRole = userService.addRole(
                        userChangeRoleRequestForm.getUserEmail(),
                        userChangeRoleRequestForm.getRoleName()
                );
                userWithAddedRole.getRoles()
                        .forEach(e -> roleList.add(e.getName()));
                return new ResponseEntity<>(
                        new UserChangeRoleResponseForm(
                                userWithAddedRole.getEmail(), roleList
                        ), null, HttpStatus.OK
                );
            } catch (UserEmailNotFoundException | RoleNameNotFoundException e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage(), e
                );
            }
        }
        return new ResponseEntity<>(
                new UserChangeRoleResponseForm(
                        userChangeRoleRequestForm.getUserEmail(), roleList
                ), null, HttpStatus.FORBIDDEN
        );
    }

    /**
     * Delete role from user.
     *
     * @param userChangeRoleRequestForm form with data to change user role.
     * @return form with the deleted user data.
     */
    @DeleteMapping()
    public ResponseEntity<UserChangeRoleResponseForm> deleteRoleFromUser(
            @RequestBody final UserChangeRoleRequestForm userChangeRoleRequestForm
    ) {
        var user = SecurityContextHolder.getContext().getAuthentication();
        List<String> roleList = new ArrayList<>();
        UserEntity userWithAddedRole;

        if (user.getAuthorities()
                .contains(new SimpleGrantedAuthority(adminRole))
                && !userChangeRoleRequestForm.getRoleName().equals(adminRole)) {

            try {
                userWithAddedRole = userService.removeRole(
                        userChangeRoleRequestForm.getUserEmail(),
                        userChangeRoleRequestForm.getRoleName()
                );
                userWithAddedRole.getRoles()
                        .forEach(e -> roleList.add(e.getName()));
                return new ResponseEntity<>(
                        new UserChangeRoleResponseForm(
                                userWithAddedRole.getEmail(), roleList
                        ), null, HttpStatus.OK
                );
            } catch (UserEmailNotFoundException | RoleNameNotFoundException e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage(), e
                );
            }
        }
        return new ResponseEntity<>(
                new UserChangeRoleResponseForm(
                        userChangeRoleRequestForm.getUserEmail(), roleList
                ), null, HttpStatus.FORBIDDEN
        );
    }
}
