
package es.org.cxn.backapp.test.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
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

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityEntityRepository mockRepository;

    @Mock
    private DefaultImageStorageService imageStorageService;

    @InjectMocks
    private DefaultActivitiesService activitiesService;

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
        when(mockRepository.save(sampleActivity)).thenReturn(sampleActivity);
        final MultipartFile imageFile = Mockito.mock(MultipartFile.class);
        // Act
        PersistentActivityEntity result = activitiesService.addActivity(sampleActivity.getTitle(),
                sampleActivity.getDescription(), sampleActivity.getStartDate(), sampleActivity.getEndDate(),
                sampleActivity.getCategory(), imageFile);

        // Assert
        assertNotNull(result);
        assertEquals(sampleActivity.getTitle(), result.getTitle());
        assertEquals(sampleActivity.getDescription(), result.getDescription());
        verify(mockRepository, times(1)).save(sampleActivity);
    }

    /**
     * Test for the getActivity method to verify that an exception is thrown if an
     * activity is not found.
     */
    @Test
    void testGetActivityNotFound() {
        // Arrange
        when(mockRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ActivityServiceException.class, () -> {
            activitiesService.getActivity(1);
        });

        assertEquals("Activity with identifier: 1 not found.", exception.getMessage());
        verify(mockRepository, times(1)).findById(1);
    }

    /**
     * Test for the getActivity method to verify an existing activity is returned by
     * ID.
     */
    @Test
    void testGetActivitySuccess() throws ActivityServiceException {
        // Arrange
        when(mockRepository.findById(1)).thenReturn(Optional.of(sampleActivity));

        // Act
        PersistentActivityEntity result = activitiesService.getActivity(1);

        // Assert
        assertNotNull(result);
        assertEquals(sampleActivity.getId(), result.getId());
        assertEquals(sampleActivity.getTitle(), result.getTitle());
        verify(mockRepository, times(1)).findById(1);
    }

    /**
     * Test for the getAllActivities method to verify all activities are returned as
     * a Stream.
     */
    @Test
    void testGetAllActivities() {
        // Arrange
        List<PersistentActivityEntity> activityList = Arrays.asList(sampleActivity);
        when(mockRepository.findAll()).thenReturn(activityList);

        // Act
        List<PersistentActivityEntity> result = activitiesService.getAllActivities().toList();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(sampleActivity));
        verify(mockRepository, times(1)).findAll();
    }
}
