/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.service;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.exceptions.ActivityServiceException;
import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;
import es.org.cxn.backapp.service.dto.ActivityWithImageDto;

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
    PersistentActivityEntity getActivity(String title) throws ActivityServiceException;

    /**
     * Retrieves the image associated with a specified activity.
     *
     * @param title the unique title of the activity whose image is to be retrieved
     * @return a byte array representing the image data
     * @throws ActivityServiceException if no image is found for the specified
     *                                  activity, or if an error occurs while
     *                                  loading the image
     */
    public byte[] getActivityImage(final String title) throws ActivityServiceException;

    /**
     * Retrieves all activities available in the system as a Stream.
     *
     * @return a Stream of PersistentActivityEntity instances representing all
     *         activities
     * @throws ActivityServiceException When cannot load activities images.
     */
    Stream<ActivityWithImageDto> getAllActivities() throws ActivityServiceException;

}