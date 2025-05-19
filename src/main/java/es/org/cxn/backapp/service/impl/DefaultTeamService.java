
package es.org.cxn.backapp.service.impl;

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

import org.springframework.stereotype.Service;

import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.persistence.team.PersistentTeamEntity;
import es.org.cxn.backapp.repository.TeamEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.TeamService;
import es.org.cxn.backapp.service.dto.TeamInfoDto;
import es.org.cxn.backapp.service.dto.UserTeamInfoDto;
import es.org.cxn.backapp.service.exceptions.TeamServiceException;
import jakarta.transaction.Transactional;

/**
 * Default implementation of the {@link TeamService} interface.
 * <p>
 * This service provides operations to manage teams. It uses a
 * {@link TeamEntityRepository} to interact with the underlying database and
 * ensures that the repository is not null.
 * </p>
 *
 * @see TeamService
 * @see TeamEntityRepository
 */
@Service
public final class DefaultTeamService implements TeamService {

    /**
     * The teams repository.
     */
    private final TeamEntityRepository teamRepository;

    /**
     * The user repository.
     */
    private final UserEntityRepository userRepository;

    /**
     * Constructs a new {@code DefaultTeamService} with the given team repository.
     *
     * @param teamRepo the team repository; must not be null.
     * @param userRepo the user repository; must not be null.
     * @throws NullPointerException if {@code teamRepo} is null.
     */
    public DefaultTeamService(final TeamEntityRepository teamRepo, final UserEntityRepository userRepo) {
        super();
        this.teamRepository = checkNotNull(teamRepo, "Received a null pointer as team repository");
        this.userRepository = checkNotNull(userRepo, "Received a null pointer as user repository");
    }

    @Override
    @Transactional
    public TeamInfoDto addAssignedMember(final String teamName, final String userEmail) throws TeamServiceException {

        final var teamOptional = teamRepository.findById(teamName);
        if (teamOptional.isEmpty()) {
            throw new TeamServiceException(teamNotFoundMessage(teamName));
        }
        final var userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new TeamServiceException(userNotFoundMessage(userEmail));
        }
        var userEntity = userOptional.get();
        var teamEntity = teamOptional.get();

        if (userEntity.getTeamAssigned() != null) {
            throw new TeamServiceException("User with email: " + userEmail + " have assigned team.");
        }

