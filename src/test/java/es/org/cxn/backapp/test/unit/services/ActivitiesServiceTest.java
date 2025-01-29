
package es.org.cxn.backapp.test.unit.services;

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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;
import es.org.cxn.backapp.repository.ActivityEntityRepository;
import es.org.cxn.backapp.service.dto.ActivityDto;
import es.org.cxn.backapp.service.exceptions.ActivityServiceException;
import es.org.cxn.backapp.service.impl.DefaultActivitiesService;
import es.org.cxn.backapp.service.impl.DefaultImageStorageService;

/**
 * Unit tests for the {@link DefaultActivitiesService} class, focusing on
 * methods related to activities management such as adding an activity,
 * retrieving an activity by its title, and retrieving all activities. This test
 * class uses Mockito for mocking dependencies and JUnit 5 for test execution.
 *
 * <p>
 * Tests ensure that the service methods behave as expected, including verifying
 * that activities are correctly saved and retrieved, and that exceptions are
 * thrown when necessary.
 * </p>
 *
 * <p>
 * Mockito is used to mock the dependencies of the service:
 * {@link ActivityEntityRepository} for the data access layer, and
 * {@link DefaultImageStorageService} for image handling.
 * </p>
 */
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ActivitiesServiceTest {

    /**
     * Mocked repository for activity entities, used for interaction with the
     * database. This mock will simulate the behavior of the real repository in the
     * tests.
     */
    @Mock
    private ActivityEntityRepository activityRepository;

    /**
     * Mocked service for handling image storage operations, used to simulate
     * image-related functionality in activity creation.
     */
    @Mock
    private DefaultImageStorageService imageStorageService;

    /**
     * The service class under test. This instance will be injected with mocked
     * dependencies.
     */
    @InjectMocks
    private DefaultActivitiesService activitiesService;

    /**
     * A sample activity entity used across tests to verify the service's methods.
     * It represents an activity with a title, description, start and end times, and
     * a category. This entity is used to ensure the correct behavior of methods
     * such as {@link DefaultActivitiesService#addActivity}.
     */
    private PersistentActivityEntity sampleActivity;

    /**
     * The activity title.
     */
    private final String activityTitle = "Sample activity";
    /**
     * The activity description.
     */
    private final String activityDescription = "A sample description";
    /**
     * The activity start date.
     */
    private final LocalDateTime activityStartDate = LocalDateTime.now();
    /**
     * The activity end date.
     */
    private final LocalDateTime activityEndDate = LocalDateTime.now().plusHours(2);
    /**
     * The activity category.
     */
    private final String activityCategory = "Sample category";
    /**
     * The activity image source location.
     */
    private final String activityImageSrc = "srcImageSample";

    @BeforeEach
    void setUp() {

        // Create a sample activity entity for reuse in tests
        sampleActivity = new PersistentActivityEntity();
        sampleActivity.setTitle(activityTitle);
        sampleActivity.setDescription(activityDescription);
        sampleActivity.setStartDate(activityStartDate);
        sampleActivity.setEndDate(activityEndDate);
        sampleActivity.setCategory(activityCategory);
        sampleActivity.setImageSrc(activityImageSrc);
        // Set createdAt manually if it's set automatically in addActivity
        sampleActivity.setCreatedAt(LocalDateTime.now());
    }

    /**
     * Test for the addActivity method to verify a new activity is saved and
     * returned correctly.
     *
     * @throws ActivityServiceException
     */
    @Test
    void testAddActivity() throws ActivityServiceException {
        // Arrange
        when(activityRepository.save(any(PersistentActivityEntity.class))).thenReturn(sampleActivity);
        when(activityRepository.existsById(activityTitle)).thenReturn(Boolean.FALSE);
        final MultipartFile imageFile = Mockito.mock(MultipartFile.class);

        // Act
        PersistentActivityEntity result = activitiesService.addActivity(activityTitle, activityDescription,
                activityStartDate, activityEndDate, activityCategory, imageFile);

        // Assert
        assertNotNull(result);
        assertEquals(activityTitle, result.getTitle());
        assertEquals(activityDescription, result.getDescription());
        assertEquals(activityStartDate, result.getStartDate());
        assertEquals(activityEndDate, result.getEndDate());
        assertEquals(activityCategory, result.getCategory());
        verify(activityRepository, times(1)).save(any(PersistentActivityEntity.class));
    }

    /**
     * Test for the addActivity method to verify that an exception is thrown if an
     * activity with the same title already exists.
     *
     * @throws ActivityServiceException if an exception occurs while adding the
     *                                  activity
     */
    @Test
    void testAddActivityActivityIdExistsThrowException() throws ActivityServiceException {
        // Arrange
        when(activityRepository.existsById(activityTitle)).thenReturn(true);
        final MultipartFile imageFile = Mockito.mock(MultipartFile.class);
        // Act & Assert
        ActivityServiceException exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.addActivity(activityTitle, activityDescription, activityStartDate, activityEndDate,
                    activityCategory, imageFile);
        });

        assertEquals("Activity with title: " + activityTitle + " already exists.", exception.getMessage());
        // Verify if the check for existing title was made.
        verify(activityRepository, times(1)).existsById(activityTitle);
    }

    /**
     * Test for the addActivity method to verify that if an IOException occurs while
     * saving the image file, an ActivityServiceException is thrown.
     *
     * @throws ActivityServiceException if an exception occurs while adding the
     *                                  activity
     * @throws IOException              When error saving image.
     */
    @Test
    void testAddActivityIOExceptionThrown() throws ActivityServiceException, IOException {
        // Arrange
        when(activityRepository.existsById(activityTitle)).thenReturn(false);
        MultipartFile mockImageFile = Mockito.mock(MultipartFile.class);
        when(mockImageFile.isEmpty()).thenReturn(false);

        // Directly stub the mock to throw an exception when saveImage is called
        when(imageStorageService.saveImage(any(), any(), any(), any()))
                .thenThrow(new IOException("Error saving image"));

        // Act & Assert
        ActivityServiceException exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.addActivity(activityTitle, activityDescription, activityStartDate, activityEndDate,
                    activityCategory, mockImageFile);
        });

        assertEquals("Error saving activity image: Error saving image", exception.getMessage());
        verify(activityRepository, times(0)).save(any(PersistentActivityEntity.class));
    }

    /**
     * Test for the addActivity method to verify that if the activity ID doesn't
     * exist and the image file is empty, the imageSrc is set to null.
     *
     * @throws ActivityServiceException if an exception occurs while adding the
     *                                  activity
     */
    @Test
    void testAddActivityNoImageFile() throws ActivityServiceException {
        // Arrange
        // Create a sample activity entity for reuse in tests
        final var sampleActivityNoImageSrc = new PersistentActivityEntity();
        sampleActivityNoImageSrc.setTitle(activityTitle);
        sampleActivityNoImageSrc.setDescription(activityDescription);
        sampleActivityNoImageSrc.setStartDate(activityStartDate);
        sampleActivityNoImageSrc.setEndDate(activityEndDate);
        sampleActivityNoImageSrc.setCategory(activityCategory);

        // Set createdAt manually if it's set automatically in addActivity
        sampleActivityNoImageSrc.setCreatedAt(LocalDateTime.now());

        when(activityRepository.existsById(activityTitle)).thenReturn(false); // Activity title doesn't exist
        when(activityRepository.save(any(PersistentActivityEntity.class))).thenReturn(sampleActivityNoImageSrc);
        MultipartFile emptyImageFile = Mockito.mock(MultipartFile.class);
        when(emptyImageFile.isEmpty()).thenReturn(true); // Simulating an empty image file

        // Act
        PersistentActivityEntity result = activitiesService.addActivity(activityTitle, activityDescription,
                activityStartDate, activityEndDate, activityCategory, emptyImageFile);

        // Assert
        assertNotNull(result);
        assertNull(result.getImageSrc(), "Expected imageSrc to be null when imageFile is empty.");
        assertEquals(activityTitle, result.getTitle());
        assertEquals(activityDescription, result.getDescription());
        verify(activityRepository, times(1)).save(any(PersistentActivityEntity.class)); // Ensure save was called once
    }

    @Test
    void testGetActivityImageIOExceptionWhileLoadingImage() throws Exception {
        // Arrange
        String imagePath = "path/to/image.jpg";
        PersistentActivityEntity mockActivity = mock(PersistentActivityEntity.class);
        when(mockActivity.getImageSrc()).thenReturn(imagePath);
        when(activityRepository.findById(activityTitle)).thenReturn(Optional.of(mockActivity));
        when(imageStorageService.loadImage(imagePath)).thenThrow(new IOException("Image loading error"));

        // Act & Assert
        ActivityServiceException exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.getActivityImage(activityTitle);
        });

        assertEquals("Error loading activity image: Image loading error", exception.getMessage());
        verify(imageStorageService, times(1)).loadImage(imagePath);
    }

    @Test
    void testGetActivityImageNoImageSource() throws IOException {
        // Arrange
        PersistentActivityEntity mockActivity = mock(PersistentActivityEntity.class);
        when(mockActivity.getImageSrc()).thenReturn(null);
        when(activityRepository.findById(activityTitle)).thenReturn(Optional.of(mockActivity));

        // Act & Assert
        ActivityServiceException exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.getActivityImage(activityTitle);
        });

        assertEquals("No image associated with activity: " + activityTitle, exception.getMessage());
        verify(imageStorageService, never()).loadImage(anyString());
    }

    @Test
    void testGetActivityImageNoImageSourceEmpty() throws IOException {
        // Arrange
        PersistentActivityEntity mockActivity = mock(PersistentActivityEntity.class);
        when(mockActivity.getImageSrc()).thenReturn("");
        when(activityRepository.findById(activityTitle)).thenReturn(Optional.of(mockActivity));

        // Act & Assert
        ActivityServiceException exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.getActivityImage(activityTitle);
        });

        assertEquals("No image associated with activity: " + activityTitle, exception.getMessage());
        verify(imageStorageService, never()).loadImage(anyString());
    }

    @Test
    void testGetActivityImageNoImageSourceNull() throws IOException {
        // Arrange
        PersistentActivityEntity mockActivity = mock(PersistentActivityEntity.class);
        when(mockActivity.getImageSrc()).thenReturn(null);
        when(activityRepository.findById(activityTitle)).thenReturn(Optional.of(mockActivity));

        // Act & Assert
        ActivityServiceException exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.getActivityImage(activityTitle);
        });

        assertEquals("No image associated with activity: " + activityTitle, exception.getMessage());
        verify(imageStorageService, never()).loadImage(anyString());
    }

    @Test
    void testGetActivityImageSuccess() throws Exception {
        // Arrange
        String imagePath = "path/to/image.jpg";
        byte[] imageData = "sample image data".getBytes();

        PersistentActivityEntity mockActivity = mock(PersistentActivityEntity.class);
        when(mockActivity.getImageSrc()).thenReturn(imagePath);
        when(activityRepository.findById(activityTitle)).thenReturn(Optional.of(mockActivity));
        when(imageStorageService.loadImage(imagePath)).thenReturn(imageData);

        // Act
        byte[] result = activitiesService.getActivityImage(activityTitle);

        // Assert
        assertArrayEquals(imageData, result);
        verify(imageStorageService, times(1)).loadImage(imagePath);
    }

    /**
     * Test for the getActivity method to verify that an exception is thrown if an
     * activity is not found.
     */
    @Test
    void testGetActivityNotFound() {
        // Arrange
        when(activityRepository.findById(activityTitle)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.getActivity(activityTitle);
        });

        assertEquals("Activity with title: " + activityTitle + " not found.", exception.getMessage());
        verify(activityRepository, times(1)).findById(activityTitle);
    }

    /**
     * Test for the getActivity method to verify an existing activity is returned by
     * ID.
     */
    @Test
    void testGetActivitySuccess() throws ActivityServiceException {
        // Arrange
        when(activityRepository.findById(activityTitle)).thenReturn(Optional.of(sampleActivity));

        // Act
        PersistentActivityEntity result = activitiesService.getActivity(activityTitle);

        // Assert
        assertNotNull(result);
        assertEquals(sampleActivity.getTitle(), result.getTitle());
        verify(activityRepository, times(1)).findById(activityTitle);
    }

    /**
     * Test for the getAllActivities method to verify behavior when no activities
     * are found.
     */
    @Test
    void testGetAllActivitiesNoActivities() {
        // Arrange
        when(activityRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ActivityDto> result = activitiesService.getAllActivities().toList();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(activityRepository, times(1)).findAll();
    }

    @Test
    void testGetAllActivitiesSuccess() throws Exception {
        // Simulate the repository returning a list of activities
        when(activityRepository.findAll()).thenReturn(Arrays.asList(sampleActivity));

        // Mock the image retrieval
        byte[] imageData = "sample image data".getBytes();
        when(activityRepository.findById(activityTitle)).thenReturn(Optional.of(sampleActivity));
        when(imageStorageService.loadImage(activityImageSrc)).thenReturn(imageData);

        // Mocks the findById method of the repository to return an Optional containing
        // activity1
        when(activityRepository.findById(activityTitle)).thenReturn(Optional.of(sampleActivity));

        // Act
        Stream<ActivityDto> result = activitiesService.getAllActivities();

        // Assert
        assertNotNull(result);
        final var resultList = result.toList();

        assertEquals(1, resultList.size(), "One element in list.");
        assertEquals(sampleActivity.getTitle(), resultList.getFirst().title());
        assertEquals(sampleActivity.getCategory(), resultList.getFirst().category());
        assertEquals(sampleActivity.getEndDate(), resultList.getFirst().endDate());
        assertEquals(sampleActivity.getStartDate(), resultList.getFirst().startDate());
        assertEquals(sampleActivity.getDescription(), resultList.getFirst().description());
        result.close();
    }

}
