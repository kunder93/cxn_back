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
     * Hash multiplier used to generate hashCode.
     */
    private static final int HASH_MULTIPLIER = 31;

    /**
     * Constructs an {@code ActivityResponse} from an {@code ActivityEntity} and an
     * optional image as a byte array.
     *
     * @param entity The {@code ActivityEntity} providing details of the activity.
     *               Must not be null.
     * @param img    The image file associated with the activity. This is optional
     *               and can be null.
     * @throws NullPointerException if the provided {@code ActivityEntity} is null.
     */
    public ActivityResponse(final ActivityEntity entity, final byte[] img) {
        this(entity.getTitle(), entity.getDescription(), entity.getStartDate(), entity.getEndDate(),
                entity.getCategory(), img != null ? img.clone() : null);
    }

    /**
     * Constructs an {@code ActivityResponse} using all fields.
     * <p>
     * Creates a defensive copy of the provided {@code image} array to ensure
     * immutability.
     *
     * @param title       The title of the activity.
     * @param description The description of the activity.
     * @param startDate   The start date of the activity.
     * @param endDate     The end date of the activity.
     * @param category    The category of the activity.
     * @param image       The image file associated with the activity.
     */
    public ActivityResponse {
        image = image != null ? image.clone() : null; // Defensive copy of the array
    }

    /**
     * Returns the image file associated with the activity.
     * <p>
     * Provides a defensive copy of the internal {@code image} array to prevent
     * external modification.
     *
     * @return A copy of the {@code image} array, or {@code null} if no image is
     *         provided.
     */
    @Override
    public byte[] image() {
        return image != null ? image.clone() : null; // Defensive copy when returning
    }

    @Override
    public boolean equals(final Object obj) {
        boolean isEqual = false; // Initialize the result as false

        if (this == obj) {
            isEqual = true; // Same reference, objects are equal
        } else if (obj instanceof ActivityResponse other) {
            // Compare all fields for equality
            isEqual = title.equals(other.title) && description.equals(other.description)
                    && startDate.equals(other.startDate) && endDate.equals(other.endDate)
                    && category.equals(other.category) && Arrays.equals(image, other.image); // Use Arrays.equals for
                                                                                             // byte arrays
        }

        return isEqual; // Return the result at the end
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = HASH_MULTIPLIER * result + description.hashCode();
        result = HASH_MULTIPLIER * result + startDate.hashCode();
        result = HASH_MULTIPLIER * result + endDate.hashCode();
        result = HASH_MULTIPLIER * result + category.hashCode();
        result = HASH_MULTIPLIER * result + Arrays.hashCode(image);
        return result;
    }

    @Override
    public String toString() {
        return "ActivityResponse{" + "title='" + title + '\'' + ", description='" + description + '\'' + ", startDate="
                + startDate + ", endDate=" + endDate + ", category='" + category + '\'' + ", image="
                + Arrays.toString(image) + '}';
    }
}
