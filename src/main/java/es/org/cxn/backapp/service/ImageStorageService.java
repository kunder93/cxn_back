
package es.org.cxn.backapp.service;

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

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.service.impl.storage.FileLocation;

/**
 * Service interface for handling image storage operations. Provides methods to
 * save, load, and delete images associated with different entities.
 */
public interface ImageStorageService {
    /**
     * Deletes an image file from the storage based on its path.
     *
     * @param imagePath the {@link String} representing the path to the image file.
     * @throws IOException if an error occurs during file deletion.
     */
    void deleteImage(String imagePath) throws IOException;

    /**
     * Loads an image file from the storage based on its path.
     *
     * @param imagePath the {@link String} representing the path to the image file.
     * @return a byte array containing the image data.
     * @throws IOException if an error occurs during file reading.
     */
    byte[] loadImage(String imagePath) throws IOException;

    /**
     * Saves an image file.
     *
     * @param file         the {@link MultipartFile} representing the image to be
     *                     saved.
     * @param fileLocation the enum type representing the location of the file.
     * @return a {@link String} representing the path where the image is stored.
     * @throws IOException if an error occurs during file saving.
     */
    String saveImage(MultipartFile file, FileLocation fileLocation) throws IOException;

    /**
     * Saves an image file in userId folder name.
     *
     * @param file         the {@link MultipartFile} representing the image to be
     *                     saved.
     * @param userId       the user identifier for save images in folder with name
     *                     as user identifier.
     * @param fileLocation the enum type representing the location of the file.
     * @return a {@link String} representing the path where the image is stored.
     * @throws IOException if an error occurs during file saving.
     */
    String saveImage(MultipartFile file, FileLocation fileLocation, String userId) throws IOException;

}
