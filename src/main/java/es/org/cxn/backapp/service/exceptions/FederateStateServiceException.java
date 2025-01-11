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
 * Exception thrown by federate state service.
 *
 * @author Santiago Paz.
 *
 */
public final class FederateStateServiceException extends Exception {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 4511128362621185622L;

    /**
     * Main constructor with an exception message.
     *
     * @param value exception message.
     */
    public FederateStateServiceException(final String value) {
        super(value);
    }

    /**
     * Constructor with an exception message and a cause.
     *
     * @param value exception message.
     * @param cause the original exception that caused this exception.
     */
    public FederateStateServiceException(final String value, final Throwable cause) {
        super(value, cause);
    }

}
