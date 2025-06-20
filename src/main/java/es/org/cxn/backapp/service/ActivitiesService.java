
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

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;
import es.org.cxn.backapp.service.dto.ActivityDto;
import es.org.cxn.backapp.service.exceptions.activity.ActivityImageNotFoundException;
import es.org.cxn.backapp.service.exceptions.activity.ActivityNotFoundException;
import es.org.cxn.backapp.service.exceptions.activity.ActivityServiceException;

/**
 * ActivitiesService defines the contract for operations related to managing
 * activity entities in the application.
 *
 * <p>
 * This service provides methods to add a new activity, retrieve an activity by
 * its identifier, and fetch all available activities.
 * </p>
 *
 * <p>
 * This interface is licensed under the MIT License.
 * </p>
 *
 * @see PersistentActivityEntity
 */
public interface ActivitiesService {

    /**
     * Creates and adds a new activity to the system with the provided details.
     *
     * @param title       the title of the activity
     * @param description a detailed description of the activity
     * @param startDate   the start date and time of the activity
     * @param endDate     the end date and time of the activity
     * @param category    the category of the activity (e.g., "TORNEO",
     *                    "ENTRENAMIENTO")
     * @param imageFile   the image multipart file.
     * @return the created PersistentActivityEntity instance representing the new
     *         activity
     * @throws ActivityServiceException when cannot store image.
     */
    PersistentActivityEntity addActivity(String title, String description, LocalDateTime startDate,
            LocalDateTime endDate, String category, MultipartFile imageFile) throws ActivityServiceException;

    /**
     * Retrieves an activity by its unique identifier.
     *
     * @param title the activity title, unique identifier of the activity to
     *              retrieve
     * @return the PersistentActivityEntity instance representing the found activity
     * @throws ActivityServiceException When no activity found.
     */
    ActivityDto getActivity(String title) throws ActivityServiceException;

    /**
     * Retrieves the image associated with a specified activity.
     *
     * @param title the unique title of the activity whose image is to be retrieved
     * @return a byte array representing the image data
     * @throws ActivityServiceException       Image loading error (I/O, etc.)
     * @throws ActivityImageNotFoundException Activity exists but no image is
     *                                        assigned
     * @throws ActivityNotFoundException      Activity does not exist
     */
    byte[] getActivityImage(String title)
            throws ActivityServiceException, ActivityNotFoundException, ActivityImageNotFoundException;

    /**
     * Retrieves all activities available in the system as a Stream.
     *
     * @return a Stream of PersistentActivityEntity instances representing all
     *         activities
     */
    Stream<ActivityDto> getAllActivities();

    /**
     * Removes an activity from the system by its title.
     *
     * <p>
     * If no activity exists with the specified title, an
     * {@link ActivityNotFoundException} is thrown.
     * </p>
     *
     * @param title the title of the activity to remove; must not be null or empty
     * @throws ActivityNotFoundException if no activity is found with the specified
     *                                   title
     */
    void remove(String title) throws ActivityNotFoundException;
}
