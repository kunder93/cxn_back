
package es.org.cxn.backapp.service.exceptions;

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

import java.io.Serial;

/**
 * Exception thrown by magazine service.
 *
 * @author Santiago Paz.
 *
 */
public final class MagazineServiceException extends Exception {

    /**
     * Serial UID.
     */
    @Serial
    private static final long serialVersionUID = 1991698283628223252L;

    /**
     * Main constructor.
     *
     * @param value exception message.
     */
    public MagazineServiceException(final String value) {
        super(value);
    }

    /**
     * Main constructor.
     *
     * @param value     exception message.
     * @param exception The high order exception.
     */
    public MagazineServiceException(final String value, final Throwable exception) {
        super(value, exception);
    }
}
