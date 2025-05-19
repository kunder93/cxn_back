
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.service.TeamService;
import es.org.cxn.backapp.service.dto.UserTeamInfoDto;
import es.org.cxn.backapp.service.exceptions.TeamServiceException;
import jakarta.validation.constraints.Size;

/**
 * Controller class for managing team-related operations such as creation,
 * retrieval, modification, and deletion of teams and their members.
 */
@RestController
@RequestMapping("/api/team/preference")
public class TeamPreferenceController {

    /**
     * Max length of team name.
     */
    private static final int TEAM_NAME_MAX_LENGTH = 100;

    /**
     * The team service.
     */
    private final TeamService teamService;

    /**
     * Constructor for TeamPreferenceController.
     *
     * @param service The TeamService instance for handling team operations.
     */
    public TeamPreferenceController(final TeamService service) {
        super();
        teamService = checkNotNull(service, "Received a null pointer as service");
    }

    /**
     * Adds team as user preference.
     *
     * @param teamName The name of the team.
     * @return The updated team information.
     */
    @PatchMapping("/{teamName}")
    public ResponseEntity<UserTeamInfoDto> addUserToTeam(@PathVariable
    @Size(max = TEAM_NAME_MAX_LENGTH) final String teamName) {
        final var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            final var userInfo = teamService.addTeamPreference(userEmail, teamName);
            return ResponseEntity.ok(userInfo);
        } catch (TeamServiceException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    /**
     * Deletes user own team preference.
     *
     * @return Ok response (HTTP 200) if successful.
     */
    @DeleteMapping()
    public ResponseEntity<UserTeamInfoDto> removeTeam() {
        final var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            final var userInfo = teamService.removeTeamPreference(userEmail);
            return ResponseEntity.ok(userInfo);
        } catch (TeamServiceException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

}
