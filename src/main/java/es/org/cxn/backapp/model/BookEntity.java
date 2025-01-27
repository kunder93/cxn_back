
package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

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
 * Interface representing a book entity in the library. This interface provides
 * methods to access various details about a book, such as its ISBN number,
 * title, genre, publication year, language, and associated authors.
 *
 * @author Santiago Paz.
 */
public interface BookEntity extends Serializable {

    /**
     * Gets the set of authors associated with the book.
     *
     * @return A set of {@link PersistentAuthorEntity} representing the authors of
     *         the book.
     */
    Set<PersistentAuthorEntity> getAuthors();

    /**
     * Get the book description.
     *
     * @return The book description.
     */
    String getDescription();

    /**
     * Gets the genre of the book.
     *
     * @return The book's genre.
     */
    String getGenre();

    /**
     * Gets the ISBN number of the book.
     *
     * @return The book's ISBN number.
     */
    String getIsbn();

    /**
     * Gets the language of the book.
     *
     * @return The language in which the book is written.
     */
    String getLanguage();

    /**
     * Gets the publication year of the book.
     *
     * @return The book's publication year as a {@link LocalDate}.
     */
    LocalDate getPublishYear();

    /**
     * Gets the title of the book.
     *
     * @return The book's title.
     */
    String getTitle();
}
