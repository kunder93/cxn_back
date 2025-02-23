package es.org.cxn.backapp.service.impl.storage;

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

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Enum representing different file storage locations within the application.
 * This enum defines specific directories for storing various types of images
 * and documents, such as profile images, activity images, DNI (identity
 * documents), book covers, and magazine covers.
 * <p>
 * Each enum constant corresponds to a directory key that is used to resolve
 * file storage paths.
 * </p>
 *
 */
public enum FileLocation {

    /**
     * Directory for storing profile images.
     */
    PROFILE_IMAGES("profile_images"),

    /**
     * Directory for storing images related to activities.
     */
    ACTIVITY_IMAGES("activity_images"),

    /**
     * Directory for storing scanned images of DNI (National Identity Documents).
     */
    DNI("dni"),

    /**
     * Directory for storing book cover images.
     */
    BOOK_COVERS("book_covers"),

    /**
     * Directory for storing magazine cover images.
     */
    MAGAZINE_COVERS("magazine_covers");

    /**
     * The directory key representing the storage location.
     */
    private final String directoryKey;

    /**
     * Constructs a {@code FileLocation} enum constant with the specified directory
     * key.
     *
     * @param directoryKey The key representing the storage directory.
     */
    FileLocation(final String directoryKey) {
        this.directoryKey = directoryKey;
    }

    /**
     * Gets the directory key associated with this storage location.
     *
     * @return The directory key as a {@code String}.
     */
    public String getDirectoryKey() {
        return this.directoryKey;
    }

    /**
     * Resolves the full storage path by combining the given base directory with the
     * directory key.
     *
     * @param baseDirectory The base directory where files are stored.
     * @return A {@code Path} object representing the resolved storage path.
     */
    public Path resolvePath(final String baseDirectory) {
        return Paths.get(baseDirectory, directoryKey);
    }
}
