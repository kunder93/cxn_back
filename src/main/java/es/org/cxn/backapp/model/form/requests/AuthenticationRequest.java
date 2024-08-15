/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.model.form.requests;

import es.org.cxn.backapp.model.form.Constants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used login user.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller, and mapping all the values from the form. Each of field in the
 * DTO matches a field in the form.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz Perez.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public final class AuthenticationRequest implements Serializable {

  /**
   * Serialization ID.
   */
  private static final long serialVersionUID = 1328776989453353491L;

  /**
   * Email field.
   * <p>
   * This is a required field and can't be empty.
   */
  @NotBlank(message = Constants.EMAIL_NOT_VALID)
  @Size(
        max = Constants.EMAIL_MAX_SIZE,
        message = Constants.MAX_SIZE_EMAIL_MESSAGE
  )
  @Email(message = Constants.EMAIL_NOT_VALID)
  private String email;

  /**
   * Password field.
   * <p>
   * This is a required field and can't be empty.
   */
  @NotNull(message = Constants.NOT_NULL_PASSWORD)
  @Length(
        min = Constants.MIN_PASSWORD_LENGHT,
        max = Constants.MAX_PASSWORD_LENGHT,
        message = Constants.LENGTH_PASSWORD_MESSAGE
  )
  private String password;

}
