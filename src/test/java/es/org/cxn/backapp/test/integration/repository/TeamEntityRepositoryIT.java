
package es.org.cxn.backapp.test.integration.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.org.cxn.backapp.model.persistence.team.PersistentTeamEntity;
import es.org.cxn.backapp.repository.TeamEntityRepository;

/**
 * Integration test for the {@link TeamEntityRepository}.
 * <p>
 * This test verifies the basic persistence operations such as saving and
 * retrieving a {@link PersistentTeamEntity} from the database. It uses
 * in-memory database configurations provided by the {@code @DataJpaTest}
 * annotation for fast execution.
 * </p>
 * <p>
 * The repository is tested with a {@link PersistentTeamEntity} to ensure that
 * it can persist and retrieve entities correctly.
 * </p>
 *
 *
 * @author Santi
 */
@DataJpaTest
class TeamEntityRepositoryIT {
    /**
     * Team entity repository.
     */
    @Autowired
    private TeamEntityRepository teamRepository;

    /**
     * The team entity.
     */
    private PersistentTeamEntity testTeam;

    /**
     * Initializes test data before each test execution.
     * <p>
     * Creates and saves a {@link PersistentTeamEntity} to be used in test cases.
     * </p>
     */
    @BeforeEach
    void setUp() {
        testTeam = new PersistentTeamEntity("Team A", "Category A", "Description A");
        teamRepository.save(testTeam);
    }

    /**
     * Tests if a team entity can be successfully deleted from the repository.
     * <p>
     * This test deletes a previously saved team entity and verifies that it no
     * longer exists in the repository.
     * </p>
     */
    @Test
    void shouldDeleteTeam() {
        teamRepository.deleteById("Team A");
        Optional<PersistentTeamEntity> foundTeam = teamRepository.findById("Team A");
        assertFalse(foundTeam.isPresent());
    }

    /**
     * Tests if a team entity can be found by its ID.
     * <p>
     * This test retrieves a previously saved team entity and asserts that the
     * retrieved entity has the expected properties.
     * </p>
     */
    @Test
    void shouldFindTeamById() {
        Optional<PersistentTeamEntity> foundTeam = teamRepository.findById("Team A");
        assertTrue(foundTeam.isPresent());
        assertEquals("Team A", foundTeam.get().getName());
    }

    /**
     * Tests saving and retrieving a new team entity.
     * <p>
     * This test saves a new {@link PersistentTeamEntity} to the repository and
     * verifies that it can be correctly retrieved.
     * </p>
     */
    @Test
    void shouldSaveAndRetrieveTeam() {
        PersistentTeamEntity newTeam = new PersistentTeamEntity("Team B", "Category B", "Description B");
        teamRepository.save(newTeam);

        Optional<PersistentTeamEntity> foundTeam = teamRepository.findById("Team B");
        assertTrue(foundTeam.isPresent());
        assertEquals("Category B", foundTeam.get().getCategory());
    }

}
