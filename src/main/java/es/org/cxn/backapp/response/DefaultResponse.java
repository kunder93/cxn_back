
package es.org.cxn.backapp.response;

import java.util.Objects;

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
 * Default Response.
 *
 * @author Santiago Paz.
 *
 * @param <T> the type of default response.
 */
public class DefaultResponse<T> implements Response<T> {

    /**
     * Content type.
     */
    private T content;

    /**
     * Response status.
     */
    private ResponseStatus status = ResponseStatus.SUCCESS;

    /**
     * Constructor.
     */
    public DefaultResponse() {
        super();
    }

    /**
     * Constructor with param.
     *
     * @param cont the response content.
     */
    public DefaultResponse(final T cont) {
        super();

        content = Objects.requireNonNull(cont, "Missing content");
    }

    /**
     * Constructor response with content and response status.
     *
     * @param cont the response content.
     * @param stat the response status.
     */
    public DefaultResponse(final T cont, final ResponseStatus stat) {
        super();

        content = Objects.requireNonNull(cont, "Missing content");
        status = Objects.requireNonNull(stat, "Missing status");
    }

    /**
     * Content getter.
     */
    @Override
    public T getContent() {
        return content;
    }

    /**
     * Response status getter.
     */
    @Override
    public ResponseStatus getStatus() {
        return status;
    }

    /**
     * Set response content.
     *
     * @param value the response content.
     */
    public void setContent(final T value) {
        content = value;
    }

    /**
     * Set the response status.
     *
     * @param value the response status.
     */
    public void setStatus(final ResponseStatus value) {
        status = value;
    }

    /**
     * To string method.
     */
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("status=").append(status).append(", content=").append(content);
        return sb.toString();
    }

}
