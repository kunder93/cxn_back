package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentProfileImageEntity;

/**
 * Represents a response containing profile image information.
 *
 * <p>
 * This record encapsulates details about a user's profile image, including its
 * extension, storage status, URL, and optional file data.
 * </p>
 *
 * @param imageExtension the extension of the image (e.g., "jpg", "png").
 * @param stored         indicates whether the image is stored.
 * @param url            the URL where the image can be accessed.
 * @param file           optional data representing the image file content.
 */
public record ProfileImageResponse(String imageExtension, boolean stored, String url, String file) {

    /**
     * Constructs a ProfileImageResponse from a PersistentProfileImageEntity.
     *
     * @param profileImageEntity the entity containing profile image data. Must not
     *                           be null.
     */
    public ProfileImageResponse(final PersistentProfileImageEntity profileImageEntity) {
        // Convert the ImageExtension enum to a string using name() or toString()
        this(profileImageEntity.getExtension().name(), profileImageEntity.getStored(), profileImageEntity.getUrl(),
                null);
    }

    /**
     * Constructs a ProfileImageResponse from a PersistentProfileImageEntity and
     * image file data.
     *
     * @param profileImageEntity the entity containing profile image data. Must not
     *                           be null.
     * @param imageFileData      the data representing the image file content.
     */
    public ProfileImageResponse(final PersistentProfileImageEntity profileImageEntity, final String imageFileData) {
        // Convert the ImageExtension enum to a string using name() or toString()
        this(profileImageEntity.getExtension().name(), profileImageEntity.getStored(), profileImageEntity.getUrl(),
                imageFileData);
    }

}
