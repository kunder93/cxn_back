
package es.org.cxn.backapp.model.persistence;

/**
 * Enum representing the various image extensions that can be used for profile
 * images.
 * <p>
 * The available extensions include:
 * <ul>
 * <li>{@link #AVIF} - AVIF image format</li>
 * <li>{@link #WEBP} - WebP image format</li>
 * <li>{@link #JPEG} - JPEG image format</li>
 * <li>{@link #JPG} - JPG image format</li>
 * <li>{@link #PNG} - PNG image format</li>
 * <li>{@link #PROVIDED} - A placeholder for images provided by the
 * application</li>
 * </ul>
 */
public enum ImageExtension {

    /**
     * AVIF image format.
     */
    AVIF("avif"),

    /**
     * WebP image format.
     */
    WEBP("webp"),

    /**
     * JPEG image format.
     */
    JPEG("jpeg"),

    /**
     * JPG image format, commonly used.
     */
    JPG("jpg"),

    /**
     * PNG image format, known for its lossless compression.
     */
    PNG("png"),

    /**
     * A placeholder for images provided by the application.
     */
    PROVIDED("provided");

    /**
     * Get the ImageExtension corresponding to a given string.
     *
     * @param extension the extension string to convert.
     * @return the corresponding ImageExtension, or null if invalid.
     */
    public static ImageExtension fromString(final String extension) {
        for (final ImageExtension imgExt : values()) {
            if (imgExt.getExtension().equalsIgnoreCase(extension)) {
                return imgExt;
            }
        }
        return null; // Return null if no match found
    }

    /**
     * Check if the provided extension is valid.
     *
     * @param extension the extension to check.
     * @return true if the extension is valid, false otherwise.
     */
    public static boolean isValidExtension(final String extension) {
        for (final ImageExtension imgExt : values()) {
            if (imgExt.getExtension().equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The string representation of the image extension.
     * <p>
     * This field stores the specific file extension associated with each image
     * format in the enum. It is used to provide a standardized way to refer to
     * image types across the application.
     * </p>
     */
    private final String extension;

    /**
     * Constructor for the enum to set the extension value.
     *
     * @param value the string representation of the image extension.
     */
    ImageExtension(final String value) {
        this.extension = value;
    }

    /**
     * Gets the string representation of the image extension.
     *
     * @return the extension as a string.
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Returns the string representation of the image extension.
     *
     * @return the string value of the enum constant.
     */
    @Override
    public String toString() {
        return extension;
    }
}
