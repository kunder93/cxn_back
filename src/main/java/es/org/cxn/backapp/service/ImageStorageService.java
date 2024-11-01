/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for handling image storage operations. Provides methods to
 * save, load, and delete images associated with different entities.
 */
public interface ImageStorageService {
    /**
     * Deletes an image file from the storage based on its path.
     *
     * @param baseDirectory the root directory for storing images, specified by the
     *                      calling service.
     * @param imagePath     the {@link String} representing the path to the image
     *                      file.
     * @throws IOException if an error occurs during file deletion.
     */
    void deleteImage(String baseDirectory, String imagePath) throws IOException;

    /**
     * Loads an image file from the storage based on its path.
     *
     * @param baseDirectory the root directory for storing images, specified by the
     *                      calling service.
     * @param imagePath     the {@link String} representing the path to the image
     *                      file.
     * @return a byte array containing the image data.
     * @throws IOException if an error occurs during file reading.
     */
    byte[] loadImage(String baseDirectory, String imagePath) throws IOException;

    /**
     * Saves an image file associated with a specific entity.
     *
     * @param file          the {@link MultipartFile} representing the image to be
     *                      saved.
     * @param baseDirectory the root directory for storing images, specified by the
     *                      calling service.
     * @param entityType    a string representing the type of entity (e.g.,
     *                      "activity", "user").
     * @param entityId      the unique identifier of the entity associated with the
     *                      image.
     * @return a {@link String} representing the path where the image is stored.
     * @throws IOException if an error occurs during file saving.
     */
    String saveImage(MultipartFile file, String baseDirectory, String entityType, String entityId) throws IOException;
}
