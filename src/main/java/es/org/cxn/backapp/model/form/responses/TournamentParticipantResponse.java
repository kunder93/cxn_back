
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity.TournamentCategory;

import java.math.BigInteger;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a tournament participant response.
 * <p>
 * This class is used to transfer data from the service layer to the
 * presentation layer, encapsulating the details of a tournament participant.
 * It includes the FIDE ID, name, club, birth date, and tournament category of
 * the participant.
 * </p>
 *
 * <p>
 * The DTO is immutable and is used primarily to return participant information
 * in response to client requests.
 * </p>
 *
 * @param fideId The FIDE ID of the tournament participant.
 * @param name The name of the tournament participant.
 * @param club The club the participant is associated with.
 * @param birthDate The birth date of the participant.
 * @param category The tournament category the participant belongs to.
 */
public record TournamentParticipantResponse(

      BigInteger fideId,

      String name,

      String club,

      LocalDate birthDate,

      TournamentCategory category

) {

}
