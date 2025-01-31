
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

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;
import es.org.cxn.backapp.repository.ChessQuestionEntityRepository;
import jakarta.transaction.Transactional;

/**
 * Integration test for the {@link ChessQuestionEntityRepository}.
 * <p>
 * This test verifies the basic persistence operations such as saving and
 * retrieving a {@link PersistentChessQuestionEntity} from the database. It uses
 * in-memory database configurations provided by the {@code @DataJpaTest}
 * annotation for fast execution.
 * </p>
 * <p>
 * The repository is tested with a {@link PersistentChessQuestionEntity} to
 * ensure that it can persist and retrieve entities correctly.
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
class ChessQuestionEntityRepositoryIT {

    /**
     * The repository for managing {@link PersistentChessQuestionEntity} persistence
     * operations.
     * <p>
     * This repository is autowired into the test class to allow for testing of the
     * data access layer.
     * </p>
     */
    @Autowired
    private ChessQuestionEntityRepository repository;

    /**
     * Tests saving a {@link PersistentChessQuestionEntity} to the database and then
     * retrieving it by its identifier.
     * <p>
     * The test ensures that the entity is correctly persisted and that all fields
     * can be accurately retrieved.
     * </p>
     *
     * <p>
     * Assertions are made using AssertJ to validate that the entity was saved and
     * retrieved correctly, and that all fields match the expected values.
     * </p>
     */
    @Transactional
    @Test
    void testSaveAndFindById() {
        var dateNow = LocalDateTime.now();
        var entity = PersistentChessQuestionEntity.builder().topic("Chess").message("How to checkmate?")
                .category("Strategy").email("example@example.com").date(dateNow).build();

        repository.save(entity);

        var savedEntity = repository.findById(entity.getIdentifier()).orElse(null);
        assertThat(savedEntity).as("Entity is not null").isNotNull();
        assertThat(savedEntity.getTopic()).as("Topic check").isEqualTo("Chess");
        assertThat(savedEntity.getDate()).as("Date check").isEqualTo(dateNow);
        assertThat(savedEntity.getMessage()).as("Message check").isEqualTo("How to checkmate?");
        assertThat(savedEntity.getCategory()).as("Strategy check").isEqualTo("Strategy");
        assertThat(savedEntity.getEmail()).as("Email check").isEqualTo("example@example.com");
    }
}
