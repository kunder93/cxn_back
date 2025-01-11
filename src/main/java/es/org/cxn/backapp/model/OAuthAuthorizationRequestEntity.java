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
import java.time.LocalDateTime;

/**
 * Interface representing an OAuth Authorization Request.
 */
public interface OAuthAuthorizationRequestEntity extends Serializable {
    /**
     * Gets the code verifier for the authorization request.
     *
     * @return the code verifier
     */
    String getCodeVerifier();

    /**
     * Gets the creation timestamp of the authorization request.
     *
     * @return the creation timestamp
     */
    LocalDateTime getCreatedAt();

    /**
     * Gets the state of the authorization request.
     *
     * @return the state
     */
    String getState();

    /**
     * Gets the user's DNI associated with the authorization request.
     *
     * @return the user's DNI
     */
    String getUserDni();

    /**
     * Sets the code verifier for the authorization request.
     *
     * @param codeVerifier the code verifier
     */
    void setCodeVerifier(String codeVerifier);

    /**
     * Sets the creation timestamp of the authorization request.
     *
     * @param createdAt the creation timestamp
     */
    void setCreatedAt(LocalDateTime createdAt);

    /**
     * Sets the state of the authorization request.
     *
     * @param state the state
     */
    void setState(String state);

    /**
     * Sets the user's DNI associated with the authorization request.
     *
     * @param userDni the user's DNI
     */
    void setUserDni(String userDni);
}
