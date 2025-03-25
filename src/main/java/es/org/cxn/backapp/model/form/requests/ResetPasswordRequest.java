package es.org.cxn.backapp.model.form.requests;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for handling password reset requests.
 *
 * <p>
 * This record encapsulates the necessary information for a user to reset their
 * password using a one-time token.
 * </p>
 *
 * <p>
 * Validations are applied to ensure data integrity:
 * </p>
 * <ul>
 * <li>The {@code token} must not be null, blank, and must not exceed 255
 * characters.</li>
 * <li>The {@code newPassword} must meet the constraints defined in
 * {@link ValidationConstants}.</li>
 * </ul>
 *
 * @param token       The one-time token used for authentication. Must not be
 *                    null, blank, and must not exceed 255 characters.
 * @param newPassword The new password to set for the user. Must comply with
 *                    {@link ValidationConstants} constraints.
 */
public record ResetPasswordRequest(@NotNull(message = "Token must not be null")
@NotBlank(message = "Token must not be blank")
@Size(max = 255, message = "Token length must not exceed 255 characters") String token,
        @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE)
        @Size(min = ValidationConstants.PASSWORD_MIN_LENGTH, max = ValidationConstants.PASSWORD_MAX_LENGTH,
                message = ValidationConstants.PASSWORD_SIZE_MESSAGE) String newPassword) {

}
