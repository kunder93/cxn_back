
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
 * Represents the form used by the controller as a request to create a chess
 * question.
 * <p>
 * This is a Data Transfer Object (DTO) meant to facilitate communication
 * between the view layer and the controller. It includes Java validation
 * annotations to ensure that all required data is provided and adheres to the
 * necessary constraints.
 * </p>
 *
 * <p>
 * This DTO is immutable and includes the following fields:
 * </p>
 *
 * <ul>
 *   <li>{@code email}: The email of the user submitting the question.
 *   This field must not be blank and must adhere to email format
 *   validation.</li>
 *   <li>{@code category}: The category of the chess question.
 *   This field must not be blank and must not exceed 30 characters.</li>
 *   <li>{@code topic}: The topic related to the chess question.
 *   This field must not be blank and must not exceed 50 characters.</li>
 *   <li>{@code message}: The content of the chess question.
 *   This field must not be blank.</li>
 * </ul>
 *
 * @param email   The email address of the user submitting the question.
 * Must not be blank and must be a valid email format.
 * @param category The category to which the chess question belongs.
 * Must not be blank and must not exceed 30 characters.
 * @param topic    The topic of the chess question. Must not be blank and
 * must not exceed 50 characters.
 * @param message  The content of the chess question. Must not be blank.
 *
 * @author Santiago Paz
 */
public record CreateChessQuestionRequest(

      @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
      @Email(message = ValidationConstants.EMAIL_INVALID_MESSAGE)
      @Size(
            max = ValidationConstants.EMAIL_MAX_SIZE,
            message = ValidationConstants.EMAIL_SIZE_MESSAGE
      )
      String email,

      @NotBlank(message = ValidationConstants.CATEGORY_NOT_BLANK_MESSAGE) @Size(
            max = ValidationConstants.CATEGORY_MAX_SIZE,
            message = ValidationConstants.CATEGORY_SIZE_MESSAGE
      )
      String category,

      @NotBlank(message = ValidationConstants.TOPIC_NOT_BLANK_MESSAGE) @Size(
            max = ValidationConstants.TOPIC_MAX_SIZE,
            message = ValidationConstants.TOPIC_SIZE_MESSAGE
      )
      String topic,

      @NotBlank(message = ValidationConstants.MESSAGE_NOT_BLANK_MESSAGE) @Size(
            max = ValidationConstants.MESSAGE_MAX_LENGTH,
            message = ValidationConstants.MESSAGE_MAX_LENGTH_MESSAGE
      )
      String message
) {

}
