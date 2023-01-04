/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package es.org.cxn.backapp.model.form;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the form used for response authenticating user.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller, and mapping all the values from the form. Each of field in the
 * DTO matches a field in the form.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz Perez
 */
public final class AuthenticationResponse implements Serializable {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 1333322989450853491L;

    /**
     * Jwt field.
     */
    private String jwt;

    /**
     * Constructs a DTO for user info response.
     */
    public AuthenticationResponse() {
        super();
    }

    /**
     * Constructs a DTO for user info response with fields values.
     *
     * @param value the jwt field.
     */
    public AuthenticationResponse(final String value) {
        super();
        this.jwt = value;
    }

    /**
     * Returns the value of the jwt token.
     *
     * @return the value of the jwt field.
     */
    public String getJwt() {
        return jwt;
    }

    /**
     * Sets the value of the Jwt field.
     *
     * @param value the new value for the Jwt token.
     */
    public void setJwt(final String value) {
        this.jwt = checkNotNull(value, "Received a null pointer as name");
    }

    @Override
    public int hashCode() {
        return Objects.hash(jwt);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AuthenticationResponse other = (AuthenticationResponse) obj;
        return Objects.equals(jwt, other.jwt);
    }

    @Override
    public String toString() {
        return "UserInfoResponseForm [jwt=" + jwt + "]";
    }

}
