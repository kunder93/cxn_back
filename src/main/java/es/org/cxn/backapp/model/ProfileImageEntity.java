package es.org.cxn.backapp.model;

import es.org.cxn.backapp.model.persistence.ImageExtension;

/**
 * Represents a profile image entity within the application. This interface
 * defines methods to get and set properties related to a user's profile image,
 * such as its extension, storage status, URL, associated user, and user DNI
 * (Document National Identity).
 */
public interface ProfileImageEntity extends java.io.Serializable {

    /**
     * Retrieves the file extension of the profile image.
     *
     * @return the extension of the profile image (e.g., "jpg", "png").
     */
    ImageExtension getExtension();

    /**
     * Retrieves the URL where the profile image is accessible.
     *
     * @return the URL of the profile image.
     */
    String getUrl();

    /**
     * Retrieves the user associated with this profile image.
     *
     * @return the {@link UserEntity} associated with this profile image.
     */
    UserEntity getUser();

    /**
     * Retrieves the DNI (Document National Identity) of the user associated with
     * this profile image.
     *
     * @return the user's DNI.
     */
    String getUserDni();

    /**
     * Checks if the profile image is stored in the system.
     *
     * @return true if the profile image is stored, false otherwise.
     */
    Boolean isStored();

    /**
     * Sets the file extension of the profile image.
     *
     * @param value the extension to set for the profile image.
     */
    void setExtension(ImageExtension value);

    /**
     * Sets the storage status of the profile image.
     *
     * @param value true if the profile image is stored, false otherwise.
     */
    void setStored(Boolean value);

    /**
     * Sets the URL of the profile image.
     *
     * @param value the URL to set for the profile image.
     */
    void setUrl(String value);

    /**
     * Sets the user dni.
     *
     * @param value The user dni.
     */
    void setUserDni(String value);

}
