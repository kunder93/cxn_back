package es.org.cxn.backapp.service.exceptions;

import java.io.Serial;

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

/**
 * Exception thrown by the Lichess authentication service.
 * <p>
 * This exception is used to indicate errors that occur during the
 * authentication process with Lichess. It extends {@link Exception} and
 * provides a way to signal specific issues related to the Lichess
 * authentication service.
 * </p>
 *
 * @author Santiago Paz
 */
public final class LichessServiceException extends Exception {

    /**
     * Serial version UID for serialization.
     */
    @Serial
    private static final long serialVersionUID = -2343250182475869712L;

    /**
     * Constructs a new LichessAuthServiceException with the specified detail
     * message.
     *
     * @param value the detail message (which is saved for later retrieval by the
     *              {@link Throwable#getMessage()} method).
     */
    public LichessServiceException(final String value) {
        super(value);
    }

    /**
     * Constructs a new {@code LichessServiceException} with the specified detail
     * message and cause.
     * <p>
     * This constructor is used to create an exception that includes both a specific
     * message and the underlying cause, which allows the original exception's stack
     * trace to be preserved.
     * </p>
     *
     * @param value the detail message (which is saved for later retrieval by the
     *              {@link Throwable#getMessage()} method).
     * @param cause the cause of the exception (which is saved for later retrieval
     *              by the {@link Throwable#getCause()} method). (A {@code null}
     *              value is permitted, and indicates that the cause is nonexistent
     *              or unknown.)
     */
    public LichessServiceException(final String value, final Throwable cause) {
        super(value, cause);
    }

}
