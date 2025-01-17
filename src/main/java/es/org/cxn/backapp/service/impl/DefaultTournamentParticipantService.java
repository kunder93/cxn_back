
package es.org.cxn.backapp.service.impl;

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

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity;
import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity.TournamentCategory;
import es.org.cxn.backapp.repository.TournamentParticipantRepository;
import es.org.cxn.backapp.service.TournamentParticipantService;

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
        final LocalDate birthDate, final TournamentCategory category,
        final String byes
  ) {
    final var participant = PersistentTournamentParticipantEntity.builder();
    participant.fideId(fideId);
    participant.name(name);
    participant.club(club);
    participant.birthDate(birthDate);
    participant.category(category);
    participant.byes(byes);
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
