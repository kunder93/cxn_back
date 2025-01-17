
package es.org.cxn.backapp.model.form.requests;

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

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents the user form used by the controller as a request to change the
 * user's password.
 * <p>
 * This is a DTO (Data Transfer Object) used for communication between the view
 * and the controller. The record is immutable and provides built-in
 * implementations for methods such as {@code equals()}, {@code hashCode()},
 * and {@code toString()}.
 * <p>
 * The record includes fields for the user email, current password, and new
 * password.
 *
 * @param email          The user's email address.
 * @param currentPassword The user's current password.
 * @param newPassword     The user's new password.
 *
 * @author Santiago Paz.
 */
public record UserChangePasswordRequest(
      @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
      @Email(message = ValidationConstants.EMAIL_INVALID_MESSAGE)
      String email,

      @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE) @Size(
            min = ValidationConstants.PASSWORD_MIN_LENGTH,
            max = ValidationConstants.PASSWORD_MAX_LENGTH,
            message = ValidationConstants.PASSWORD_SIZE_MESSAGE
      )
      String currentPassword,

      @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE) @Size(
            min = ValidationConstants.PASSWORD_MIN_LENGTH,
            max = ValidationConstants.PASSWORD_MAX_LENGTH,
            message = ValidationConstants.PASSWORD_SIZE_MESSAGE
      )
      String newPassword
) {
}
