package es.org.cxn.backapp.model.form.responses;

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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import es.org.cxn.backapp.model.AuthorEntity;

/**
 * Represents the form used by the controller as a response for requesting all
 * authors.
 * <p>
 * This is a Data Transfer Object (DTO), designed to facilitate communication
 * between the view and the controller.
 * </p>
 *
 * @param authorList The author list with authors responses.
 * @author Santiago Paz.
 */
public record AuthorListResponse(Set<AuthorResponse> authorList) {

    /**
     * Private constructor to convert a list of {@link AuthorEntity} into a set of
     * {@link AuthorResponse}.
     *
     * @param value The list of author entities.
     */
    private AuthorListResponse(final List<AuthorEntity> value) {
        this(value.stream().map(AuthorResponse::new).collect(Collectors.toCollection(HashSet::new)));
    }

    /**
     * Constructs an {@code AuthorListResponse} with a defensive copy of the
     * provided set to prevent external modifications.
     *
     * @param authorList The set of {@link AuthorResponse}.
     */
    public AuthorListResponse {
        authorList = authorList != null ? Collections.unmodifiableSet(new HashSet<>(authorList))
                : Collections.emptySet();
    }

    /**
     * Factory method that takes a list of {@link AuthorEntity} and converts it into
     * a set of {@link AuthorResponse}.
     *
     * @param value The list of author entities.
     * @return An instance of {@link AuthorListResponse}.
     */
    public static AuthorListResponse from(final List<AuthorEntity> value) {
        return new AuthorListResponse(value);
    }

    /**
     * Returns an immutable set of authors to prevent external modification.
     *
     * @return An unmodifiable set of {@link AuthorResponse}.
     */
    @Override
    public Set<AuthorResponse> authorList() {
        return Collections.unmodifiableSet(authorList);
    }
}
