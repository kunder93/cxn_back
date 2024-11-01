package es.org.cxn.backapp.model;

import java.time.LocalDateTime;

/**
 * Represents an activity entity with details such as category, title,
 * description, start and end dates, creation timestamp, and unique identifier.
 * This interface provides getter and setter methods for each attribute.
 */
public interface ActivityEntity extends java.io.Serializable {

    /**
     * Gets the category of the activity.
     *
     * @return the activity category as a String.
     */
    String getCategory();

    /**
     * Gets the creation timestamp of the activity.
     *
     * @return the LocalDateTime when the activity was created.
     */
    LocalDateTime getCreatedAt();

    /**
     * Gets the description of the activity.
     *
     * @return the description as a String.
     */
    String getDescription();

    /**
     * Gets the end date and time of the activity.
     *
     * @return the LocalDateTime when the activity ends.
     */
    LocalDateTime getEndDate();

    /**
     * Gets the unique identifier for the activity.
     *
     * @return the activity ID as an Integer.
     */
    Integer getId();

    /**
     * Gets the image source of the activity.
     *
     * @return The image source as String.
     */
    String getImageSrc();

    /**
     * Gets the start date and time of the activity.
     *
     * @return the LocalDateTime when the activity starts.
     */
    LocalDateTime getStartDate();

    /**
     * Gets the title of the activity.
     *
     * @return the title as a String.
     */
    String getTitle();

    /**
     * Sets the category of the activity.
     *
     * @param category the category to set for this activity.
     */
    void setCategory(String category);

    /**
     * Sets the creation timestamp for the activity.
     *
     * @param value the LocalDateTime to set as the creation timestamp.
     */
    void setCreatedAt(LocalDateTime value);

    /**
     * Sets the description of the activity.
     *
     * @param value the description to set for this activity.
     */
    void setDescription(String value);

    /**
     * Sets the end date and time of the activity.
     *
     * @param value the LocalDateTime when the activity ends.
     */
    void setEndDate(LocalDateTime value);

    /**
     * Sets the unique identifier for the activity.
     *
     * @param value the ID to set for this activity.
     */
    void setId(Integer value);

    /**
     * Sets the image source of the activity.
     *
     * @param value the image source to set for this activity.
     */
    void setImageSrc(String value);

    /**
     * Sets the start date and time of the activity.
     *
     * @param value the LocalDateTime when the activity starts.
     */
    void setStartDate(LocalDateTime value);

    /**
     * Sets the title of the activity.
     *
     * @param value the title to set for this activity.
     */
    void setTitle(String value);

}
