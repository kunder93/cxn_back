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
 * This DTO encapsulates key information about magazines, including publication
 * details, edition-specific data, and associated authors, while avoiding direct
 * exposure of persistence entities. It is typically used in API responses or
 * service-layer communication.
 * </p>
 *
 * @param issn          The International Standard Serial Number (ISSN) uniquely
 *                      identifying the magazine. It is an 8-digit code divided
 *                      into two groups of four digits (e.g., "1234-5678").
 *                      <p>
 *                      Example: {@code "2451-0569"}
 *                      </p>
 *
 * @param title         The title of the magazine.
 *                      <p>
 *                      Example: {@code "Chess Monthly Digest"}
 *                      </p>
 *
 * @param publisher     The name of the company or organization that publishes
 *                      the magazine.
 *                      <p>
 *                      Example: {@code "CXN Publications"}
 *                      </p>
 *
 * @param editionNumber The edition number of the magazine. Represents the
 *                      specific release in a series of editions.
 *                      <p>
 *                      Example: {@code 15}
 *                      </p>
 *
 * @param description   A brief summary describing the magazine's content. This
 *                      may include the focus, target audience, or highlights of
 *                      the issue.
 *                      <p>
 *                      Example: {@code "A special edition covering the history
 *                      of modern chess tournaments."}
 *                      </p>
 *
 * @param publishDate   The publication date of the magazine in the format
 *                      {@code "YYYY-MM-DD"}.
 *                      <p>
 *                      Example: {@code "2025-02-15"}
 *                      </p>
 *
 * @param pagesAmount   The total number of pages in the magazine.
 *                      <p>
 *                      Example: {@code 128}
 *                      </p>
 *
 * @param language      The primary language in which the magazine is written.
 *                      <p>
 *                      Example: {@code "English"}
 *                      </p>
 *
 * @param authors       A set of {@link AuthorDataDto} objects representing the
 *                      authors or contributors of the magazine. Each
 *                      {@link AuthorDataDto} contains detailed information
 *                      about an individual author.
 *                      <p>
 *                      Example: A magazine may have multiple contributors like
 *                      editors, writers, or illustrators.
 *                      </p>
 *
 * @see AuthorDataDto
 */
public record MagazineDataImageDto(String issn, String title, String publisher, int editionNumber, String description,
        String publishDate, int pagesAmount, String language, Set<AuthorDataDto> authors) {

}
