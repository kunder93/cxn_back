
package es.org.cxn.backapp.test.integration.services;

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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import es.org.cxn.backapp.model.persistence.user.UserProfile;
import es.org.cxn.backapp.model.persistence.user.UserType;
import es.org.cxn.backapp.repository.TeamEntityRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;
import es.org.cxn.backapp.service.TeamService;
import es.org.cxn.backapp.service.dto.TeamInfoDto;
import es.org.cxn.backapp.service.exceptions.TeamServiceException;
import es.org.cxn.backapp.service.impl.DefaultTeamService;
import jakarta.transaction.Transactional;

/**
 * Integration tests for the {@link DefaultTeamService} class.
 * <p>
 * These tests cover team and user operations such as creating a team,
 * adding/removing members, and retrieving team information. Each test ensures
 * that the service behaves as expected under various scenarios.
 */
@SpringBootTest()
@ActiveProfiles("test")
@TestPropertySource(properties = { "spring.mail.host=", "storage.location=" })
final class DefaultTeamServiceIT {

    /**
     * The email address of the test user. This constant is used for various test
     * scenarios where a user needs to be associated with a team.
     */
    private static final String USER_EMAIL = "email@email.es";

    /**
     * The name of the test team. This constant is used for testing team creation,
     * member management, and retrieval operations in the test methods.
     */
    private static final String TEAM_NAME = "ChessTeam";

    /**
     * A description of the test team. This constant is used for testing team
     * creation and ensuring the correct details are persisted.
     */
    private static final String TEAM_DESCRIPTION = "A club for chess lovers";

    /**
     * The category of the test team. This constant is used during team creation to
     * classify the team within a specific category (e.g., "Primera").
     */
    private static final String TEAM_CATEGORY = "Primera";

    /**
     * Repository for interacting with the Team entities in the database. This is
     * used to perform CRUD operations on team data during the integration tests.
     */
    @Autowired
    private TeamEntityRepository teamRepository;

    /**
     * Repository for interacting with the User entities in the database. This is
     * used to perform CRUD operations on user data, particularly when
     * adding/removing members from teams during tests.
     */
    @Autowired
    private UserEntityRepository userRepository;

    /**
     * The service class that contains the business logic for managing teams. This
     * is used to perform operations such as creating teams, adding/removing
     * members, and retrieving team information during the integration tests.
     */
    @Autowired
    private TeamService teamService;

    /**
     * A persistent user entity for testing purposes. This user is created before
     * each test and can be added as a member of a team to test various user-related
     * operations such as adding/removing a user from a team.
     */
    PersistentUserEntity userEntity;

    /**
     * Sets up the test environment by clearing repositories and setting up a test
     * user.
     */
    @BeforeEach
    void setUp() {
        teamRepository.deleteAll();
        userRepository.deleteAll();
        UserProfile userProfile = new UserProfile();
        userProfile.setName("userName");
        userProfile.setFirstSurname("FirstSurname");
        userProfile.setSecondSurname("SecondSurname");
        userProfile.setGender("male");
        userProfile.setBirthDate(LocalDate.of(1991, 8, 22));

        userEntity = userRepository.save(new PersistentUserEntity("11111111H", userProfile, "pas123123", USER_EMAIL,
                UserType.SOCIO_NUMERO, true, null));
    }

    /**
     * Tests that a member can be added to a team successfully.
     *
     * @throws TeamServiceException if an error occurs during the service method
     *                              execution
     */
    @Test
    @Transactional
    void shouldAddMemberToTeam() throws TeamServiceException {
        teamService.createTeam(TEAM_NAME, TEAM_DESCRIPTION, TEAM_CATEGORY);

        TeamInfoDto updatedTeam = teamService.addMember(TEAM_NAME, USER_EMAIL);
        assertThat(updatedTeam.users()).hasSize(1);
    }

    /**
     * Tests that a team can be created successfully.
     *
     * @throws TeamServiceException when fails.
     */
    @Test
    @Transactional
    void shouldCreateTeamSuccessfully() throws TeamServiceException {
        TeamInfoDto team = teamService.createTeam(TEAM_NAME, TEAM_DESCRIPTION, TEAM_CATEGORY);
        assertThat(team).isNotNull();
        assertThat(team.name()).isEqualTo(TEAM_NAME);
    }

