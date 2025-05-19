
package es.org.cxn.backapp.test.integration.repository;

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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.org.cxn.backapp.model.persistence.team.PersistentTeamEntity;
import es.org.cxn.backapp.repository.TeamEntityRepository;
import jakarta.transaction.Transactional;

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
 * <p>
 * The {@link Transactional} annotation is used to ensure that each test runs in
 * a transaction, which is rolled back after the test completes, ensuring
 * isolation between tests.
 * </p>
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

    @BeforeEach
    void setUp() {
        testTeam = new PersistentTeamEntity("Team A", "Category A", "Description A");
        teamRepository.save(testTeam);
    }

    @Test
    void shouldDeleteTeam() {
        teamRepository.deleteById("Team A");
        Optional<PersistentTeamEntity> foundTeam = teamRepository.findById("Team A");
        assertThat(foundTeam).isEmpty();
    }

    @Test
    void shouldFindTeamById() {
        Optional<PersistentTeamEntity> foundTeam = teamRepository.findById("Team A");
        assertThat(foundTeam).isPresent();
        assertThat(foundTeam.get().getName()).isEqualTo("Team A");
    }

    @Test
    void shouldSaveAndRetrieveTeam() {
        PersistentTeamEntity newTeam = new PersistentTeamEntity("Team B", "Category B", "Description B");
        teamRepository.save(newTeam);

        Optional<PersistentTeamEntity> foundTeam = teamRepository.findById("Team B");
        assertThat(foundTeam).isPresent();
        assertThat(foundTeam.get().getCategory()).isEqualTo("Category B");
    }

}
