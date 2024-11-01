package es.org.cxn.backapp.model.form.requests;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.form.requests.validation.ValidImageFile;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents a request to add a new activity.
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
 * @param imageFile   The image file associated with the activity. Must be a
 *                    valid image file type (JPEG, PNG, WebP, AVIF) and must not
 *                    exceed
 *                    {@link ValidationConstants#ACTIVITY_IMAGE_FILE_MAX_SIZE}
 *                    bytes.
 */
public record AddActivityRequest(

        /**
         * The title of the activity. Must not be null and must have a length between
         * {@link ValidationConstants#ACTIVITY_TITLE_MIN_LENGTH} and
         * {@link ValidationConstants#ACTIVITY_TITLE_MAX_LENGTH} characters.
         */
        @NotNull(message = ValidationMessages.ACTIVITY_TITLE_NOT_NULL)
        @Size(min = ValidationConstants.ACTIVITY_TITLE_MIN_LENGTH, max = ValidationConstants.ACTIVITY_TITLE_MAX_LENGTH,
                message = ValidationMessages.ACTIVITY_TITLE_MAX_MIN_LENGTH) String title,

        /**
         * A description of the activity. Must not be null and must have a length
         * between {@link ValidationConstants#ACTIVITY_DESCRIPTION_MIN_LENGTH} and
         * {@link ValidationConstants#ACTIVITY_DESCRIPTION_MAX_LENGTH} characters.
         */
        @NotNull(message = ValidationMessages.ACTIVITY_DESCRIPTION_NOT_NULL)
        @Size(min = ValidationConstants.ACTIVITY_DESCRIPTION_MIN_LENGTH,
                max = ValidationConstants.ACTIVITY_DESCRIPTION_MAX_LENGTH,
                message = ValidationMessages.ACTIVITY_DESCRIPTION_MAX_MIN_LENGTH) String description,

        /**
         * The start date and time of the activity. Must not be null and must be present
         * or in the future.
         */
        @FutureOrPresent(message = ValidationMessages.ACTIVITY_START_DATE_PRESENT_OR_FUTURE)
        @NotNull(message = ValidationMessages.ACTIVITY_START_DATE_NOT_NULL) LocalDateTime startDate,

        /**
         * The end date and time of the activity. Must not be null and must be present
         * or in the future.
         */
        @FutureOrPresent(message = ValidationMessages.ACTIVITY_END_DATE_PRESENT_OR_FUTURE)
        @NotNull(message = ValidationMessages.ACTIVITY_END_DATE_NOT_NULL) LocalDateTime endDate,

        /**
         * The category of the activity. Must not be null.
         */
        @NotNull(message = ValidationMessages.ACTIVITY_CATEGORY_NOT_NULL) String category,

        /**
         * The image file associated with the activity. Must be a valid image file type
         * and must not exceed {@link ValidationConstants#ACTIVITY_IMAGE_FILE_MAX_SIZE}
         * bytes.
         */
        @ValidImageFile(message = ValidationMessages.ACTIVITY_IMAGE_FILE_VALID,
                filesize = ValidationConstants.ACTIVITY_IMAGE_FILE_MAX_SIZE) MultipartFile imageFile

) {
}
