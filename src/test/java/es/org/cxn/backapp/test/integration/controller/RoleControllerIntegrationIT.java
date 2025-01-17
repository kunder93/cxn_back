
package es.org.cxn.backapp.test.integration.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.model.UserRoleName;
import es.org.cxn.backapp.model.form.requests.UserChangeRoleRequest;
import es.org.cxn.backapp.model.form.responses.SignUpResponseForm;
import es.org.cxn.backapp.model.form.responses.UserChangeRoleResponse;
import es.org.cxn.backapp.service.impl.DefaultEmailService;
import es.org.cxn.backapp.test.utils.LocalDateAdapter;
import es.org.cxn.backapp.test.utils.UsersControllerFactory;

/**
 * @author Santiago Paz. Authentication controller integration tests.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("/application.properties")
class RoleControllerIntegrationIT {

    /**
     * URL endpoint for user signup.
     */
    private static final String SIGN_UP_URL = "/api/auth/signup";

    /**
     * URL endpoint for role management.
     */
    private static final String ROLES_URL = "/api/user/role";

    /**
     * Gson instance for JSON serialization and deserialization.
     */
    private static Gson gson;

    @BeforeAll
    static void setup() {
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
    }

    /**
     * MockMvc instance used for performing HTTP requests in tests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked email service.
     */
    @MockitoBean
    private DefaultEmailService emailService;

    /**
     * Main class constructor.
     */
    RoleControllerIntegrationIT() {
        super();
    }

    /**
     * Add role to not existing user returns bad request.
     *
     * @throws Exception When fails.
     */
    @DisplayName("Add role to not existing user is bad request.")
    @Test
    @Transactional
    @WithMockUser(username = "santi@santi.es", roles = { "ADMIN" })
    void testAddRoleToNotExistingUserBadRequest() throws Exception {
        var userRolesList = new ArrayList<UserRoleName>();
        userRolesList.add(UserRoleName.ROLE_TESORERO);

        var notExisitngUserEmail = "NotExistingEmail@Email.com";
        var userChangeRoleRequest = new UserChangeRoleRequest(notExisitngUserEmail, userRolesList);
        var userChangeRoleRequestJson = gson.toJson(userChangeRoleRequest);
        mockMvc.perform(patch(ROLES_URL).contentType(MediaType.APPLICATION_JSON).content(userChangeRoleRequestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Put a list of roles with less roles. Check result.
     *
     * @throws Exception When fails.
     */
    @DisplayName("Change user roles to less roles")
    @Test
    @Transactional
    @WithMockUser(username = "santi@santi.es", roles = { "ADMIN" })
    void testChangeRolesToLessRolesCheckRoles() throws Exception {
        var userRolesList = new ArrayList<UserRoleName>();
        userRolesList.add(UserRoleName.ROLE_SOCIO);
        userRolesList.add(UserRoleName.ROLE_TESORERO);
        var userARequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var userARequestJson = gson.toJson(userARequest);
        // Register user correctly
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        var userChangeRoleRequestForm = new UserChangeRoleRequest(UsersControllerFactory.USER_A_EMAIL, userRolesList);
        var userChangeRoleRequestJson = gson.toJson(userChangeRoleRequestForm);
        mockMvc.perform(patch(ROLES_URL).contentType(MediaType.APPLICATION_JSON).content(userChangeRoleRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
        userRolesList = new ArrayList<UserRoleName>();
        userRolesList.add(UserRoleName.ROLE_SOCIO);

        userChangeRoleRequestForm = new UserChangeRoleRequest(UsersControllerFactory.USER_A_EMAIL, userRolesList);
        userChangeRoleRequestJson = gson.toJson(userChangeRoleRequestForm);
        var rolesFinalResponseJson = mockMvc
                .perform(patch(ROLES_URL).contentType(MediaType.APPLICATION_JSON).content(userChangeRoleRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();

        var rolesFinalResponse = gson.fromJson(rolesFinalResponseJson, UserChangeRoleResponse.class);

        Assertions.assertEquals(UsersControllerFactory.USER_A_EMAIL, rolesFinalResponse.userName(),
                "userName with roles is user A email.");
        Assertions.assertEquals(1, rolesFinalResponse.userRoles().size(), "Only 1 role in roles list");
        Assertions.assertTrue(rolesFinalResponse.userRoles().contains(UserRoleName.ROLE_SOCIO),
                "the only one role is ROLE_SOCIO");
    }

    /**
     * Register user and expect user role default user role.
     *
     * @throws Exception When fails.
     */
    @DisplayName("Create user and expect only default user role.")
    @Test
    @Transactional
    void testCreateUserExpectDefaultUserRole() throws Exception {
        var userARequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var userARequestJson = gson.toJson(userARequest);

        // Register user correctly
        var responseJson = mockMvc
                .perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();

        var response = gson.fromJson(responseJson, SignUpResponseForm.class);
        Assertions.assertEquals(response.userRoles().size(), Integer.valueOf(1), "Only have one role.");
        Assertions.assertTrue(response.userRoles().contains(UserRoleName.ROLE_CANDIDATO_SOCIO),
                "Default user role is CANDIDATO_SOCIO.");
    }

    /**
     * Add role to created user and expect it in user data.
     *
     * @throws Exception When fails.
     */
    @DisplayName("Add role to created user expect user data with role added.")
    @Test
    @Transactional
    @WithMockUser(username = "santi@santi.es", roles = { "ADMIN" })
    void testUserChangeRolesToMoreRolesCheckRoles() throws Exception {
        List<UserRoleName> userRoleListToSet = new ArrayList<>();
        userRoleListToSet.add(UserRoleName.ROLE_TESORERO);
        userRoleListToSet.add(UserRoleName.ROLE_SOCIO);
        var userARequest = UsersControllerFactory.getSignUpRequestFormUserA();
        var userARequestJson = gson.toJson(userARequest);
        // Register user correctly
        mockMvc.perform(post(SIGN_UP_URL).contentType(MediaType.APPLICATION_JSON).content(userARequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        var userChangeRoleRequest = new UserChangeRoleRequest(UsersControllerFactory.USER_A_EMAIL, userRoleListToSet);
        var userChangeRoleRequestJson = gson.toJson(userChangeRoleRequest);
        var addRoleResponseJson = mockMvc
                .perform(patch(ROLES_URL).contentType(MediaType.APPLICATION_JSON).content(userChangeRoleRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        var addRoleResponse = gson.fromJson(addRoleResponseJson, UserChangeRoleResponse.class);

        Assertions.assertEquals(addRoleResponse.userRoles().size(), Integer.valueOf(2), "User have 2 roles.");
        Assertions.assertTrue(addRoleResponse.userRoles().contains(UserRoleName.ROLE_TESORERO),
                "response have userRoles list and contains role_tesorero");
        Assertions.assertTrue(addRoleResponse.userRoles().contains(UserRoleName.ROLE_SOCIO),
                "response have userRoles list and contains role_socio");
        Assertions.assertEquals(UsersControllerFactory.USER_A_EMAIL, addRoleResponse.userName(),
                "user name is expected created user email");
    }
}
