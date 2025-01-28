package es.org.cxn.backapp.service.dto;

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

import java.util.Set;

/**
 * Represents a Data Transfer Object (DTO) for transferring magazine data
 * between application layers.
 * <p>
 * This DTO encapsulates core book information along with associated authors,
 * while avoiding direct exposure of persistence entities. It is typically used
 * in API responses or service-layer communication.
 * </p>
 *
 * @param issn        The International Standard Serial Number (ISSN) uniquely
 *                    identifying the magazine
 * @param title       The title of the magazine
 * @param description A brief summary describing the magazine's content
 * @param publishDate The publication date of the magazine (format as String,
 *                    e.g., "YYYY-MM-DD")
 * @param language    The language in which the magazine is written (e.g.,
 *                    "English", "Spanish")
 * @param authors     A set of {@link AuthorDataDto} objects representing the
 *                    magazine's authors
 */
public record MagazineDataImageDto(String issn, String title, String description, String publishDate, String language,
        Set<AuthorDataDto> authors) {

}
