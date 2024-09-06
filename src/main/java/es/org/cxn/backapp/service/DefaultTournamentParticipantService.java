
package es.org.cxn.backapp.service;

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity;
import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity.TournamentCategory;
import es.org.cxn.backapp.repository.TournamentParticipantRepository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Default implementation of the {@link TournamentParticipantService}.
 * <p>
 * This service provides default implementations for adding a participant
 * and retrieving all participants from the repository.
 *
 * @author Santiago Paz.
 */
@Service
public final class DefaultTournamentParticipantService
      implements TournamentParticipantService {

  /**
   * Repository for the tournament participant entities handled by the service.
   */
  private final TournamentParticipantRepository tournamentParticipantRepository;

  /**
   * Constructs an entities service with the specified repository.
   *
   * @param repoTour The tournament participant repository
   *                 {@link TournamentParticipantRepository}.
   */
  public DefaultTournamentParticipantService(
        final TournamentParticipantRepository repoTour
  ) {
    super();
    this.tournamentParticipantRepository = checkNotNull(
          repoTour,
          "Received a null pointer as tournament participant repository"
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PersistentTournamentParticipantEntity addParticipant(
        final BigInteger fideId, final String name, final String club,
        final LocalDate birthDate, final TournamentCategory category
  ) {
    final var participant = PersistentTournamentParticipantEntity.builder();
    participant.fideId(fideId);
    participant.name(name);
    participant.club(club);
    participant.birthDate(birthDate);
    participant.category(category);
    final var entity = participant.build();
    return tournamentParticipantRepository.save(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PersistentTournamentParticipantEntity> getAllParticipants() {
    return tournamentParticipantRepository.findAll();
  }
}
