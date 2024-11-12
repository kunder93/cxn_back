package es.org.cxn.backapp.model.form.requests;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents a request to add a new activity.
 * <p>
 * Fields:
 * <ul>
 * <li>title - The title of the activity. Must not be null and must have a
 * length between {@link ValidationConstants#ACTIVITY_TITLE_MIN_LENGTH} and
 * {@link ValidationConstants#ACTIVITY_TITLE_MAX_LENGTH} characters.</li>
 * <li>description - A description of the activity. Must not be null and must
 * have a length between
 * {@link ValidationConstants#ACTIVITY_DESCRIPTION_MIN_LENGTH} and
 * {@link ValidationConstants#ACTIVITY_DESCRIPTION_MAX_LENGTH} characters.</li>
 * <li>startDate - The start date and time of the activity. Must not be null and
 * must be present or in the future.</li>
 * <li>endDate - The end date and time of the activity. Must not be null and
 * must be present or in the future.</li>
 * <li>category - The category of the activity. Must not be null.</li>
 * </ul>
 *
 * @param title       The title of the activity. Must not be null and must have
 *                    a length between
 *                    {@link ValidationConstants#ACTIVITY_TITLE_MIN_LENGTH} and
 *                    {@link ValidationConstants#ACTIVITY_TITLE_MAX_LENGTH}
 *                    characters.
 * @param description A description of the activity. Must not be null and must
 *                    have a length between
 *                    {@link ValidationConstants#ACTIVITY_DESCRIPTION_MIN_LENGTH}
 *                    and
 *                    {@link ValidationConstants#ACTIVITY_DESCRIPTION_MAX_LENGTH}
 *                    characters.
 * @param startDate   The start date and time of the activity. Must not be null
 *                    and must be present or in the future.
 * @param endDate     The end date and time of the activity. Must not be null
 *                    and must be present or in the future.
 * @param category    The category of the activity. Must not be null.
 */
public record AddActivityRequestData(

        @NotNull(message = ValidationMessages.ACTIVITY_TITLE_NOT_NULL)
        @Size(min = ValidationConstants.ACTIVITY_TITLE_MIN_LENGTH, max = ValidationConstants.ACTIVITY_TITLE_MAX_LENGTH,
                message = ValidationMessages.ACTIVITY_TITLE_MAX_MIN_LENGTH) String title,

        @NotNull(message = ValidationMessages.ACTIVITY_DESCRIPTION_NOT_NULL)
        @Size(min = ValidationConstants.ACTIVITY_DESCRIPTION_MIN_LENGTH,
                max = ValidationConstants.ACTIVITY_DESCRIPTION_MAX_LENGTH,
                message = ValidationMessages.ACTIVITY_DESCRIPTION_MAX_MIN_LENGTH) String description,

        @FutureOrPresent(message = ValidationMessages.ACTIVITY_START_DATE_PRESENT_OR_FUTURE)
        @NotNull(message = ValidationMessages.ACTIVITY_START_DATE_NOT_NULL)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

        @FutureOrPresent(message = ValidationMessages.ACTIVITY_END_DATE_PRESENT_OR_FUTURE)
        @NotNull(message = ValidationMessages.ACTIVITY_END_DATE_NOT_NULL)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,

        @NotNull(message = ValidationMessages.ACTIVITY_CATEGORY_NOT_NULL) String category) {
}
