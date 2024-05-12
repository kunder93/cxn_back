
package es.org.cxn.backapp.test.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;
import es.org.cxn.backapp.repository.ChessQuestionEntityRepository;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ChessQuestionEntityRepositoryTest {

  @Autowired
  private ChessQuestionEntityRepository repository;

  @Transactional
  @Test
  void testSaveAndFindById() {
    var entity = PersistentChessQuestionEntity.builder().topic("Chess")
          .message("How to checkmate?").category("Strategy")
          .email("example@example.com").build();

    repository.save(entity);

    var savedEntity = repository.findById(entity.getIdentifier()).orElse(null);
    assertThat(savedEntity).as("Entity is not null").isNotNull();
    assertThat(savedEntity.getTopic()).as("Topic check").isEqualTo("Chess");
    assertThat(savedEntity.getMessage()).as("Message check")
          .isEqualTo("How to checkmate?");
    assertThat(savedEntity.getCategory()).as("Strategy check")
          .isEqualTo("Strategy");
    assertThat(savedEntity.getEmail()).as("Email check")
          .isEqualTo("example@example.com");
  }
}
