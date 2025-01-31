
package es.org.cxn.backapp.controller.entity;

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

import java.util.stream.Stream;

import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.model.form.requests.AddActivityRequestData;
import es.org.cxn.backapp.model.form.responses.CreatedActivityResponse;
import es.org.cxn.backapp.service.ActivitiesService;
import es.org.cxn.backapp.service.dto.ActivityDto;
import es.org.cxn.backapp.service.exceptions.activity.ActivityImageNotFoundException;
import es.org.cxn.backapp.service.exceptions.activity.ActivityNotFoundException;
import es.org.cxn.backapp.service.exceptions.activity.ActivityServiceException;
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
     * Handles the HTTP POST request to add a new activity. This endpoint expects a
     * multipart request with activity data and an optional image file. The activity
     * data includes the title, description, start date, end date, and category.
     * Only users with roles of 'ADMIN', 'PRESIDENTE', 'TESORERO', or 'SECRETARIO'
     * are authorized to add new activities.
     *
     * @param activityData An {@link AddActivityRequestData} object containing the
     *                     activity details, such as title, description, start date,
     *                     end date, and category.
     * @param imageFile    An optional {@link MultipartFile} representing the
     *                     activity's image. If provided, the image is stored
     *                     alongside the activity data. Can be null if no image is
     *                     uploaded.
     *
     * @return A {@link ResponseEntity} containing a {@link CreatedActivityResponse}
     *         object with the created activity's details and an HTTP status of 201
     *         (Created) if the activity was successfully added.
     *
     * @throws ResponseStatusException if there is an error while adding the
     *                                 activity, resulting in a HTTP 400 (Bad
     *                                 Request) response. The exception message
     *                                 provides additional details about the error.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('TESORERO') or hasRole('SECRETARIO')")
    public ResponseEntity<CreatedActivityResponse> addActivity(@RequestPart("data")
    @Valid final AddActivityRequestData activityData, @RequestPart(value = "imageFile", required = false)
    /* @ValidImageFile */ final MultipartFile imageFile) {
        try {
            // Call the service with file and other parameters
            final var createdActivityEntity = activitiesService.addActivity(activityData.title(),
                    activityData.description(), activityData.startDate(), activityData.endDate(),
                    activityData.category(), imageFile);
            return new ResponseEntity<>(new CreatedActivityResponse(createdActivityEntity), HttpStatus.CREATED);
        } catch (ActivityServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Handles the HTTP GET request to retrieve an activity by title.
     *
     * @param title The title of the activity to retrieve.
     * @return A {@link ResponseEntity} containing the activity details and HTTP
     *         status 200 (OK).
     * @throws ResponseStatusException if the activity is not found, resulting in an
     *                                 HTTP 404 (Not Found) response.
     */
    @GetMapping("/{title}")
    public ResponseEntity<ActivityDto> getActivity(@PathVariable final String title) {
        try {
            final var activity = activitiesService.getActivity(title);
            return new ResponseEntity<>(activity, HttpStatus.OK);
        } catch (ActivityServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Handles the HTTP GET request to retrieve the image of an activity by title.
     *
     * @param title The title of the activity whose image is to be retrieved.
     * @return A {@link ResponseEntity} containing the image as a byte array and the
     *         appropriate content type.
     * @throws ResponseStatusException if the image is not found, resulting in an
     *                                 HTTP 404 (Not Found) response.
     */
    @GetMapping("/{title}/image")
    public ResponseEntity<byte[]> getActivityImage(@PathVariable final String title) {
        try {
            byte[] image = activitiesService.getActivityImage(title);

            // Detect content type using Apache Tika
            Tika tika = new Tika();
            String contentType = tika.detect(image);

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(image);

        } catch (ActivityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (ActivityImageNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage()); // 204 No Content for missing
                                                                                      // image

        } catch (ActivityServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving image", e);
        }
    }

    /**
     * Handles the HTTP GET request to retrieve all activities.
     *
     * @return A {@link ResponseEntity} containing a stream of {@link ActivityDto}
     *         representing all activities and HTTP status 200 (OK).
     */
    @GetMapping()
    public ResponseEntity<Stream<ActivityDto>> getAllActivities() {
        final Stream<ActivityDto> activitiesList;
        activitiesList = activitiesService.getAllActivities();
        return new ResponseEntity<>(activitiesList, HttpStatus.OK);
    }

}
