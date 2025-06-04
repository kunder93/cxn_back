
package es.org.cxn.backapp.controller.entity;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.org.cxn.backapp.model.form.requests.AddTournamentParticipantRequest;
import es.org.cxn.backapp.model.form.responses.TournamentParticipantResponse;
import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity;
import es.org.cxn.backapp.service.TournamentParticipantService;
import jakarta.validation.Valid;

/**
 * Rest controller for tournament participants.
 *
 * This controller provides endpoints to manage tournament participants,
 * including adding new participants and retrieving all participants.
 *
 * @author Santiago Paz
 */
@RestController
@RequestMapping("/api/participants")
public class TournamentParticipantController {

    /**
     * The tournament participant service.
     */
    private final TournamentParticipantService tournamentParticipantService;

    /**
     * Constructs a controller with the specified dependencies.
     *
     * @param service The tournament participant service.
     */
    public TournamentParticipantController(final TournamentParticipantService service) {
        super();
        this.tournamentParticipantService = Objects.requireNonNull(service, "Received a null pointer as service");
    }

    /**
     * Endpoint to add a new tournament participant.
     *
     * @param participant The participant to be added.
     * @return The added participant.
     */
    @PostMapping
    public ResponseEntity<TournamentParticipantResponse> addParticipant(@Valid
    @RequestBody final AddTournamentParticipantRequest participant) {
        final var savedParticipant = tournamentParticipantService.addParticipant(participant.fideId(),
                participant.name(), participant.club(), participant.birthDate(), participant.category(),
                participant.byes());
        final var response = new TournamentParticipantResponse(savedParticipant.getFideId(), savedParticipant.getName(),
                savedParticipant.getClub(), savedParticipant.getBirthDate(), savedParticipant.getCategory(),
                savedParticipant.getByes());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve all tournament participants.
     *
     * @return A list of all participants.
     */
    @GetMapping
    public ResponseEntity<List<PersistentTournamentParticipantEntity>> getAllParticipants() {
        final var participants = tournamentParticipantService.getAllParticipants();
        return ResponseEntity.ok(participants);
    }
}
