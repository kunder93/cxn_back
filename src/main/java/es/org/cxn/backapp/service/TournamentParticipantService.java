
package es.org.cxn.backapp.service;

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
   * @param byes         The byes requested by participant.
   *
   * @return             the {@link PersistentTournamentParticipantEntity}
   *                     representing the added participant, which includes
   *                     the participant's details and any changes made during
   *                     the save operation
   */
  PersistentTournamentParticipantEntity addParticipant(
        BigInteger fideId, String name, String club, LocalDate birthDate,
        TournamentCategory category, String byes
  );

  /**
   * Retrieves all tournament participants.
   *
   * @return a list of all participant entities
   */
  List<PersistentTournamentParticipantEntity> getAllParticipants();
}
