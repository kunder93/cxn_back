package es.org.cxn.backapp.model.form.responses.user;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a pair of images for a user's DNI (front and back).
 *
 * @param frontImage The byte array representing the front side of the DNI.
 * @param backImage  The byte array representing the back side of the DNI.
 */
public record DniImagesResponse(byte[] frontImage, byte[] backImage) {

    /**
     * Checks if two DniImages objects are equal based on their byte array contents.
     *
     * @param obj The object to compare.
     * @return true if both front and back images are equal, false otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DniImagesResponse that = (DniImagesResponse) obj;
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
