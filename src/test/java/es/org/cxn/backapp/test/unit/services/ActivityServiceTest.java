
package es.org.cxn.backapp.test.unit.services;

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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.exceptions.ActivityServiceException;
import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;
import es.org.cxn.backapp.repository.ActivityEntityRepository;
import es.org.cxn.backapp.service.DefaultActivitiesService;
import es.org.cxn.backapp.service.DefaultImageStorageService;
import es.org.cxn.backapp.service.dto.ActivityWithImageDto;

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
@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    /**
     * Mocked repository for activity entities, used for interaction with the
     * database. This mock will simulate the behavior of the real repository in the
     * tests.
     */
    @Mock
    private ActivityEntityRepository mockRepository;

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

    @BeforeEach
    void setUp() {
        activitiesService = new DefaultActivitiesService(mockRepository, imageStorageService);

        // Create a sample activity entity for reuse in tests
        sampleActivity = new PersistentActivityEntity();
        sampleActivity.setTitle("Sample Activity");
        sampleActivity.setDescription("A sample description");
        sampleActivity.setStartDate(LocalDateTime.now());
        sampleActivity.setEndDate(LocalDateTime.now().plusHours(2));
        sampleActivity.setCategory("Sample Category");

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
        when(mockRepository.save(any(PersistentActivityEntity.class))).thenReturn(sampleActivity);
        final MultipartFile imageFile = Mockito.mock(MultipartFile.class);

        // Act
        PersistentActivityEntity result = activitiesService.addActivity(sampleActivity.getTitle(),
                sampleActivity.getDescription(), sampleActivity.getStartDate(), sampleActivity.getEndDate(),
                sampleActivity.getCategory(), imageFile);

        // Assert
        assertNotNull(result);
        assertEquals(sampleActivity.getTitle(), result.getTitle());
        assertEquals(sampleActivity.getDescription(), result.getDescription());
        verify(mockRepository, times(1)).save(any(PersistentActivityEntity.class));
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
        String existingActivityTitle = "Sample Activity";
        when(mockRepository.existsById(existingActivityTitle)).thenReturn(true);

        // Act & Assert
        ActivityServiceException exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.addActivity(existingActivityTitle, "Some description", LocalDateTime.now(),
                    LocalDateTime.now().plusHours(2), "Sample Category", Mockito.mock(MultipartFile.class));
        });

        assertEquals("Activity with title: Sample Activity already exists.", exception.getMessage());
        verify(mockRepository, times(1)).existsById(existingActivityTitle); // Verify if the check for existing title
                                                                            // was made
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
        String newActivityTitle = "New Activity with IO Exception";

        when(mockRepository.existsById(newActivityTitle)).thenReturn(false);
        MultipartFile mockImageFile = Mockito.mock(MultipartFile.class);
        when(mockImageFile.isEmpty()).thenReturn(false);

        // Directly stub the mock to throw an exception when saveImage is called
        when(imageStorageService.saveImage(any(), any(), any(), any()))
                .thenThrow(new IOException("Error saving image"));

        // Act & Assert
        ActivityServiceException exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.addActivity(newActivityTitle, "Description of activity with image failure",
                    LocalDateTime.now(), LocalDateTime.now().plusHours(2), "New Category", mockImageFile);
        });

        assertEquals("Error saving activity image: Error saving image", exception.getMessage());
        verify(mockRepository, times(0)).save(any(PersistentActivityEntity.class));
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
        String newActivityTitle = "Sample Activity";
        when(mockRepository.existsById(newActivityTitle)).thenReturn(false); // Activity title doesn't exist
        when(mockRepository.save(any(PersistentActivityEntity.class))).thenReturn(sampleActivity);
        MultipartFile emptyImageFile = Mockito.mock(MultipartFile.class);
        when(emptyImageFile.isEmpty()).thenReturn(true); // Simulating an empty image file

        // Act
        PersistentActivityEntity result = activitiesService.addActivity(newActivityTitle, "Description of new activity",
                LocalDateTime.now(), LocalDateTime.now().plusHours(2), "New Category", emptyImageFile);

        // Assert
        assertNotNull(result);
        assertNull(result.getImageSrc(), "Expected imageSrc to be null when imageFile is empty.");
        assertEquals(newActivityTitle, result.getTitle());
        assertEquals("A sample description", result.getDescription());
        verify(mockRepository, times(1)).save(any(PersistentActivityEntity.class)); // Ensure save was called once
    }

    @Test
    void testGetActivityImage_IOExceptionWhileLoadingImage() throws Exception {
        // Arrange
        String title = "Sample Activity with Image";
        String imagePath = "path/to/image.jpg";
        PersistentActivityEntity mockActivity = mock(PersistentActivityEntity.class);
        when(mockActivity.getImageSrc()).thenReturn(imagePath);
        when(mockRepository.findById(title)).thenReturn(Optional.of(mockActivity));
        when(imageStorageService.loadImage(imagePath)).thenThrow(new IOException("Image loading error"));

        // Act & Assert
        ActivityServiceException exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.getActivityImage(title);
        });

        assertEquals("Error loading activity image: Image loading error", exception.getMessage());
        verify(imageStorageService, times(1)).loadImage(imagePath);
    }

    @Test
    void testGetActivityImage_Success() throws Exception {
        // Arrange
        String title = "Sample Activity";
        String imagePath = "path/to/image.jpg";
        byte[] imageData = "sample image data".getBytes();

        PersistentActivityEntity mockActivity = mock(PersistentActivityEntity.class);
        when(mockActivity.getImageSrc()).thenReturn(imagePath);
        when(mockRepository.findById(title)).thenReturn(Optional.of(mockActivity));
        when(imageStorageService.loadImage(imagePath)).thenReturn(imageData);

        // Act
        byte[] result = activitiesService.getActivityImage(title);

        // Assert
        assertArrayEquals(imageData, result);
        verify(imageStorageService, times(1)).loadImage(imagePath);
    }

    @Test
    void testGetActivityImageNoImageSource() throws IOException {
        // Arrange
        String title = "Sample Activity with No Image";
        PersistentActivityEntity mockActivity = mock(PersistentActivityEntity.class);
        when(mockActivity.getImageSrc()).thenReturn(null);
        when(mockRepository.findById(title)).thenReturn(Optional.of(mockActivity));

        // Act & Assert
        ActivityServiceException exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.getActivityImage(title);
        });

        assertEquals("No image associated with activity: " + title, exception.getMessage());
        verify(imageStorageService, never()).loadImage(anyString());
    }

    @Test
    void testGetActivityImageNoImageSourceEmpty() throws IOException {
        // Arrange
        String title = "Sample Activity with Empty Image";
        PersistentActivityEntity mockActivity = mock(PersistentActivityEntity.class);
        when(mockActivity.getImageSrc()).thenReturn("");
        when(mockRepository.findById(title)).thenReturn(Optional.of(mockActivity));

        // Act & Assert
        ActivityServiceException exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.getActivityImage(title);
        });

        assertEquals("No image associated with activity: " + title, exception.getMessage());
        verify(imageStorageService, never()).loadImage(anyString());
    }

    @Test
    void testGetActivityImageNoImageSourceNull() throws IOException {
        // Arrange
        String title = "Sample Activity with No Image";
        PersistentActivityEntity mockActivity = mock(PersistentActivityEntity.class);
        when(mockActivity.getImageSrc()).thenReturn(null);
        when(mockRepository.findById(title)).thenReturn(Optional.of(mockActivity));

        // Act & Assert
        ActivityServiceException exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.getActivityImage(title);
        });

        assertEquals("No image associated with activity: " + title, exception.getMessage());
        verify(imageStorageService, never()).loadImage(anyString());
    }

    /**
     * Test for the getActivity method to verify that an exception is thrown if an
     * activity is not found.
     */
    @Test
    void testGetActivityNotFound() {
        // Arrange
        when(mockRepository.findById("Sample Activity")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.getActivity("Sample Activity");
        });

        assertEquals("Activity with title: Sample Activity not found.", exception.getMessage());
        verify(mockRepository, times(1)).findById("Sample Activity");
    }

    /**
     * Test for the getActivity method to verify an existing activity is returned by
     * ID.
     */
    @Test
    void testGetActivitySuccess() throws ActivityServiceException {
        // Arrange
        when(mockRepository.findById("Sample Activity")).thenReturn(Optional.of(sampleActivity));

        // Act
        PersistentActivityEntity result = activitiesService.getActivity("Sample Activity");

        // Assert
        assertNotNull(result);
        assertEquals(sampleActivity.getTitle(), result.getTitle());
        verify(mockRepository, times(1)).findById("Sample Activity");
    }

