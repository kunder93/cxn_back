/**
 * The MIT License (MIT)
 *
 * <p>Copyright (c) 2020 the original author or authors.
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.controller.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.exceptions.ChessQuestionServiceException;
import es.org.cxn.backapp.model.form.requests.ChangeChessQuestionHasSeenRequest;
import es.org.cxn.backapp.model.form.requests.CreateChessQuestionRequest;
import es.org.cxn.backapp.model.form.responses.ChessQuestionResponse;
import es.org.cxn.backapp.model.form.responses.ChessQuestionsListResponse;
import es.org.cxn.backapp.model.persistence.PersistentChessQuestionEntity;
import es.org.cxn.backapp.service.ChessQuestionsService;
import jakarta.validation.Valid;

/**
 * Rest controller for chess questions.
 *
 * @author Santiago Paz
 */
@RestController
@RequestMapping("/api/chessQuestion")
public class ChessQuestionsController {

    /**
     * The chess questions service.
     */
    private final ChessQuestionsService chessQuestionsService;

    /**
     * Constructs a controller with the specified dependencies.
     *
     * @param service company service.
     */
    public ChessQuestionsController(final ChessQuestionsService service) {
        super();

        chessQuestionsService = checkNotNull(service, "Received a null pointer as service");
    }

    /**
     * Change chess question has been seen state.
     *
     * @param chessQuestionHasSeenRequestForm form with chess question identifier.
     *                                        {@link ChangeChessQuestionHasSeenRequest}.
     * @return form with the modified chess question.
     */
    @PostMapping("/changeChessQuestionHasSeen")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<ChessQuestionResponse> changeChessQuestionHasSeen(@RequestBody
    @Valid final ChangeChessQuestionHasSeenRequest chessQuestionHasSeenRequestForm) {
        try {
            final var result = chessQuestionsService.changeChessQuestionSeen(chessQuestionHasSeenRequestForm.id());
            final var response = new ChessQuestionResponse(result.getIdentifier(), result.getEmail(),
                    result.getCategory(), result.getTopic(), result.getMessage(), result.getDate(), result.isSeen());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ChessQuestionServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Create a new chess question.
     *
     * @param createChessQuestionRequestForm form with data to create a chess
     *                                       question.
     *                                       {@link CreateChessQuestionRequest}.
     * @return form with the created chess question data.
     */
    @PostMapping()
    public ResponseEntity<ChessQuestionResponse> createChessQuestion(@RequestBody
    @Valid final CreateChessQuestionRequest createChessQuestionRequestForm) {
        try {
            final var result = chessQuestionsService.add(createChessQuestionRequestForm.email(),
                    createChessQuestionRequestForm.category(), createChessQuestionRequestForm.topic(),
                    createChessQuestionRequestForm.message());
            final var response = new ChessQuestionResponse(result.getIdentifier(), result.getEmail(),
                    result.getCategory(), result.getTopic(), result.getMessage(), result.getDate(), result.isSeen());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Delete chess question by ID.
     *
     * @param identifier the identifier of the chess question to delete.
     * @return response OK if delete is successful.
     */
    @DeleteMapping("/{identifier}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<Void> deleteChessQuestion(@PathVariable final Integer identifier) {
        try {
            chessQuestionsService.delete(identifier);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ChessQuestionServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Return all stored chess questions with their data.
     *
     * @return all stored chess questions.
     */
    @CrossOrigin
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or" + " hasRole('SECRETARIO')")
    public ResponseEntity<ChessQuestionsListResponse> getAllChessQuestions() {
        // Retrieve the collection of PersistentChessQuestionEntity from the service
        final Collection<PersistentChessQuestionEntity> chessQuestionsList = chessQuestionsService.getAll();

        // Convert the collection into a ChessQuestionsListResponse using the
        // static factory method
        final var response = ChessQuestionsListResponse.from(chessQuestionsList);

        // Return the response wrapped in a ResponseEntity with HttpStatus.OK
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
