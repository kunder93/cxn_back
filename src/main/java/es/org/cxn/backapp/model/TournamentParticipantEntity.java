
package es.org.cxn.backapp.model;

import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity;

import java.math.BigInteger;
import java.time.LocalDate;

/**
 * Interface defining the contract for a tournament participant entity.
 */
public interface TournamentParticipantEntity {

  /**
   * Gets the FIDE ID of the participant.
   *
   * @return the FIDE ID
   */
  BigInteger getFideId();

  /**
   * Gets the name of the participant.
   *
   * @return the name
   */
  String getName();

  /**
   * Gets the club of the participant.
   *
   * @return the club name
   */
  String getClub();

  /**
   * Gets the birth date of the participant.
   *
   * @return the birth date
   */
  LocalDate getBirthDate();

  /**
   * Gets the tournament category of the participant.
   *
   * @return the tournament category
   */
  PersistentTournamentParticipantEntity.TournamentCategory getCategory();
}