//    @Test
//    void testGetAllActivities_Success() throws Exception {
//        // Arrange
//        PersistentActivityEntity activity1 = mock(PersistentActivityEntity.class);
//        when(activity1.getTitle()).thenReturn("Sample Activity");
//        when(activity1.getDescription()).thenReturn("Description 1");
//        when(activity1.getStartDate()).thenReturn(LocalDateTime.now());
//        when(activity1.getEndDate()).thenReturn(LocalDateTime.now().plusHours(2));
//        when(activity1.getCategory()).thenReturn("Category 1");
//
//        // Mock the image retrieval
//        byte[] imageData = "sample image data".getBytes();
//        when(imageStorageService.loadImage(anyString())).thenReturn(imageData);
//
//        // Simulate the repository returning a list of activities
//        when(mockRepository.findAll()).thenReturn(Arrays.asList(activity1));
//
//        // Mocks the findById method of the repository to return an Optional containing
//        // activity1
//        when(mockRepository.findById("Sample Activity")).thenReturn(Optional.of(activity1));
//
//        // Act
//        Stream<ActivityWithImageDto> result = activitiesService.getAllActivities();
//
//        // Assert
//        assertNotNull(result);
//        ActivityWithImageDto activityDto = result.findFirst().get();
//        assertEquals("Activity 1", activityDto.title());
//        assertEquals("Description 1", activityDto.description());
//        assertNotNull(activityDto.image()); // Image should be encoded
//    }

    /**
     * Test for the getAllActivities method to verify behavior when no activities
     * are found.
     */
    @Test
    void testGetAllActivitiesNoActivities() throws ActivityServiceException {
        // Arrange
        when(mockRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ActivityWithImageDto> result = activitiesService.getAllActivities().toList();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockRepository, times(1)).findAll();
    }

}
