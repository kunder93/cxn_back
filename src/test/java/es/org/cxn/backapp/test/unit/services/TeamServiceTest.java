package es.org.cxn.backapp.test.unit.services;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.org.cxn.backapp.model.persistence.team.PersistentTeamEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.repository.TeamEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.dto.TeamInfoDto;
import es.org.cxn.backapp.service.exceptions.TeamServiceException;
import es.org.cxn.backapp.service.impl.DefaultTeamService;

/**
 * Unit tests for the {@link DefaultTeamService} class, which is responsible for
 * managing teams and users within the application.
 * <p>
 * This class contains test cases for verifying the correct behavior of the
 * methods within the {@link DefaultTeamService} class, such as adding and
 * removing members to/from a team, creating teams, and handling exceptions when
 * teams or users are not found.
 * </p>
 *
 * <p>
 * The tests use the Mockito framework to mock the necessary dependencies, such
 * as the {@link UserEntityRepository} and {@link TeamEntityRepository}, and
 * inject them into the {@link DefaultTeamService} instance.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    /**
     * Constant representing the name of a sample team used in test cases.
     */
    private static final String TEAM_NAME = "ChessTeam";

    /**
     * Constant representing the email of a sample user used in test cases.
     */
    private static final String USER_EMAIL = "user@email.com";

    /**
     * Mock of the {@link UserEntityRepository} used to interact with the user
     * repository. This mock will simulate the behavior of the actual repository in
     * the test cases.
     */
    @Mock
    private UserEntityRepository userRepository;

    /**
     * Mock of the {@link TeamEntityRepository} used to interact with the team
     * repository. This mock will simulate the behavior of the actual repository in
     * the test cases.
     */
    @Mock
    private TeamEntityRepository teamEntityRepository;

    /**
     * Instance of {@link DefaultTeamService} with dependencies injected via
     * Mockito. This is the service being tested.
     */
    @InjectMocks
    private DefaultTeamService teamService;

    /**
     * A sample instance of {@link PersistentTeamEntity} used for test cases. This
     * represents a team entity in the repository and is used for testing
     * team-related methods.
     */
    private PersistentTeamEntity teamEntity;

    /**
     * A sample instance of {@link PersistentUserEntity} used for test cases. This
     * represents a user entity in the repository and is used for testing
     * user-related methods.
     */
    private PersistentUserEntity userEntity;

    /**
     * Test case to verify that an exception is thrown when trying to add a member
     * to a team that does not exist.
     * <p>
     * This test simulates the case where the team does not exist in the repository,
     * and the method should throw a {@link TeamServiceException} with the message
     * "Team with name: {teamName} not found.".
     * </p>
     *
     * @throws TeamServiceException if an exception occurs during the process
     */
    @Test
    void addMemberShouldThrowExceptionWhenTeamNotFound() {
        String teamName = "nonexistent-team";
        String userEmail = "user@email.com";

        when(teamEntityRepository.findById(teamName)).thenReturn(Optional.empty());

        TeamServiceException exception = assertThrows(TeamServiceException.class, () -> {
            teamService.addAssignedMember(teamName, userEmail);
        });

        assertEquals("Team with name: " + teamName + " not found.", exception.getMessage());
    }

    /**
     * Test case to verify that an exception is thrown when trying to add a member
     * to a team when the user does not exist in the repository.
     * <p>
     * This test simulates the case where the user does not exist, and the method
     * should throw a {@link TeamServiceException} with the message "User with
     * provided email: {userEmail} not found.".
     * </p>
     *
     * @throws TeamServiceException if an exception occurs during the process
     */
    @Test
    void addMemberShouldThrowExceptionWhenUserNotFound() {
        String teamName = "team-name";
        String userEmail = "nonexistent-user@email.com";

        when(teamEntityRepository.findById(teamName)).thenReturn(Optional.of(new PersistentTeamEntity()));
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        TeamServiceException exception = assertThrows(TeamServiceException.class, () -> {
            teamService.addAssignedMember(teamName, userEmail);
        });

        assertEquals("User with provided email: " + userEmail + " not found.", exception.getMessage());
    }

    @BeforeEach
    void setUp() {
        UserProfile userProfile = new UserProfile();
        userProfile.setName("UserName");
        userProfile.setFirstSurname("FirstSurname");
        userProfile.setSecondSurname("SecondSurname");
        userProfile.setGender("male");
        userProfile.setBirthDate(LocalDate.of(1991, 5, 19));
        teamEntity = new PersistentTeamEntity(TEAM_NAME, "Primera", "A chess team");
        userEntity = new PersistentUserEntity();
        userEntity.setProfile(userProfile);
        userEntity.setEmail(USER_EMAIL);
    }

    /**
     * Test case to verify that a user is successfully added to a team.
     *
     * @throws TeamServiceException if any exception occurs during the process
     */
    @Test
    void shouldAddMemberToTeam() throws TeamServiceException {
        when(teamEntityRepository.findById(TEAM_NAME)).thenReturn(Optional.of(teamEntity));
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(userEntity));
        when(teamEntityRepository.save(any(PersistentTeamEntity.class))).thenReturn(teamEntity);

        TeamInfoDto result = teamService.addAssignedMember(TEAM_NAME, USER_EMAIL);

        assertThat(result).isNotNull();
        assertThat(result.users()).isNotEmpty();
        verify(teamEntityRepository).save(any(PersistentTeamEntity.class));
        verify(userRepository).save(any(PersistentUserEntity.class));
    }

    /**
     * Test case to verify that a team can be successfully created.
     *
     * @throws TeamServiceException when team already exists.
     */
    @Test
    void shouldCreateTeam() throws TeamServiceException {
        when(teamEntityRepository.save(any(PersistentTeamEntity.class))).thenReturn(teamEntity);

        TeamInfoDto result = teamService.createTeam(TEAM_NAME, "A chess team", "Primera");

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(TEAM_NAME);
        verify(teamEntityRepository).save(any(PersistentTeamEntity.class));
    }

    /**
     * Test case to verify that team information can be retrieved.
     *
     * @throws TeamServiceException if any exception occurs during the process
     */
    @Test
    void shouldGetTeamInfo() throws TeamServiceException {
        when(teamEntityRepository.findById(TEAM_NAME)).thenReturn(Optional.of(teamEntity));

        TeamInfoDto result = teamService.getTeamInfo(TEAM_NAME);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(TEAM_NAME);
        verify(teamEntityRepository).findById(TEAM_NAME);
    }

    /**
     * Test case to verify that a user can be successfully removed from a team.
     *
     * @throws TeamServiceException if any exception occurs during the process
     */
    @Test
    void shouldRemoveMemberFromTeam() throws TeamServiceException {
        teamEntity.setUsersAssigned(new ArrayList<>(List.of(userEntity)));
        userEntity.setTeamAssigned(teamEntity);

        when(teamEntityRepository.findById(TEAM_NAME)).thenReturn(Optional.of(teamEntity));
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(userEntity));
        when(teamEntityRepository.save(any(PersistentTeamEntity.class))).thenReturn(teamEntity);

        TeamInfoDto result = teamService.removeAssignedMember(TEAM_NAME, USER_EMAIL);

        assertThat(result).isNotNull();
        assertThat(result.users()).isEmpty();
        verify(teamEntityRepository).save(any(PersistentTeamEntity.class));
        verify(userRepository).save(any(PersistentUserEntity.class));
    }

    /**
     * Test case to verify that a team can be successfully removed.
     *
     * @throws TeamServiceException if any exception occurs during the process
     */
    @Test
    void shouldRemoveTeam() throws TeamServiceException {
        when(teamEntityRepository.findById(TEAM_NAME)).thenReturn(Optional.of(teamEntity));

        teamService.removeTeam(TEAM_NAME);

        verify(teamEntityRepository).delete(any(PersistentTeamEntity.class));
    }

    /**
     * Test case to verify that removing a team with multiple members works
     * correctly.
     *
     * @throws TeamServiceException if any exception occurs during the process
     */
    @Test
    void shouldRemoveTeamWithSeveralMembers() throws TeamServiceException {
        PersistentUserEntity user1 = new PersistentUserEntity();
        UserProfile userProfile1 = new UserProfile();
        userProfile1.setName("User1");
        userProfile1.setFirstSurname("FirstSurname1");
        userProfile1.setSecondSurname("SecondSurname1");
        userProfile1.setBirthDate(LocalDate.of(1991, 5, 23));
        user1.setEmail("user1@email.com");
        user1.setProfile(userProfile1);
        user1.setTeamAssigned(teamEntity);

        PersistentUserEntity user2 = new PersistentUserEntity();
        UserProfile userProfile2 = new UserProfile();
        userProfile2.setName("User2");
        userProfile2.setFirstSurname("FirstSurname2");
        userProfile2.setSecondSurname("SecondSurname2");
        userProfile2.setBirthDate(LocalDate.of(1991, 5, 23));
        user2.setEmail("user2@email.com");
        user2.setProfile(userProfile2);
        user2.setTeamAssigned(teamEntity);

        List<PersistentUserEntity> users = new ArrayList<>(List.of(user1, user2));
        teamEntity.setUsersAssigned(users);

        when(teamEntityRepository.findById(TEAM_NAME)).thenReturn(Optional.of(teamEntity));
        when(userRepository.findByEmail("user1@email.com")).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail("user2@email.com")).thenReturn(Optional.of(user2));

        when(teamEntityRepository.save(any(PersistentTeamEntity.class))).thenReturn(teamEntity);

        teamService.removeTeam(TEAM_NAME);

        verify(userRepository, times(1)).save(user1);
        verify(userRepository, times(1)).save(user2);
        verify(teamEntityRepository, times(1)).delete(teamEntity);
    }

    /**
     * Test case to verify that an exception is thrown when attempting to remove a
     * member from a non-existent team.
     */
    @Test
    void shouldThrowExceptionWhenTeamNotFound() {
        String teamName = "nonexistent-team";
        String userEmail = "user@email.com";

        when(teamEntityRepository.findById(teamName)).thenReturn(Optional.empty());

        TeamServiceException exception = assertThrows(TeamServiceException.class, () -> {
            teamService.removeAssignedMember(teamName, userEmail);
        });

        assertEquals("Team with name: " + teamName + " not found.", exception.getMessage());
    }

    /**
     * Test case to verify that an exception is thrown when attempting to remove a
     * member from a team, and the user is not found in the repository.
     * <p>
     * This test simulates the case where the user does not exist in the repository,
     * and the method should throw a {@link TeamServiceException} with the message
     * "User with provided email: {userEmail} not found.".
     * </p>
     *
     * @throws TeamServiceException if an exception occurs during the process
     */
    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String teamName = "team-name";
        String userEmail = "nonexistent-user@email.com";

        when(teamEntityRepository.findById(teamName)).thenReturn(Optional.of(new PersistentTeamEntity()));
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        TeamServiceException exception = assertThrows(TeamServiceException.class, () -> {
            teamService.removeAssignedMember(teamName, userEmail);
        });

        assertEquals("User with provided email: " + userEmail + " not found.", exception.getMessage());
    }

    /**
     * Test case to verify that an exception is thrown when attempting to remove a
     * team that does not exist in the repository.
     * <p>
     * This test simulates the case where the team does not exist in the repository,
     * and the method should throw a {@link TeamServiceException} with the message
     * "Team with name: {teamName} not found.".
     * </p>
     *
     * @throws TeamServiceException if an exception occurs during the process
     */
    @Test
    void shouldThrowWhenRemovingNonExistentTeam() {
        when(teamEntityRepository.findById(TEAM_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamService.removeTeam(TEAM_NAME)).isInstanceOf(TeamServiceException.class)
                .hasMessage("Team with name: " + TEAM_NAME + " not found.");
    }

    /**
     * Test case to verify that an exception is thrown when trying to remove a user
     * who does not belong to the team.
     */
    @Test
    void shouldThrowWhenRemovingUserFromDifferentTeam() {
        PersistentTeamEntity anotherTeam = new PersistentTeamEntity("AnotherTeam", "Segunda", "Another chess team");
        userEntity.setTeamAssigned(anotherTeam);

        when(teamEntityRepository.findById(TEAM_NAME)).thenReturn(Optional.of(teamEntity));
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(userEntity));

        assertThatThrownBy(() -> teamService.removeAssignedMember(TEAM_NAME, USER_EMAIL))
                .isInstanceOf(TeamServiceException.class)
                .hasMessage("User with email: " + USER_EMAIL + " no found in team.");
    }

    /**
     * Test case to verify that an exception is thrown when trying to remove a user
     * who has no team assigned.
     */
    @Test
    void shouldThrowWhenRemovingUserWithNoTeam() {
        when(teamEntityRepository.findById(TEAM_NAME)).thenReturn(Optional.of(teamEntity));
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(userEntity));

        assertThatThrownBy(() -> teamService.removeAssignedMember(TEAM_NAME, USER_EMAIL))
                .isInstanceOf(TeamServiceException.class)
                .hasMessage("User with email: " + USER_EMAIL + " no have assigned team.");
    }

    /**
     * Test case to verify that an exception is thrown when attempting to get
     * information of a non-existent team.
     */
    @Test
    void shouldThrowWhenTeamNotFound() {
        when(teamEntityRepository.findById(TEAM_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamService.getTeamInfo(TEAM_NAME)).isInstanceOf(TeamServiceException.class)
                .hasMessage("Team with name: " + TEAM_NAME + " not found.");
    }

}