    /**
     * Tests that a team can be deleted successfully and throws an exception when
     * attempting to retrieve it after deletion.
     *
     * @throws TeamServiceException if an error occurs during the service method
     *                              execution
     */
    @Test
    @Transactional
    void shouldDeleteTeamSuccessfully() throws TeamServiceException {
        // Step 1: Create a team
        teamService.createTeam(TEAM_NAME, TEAM_DESCRIPTION, TEAM_CATEGORY);

        // Step 2: Retrieve the created team
        var createdTeam = teamService.getTeamInfo(TEAM_NAME);

        // Step 3: Assert the created team is not null and contains expected values
        assertNotNull(createdTeam);
        assertEquals(TEAM_NAME, createdTeam.name());
        assertEquals(TEAM_DESCRIPTION, createdTeam.description());
        assertEquals(TEAM_CATEGORY, createdTeam.category());

        // Step 4: Remove the team
        teamService.removeTeam(TEAM_NAME);

        // Step 5: Assert that trying to retrieve the deleted team raises an exception
        assertThrows(TeamServiceException.class, () -> {
            teamService.getTeamInfo(TEAM_NAME);
        });
    }

    /**
     * Tests that a member can be removed from a team successfully.
     *
     * @throws TeamServiceException if an error occurs during the service method
     *                              execution
     */
    @Test
    @Transactional
    void shouldRemoveMemberFromTeam() throws TeamServiceException {
        teamService.createTeam(TEAM_NAME, TEAM_DESCRIPTION, TEAM_CATEGORY);
        teamService.addMember(TEAM_NAME, USER_EMAIL);

        TeamInfoDto updatedTeam = teamService.removeMember(TEAM_NAME, USER_EMAIL);
        assertThat(updatedTeam.users()).isEmpty();
    }

    /**
     * Tests that team information can be retrieved successfully.
     *
     * @throws TeamServiceException if an error occurs during the service method
     *                              execution
     */
    @Test
    @Transactional
    void shouldRetrieveTeamInfo() throws TeamServiceException {
        teamService.createTeam(TEAM_NAME, TEAM_DESCRIPTION, TEAM_CATEGORY);
        TeamInfoDto teamInfo = teamService.getTeamInfo(TEAM_NAME);
        assertThat(teamInfo).isNotNull();
        assertThat(teamInfo.name()).isEqualTo(TEAM_NAME);
    }

    /**
     * Tests that attempting to add a non-existing user to a team throws a
     * {@link TeamServiceException}.
     *
     * @throws TeamServiceException when team already exists.
     */
    @Test
    @Transactional
    void shouldThrowExceptionWhenAddNotExistingUserToTeam() throws TeamServiceException {
        // Create a team first
        teamService.createTeam(TEAM_NAME, TEAM_DESCRIPTION, TEAM_CATEGORY);

        // Try adding a non-existing user
        assertThatThrownBy(() -> teamService.addMember(TEAM_NAME, "nonExistentUser@email.es"))
                .isInstanceOf(TeamServiceException.class)
                .hasMessageContaining("User with provided email: " + "nonExistentUser@email.es" + " not found.");
    }

    /**
     * Tests that attempting to add a user to a non-existing team throws a
     * {@link TeamServiceException}.
     */
    @Test
    @Transactional
    void shouldThrowExceptionWhenAddUserToNotExistingTeam() {
        // Try adding a member to a non-existing team
        assertThatThrownBy(() -> teamService.addMember("NonExistentTeam", USER_EMAIL))
                .isInstanceOf(TeamServiceException.class).hasMessageContaining("Team not found");
    }

    /**
     * Tests that attempting to remove a member from a non-existing team throws a
     * {@link TeamServiceException}.
     */
    @Test
    @Transactional
    void shouldThrowExceptionWhenRemoveFromNotExistingTeam() {

        // Try removing a member from a non-existing team
        assertThatThrownBy(() -> teamService.removeMember("NonExistentTeam", USER_EMAIL))
                .isInstanceOf(TeamServiceException.class).hasMessageContaining("Team not found");
    }

    /**
     * Tests that attempting to remove a user who is not part of a team throws a
     * {@link TeamServiceException}.
     *
     * @throws TeamServiceException when team already exists.
     */
    @Test
    @Transactional
    void shouldThrowExceptionWhenRemoveNotExistingUserFromTeam() throws TeamServiceException {
        // Create a team first
        teamService.createTeam(TEAM_NAME, TEAM_DESCRIPTION, TEAM_CATEGORY);

        // Try removing a user that is not part of the team
        assertThatThrownBy(() -> teamService.removeMember(TEAM_NAME, "nonExistentUser@email.es"))
                .isInstanceOf(TeamServiceException.class)
                .hasMessageContaining("User with provided email: " + "nonExistentUser@email.es" + " not found.");
    }

    /**
     * Tests that attempting to retrieve information for a non-existing team throws
     * a {@link TeamServiceException}.
     */
    @Test
    @Transactional
    void shouldThrowExceptionWhenRetrieveTeamInfoFromNotExistingTeam() {
        // Try retrieving team info for a non-existing team
        assertThatThrownBy(() -> teamService.getTeamInfo("NonExistentTeam")).isInstanceOf(TeamServiceException.class)
                .hasMessageContaining("not found");
    }

}
