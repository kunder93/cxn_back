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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.exceptions.ActivityServiceException;
import es.org.cxn.backapp.model.form.requests.AddActivityRequest;
import es.org.cxn.backapp.model.form.responses.CreatedActivityResponse;
import es.org.cxn.backapp.service.ActivitiesService;
import jakarta.validation.Valid;

/**
 * REST controller for managing activities in the application. This controller
 * provides endpoints for creating new activities.
 */
@RestController
@RequestMapping("/api/activities")
public class ActivitiesController {

    /**
     * The activities service that handles business logic for activities.
     */
    private final ActivitiesService activitiesService;

    /**
     * Constructs a controller with the specified dependencies.
     *
     * @param service The activities service to be used by this controller. Must not
     *                be null.
     * @throws NullPointerException if the provided service is null.
     */
    public ActivitiesController(final ActivitiesService service) {
        super();
        activitiesService = checkNotNull(service, "Received a null pointer as service");
    }

    /**
     * Handles the HTTP POST request to add a new activity.
     *
     * @param addActivityRequest The request body containing the details of the
     *                           activity to be added. This must be a valid
     *                           {@link AddActivityRequest}.
     * @return A {@link ResponseEntity} containing the created activity response and
     *         HTTP status 201 (Created).
     * @throws ResponseStatusException if there is an error while adding the
     *                                 activity, resulting in a HTTP 400 (Bad
     *                                 Request) response.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or hasRole('SECRETARIO')")
    public ResponseEntity<CreatedActivityResponse> addActivity(@RequestBody
    @Valid final AddActivityRequest addActivityRequest) {
        try {
            final var createdActivityEntity = activitiesService.addActivity(addActivityRequest.title(),
                    addActivityRequest.description(), addActivityRequest.startDate(), addActivityRequest.endDate(),
                    addActivityRequest.category(), addActivityRequest.imageFile());

            return new ResponseEntity<>(new CreatedActivityResponse(createdActivityEntity), HttpStatus.CREATED);

        } catch (ActivityServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
