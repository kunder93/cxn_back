
package es.org.cxn.backapp.service.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing an activity, including its details
 * and associated image. This DTO is used by services to transfer activity
 * information along with an optional image in binary format.
 *
 * @param title       The title of the activity. Must not be null.
 * @param description A brief description of the activity. Must not be null.
 * @param startDate   The start date and time of the activity. Must not be null.
 * @param endDate     The end date and time of the activity. Must not be null.
 * @param category    category or type of the activity. Must not be null.
 * @param image       A byte array representing the image associated with the
 *                    activity. This field is optional and can be null if there
 *                    is no image provided.
 */
public record ActivityWithImageDto(String title, String description, LocalDateTime startDate, LocalDateTime endDate,
        String category, String image) {

}