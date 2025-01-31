
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;
import es.org.cxn.backapp.repository.ActivityEntityRepository;
import es.org.cxn.backapp.service.ActivitiesService;
import es.org.cxn.backapp.service.dto.ActivityDto;
import es.org.cxn.backapp.service.exceptions.activity.ActivityImageNotFoundException;
import es.org.cxn.backapp.service.exceptions.activity.ActivityNotFoundException;
import es.org.cxn.backapp.service.exceptions.activity.ActivityServiceException;
import jakarta.transaction.Transactional;

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

    /**
     * The repository for performing CRUD operations on activity entities. This
     * repository interface provides methods for interacting with the
     * {@link PersistentActivityEntity} data in the database.
     */
    private final ActivityEntityRepository activityRepository;

    /**
     * The image storage service for handling activity image uploads. This service
     * is responsible for saving images associated with activities to the specified
     * storage location.
     */
    private final DefaultImageStorageService imageStorageService;

    /**
     * Path for storing activities images.
     */
    @Value("${image.location.activity}")
    private String imageLocationActivity;

    /**
     * Constructs a new DefaultActivitiesService with the specified repository and
     * image storage service.
     *
     * @param repoActivity      the activity repository used for database operations
     * @param imgStorageService the image storage service used to save activity
     *                          images
     * @throws NullPointerException if {@code repoActivity} or
     *                              {@code imageStorageService} is null
     */
    public DefaultActivitiesService(final ActivityEntityRepository repoActivity,
            final DefaultImageStorageService imgStorageService) {
        super();
        this.activityRepository = checkNotNull(repoActivity, "Received a null pointer as activity repository");
        this.imageStorageService = checkNotNull(imgStorageService, "Received a null pointer as image storage service");
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
    @Transactional
    public PersistentActivityEntity addActivity(final String title, final String description,
            final LocalDateTime startDate, final LocalDateTime endDate, final String category,
            final MultipartFile imageFile) throws ActivityServiceException {

        if (activityRepository.existsById(title)) {
            throw new ActivityServiceException("Activity with title: " + title + " already exists.");
        }
        final PersistentActivityEntity activityEntity = new PersistentActivityEntity();
        activityEntity.setTitle(title);
        activityEntity.setDescription(description);
        activityEntity.setStartDate(startDate);
        activityEntity.setEndDate(endDate);
        activityEntity.setCategory(category);
        activityEntity.setCreatedAt(LocalDateTime.now());

        if (imageFile == null || imageFile.isEmpty()) {
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
     * @param title the activity title, unique identifier of the activity
     * @return the found {@link PersistentActivityEntity} instance
     * @throws ActivityServiceException if no activity is found with the specified
     *                                  identifier
     */
    @Override
    public ActivityDto getActivity(final String title) throws ActivityServiceException {
        final var optionalActivity = activityRepository.findById(title);
        if (optionalActivity.isEmpty()) {
            throw new ActivityServiceException("Activity with title: " + title + " not found.");
        } else {
            final var activityEntity = optionalActivity.get();
            return new ActivityDto(activityEntity.getTitle(), activityEntity.getDescription(),
                    activityEntity.getStartDate(), activityEntity.getEndDate(), activityEntity.getCategory());
        }
    }

    /**
     * Retrieves an activity's image by its title.
     *
     * @param title the activity title, used to locate the associated image file.
     * @return the {@link MultipartFile} representing the image file.
     * @throws ActivityServiceException       Image loading error (I/O, etc.)
     * @throws ActivityImageNotFoundException Activity exists but no image is
     *                                        assigned
     * @throws ActivityNotFoundException      Activity does not exist
     */
    @Override
    public byte[] getActivityImage(final String title)
            throws ActivityNotFoundException, ActivityImageNotFoundException, ActivityServiceException {
        final var activityOptional = activityRepository.findById(title);

        // Case 1: Activity does not exist
        if (activityOptional.isEmpty()) {
            throw new ActivityNotFoundException("Activity with title: " + title + " not found.");
        }

        final var activity = activityOptional.get();

        // Case 2: Activity exists but has no image
        if (activity.getImageSrc() == null || activity.getImageSrc().isEmpty()) {
            throw new ActivityImageNotFoundException("No image associated with activity: " + title);
        }

        try {
            // Case 3: Activity exists and has an image
            return imageStorageService.loadImage(activity.getImageSrc());

        } catch (IOException e) {
            throw new ActivityServiceException("Error loading activity image: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all activities from the database along with their associated
     * images.
     *
     * @return a {@link Stream} with entities data converted in {@link ActivityDto}
     *         instances
     */
    @Override
    public Stream<ActivityDto> getAllActivities() {
        final List<PersistentActivityEntity> activitiesList = activityRepository.findAll();
        return activitiesList.stream().map(activity -> new ActivityDto(activity.getTitle(), activity.getDescription(),
                activity.getStartDate(), activity.getEndDate(), activity.getCategory()));
    }

}
