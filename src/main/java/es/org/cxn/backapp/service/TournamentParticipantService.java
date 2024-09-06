
package es.org.cxn.backapp.service;

import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity;
import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity.TournamentCategory;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for the Tournament participant entity domain.
 * <p>
 * This service provides operations for managing tournament participants,
 * specifically for adding new participants and retrieving all participants.
 *
 * @author Santiago Paz Perez.
 */
public interface TournamentParticipantService {

  /**
   * Adds a new tournament participant to the repository.
   * <p>
   * This method accepts details of a participant, creates a new
   * {@link PersistentTournamentParticipantEntity} instance with those details,
   * and saves it to the repository. The saved entity, including its generated
   * ID and any other modifications made during the save process, is then
   * returned.
   * </p>
   *
   * @param fideId       the unique FIDE ID of the participant; must be a
   *                     positive number and within the valid range
   * @param name         the full name of the participant; must not be null or
   *                     empty
   * @param club         the name of the club the participant is affiliated
   *                     with; must not be null or empty
   * @param birthDate    the birth date of the participant; must not be null
   *                     and must be a valid date
   * @param category     the tournament category of the participant; must be
   *                     one of the predefined categories
   * @return             the {@link PersistentTournamentParticipantEntity}
   *                     representing the added participant, which includes
   *                     the participant's details and any changes made during
   *                     the save operation
   */
  PersistentTournamentParticipantEntity addParticipant(
        BigInteger fideId, String name, String club, LocalDate birthDate,
        TournamentCategory category
  );

  /**
   * Retrieves all tournament participants.
   *
   * @return a list of all participant entities
   */
  List<PersistentTournamentParticipantEntity> getAllParticipants();
}
