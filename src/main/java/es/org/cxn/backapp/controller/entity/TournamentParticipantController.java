
package es.org.cxn.backapp.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.model.form.requests.AddTournamentParticipantRequest;
import es.org.cxn.backapp.model.form.responses.TournamentParticipantResponse;
import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity;
import es.org.cxn.backapp.service.TournamentParticipantService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public TournamentParticipantController(
        final TournamentParticipantService service
  ) {
    super();
    this.tournamentParticipantService =
          checkNotNull(service, "Received a null pointer as service");
  }

  /**
   * Endpoint to retrieve all tournament participants.
   *
   * @return A list of all participants.
   */
  @GetMapping
  public ResponseEntity<List<PersistentTournamentParticipantEntity>>
        getAllParticipants() {
    final var participants = tournamentParticipantService.getAllParticipants();
    return ResponseEntity.ok(participants);
  }

  /**
   * Endpoint to add a new tournament participant.
   *
   * @param participant The participant to be added.
   * @return The added participant.
   */
  @PostMapping
  public ResponseEntity<TournamentParticipantResponse>
        addParticipant(@Valid @RequestBody
  final AddTournamentParticipantRequest participant) {
    final var savedParticipant = tournamentParticipantService.addParticipant(
          participant.fideId(), participant.name(), participant.club(),
          participant.birthDate(), participant.category()
    );
    final var response = new TournamentParticipantResponse(
          savedParticipant.getFideId(), savedParticipant.getName(),
          savedParticipant.getClub(), savedParticipant.getBirthDate(),
          savedParticipant.getCategory()
    );
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}
