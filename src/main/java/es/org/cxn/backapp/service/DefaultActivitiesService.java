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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

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

    /**
     * Constructs a new DefaultActivitiesService with the specified repository.
     *
     * @param repoActivity the activity repository used for database operations
     * @throws NullPointerException if {@code repoActivity} is null
     */
    public DefaultActivitiesService(final ActivityEntityRepository repoActivity) {
        super();
        activityRepository = checkNotNull(repoActivity, "Received a null pointer as activity repository");
    }

    /**
     * Creates and saves a new activity in the database.
     *
     * @param title       the title of the activity
     * @param description the description of the activity
     * @param startDate   the start date and time of the activity
     * @param endDate     the end date and time of the activity
     * @param category    the category of the activity
     * @return the saved {@link PersistentActivityEntity} instance
     */
    @Override
    public PersistentActivityEntity addActivity(String title, String description, LocalDateTime startDate,
            LocalDateTime endDate, String category) {
        PersistentActivityEntity activityEntity = new PersistentActivityEntity();
        activityEntity.setTitle(title);
        activityEntity.setDescription(description);
        activityEntity.setStartDate(startDate);
        activityEntity.setEndDate(endDate);
        activityEntity.setCategory(category);

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
    public PersistentActivityEntity getActivity(Integer identifier) throws ActivityServiceException {
        var optionalActivity = activityRepository.findById(identifier);
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
        List<PersistentActivityEntity> activitiesList = activityRepository.findAll();
        return activitiesList.stream();
    }

}