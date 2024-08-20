
package es.org.cxn.backapp.test.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
   * First chess question identifier.
   */
  private static final Integer CHESS_QUESTION_ID = 1;

  /**
   * First chess question topic.
   */
  private static final String CHESS_QUESTION_TOPIC = "class test";

  /**
   * First chess question message content.
   */
  private static final String CHESS_QUESTION_MESSAGE =
        "My son wanna get into class, where is located?";

  /**
   * First chess question category.
   */
  private static final String CHESS_QUESTION_CATEGORY = "Kids class";

  /**
   * First chess question email.
   */
  private static final String CHESS_QUESTION_EMAIL = "firstEmail@email.es";

  /**
   * Chess question date. When question was done.
   */
  private static final LocalDateTime CHESS_QUESTION_DATE = LocalDateTime.now();

  /**
   * State for check if question has been seen.
   */
  private static final boolean CHESS_QUESTION_SEEN = false;

  private PersistentChessQuestionEntity createTestEntity() {
    return PersistentChessQuestionEntity.builder().email(CHESS_QUESTION_EMAIL)
          .category(CHESS_QUESTION_CATEGORY).topic(CHESS_QUESTION_TOPIC)
          .date(CHESS_QUESTION_DATE).message(CHESS_QUESTION_MESSAGE)
          .seen(CHESS_QUESTION_SEEN).build();
  }

  @Test
  void testAdd() {
    var expectedEntity = createTestEntity();

    when(mockRepository.save(any(PersistentChessQuestionEntity.class)))
          .thenReturn(expectedEntity);

    var actualEntity = chessQuestionService.add(
          CHESS_QUESTION_EMAIL, CHESS_QUESTION_CATEGORY, CHESS_QUESTION_TOPIC,
          CHESS_QUESTION_MESSAGE
    );

    assertThat(actualEntity).as(
          "The returned entity should match the expected entity with "
                + "email: %s, category: %s, topic: %s, and message: %s",
          CHESS_QUESTION_EMAIL, CHESS_QUESTION_CATEGORY, CHESS_QUESTION_TOPIC,
          CHESS_QUESTION_MESSAGE
    ).isEqualTo(expectedEntity);
    verify(mockRepository, times(1))
          .save(
                argThat(
                      entity -> CHESS_QUESTION_EMAIL.equals(entity.getEmail())
                            && CHESS_QUESTION_CATEGORY
                                  .equals(entity.getCategory())
                            && CHESS_QUESTION_TOPIC.equals(entity.getTopic())
                            && CHESS_QUESTION_MESSAGE
                                  .equals(entity.getMessage())
                )
          );
  }

  @Test
  void testChangeChessQuestionSeen() throws ChessQuestionServiceException {
    // Arrange
    var mockEntity = new PersistentChessQuestionEntity();
    mockEntity.setIdentifier(CHESS_QUESTION_ID);
    mockEntity.setSeen(CHESS_QUESTION_SEEN); // Set the initial value as needed

    when(mockRepository.findById(CHESS_QUESTION_ID))
          .thenReturn(Optional.of(mockEntity));
    when(mockRepository.save(any(PersistentChessQuestionEntity.class)))
          .thenReturn(mockEntity);

    // Act
    var updatedEntity =
          chessQuestionService.changeChessQuestionSeen(CHESS_QUESTION_ID);

    // Assert
    Assertions.assertTrue(
          updatedEntity.isSeen(),
          "Expected the 'seen' property of the updated entity to be true"
    );

    verify(mockRepository, times(1)).findById(CHESS_QUESTION_ID);
    verify(mockRepository, times(1)).save(mockEntity);
  }

  @Test
  void testChangeChessQuestionSeenQuestionNotFound() {
    // Arrange
    var chessQuestionId = CHESS_QUESTION_ID;
    when(mockRepository.findById(chessQuestionId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
          ChessQuestionServiceException.class,
          () -> chessQuestionService.changeChessQuestionSeen(chessQuestionId),
          "Expected changeChessQuestionSeen to throw "
                + "ChessQuestionServiceException when the chess"
                + " question is not found"
    );

    verify(mockRepository, times(1)).findById(chessQuestionId);
    verify(mockRepository, never()).save(any());
  }
}
