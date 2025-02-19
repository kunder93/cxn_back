
package es.org.cxn.backapp.service.impl.storage;

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
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
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
     * Location for storage images. Provided by app .properties file.
     */
    @Value("${storage.location}")
    private String baseDirectory;

    /**
     * Constructor for DefaultImageStorageService class. This constructor is needed
     * for Spring Framework's dependency injection.
     * <p>
     * The default constructor is implicitly used by Spring to inject dependencies,
     * if any are required.
     * </p>
     */
    public DefaultImageStorageService() {
        super();
        // No need for explicit constructor logic if no dependencies are being injected.
    }

    /**
     * Deletes an image file at the specified path within a base directory.
     *
     *
     * @param imagePath the relative path of the image to delete
     * @throws IOException if an error occurs while deleting the file
     */
    @Override
    public void deleteImage(final String imagePath) throws IOException {
        final Path path = Path.of(imagePath);
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
        final Path path = Path.of(imagePath);
        return Files.readAllBytes(path);
    }

    /**
     * Saves an image file in userId directory and inside this directory in
     * FileLocation directory.
     *
     * @param file         the MultipartFile representing the image to save
     * @param fileLocation the enum type representing the location of the file
     * @return the path where the image was saved as a String
     * @throws IOException if an error occurs while saving the file
     */
    @Override
    public String saveImage(final MultipartFile file, final FileLocation fileLocation) throws IOException {
        // Resolve the directory for the specified file location
        final Path directoryPath = Path.of(baseDirectory).resolve(fileLocation.getDirectoryKey());
        Files.createDirectories(directoryPath);

        // Save the file to the resolved path
        final Path filePath = directoryPath.resolve(file.getOriginalFilename());
        file.transferTo(filePath.toFile());

        return filePath.toString();
    }

    /**
     * Saves an image file in userId directory and inside this directory in
     * FileLocation directory.
     *
     * @param file         the MultipartFile representing the image to save
     * @param fileLocation the enum type representing the location of the file
     * @param userId       the user identifier for save image in folder with user id
     *                     name.
     * @return the path where the image was saved as a String
     * @throws IOException if an error occurs while saving the file
     */
    @Override
    public String saveImage(final MultipartFile file, final FileLocation fileLocation, final String userId)
            throws IOException {
        // Resolve the directory for the specified file location
        final Path directoryPath = Path.of(baseDirectory).resolve(userId).resolve(fileLocation.getDirectoryKey());
        Files.createDirectories(directoryPath);

        // Save the file to the resolved path
        final Path filePath = directoryPath.resolve(file.getOriginalFilename());
        file.transferTo(filePath.toFile());

        return filePath.toString();
    }
}
