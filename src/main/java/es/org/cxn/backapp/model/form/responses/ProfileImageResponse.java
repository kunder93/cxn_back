package es.org.cxn.backapp.model.form.responses;

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
public record ProfileImageResponse(String imageExtension, Boolean stored, String url, String file) {

    /**
     * Constructs a ProfileImageResponse from a PersistentProfileImageEntity.
     *
     * @param profileImageEntity the entity containing profile image data. Must not
     *                           be null.
     */
    public ProfileImageResponse(final PersistentProfileImageEntity profileImageEntity) {
        // Convert the ImageExtension enum to a string using name() or toString()
        this(profileImageEntity.getExtension().name(), profileImageEntity.isStored(), profileImageEntity.getUrl(),
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
        this(profileImageEntity.getExtension().name(), profileImageEntity.isStored(), profileImageEntity.getUrl(),
                imageFileData);
    }

}
