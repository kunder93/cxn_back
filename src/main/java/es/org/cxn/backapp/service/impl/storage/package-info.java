/**
 * Provides implementations for storage-related services within the application.
 * <p>
 * This package includes classes and enumerations responsible for managing file
 * storage, handling image uploads, and organizing storage locations.
 * </p>
 *
 * <h2>Key Components:</h2>
 * <ul>
 * <li>{@link es.org.cxn.backapp.service.impl.storage.FileLocation} - Enum
 * defining various storage locations for different types of files.</li>
 * <li>{@link es.org.cxn.backapp.service.impl.storage.DefaultImageStorageService}
 * - Implementation of the
 * {@link es.org.cxn.backapp.service.ImageStorageService} interface, responsible
 * for storing and retrieving images.</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * <p>
 * The {@code DefaultImageStorageService} allows storing images under different
 * categories defined in {@code FileLocation}, ensuring organized file
 * management.
 * </p>
 *
 */
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
