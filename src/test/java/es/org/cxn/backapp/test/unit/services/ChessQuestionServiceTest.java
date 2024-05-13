
package es.org.cxn.backapp.test.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;
import es.org.cxn.backapp.repository.ChessQuestionEntityRepository;
import es.org.cxn.backapp.service.DefaultChessQuestionService;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ChessQuestionServiceTest {
  @Mock
  private ChessQuestionEntityRepository mockRepository;

  private DefaultChessQuestionService chessQuestionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    chessQuestionService = new DefaultChessQuestionService(mockRepository);
  }

  //Declarar constantes para valores comunes
  private static final String TEST_EMAIL = "test@example.com";
  private static final String TEST_CATEGORY = "Test Category";
  private static final String TEST_TOPIC = "Test Topic";
  private static final String TEST_MESSAGE = "Test Message";
  private static final LocalDateTime DATE_NOW = LocalDateTime.now();

  @Test
  void testAdd() {
    // Given
    var expectedEntity = PersistentChessQuestionEntity.builder()
          .email(TEST_EMAIL).category(TEST_CATEGORY).topic(TEST_TOPIC)
          .date(DATE_NOW).message(TEST_MESSAGE).build();

    when(mockRepository.save(any(PersistentChessQuestionEntity.class)))
          .thenReturn(expectedEntity);

    // When
    var actualEntity = chessQuestionService
          .add(TEST_EMAIL, TEST_CATEGORY, TEST_TOPIC, TEST_MESSAGE);

    // Then
    assertThat(actualEntity)
          .as("The returned entity should match the expected entity")
          .isEqualTo(expectedEntity);
    verify(mockRepository, times(1)).save(
          argThat(
                entity -> entity.getEmail().equals(TEST_EMAIL)
                      && entity.getCategory().equals(TEST_CATEGORY)
                      && entity.getTopic().equals(TEST_TOPIC)
                      && entity.getMessage().equals(TEST_MESSAGE)
          )
    );
  }
}
