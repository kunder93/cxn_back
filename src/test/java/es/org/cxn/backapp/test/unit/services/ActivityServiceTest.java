
package es.org.cxn.backapp.test.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * Test for the getAllActivities method to verify behavior when no activities
     * are found.
     */
    @Test
    void testGetAllActivities_NoActivities() throws ActivityServiceException {
        // Arrange
        when(mockRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ActivityWithImageDto> result = activitiesService.getAllActivities().collect(Collectors.toList());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockRepository, times(1)).findAll();
    }
}
