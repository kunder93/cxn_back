package es.org.cxn.backapp.model;

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
