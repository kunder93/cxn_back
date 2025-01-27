
package es.org.cxn.backapp.model.form.requests.member_resources;

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

import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;

/**
 * Represents the form used by the controller to add a new author for a book.
 * <p>
 * This Data Transfer Object (DTO) is designed to facilitate communication
 * between the view and the controller by encapsulating the author's information
 * such as first name, last name, and nationality. It is specifically used when
 * adding a new book to the system, where author details are required.
 * </p>
 *
 * <p>
 * The {@code AuthorRequest} can be constructed directly with author details, or
 * by using an existing {@link PersistentAuthorEntity}. This flexibility allows
 * it to be easily integrated with both new author data inputs and existing
 * author data retrievals.
 * </p>
 *
 * @param firstName The first name of the author. This is a required field
 *                  representing the given name of the author.
 * @param lastName  The last name of the author. This is a required field
 *                  representing the family name or surname.
 *                  </p>
 *
 * @author Santiago Paz
 */
public record AuthorRequest(String firstName, String lastName) {

    /**
     * Constructs an {@code AuthorRequest} using the data from an existing
     * {@link PersistentAuthorEntity}.
     *
     * @param authEntity The {@link PersistentAuthorEntity} containing the author's
     *                   information.
     */
    public AuthorRequest(final PersistentAuthorEntity authEntity) {
        this(authEntity.getFirstName(), authEntity.getLastName());
    }
}
