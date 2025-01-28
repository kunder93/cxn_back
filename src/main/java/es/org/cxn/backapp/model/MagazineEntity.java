
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
 * Interface representing a magazine entity in the library. This interface
 * provides methods to access various details about a magazine, such as its ISSN
 * number, title, publication year, language, and associated authors.
 *
 * @author Santiago Paz.
 */
public interface MagazineEntity extends Serializable {

    /**
     * Gets the set of authors associated with the magazine.
     *
     * @return A set of {@link PersistentAuthorEntity} representing the authors of
     *         the magazine.
     */
    Set<PersistentAuthorEntity> getAuthors();

    /**
     * Get the magazine cover image source.
     *
     * @return the cover image location.
     */
    String getCoverSrc();

    /**
     * Get the magazine description.
     *
     * @return The magazine description.
     */
    String getDescription();

    /**
     * Gets the ISSN number of the magazine.
     *
     * @return The magazine's ISSN number.
     */
    String getIssn();

    /**
     * Gets the language of the magazine.
     *
     * @return The language in which the magazine is written.
     */
    String getLanguage();

    /**
     * Gets the publication year of the magazine.
     *
     * @return The magazine's publication year as a {@link LocalDate}.
     */
    LocalDate getPublishDate();

    /**
     * Gets the title of the magazine.
     *
     * @return The magazine's title.
     */
    String getTitle();

}
