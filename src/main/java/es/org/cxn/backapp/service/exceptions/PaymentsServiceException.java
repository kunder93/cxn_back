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

/**
 * PaymentServiceException is a custom exception for handling errors within the
 * Payments Service. This exception extends {@link Exception} and provides a
 * mechanism to capture error messages specific to activity-related operations.
 *
 * <p>
 * This class is marked as {@code final} to prevent extension and ensure
 * consistent usage for service-specific exceptions.
 * </p>
 *
 * @see Exception
 */
public final class PaymentsServiceException extends Exception {

    /** Serial version UID for serialization compatibility. */
    private static final long serialVersionUID = 1385589232224464048L;

    /**
     * Constructs a new PaymentServiceException with a specified error message.
     *
     * @param value the detailed message for the exception
     */
    public PaymentsServiceException(final String value) {
        super(value);
    }

    /**
     * Constructs a new PaymentServiceException with a specified error message and a
     * cause.
     *
     * @param message the detailed message for the exception
     * @param cause   the underlying cause of the exception
     */
    public PaymentsServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
