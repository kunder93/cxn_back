
package es.org.cxn.backapp.test.unit.services;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity;
import es.org.cxn.backapp.repository.TournamentParticipantRepository;
import es.org.cxn.backapp.service.impl.DefaultTournamentParticipantService;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for the {@link DefaultTournamentParticipantService} class.
 * <p>
 * This test class verifies the correct behavior of the service's methods:
 * {@code addParticipant} and {@code getAllParticipants}.
 * </p>
 * <p>
 * The tests use Mockito to mock dependencies and verify interactions with
 * the {@link TournamentParticipantRepository}.
 * </p>
 * <p>
 * <strong>Author:</strong> Santiago Paz
 * </p>
 */
class TournamentParticipantServiceTest {

  /**
   * The FIDE ID for the first participant used in the tests.
   * <p>
   * This constant represents a unique identifier for a participant in the
   * tournament. It is used in test cases to ensure consistency and to avoid
   * magic numbers in the test code.
   * </p>
   */
  private static final BigInteger FIDE_ID_1 =
        new BigInteger("12345678901234567890");

  /**
   * The name of the first participant used in the tests.
   * <p>
   * This constant represents the name of a participant and is used in test
   * cases to verify that the name is handled correctly by the service methods.
   * </p>
   */
  private static final String NAME_1 = "John Doe";

  /**
   * The club of the first participant used in the tests.
   * <p>
   * This constant represents the club to which a participant belongs. It is
   * used in test cases to check that the service correctly processes the club
   * information.
   * </p>
   */
  private static final String CLUB_1 = "Chess Club";

  /**
   * The birth date of the first participant used in the tests.
   * <p>
   * This constant represents the birth date of a participant. It is used in
   * test cases to ensure that the service correctly handles and returns the
   * birth date information.
   * </p>
   */
  private static final LocalDate BIRTH_DATE_1 = LocalDate.of(2008, 5, 20);

  /**
   * The tournament category of the first participant used in the tests.
   * <p>
   * This constant represents the category to which a participant belongs. It
   * is used in test cases to verify that the category is correctly processed
   * by the service methods.
   * </p>
   */
  private static final PersistentTournamentParticipantEntity.TournamentCategory CATEGORY_1 =
        PersistentTournamentParticipantEntity.TournamentCategory.SUB12;

  /**
   * The FIDE ID for the second participant used in the tests.
   * <p>
   * This constant represents a unique identifier for a different participant
   * in the tournament. It is used in test cases to ensure that the service
   * can handle multiple participants correctly.
   * </p>
   */
  private static final BigInteger FIDE_ID_2 =
        new BigInteger("09876543210987654321");

  /**
   * The name of the second participant used in the tests.
   * <p>
   * This constant represents the name of another participant and is used in
   * test cases to verify that the service correctly processes and returns the
   * name information.
   * </p>
   */
  private static final String NAME_2 = "Jane Doe";

  /**
   * The club of the second participant used in the tests.
   * <p>
   * This constant represents the club to which the second participant belongs.
   * It is used in test cases to check that the service correctly handles the
   * club information for multiple participants.
   * </p>
   */
  private static final String CLUB_2 = "Chess Club";

  /**
   * The birth date of the second participant used in the tests.
   * <p>
   * This constant represents the birth date of another participant. It is used
   * in test cases to ensure that the service correctly processes and returns
   * the birth date information for multiple participants.
   * </p>
   */
  private static final LocalDate BIRTH_DATE_2 = LocalDate.of(2010, 8, 15);

  /**
   * The tournament category of the second participant used in the tests.
   * <p>
   * This constant represents the category to which the second participant
   * belongs. It is used in test cases to verify that the service correctly
   * processes the category information for multiple participants.
   * </p>
   */
  private static final PersistentTournamentParticipantEntity.TournamentCategory CATEGORY_2 =
        PersistentTournamentParticipantEntity.TournamentCategory.SUB10;

  /**
   * The tournament participants repository.
   */
  @Mock
  private TournamentParticipantRepository tournamentParticipantRepository;

  /**
   * The tournament participants service.
   */
  @InjectMocks
  private DefaultTournamentParticipantService tournamentParticipantService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * Tests the {@link DefaultTournamentParticipantService#addParticipant}
   * method to ensure it correctly adds a participant and returns the saved
   * entity.
   * <p>
   * This test verifies that the repository's {@code save} method is called
   * with the expected participant and that the returned entity matches the
   * expected values.
   * </p>
   */
  @Test
  void testAddParticipant() {
    // Given: a participant entity
    final var participant = new PersistentTournamentParticipantEntity();
    participant.setFideId(FIDE_ID_1);
    participant.setName(NAME_1);
    participant.setClub(CLUB_1);
    participant.setBirthDate(BIRTH_DATE_1);
    participant.setCategory(CATEGORY_1);
    participant.setByes("1");

    // When: the repository save method is called
    when(
          tournamentParticipantRepository
                .save(any(PersistentTournamentParticipantEntity.class))
    ).thenReturn(participant);

    var result = tournamentParticipantService.addParticipant(
          FIDE_ID_1, NAME_1, CLUB_1, BIRTH_DATE_1, CATEGORY_1, "1"
    );

    // Then: verify that the save method was called and the result is as
    // expected.
    verify(tournamentParticipantRepository).save(participant);
    assertThat(result).as("Check that the returned participant is not null")
          .isNotNull();
    assertThat(result.getFideId()).as("Check the participant's FIDE ID")
          .isEqualTo(FIDE_ID_1);
    assertThat(result.getName()).as("Check the participant's name")
          .isEqualTo(NAME_1);
    assertThat(result.getClub()).as("Check the participant's club")
          .isEqualTo(CLUB_1);
    assertThat(result.getBirthDate()).as("Check the participant's birth date")
          .isEqualTo(BIRTH_DATE_1);
    assertThat(result.getCategory()).as("Check the participant's category")
          .isEqualTo(CATEGORY_1);
  }

  /**
   * Tests the {@link DefaultTournamentParticipantService#getAllParticipants}
   * method to ensure it correctly retrieves all participants from the
   * repository.
   * <p>
   * This test verifies that the repository's {@code findAll} method is called
   * and the result matches the expected list of participants.
   * </p>
   */
  @Test
  void testGetAllParticipants() {
    // Given: a list of participants
    final var participant1 = new PersistentTournamentParticipantEntity();
    participant1.setFideId(FIDE_ID_1);
    participant1.setName(NAME_1);
    participant1.setClub(CLUB_1);
    participant1.setBirthDate(BIRTH_DATE_1);
    participant1.setCategory(CATEGORY_1);

    final var participant2 = new PersistentTournamentParticipantEntity();
    participant2.setFideId(FIDE_ID_2);
    participant2.setName(NAME_2);
    participant2.setClub(CLUB_2);
    participant2.setBirthDate(BIRTH_DATE_2);
    participant2.setCategory(CATEGORY_2);

    List<PersistentTournamentParticipantEntity> participants =
          List.of(participant1, participant2);

    // When: the repository findAll method is called
    when(tournamentParticipantRepository.findAll()).thenReturn(participants);

    var result = tournamentParticipantService.getAllParticipants();

    // Then: verify that the findAll method was called and the result matches
    // the expected list.
    verify(tournamentParticipantRepository).findAll();
    assertThat(result).as("Check that the result list is correct").isNotNull()
          .hasSize(2).containsExactlyInAnyOrder(participant1, participant2);
  }
}
