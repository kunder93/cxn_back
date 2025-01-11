
package es.org.cxn.backapp;

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
 * A utility class that contains the URL endpoints used in the application.
 * <p>
 * This class provides a set of constants for commonly used URL paths in the
 * application, such as authentication and resource access.
 * </p>
 * <p>
 * The class is marked as {@code final} to prevent inheritance, and the
 * constructor is private to enforce non-instantiability.
 * </p>
 */
public final class AppURL {

    /**
     * The URL for the user sign-up endpoint.
     */
    public static final String SIGN_UP_URL = "/api/auth/signup";

    /**
     * The URL for the user sign-in endpoint.
     * <p>
     * Note: there seems to be a typo in the path (should be
     * {@code "/api/auth/signin"})
     * </p>
     */
    public static final String SIGN_IN_URL = "/api/auth/signinn";

    /**
     * The URL for accessing chess questions.
     */
    public static final String CHESS_QUESTION_URL = "/api/chessQuestion";

    /**
     * The URL for accessing the participants.
     */
    public static final String PARTICIPANTS_URL = "/api/participants";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AppURL() {
        // Private constructor to prevent instantiation.
    }
}
