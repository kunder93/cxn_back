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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.exceptions.ActivityServiceException;
import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;
import es.org.cxn.backapp.repository.ActivityEntityRepository;

/**
 * DefaultActivitiesService is the default implementation of the
 * {@link ActivitiesService} interface. It provides methods to add, retrieve,
 * and list activities, interacting with the {@link ActivityEntityRepository} to
 * perform data operations.
 *
 * <p>
 * This service is annotated with {@code @Service} to be automatically detected
 * and managed by Spring.
 * </p>
 *
 * @see ActivitiesService
 * @see ActivityEntityRepository
 */
@Service
public final class DefaultActivitiesService implements ActivitiesService {

    private final ActivityEntityRepository activityRepository;
    private final DefaultImageStorageService imageStorageService;

    @Value("${image.location.activity}")
    private String imageLocationActivity;

    /**
     * Constructs a new DefaultActivitiesService with the specified repository and
     * image storage service.
     *
     * @param repoActivity        the activity repository used for database
     *                            operations
     * @param imageStorageService the image storage service used to save activity
     *                            images
     * @throws NullPointerException if {@code repoActivity} or
     *                              {@code imageStorageService} is null
     */
    public DefaultActivitiesService(final ActivityEntityRepository repoActivity,
            final DefaultImageStorageService imageStorageService) {
        super();
        this.activityRepository = checkNotNull(repoActivity, "Received a null pointer as activity repository");
        this.imageStorageService = checkNotNull(imageStorageService,
                "Received a null pointer as image storage service");
    }

    /**
     * Creates and saves a new activity in the database.
     *
     * @param title       the title of the activity
     * @param description the description of the activity
     * @param startDate   the start date and time of the activity
     * @param endDate     the end date and time of the activity
     * @param category    the category of the activity
     * @param imageFile   the image multipart file of the activity.
     * @return the saved {@link PersistentActivityEntity} instance
     */
    @Override
    public PersistentActivityEntity addActivity(final String title, final String description,
            final LocalDateTime startDate, final LocalDateTime endDate, final String category,
            final MultipartFile imageFile) throws ActivityServiceException {
        final PersistentActivityEntity activityEntity = new PersistentActivityEntity();
        activityEntity.setTitle(title);
        activityEntity.setDescription(description);
        activityEntity.setStartDate(startDate);
        activityEntity.setEndDate(endDate);
        activityEntity.setCategory(category);

        if (imageFile.isEmpty()) {
            activityEntity.setImageSrc(null);

        } else {
            try {
                // Use activity title like a unique ID.
                final String entityIdForImage = title;
                final String entityType = "activity"; // Specify "activity" as the entity type

                // Save the image file using the image storage service with specified parameters
                final String imagePath = imageStorageService.saveImage(imageFile, imageLocationActivity, entityType,
                        entityIdForImage);

                // Store the image path or URL in the activity entity
                activityEntity.setImageSrc(imagePath);

            } catch (IOException e) {
                throw new ActivityServiceException("Error saving activity image: " + e.getMessage(), e);
            }
        }
        // Save the activity entity to the database
        return activityRepository.save(activityEntity);
    }

    /**
     * Retrieves an activity by its identifier.
     *
     * @param identifier the unique identifier of the activity
     * @return the found {@link PersistentActivityEntity} instance
     * @throws ActivityServiceException if no activity is found with the specified
     *                                  identifier
     */
    @Override
    public PersistentActivityEntity getActivity(final Integer identifier) throws ActivityServiceException {
        final var optionalActivity = activityRepository.findById(identifier);
        if (optionalActivity.isEmpty()) {
            throw new ActivityServiceException("Activity with identifier: " + identifier + " not found.");
        } else {
            return optionalActivity.get();
        }
    }

    /**
     * Retrieves all activities from the database.
     *
     * @return a {@link Stream} of all {@link PersistentActivityEntity} instances
     */
    @Override
    public Stream<PersistentActivityEntity> getAllActivities() {
        final List<PersistentActivityEntity> activitiesList = activityRepository.findAll();
        return activitiesList.stream();
    }

}