        userEntity.setTeamAssigned(teamEntity);
        var teamUsers = teamEntity.getUsersAssigned();
        teamUsers.add(userEntity);
        teamEntity.setUsersAssigned(teamUsers);
        final var savedTeamEntity = teamRepository.save(teamEntity);
        userRepository.save(userEntity);
        return new TeamInfoDto(savedTeamEntity);
    }

    @Override
    @Transactional
    public UserTeamInfoDto addTeamPreference(final String userEmail, final String teamName)
            throws TeamServiceException {
        final var teamOptional = teamRepository.findById(teamName);
        if (teamOptional.isEmpty()) {
            throw new TeamServiceException(teamNotFoundMessage(teamName));
        }
        final var userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new TeamServiceException(userNotFoundMessage(userEmail));
        }

        final var userEntity = userOptional.get();
        final var teamEntity = teamOptional.get();

        userEntity.setTeamPreferred(teamEntity);
        var userPreferredTeamList = teamEntity.getUsersPreferred();
        userPreferredTeamList.add(userEntity);

        final var userWithNoPreference = userRepository.save(userEntity);
        teamRepository.save(teamEntity);

        return new UserTeamInfoDto(userWithNoPreference.getDni(), userWithNoPreference.getEmail(),
                userWithNoPreference.getProfile().getName(), userWithNoPreference.getProfile().getFirstSurname(),
                userWithNoPreference.getProfile().getSecondSurname(), userWithNoPreference.getProfile().getGender(),
                userWithNoPreference.getProfile().getBirthDate().toString(),
                userWithNoPreference.getTeamAssigned() == null ? null
                        : userWithNoPreference.getTeamAssigned().getName(),
                userWithNoPreference.getTeamPreferred() == null ? null
                        : userWithNoPreference.getTeamPreferred().getName());
    }

    @Override
    public TeamInfoDto createTeam(final String name, final String description, final String category)
            throws TeamServiceException {
        if (teamRepository.existsById(name)) {
            throw new TeamServiceException("Team with name: " + name + "exits.");
        } else {

            PersistentTeamEntity team = new PersistentTeamEntity(name, category, description);
            final var savedEntity = teamRepository.save(team);
            return new TeamInfoDto(savedEntity);
        }
    }

    @Override
    public List<TeamInfoDto> getAllTeams() {
        final var teams = teamRepository.findAll();
        ArrayList<TeamInfoDto> responseTeams = new ArrayList<>();
        teams.forEach((PersistentTeamEntity team) -> responseTeams.add(new TeamInfoDto(team)));
        return responseTeams;
    }

    @Override
    public TeamInfoDto getTeamInfo(final String teamName) throws TeamServiceException {
        final var teamOptional = teamRepository.findById(teamName);
        if (teamOptional.isEmpty()) {
            throw new TeamServiceException(teamNotFoundMessage(teamName));
        }
        final var teamEntity = teamOptional.get();
        return new TeamInfoDto(teamEntity);
    }

    @Override
    @Transactional
    public TeamInfoDto removeAssignedMember(final String teamName, final String userEmail) throws TeamServiceException {
        final var teamOptional = teamRepository.findById(teamName);
        if (teamOptional.isEmpty()) {
            throw new TeamServiceException(teamNotFoundMessage(teamName));
        }
        final var userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new TeamServiceException(userNotFoundMessage(userEmail));
        }
        var userEntity = userOptional.get();
        var teamEntity = teamOptional.get();
        if (userEntity.getTeamAssigned() == null) {
            throw new TeamServiceException("User with email: " + userEmail + " no have assigned team.");
        }
        if (!teamEntity.getUsersAssigned().contains(userEntity)) {
            throw new TeamServiceException("User with email: " + userEmail + " no found in team.");
        }
        var teamUsers = teamEntity.getUsersAssigned();
        teamUsers.remove(userEntity);
        userEntity.setTeamAssigned(null);
        userRepository.save(userEntity);
        final var savedTeamEntity = teamRepository.save(teamEntity);

        return new TeamInfoDto(savedTeamEntity);
    }

    @Override
    @Transactional
    public void removeTeam(final String teamName) throws TeamServiceException {
        final var teamOptional = teamRepository.findById(teamName);
        if (teamOptional.isEmpty()) {
            throw new TeamServiceException(teamNotFoundMessage(teamName));
        }
        var teamEntity = teamOptional.get();
        var teamUsers = new ArrayList<>(teamEntity.getUsersAssigned()); // Copia para evitar errores de modificación

        for (UserEntity user : teamUsers) {
            removeAssignedMember(teamName, user.getEmail());
        }
        teamRepository.delete(teamEntity);
    }

    @Override
    @Transactional
    public UserTeamInfoDto removeTeamPreference(final String userEmail) throws TeamServiceException {

        final var userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new TeamServiceException(userNotFoundMessage(userEmail));
        }
        final var userEntity = userOptional.get();
        final var teamEntity = userEntity.getTeamPreferred();

        if (teamEntity == null) {
            throw new TeamServiceException("User with email: " + userEmail + " no have preferred team.");
        }

        if (userEntity.getTeamPreferred() == null) {
            throw new TeamServiceException("User with email: " + userEmail + " no have team preference.");
        }
        if (!teamEntity.getUsersPreferred().contains(userEntity)) {
            throw new TeamServiceException("Team with name: " + teamEntity.getName() + " no contain user with email: "
                    + userEmail + " as users with this team preference");
        }

        final var usersThatPreferTeam = teamEntity.getUsersPreferred();
        usersThatPreferTeam.remove(userEntity);
        teamEntity.setUsersPreferred(usersThatPreferTeam);
        userEntity.setTeamPreferred(null);

        final var userWithNoPreference = userRepository.save(userEntity);
        teamRepository.save(teamEntity);

        return new UserTeamInfoDto(userWithNoPreference.getDni(), userWithNoPreference.getEmail(),
                userWithNoPreference.getProfile().getName(), userWithNoPreference.getProfile().getFirstSurname(),
                userWithNoPreference.getProfile().getSecondSurname(), userWithNoPreference.getProfile().getGender(),
                userWithNoPreference.getProfile().getBirthDate().toString(),
                userWithNoPreference.getTeamAssigned() == null ? null
                        : userWithNoPreference.getTeamAssigned().getName(),
                userWithNoPreference.getTeamPreferred() == null ? null
                        : userWithNoPreference.getTeamPreferred().getName());
    }

    private String teamNotFoundMessage(final String teamName) {
        return ("Team with name: " + teamName + " not found.");
    }

    private String userNotFoundMessage(final String userEmail) {
        return ("User with provided email: " + userEmail + " not found.");
    }

}
