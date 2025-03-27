
package es.org.cxn.backapp.model.form.responses.user.auth;

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
 * Represents the form used for responding to a user authentication request.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller, encapsulating the authentication token (JWT) issued upon
 * successful user authentication.
 * </p>
 *
 * <p>
 * The {@code AuthenticationResponse} record is immutable, ensuring that the
 * JSON Web Token (JWT) remains consistent and unaltered throughout its
 * lifecycle once it is created.
 * </p>
 *
 * @param jwt The JSON Web Token (JWT) issued upon successful authentication.
 * This token is used to authorize subsequent requests made by the
 * authenticated user.
 *
 * @author Santiago Paz Perez
 */
public record AuthenticationResponse(String jwt) {
}
