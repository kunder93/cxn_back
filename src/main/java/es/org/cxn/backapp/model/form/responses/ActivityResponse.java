package es.org.cxn.backapp.model.form.responses;

import java.time.LocalDateTime;
import java.util.Arrays;

import es.org.cxn.backapp.model.ActivityEntity;

/**
 * Represents the response model for an activity. This record encapsulates the
 * details of an activity including title, description, start and end dates,
 * category, and an associated image file. Used to transfer activity data to
 * clients.
 *
 * @param title       The title of the activity. Must not be null.
 * @param description The description of the activity. Must not be null.
 * @param startDate   The start date and time of the activity. Must not be null.
 * @param endDate     The end date and time of the activity. Must not be null.
 * @param category    The category of the activity. Must not be null.
 * @param image       The image file associated with the activity. This is
 *                    optional and can be null if no image is provided.
 */
public record ActivityResponse(String title, String description, LocalDateTime startDate, LocalDateTime endDate,
        String category, byte[] image) {

    /**
     * Constructs an {@code ActivityResponse} from an {@code ActivityEntity} and an
     * optional {@code MultipartFile} image.
     *
     * @param entity The {@code ActivityEntity} providing details of the activity.
     *               Must not be null.
     * @param image  The image file associated with the activity. This is optional
     *               and can be null.
     * @throws NullPointerException if the provided {@code ActivityEntity} is null.
     */
    public ActivityResponse(final ActivityEntity entity, final byte[] image) {
        this(entity.getTitle(), entity.getDescription(), entity.getStartDate(), entity.getEndDate(),
                entity.getCategory(), image);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true; // Check if the same reference
        }
        if (!(o instanceof ActivityResponse other)) {
            return false; // Check if the object is an instance of the same class
        }

        // Compare all fields for equality
        return title.equals(other.title) && description.equals(other.description) && startDate.equals(other.startDate)
                && endDate.equals(other.endDate) && category.equals(other.category)
                && Arrays.equals(image, other.image); // Use Arrays.equals for byte arrays
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }

    @Override
    public String toString() {
        return "ActivityResponse{" + "title='" + title + '\'' + ", description='" + description + '\'' + ", startDate="
                + startDate + ", endDate=" + endDate + ", category='" + category + '\'' + ", image="
                + Arrays.toString(image) + '}';
    }
}