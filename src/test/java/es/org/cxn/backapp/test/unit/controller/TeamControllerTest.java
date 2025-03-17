package es.org.cxn.backapp.test.unit.controller;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.org.cxn.backapp.controller.entity.TeamController;
import es.org.cxn.backapp.model.form.requests.team.CreateTeamRequest;
import es.org.cxn.backapp.service.TeamService;
import es.org.cxn.backapp.service.dto.TeamInfoDto;
import es.org.cxn.backapp.service.dto.UserTeamInfoDto;
import es.org.cxn.backapp.service.exceptions.TeamServiceException;

/**
 * Test class for {@link TeamController}. This class contains unit tests for the
 * methods in the {@link TeamController} class. It uses {@link WebMvcTest} for
 * testing the controller layer of the application.
 */
@WebMvcTest(TeamController.class)
@AutoConfigureMockMvc(addFilters = false)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TeamService teamService;

    /**
     * Test for successfully adding a user to a team. It tests the scenario where a
     * user is successfully added to an existing team.
     *
     * @throws Exception if an exception occurs during the test execution
     */
    @Test
    void addUserToTeam_Success() throws Exception {
        UserTeamInfoDto userDto = new UserTeamInfoDto("12345678A", "user@test.com", "John", "Doe", "Smith", "Male",
                "1990-01-01");
        TeamInfoDto mockDto = new TeamInfoDto("team1", "Description", "Category", List.of(userDto));
        when(teamService.addMember("team1", "user@test.com")).thenReturn(mockDto);

        String requestBody = "{\"userEmail\": \"user@test.com\"}";

        mockMvc.perform(patch("/api/team/team1").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("team1"))
                .andExpect(jsonPath("$.members[0].email").value("user@test.com"));

        verify(teamService).addMember("team1", "user@test.com");
    }

    /**
     * Test for adding a user to a team when the team service throws an exception.
     * It tests the scenario where an exception is thrown due to an invalid user.
     *
     * @throws Exception if an exception occurs during the test execution
     */
    @Test
    void addUserToTeam_TeamServiceException() throws Exception {
        when(teamService.addMember("team1", "invalid@user.com"))
                .thenThrow(new TeamServiceException("Exception message"));

        String requestBody = "{\"userEmail\": \"invalid@user.com\"}";

        mockMvc.perform(patch("/api/team/team1").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("400 BAD_REQUEST \"Exception message\""))
                .andExpect(jsonPath("$.status").value("failure"));
    }

    /**
     * Test for creating a team when the team service throws an exception. It tests
     * the scenario where an exception is thrown due to invalid team details.
     *
     * @throws Exception if an exception occurs during the test execution
     */
    @Test
    void createTeam_ExceptionThrown() throws Exception {
        when(teamService.createTeam(any(), any(), any())).thenThrow(new TeamServiceException("Exception message"));

        CreateTeamRequest request = new CreateTeamRequest("Invalid", "Invalid", "Invalid");
        mockMvc.perform(post("/api/team").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("400 BAD_REQUEST \"Exception message\""));
    }

    /**
     * Test for successfully creating a team. It tests the scenario where a team is
     * successfully created.
     *
     * @throws Exception if an exception occurs during the test execution
     */
    @Test
    void createTeam_Success() throws Exception {
        CreateTeamRequest request = new CreateTeamRequest("team1", "Description", "Category");
        TeamInfoDto mockDto = new TeamInfoDto("team1", "Description", "Category", Collections.emptyList());
        when(teamService.createTeam(any(), any(), any())).thenReturn(mockDto);

        mockMvc.perform(post("/api/team").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("team1")).andExpect(jsonPath("$.members").isEmpty());
        verify(teamService).createTeam("team1", "Description", "Category");
    }

    /**
     * Test for retrieving all teams when there are no teams. It tests the scenario
     * where the team list is empty.
     *
     * @throws Exception if an exception occurs during the test execution
     */
    @Test
    void getAllTeams_Empty() throws Exception {
        when(teamService.getAllTeams()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/team")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
    }

    /**
     * Test for successfully retrieving all teams. It tests the scenario where
     * multiple teams are returned by the service.
     *
     * @throws Exception if an exception occurs during the test execution
     */
    @Test
    void getAllTeams_Success() throws Exception {
        UserTeamInfoDto user1 = new UserTeamInfoDto("dni1", "user1@test.com", "Alice", "Smith", "Brown", "Female",
                "1985-05-05");
        UserTeamInfoDto user2 = new UserTeamInfoDto("dni2", "user2@test.com", "Bob", "Johnson", "Green", "Male",
                "1990-10-10");
        TeamInfoDto team1 = new TeamInfoDto("team1", "Desc1", "Cat1", List.of(user1));
        TeamInfoDto team2 = new TeamInfoDto("team2", "Desc2", "Cat2", List.of(user2));
        when(teamService.getAllTeams()).thenReturn(List.of(team1, team2));

        mockMvc.perform(get("/api/team")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("team1"))
                .andExpect(jsonPath("$[0].members[0].email").value("user1@test.com"))

                .andExpect(jsonPath("$[1].name").value("team2"))
                .andExpect(jsonPath("$[1].members[0].email").value("user2@test.com"));

    }

    /**
     * Test for retrieving team information when the team service throws an
     * exception. It tests the scenario where an exception is thrown due to a
     * non-existent team.
     *
     * @throws Exception if an exception occurs during the test execution
     */
    @Test
    void getTeamInfo_Exception() throws Exception {
        when(teamService.getTeamInfo("unknown")).thenThrow(new TeamServiceException("Exception  message"));

        mockMvc.perform(get("/api/team/unknown")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("400 BAD_REQUEST \"Exception  message\""))
                .andExpect(jsonPath("$.status").value("failure"));
    }

    /**
     * Test for successfully retrieving team information. It tests the scenario
     * where a team is successfully fetched by the team service.
     *
     * @throws Exception if an exception occurs during the test execution
     */
    @Test
    void getTeamInfo_Success() throws Exception {
        UserTeamInfoDto user = new UserTeamInfoDto("dni3", "member@test.com", "Charlie", "Davis", "White", "Male",
                "2000-01-01");
        TeamInfoDto mockDto = new TeamInfoDto("team1", "Desc", "Cat", List.of(user));
        when(teamService.getTeamInfo("team1")).thenReturn(mockDto);

        mockMvc.perform(get("/api/team/team1")).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("team1"))
                .andExpect(jsonPath("$.description").value("Desc")).andExpect(jsonPath("$.category").value("Cat"))
                .andExpect(jsonPath("$.members[0].dni").value("dni3"))
                .andExpect(jsonPath("$.members[0].email").value("member@test.com"))
                .andExpect(jsonPath("$.members[0].name").value("Charlie"))
                .andExpect(jsonPath("$.members[0].firstSurname").value("Davis"))
                .andExpect(jsonPath("$.members[0].secondSurname").value("White"))
                .andExpect(jsonPath("$.members[0].birthDate").value("2000-01-01"))
                .andExpect(jsonPath("$.members[0].gender").value("Male"));
    }

    /**
     * Test for removing a team when the team service throws an exception. It tests
     * the scenario where an exception is thrown when attempting to remove a
     * non-existent team.
     *
     * @throws Exception if an exception occurs during the test execution
     */
    @Test
    void removeTeam_Exception() throws Exception {
        doThrow(new TeamServiceException("Exception message")).when(teamService).removeTeam("unknown");

        mockMvc.perform(delete("/api/team/unknown")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("400 BAD_REQUEST \"Exception message\""))
                .andExpect(jsonPath("$.status").value("failure"));
    }

    /**
     * Test for successfully removing a team. It tests the scenario where a team is
     * successfully removed.
     *
     * @throws Exception if an exception occurs during the test execution
     */
    @Test
    void removeTeam_Success() throws Exception {
        mockMvc.perform(delete("/api/team/team1")).andExpect(status().isNoContent());

        verify(teamService).removeTeam("team1");
    }

    /**
     * Test for removing a user from a team when the user is not part of the team.
     * It tests the scenario where an exception is thrown due to the user not being
     * in the team.
     *
     * @throws Exception if an exception occurs during the test execution
     */
    @Test
    void removeUserFromTeam_ExceptionThrown() throws Exception {
        when(teamService.removeMember("team1", "invalid@user.com"))
                .thenThrow(new TeamServiceException("User not in team"));

        mockMvc.perform(delete("/api/team/team1/invalid@user.com")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("400 BAD_REQUEST \"User not in team\""))

                .andExpect(jsonPath("$.status").value("failure"));
    }

    /**
     * Test for successfully removing a user from a team. It tests the scenario
     * where a user is successfully removed from a team.
     *
     * @throws Exception if an exception occurs during the test execution
     */
    @Test
    void removeUserFromTeam_Success() throws Exception {
        TeamInfoDto mockDto = new TeamInfoDto("team1", "Desc", "Cat", Collections.emptyList());
        when(teamService.removeMember("team1", "user@test.com")).thenReturn(mockDto);

        mockMvc.perform(delete("/api/team/team1/user@test.com")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("team1")).andExpect(jsonPath("$.members").isEmpty());
    }
}
