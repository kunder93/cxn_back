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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.service.ImageStorageService;

/**
 * Service class for handling image storage operations, including saving,
 * loading, and deleting images. This implementation allows each service to
 * specify its own storage path, making it flexible across different
 * environments and requirements.
 * <p>
 * Images are organized into directories based on entity type and ID.
 * </p>
 *
 * @author Santiago
 */
@Service
public final class DefaultImageStorageService implements ImageStorageService {

    /**
     * Deletes an image file at the specified path within a base directory.
     *
     * @param baseDirectory the root directory for storing images, specified by the
     *                      calling service
     * @param imagePath     the relative path of the image to delete
     * @throws IOException if an error occurs while deleting the file
     */
    @Override
    public void deleteImage(final String baseDirectory, final String imagePath) throws IOException {
        final Path path = Paths.get(baseDirectory, imagePath);
        Files.deleteIfExists(path);
    }

    /**
     * Loads an image file as a byte array from the specified path within a base
     * directory.
     *
     * @param imagePath the relative path of the image to load
     * @return a byte array containing the image data
     * @throws IOException if an error occurs while reading the file
     */
    @Override
    public byte[] loadImage(final String imagePath) throws IOException {
        final Path path = Paths.get(imagePath);
        return Files.readAllBytes(path);
    }

    /**
     * Saves an image file to a specified directory, organized by entity type and
     * ID. If the directory structure does not exist, it is created automatically.
     *
     * @param file          the MultipartFile representing the image to save
     * @param baseDirectory the root directory for storing images, specified by the
     *                      calling service
     * @param entityType    the type of entity the image is associated with (e.g.,
     *                      "activity")
     * @param entityId      the unique identifier of the entity
     * @return the path where the image was saved as a String
     * @throws IOException if an error occurs while saving the file
     */
    @Override
    public String saveImage(final MultipartFile file, final String baseDirectory, final String entityType,
            final String entityId) throws IOException {
        final Path directoryPath = Paths.get(baseDirectory, entityType, entityId);
        Files.createDirectories(directoryPath);
        final Path filePath = directoryPath.resolve(file.getOriginalFilename());
        file.transferTo(filePath.toFile());
        return filePath.toString();
    }
}
