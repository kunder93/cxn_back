package es.org.cxn.backapp.model.form.responses.member_resources;

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
import java.util.stream.Collectors;

import es.org.cxn.backapp.model.MagazineEntity;

/**
 * Represents the response DTO (Data Transfer Object) for a magazine entity.
 * This record is used to transfer magazine-related data between the backend and
 * frontend or other services.
 * <p>
 * The {@link MagazineResponse} includes information about a magazine, such as
 * its ISSN, title, publication year, language, description, and associated
 * authors.
 * </p>
 *
 * @param issn        The ISSN number of the magazine, represented as a string.
 * @param title       The title of the magazine.
 * @param publishYear The publication year of the magazine, represented as a
 *                    string.
 * @param language    The language in which the magazine is written.
 * @param description The description of the magazine.
 * @param authors     A set of {@link AuthorResponse} representing the authors
 *                    of the magazine.
 *
 * @author Santiago Paz
 */
public record MagazineResponse(String issn, String title, String publishYear, String language, String description,
        Set<AuthorResponse> authors) implements Comparable<MagazineResponse> {

    /**
     * Constructs a {@link MagazineResponse} from a {@link MagazineEntity}.
     * <p>
     * This constructor maps the properties of the {@link MagazineEntity} to the
     * corresponding fields of the {@link MagazineResponse}.
     * </p>
     *
     * @param magazine The {@link MagazineEntity} containing the magazine data.
     */
    public MagazineResponse(final MagazineEntity magazine) {
        this(magazine.getIssn(), magazine.getTitle(), magazine.getPublishDate().toString(), magazine.getLanguage(),
                magazine.getDescription(),
                magazine.getAuthors().stream().map(AuthorResponse::new).collect(Collectors.toSet()));
    }

    /**
     * Compares this {@link MagazineResponse} with another {@link MagazineResponse}
     * by ISSN.
     *
     * @param other The other {@link MagazineResponse} to compare.
     * @return A negative integer, zero, or a positive integer as this
     *         {@link MagazineResponse} is less than, equal to, or greater than the
     *         specified {@link MagazineResponse}.
     */
    @Override
    public int compareTo(MagazineResponse other) {
        return this.issn.compareTo(other.issn); // Or you can compare by title or any other field
    }
}
