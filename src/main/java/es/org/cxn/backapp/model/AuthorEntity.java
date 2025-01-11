
package es.org.cxn.backapp.model;

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

import java.io.Serializable;

/**
 * Interface representing an Author entity, typically associated with books.
 * This interface provides methods to retrieve details about the author,
 * including their first name, last name, and nationality.
 *
 * @author Santiago Paz.
 */
public interface AuthorEntity extends Serializable {

    /**
     * Gets the first name of the author.
     *
     * @return The author's first name.
     */
    String getFirstName();

    /**
     * The author entity identifier.
     *
     * @return the author identifier.
     */
    Long getIdentifier();

    /**
     * Gets the last name of the author.
     *
     * @return The author's last name.
     */
    String getLastName();

    /**
     * Gets the nationality of the author.
     *
     * @return The author's nationality.
     */
    String getNationality();

}
