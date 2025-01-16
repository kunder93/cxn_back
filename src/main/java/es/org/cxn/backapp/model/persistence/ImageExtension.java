
package es.org.cxn.backapp.model.persistence;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
     * Get the ImageExtension corresponding to a given string.
     *
     * @param extension the extension string to convert.
     * @return the corresponding ImageExtension, or null if invalid.
     */
    public static ImageExtension fromString(final String extension) {
        ImageExtension result = null; // Initialize result with null
        for (final ImageExtension imgExt : values()) {
            if (imgExt.getExtension().equalsIgnoreCase(extension)) {
                result = imgExt;
                break; // Exit the loop once a match is found
            }
        }
        return result; // Return result at the end
    }

    /**
     * Check if the provided extension is valid.
     *
     * @param extension the extension to check.
     * @return true if the extension is valid, false otherwise.
     */
    public static boolean isValidExtension(final String extension) {
        boolean isValid = false; // Initialize the result as false
        for (final ImageExtension imgExt : values()) {
            if (imgExt.getExtension().equalsIgnoreCase(extension)) {
                isValid = true; // Update the result if a match is found
                break; // Exit the loop early
            }
        }
        return isValid; // Return the result at the end
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
