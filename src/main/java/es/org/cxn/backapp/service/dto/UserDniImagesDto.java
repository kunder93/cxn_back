
package es.org.cxn.backapp.service.dto;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a pair of images for a user's DNI (front and back).
 *
 * @param frontImage The byte array representing the front side of the DNI.
 * @param backImage  The byte array representing the back side of the DNI.
 */
public record UserDniImagesDto(byte[] frontImage, byte[] backImage) {

    /**
     * Checks if two DniImages objects are equal based on their byte array contents.
     *
     * @param obj The object to compare.
     * @return true if both front and back images are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserDniImagesDto that = (UserDniImagesDto) obj;
        return Arrays.equals(frontImage, that.frontImage) && Arrays.equals(backImage, that.backImage);
    }

    /**
     * Generates a hash code based on the byte arrays of the images.
     *
     * @return The computed hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(frontImage), Arrays.hashCode(backImage));
    }

    /**
     * Returns a string representation of the object, indicating image sizes.
     *
     * @return A string describing the sizes of the front and back images.
     */
    @Override
    public String toString() {
        return "DniImages{" + "frontImageSize=" + (frontImage != null ? frontImage.length : 0) + " bytes, "
                + "backImageSize=" + (backImage != null ? backImage.length : 0) + " bytes}";
    }
}
