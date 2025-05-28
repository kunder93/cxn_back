
package es.org.cxn.backapp.service;

import java.util.List;

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

import es.org.cxn.backapp.service.dto.TeamInfoDto;
import es.org.cxn.backapp.service.dto.UserTeamInfoDto;
import es.org.cxn.backapp.service.exceptions.TeamServiceException;

/**
 * Service interface for managing teams and their members.
 * <p>
 * This service provides methods to create, retrieve, update, and delete teams,
 * as well as add or remove members from a team.
 * </p>
 *
 * @author Santiago Paz
 */
public interface TeamService {

    /**
     * Adds a member to an existing team.
     *
     * @param teamName  the name of the team to which the user will be added.
     * @param userEmail the email of the user to add to the team.
     * @return a {@link TeamInfoDto} representing the updated team information.
     * @throws TeamServiceException if the team or user does not exist, or if an
     *                              error occurs while adding the member.
     */
    TeamInfoDto addAssignedMember(String teamName, String userEmail) throws TeamServiceException;

    /**
     * Adds or remove a team preference for a user.
     *
     * @param userEmail the email of the user to assign the team preference.
     * @param teamName  the name of the team to set as preference.
     * @return a {@link UserTeamInfoDto} representing the updated user-team
     *         preference information.
     * @throws TeamServiceException if the user or team does not exist or if the
     *                              operation fails.
     */
    UserTeamInfoDto addOrRemoveTeamPreference(String userEmail, String teamName) throws TeamServiceException;

    /**
     * Creates a new team.
     *
     * @param name        the name of the team.
     * @param description a brief description of the team.
     * @param category    the category to which the team belongs.
     * @return a {@link TeamInfoDto} representing the created team.
     * @throws TeamServiceException when team with name exists.
     */
    TeamInfoDto createTeam(String name, String description, String category) throws TeamServiceException;

    /**
     * Get all teams with members.
     *
     * @return all teams with info and members info found.
     */
    List<TeamInfoDto> getAllTeams();

    /**
     * Retrieves information about a team.
     *
     * @param teamName the name of the team to retrieve.
     * @return a {@link TeamInfoDto} containing the team's details.
     * @throws TeamServiceException if the team does not exist.
     */
    TeamInfoDto getTeamInfo(String teamName) throws TeamServiceException;

    /**
     * Removes a member from a team.
     *
     * @param teamName  the name of the team from which the user will be removed.
     * @param userEmail the email of the user to remove from the team.
     * @return a {@link TeamInfoDto} representing the updated team information.
     * @throws TeamServiceException if the team or user does not exist, or if the
     *                              user is not a member of the team.
     */
    TeamInfoDto removeAssignedMember(String teamName, String userEmail) throws TeamServiceException;

    /**
     * Deletes a team and removes all its members.
     *
     * @param teamName the name of the team to delete.
     * @throws TeamServiceException if the team does not exist or if an error occurs
     *                              during deletion.
     */
    void removeTeam(String teamName) throws TeamServiceException;

    /**
     * Removes the team preference for a specific user.
     *
     * @param userEmail the email of the user whose team preference is to be
     *                  removed.
     * @return a {@link UserTeamInfoDto} representing the updated user-team
     *         preference state.
     * @throws TeamServiceException if the user does not exist or if the operation
     *                              fails.
     */
    UserTeamInfoDto removeTeamPreference(String userEmail) throws TeamServiceException;
}
