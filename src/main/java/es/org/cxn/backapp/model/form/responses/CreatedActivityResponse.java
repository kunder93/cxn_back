
package es.org.cxn.backapp.model.form.responses;

import java.time.LocalDateTime;

import es.org.cxn.backapp.model.persistence.PersistentActivityEntity;

/**
 * Represents a response containing details of a newly created activity.
 *
 * @param title       The title of the activity.
 * @param description A description of the activity.
 * @param startDate   The start date and time of the activity.
 * @param endDate     The end date and time of the activity.
 * @param category    The category of the activity.
 * @param createdAt   The date and time when the activity was created.
 */
public record CreatedActivityResponse(String title, String description, LocalDateTime startDate, LocalDateTime endDate,
        String category, LocalDateTime createdAt) {

    /**
     * Constructs a new {@code CreatedActivityResponse} from a
     * {@code PersistentActivityEntity}.
     *
     * @param activity The {@code PersistentActivityEntity} representing the
     *                 activity data.
     */
    public CreatedActivityResponse(final PersistentActivityEntity activity) {
        this(activity.getTitle(), activity.getDescription(), activity.getStartDate(), activity.getEndDate(),
                activity.getCategory(), activity.getCreatedAt());
    }
}