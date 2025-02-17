
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

import java.math.BigInteger;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity;
import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity.TournamentCategory;
import es.org.cxn.backapp.repository.TournamentParticipantRepository;

/**
 * Integration test for the {@link TournamentParticipantRepository}.
 *
 * <p>
 * This test verifies the basic persistence operations such as saving,
 * retrieving, updating, and deleting a
 * {@link PersistentTournamentParticipantEntity} from the database. It uses
 * in-memory database configurations provided by the {@code @DataJpaTest}
 * annotation for fast execution.
 * </p>
 *
 * <p>
 * The repository is tested with a {@link PersistentTournamentParticipantEntity}
 * to ensure that it can persist and retrieve entities correctly.
 * </p>
 *
 * <p>
 * The {@link org.springframework.transaction.annotation.Transactional}
 * annotation is used to ensure that each test runs in a transaction, which is
 * rolled back after the test completes, ensuring isolation between tests.
 * </p>
 *
 * @author Santi
 */
@DataJpaTest
class TournamentParticipantRepositoryIT {

    /**
     * Fide id of participant.
     */
    private static final BigInteger FIDE_ID = BigInteger.valueOf(123456789);

    /**
     * Birth date of participant.
     */
    private static final LocalDate BIRTH_DATE = LocalDate.of(2010, 1, 1);

    /**
     * The repository.
     */
    @Autowired
    private TournamentParticipantRepository repository;
    /**
     * Test participant entity used for testing persistence operations.
     */
    private PersistentTournamentParticipantEntity participant;

    /**
     * Sets up a participant entity for use in the tests. This method runs before
     * each test, ensuring a fresh instance of the entity.
     */
    @BeforeEach
    void setUp() {
        participant = new PersistentTournamentParticipantEntity();
        participant.setFideId(FIDE_ID);
        participant.setName("John Doe");
        participant.setClub("Chess Club");
        participant.setBirthDate(BIRTH_DATE);
        participant.setCategory(TournamentCategory.SUB12);
    }

    /**
     * Tests that a {@link PersistentTournamentParticipantEntity} can be deleted
     * from the database and verifies that it is no longer retrievable after
     * deletion.
     */
    @Test
    void testDeleteParticipant() {
        repository.save(participant);
        repository.deleteById(participant.getFideId());
        final var deletedParticipant = repository.findById(participant.getFideId());
        assertThat(deletedParticipant)
                .as("The participant should no longer be present in the database" + " after deletion.").isNotPresent();
    }

    /**
     * Tests that a {@link PersistentTournamentParticipantEntity} can be retrieved
     * from the database by its ID after being saved.
     */
    @Test
    void testFindParticipantById() {
        repository.save(participant);
        final var foundParticipant = repository.findById(participant.getFideId());
        assertThat(foundParticipant).as("The participant should be found in the database by its FIDE ID.").isPresent();
        assertThat(foundParticipant.get().getName()).as("The found participant's name should match the expected name.")
                .isEqualTo(participant.getName());
    }

    /**
     * Tests that a {@link PersistentTournamentParticipantEntity} can be saved to
     * the database using the repository's save method.
     */
    @Test
    void testSaveParticipant() {
        final var savedParticipant = repository.save(participant);
        assertThat(savedParticipant).as("The participant should be successfully saved to the database.").isNotNull();
        assertThat(savedParticipant.getFideId()).as("The saved participant's FIDE ID should match the expected ID.")
                .isEqualTo(participant.getFideId());
    }

    /**
     * Tests that a {@link PersistentTournamentParticipantEntity}'s details can be
     * updated and verifies that the changes are persisted in the database.
     */
    @Test
    void testUpdateParticipant() {
        var savedParticipant = repository.save(participant);
        savedParticipant.setName("Jane Doe");
        repository.save(savedParticipant);

        final var updatedParticipant = repository.findById(participant.getFideId());
        assertThat(updatedParticipant)
                .as("The updated participant should be found in the database by its" + " FIDE ID.").isPresent();
        assertThat(updatedParticipant.get().getName())
                .as("The updated participant's name should match the new name 'Jane Doe'.").isEqualTo("Jane Doe");
    }
}
