
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

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.model.form.requests.team.AddUserRequest;
import es.org.cxn.backapp.model.form.requests.team.CreateTeamRequest;
import es.org.cxn.backapp.model.form.responses.team.TeamInfoResponse;
import es.org.cxn.backapp.service.TeamService;
import es.org.cxn.backapp.service.dto.TeamInfoDto;
import es.org.cxn.backapp.service.exceptions.TeamServiceException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Controller class for managing team-related operations such as creation,
 * retrieval, modification, and deletion of teams and their members.
 */
@RestController
@RequestMapping("/api/team")
public class TeamController {

    /**
     * The team service.
     */
    private final TeamService teamService;

    /**
     * Constructor for TeamController.
     *
     * @param service The TeamService instance for handling team operations.
     */
    public TeamController(final TeamService service) {
        super();
        teamService = checkNotNull(service, "Received a null pointer as service");
    }

    /**
     * Adds a user to a team.
     *
     * @param teamName       The name of the team.
     * @param addUserRequest The request dto with email of the user to be added.
     * @return The updated team information.
     */
    @PatchMapping("/{teamName}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('SECRETARIO')")
    public ResponseEntity<TeamInfoResponse> addUserToTeam(@PathVariable
    @Size(max = 100) final String teamName, @RequestBody final AddUserRequest addUserRequest) {
        try {
            final var updatedTeam = teamService.addAssignedMember(teamName, addUserRequest.userEmail());
            return ResponseEntity.ok(new TeamInfoResponse(updatedTeam));
        } catch (TeamServiceException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    /**
     * Creates a new team.
     *
     * @param createTeamRequest The request object containing team details.
     * @return The created team information.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('SECRETARIO')")
    @PostMapping()
    public ResponseEntity<TeamInfoResponse> createTeam(@RequestBody final CreateTeamRequest createTeamRequest) {
        try {
            final var createdTeam = teamService.createTeam(createTeamRequest.name(), createTeamRequest.description(),
                    createTeamRequest.category());
            return ResponseEntity.ok(new TeamInfoResponse(createdTeam));
        } catch (TeamServiceException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    /**
     * Retrieves a list of all teams.
     *
     * @return A list of all teams.
     */
    @GetMapping()
    public ResponseEntity<List<TeamInfoResponse>> getAllTeams() {
        final var teams = teamService.getAllTeams();

        List<TeamInfoResponse> response = new ArrayList<>();
        teams.forEach((TeamInfoDto team) -> response.add(new TeamInfoResponse(team)));
        return ResponseEntity.ok(response);

    }

    /**
     * Retrieves the details of a specific team.
     *
     * @param teamName The name of the team.
     * @return The team information.
     */
    @GetMapping("/{teamName}")
    public ResponseEntity<TeamInfoResponse> getTeamInfo(@PathVariable
    @Size(max = 100) final String teamName) {
        try {
            final var team = teamService.getTeamInfo(teamName);
            return ResponseEntity.ok(new TeamInfoResponse(team));
        } catch (TeamServiceException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    /**
     * Deletes a team.
     *
     * @param teamName The name of the team to be deleted.
     * @return No content response (HTTP 204) if successful.
     */
    @DeleteMapping("/{teamName}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('SECRETARIO')")
    public ResponseEntity<Void> removeTeam(@PathVariable
    @Size(max = 100) final String teamName) {
        try {
            teamService.removeTeam(teamName);
            return ResponseEntity.noContent().build(); // HTTP 204 - No Content
        } catch (TeamServiceException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    /**
     * Removes a user from a team.
     *
     * @param teamName  The name of the team. It must not be blank and must have a
     *                  maximum length of 100 characters.
     * @param userEmail The email of the user to be removed. It must be a valid
     *                  email format and must not be blank.
     * @return The updated team information.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('SECRETARIO')")
    @DeleteMapping("/{teamName}/{userEmail}")
    public ResponseEntity<TeamInfoResponse> removeUserFromTeam(@PathVariable
    @NotBlank
    @Size(max = 100) final String teamName,
            @PathVariable
            @NotBlank
            @Email String userEmail) {
        try {
            final var updatedTeam = teamService.removeAssignedMember(teamName, userEmail);
            return ResponseEntity.ok(new TeamInfoResponse(updatedTeam));
        } catch (TeamServiceException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

}
