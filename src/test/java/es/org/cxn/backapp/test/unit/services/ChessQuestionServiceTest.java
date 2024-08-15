
package es.org.cxn.backapp.test.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.org.cxn.backapp.exceptions.ChessQuestionServiceException;
import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;
import es.org.cxn.backapp.repository.ChessQuestionEntityRepository;
import es.org.cxn.backapp.service.DefaultChessQuestionService;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link DefaultChessQuestionService} class.
 *
 * This test class uses Mockito to mock dependencies and verify the behavior
 * of the {@link DefaultChessQuestionService} methods. The tests cover scenarios
 * such as adding a chess question, changing the "seen" status of a chess
 * question, and handling cases where the chess question is not found.
 *
 * <p>Each test verifies that the service methods interact correctly with the
 * {@link ChessQuestionEntityRepository} and that the service's behavior aligns
 * with the expected outcomes.</p>
 *
 * @author Santi
 */
@ExtendWith(MockitoExtension.class)
class ChessQuestionServiceTest {
  /**
   * Mock instance of {@link ChessQuestionEntityRepository} to simulate
   * repository behavior for testing purposes.
   */
  @Mock
  private ChessQuestionEntityRepository mockRepository;

  /**
   * Instance of {@link DefaultChessQuestionService} with mocks injected.
   * This service is under test in this class.
   */
  @InjectMocks
  private DefaultChessQuestionService chessQuestionService;

  /**
   * Sets up the test environment before each test method.
   * Initializes mock objects and the service instance.
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    chessQuestionService = new DefaultChessQuestionService(mockRepository);
  }

  /**
   * Constant representing a sample email address used for testing.
   */
  private static final String TEST_EMAIL = "test@example.com";

  /**
   * Constant representing a sample category used for testing.
   */
  private static final String TEST_CATEGORY = "Test Category";

  /**
   * Constant representing a sample topic used for testing.
   */
  private static final String TEST_TOPIC = "Test Topic";

  /**
   * Constant representing a sample message used for testing.
   */
  private static final String TEST_MESSAGE = "Test Message";

  /**
   * Constant representing the current date and time used for testing.
   */
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

  @Test
  void testChangeChessQuestionSeen() throws ChessQuestionServiceException {
    // Arrange
    Integer id = 1;
    var mockEntity = new PersistentChessQuestionEntity();
    mockEntity.setIdentifier(id);
    mockEntity.setSeen(false); // Puedes establecer el valor inicial como desees

    when(mockRepository.findById(id)).thenReturn(Optional.of(mockEntity));
    when(mockRepository.save(any(PersistentChessQuestionEntity.class)))
          .thenReturn(mockEntity);

    // Act
    var updatedEntity = chessQuestionService.changeChessQuestionSeen(id);

    // Assert
    assertTrue(updatedEntity.isSeen(), "seen is true");
    verify(mockRepository, times(1)).findById(id);
    verify(mockRepository, times(1)).save(mockEntity);
  }

  @Test
  void testChangeChessQuestionSeenQuestionNotFound() {
    // Arrange
    Integer id = 1;
    when(mockRepository.findById(id)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ChessQuestionServiceException.class, () -> {
      chessQuestionService.changeChessQuestionSeen(id);
    }, "ChessQuestionServiceException expected");
    verify(mockRepository, times(1)).findById(id);
    verify(mockRepository, never()).save(any());
  }
}
