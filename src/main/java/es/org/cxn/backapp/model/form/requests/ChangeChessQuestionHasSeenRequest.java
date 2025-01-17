
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

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Represents the form used by the controller as a request to update the "seen"
 * status of a chess question.
 * <p>
 * This Data Transfer Object (DTO) facilitates communication between the view
 * and the controller, capturing the necessary information required to change
 * the "seen" status of a specific chess question.
 * </p>
 *
 * <p>
 * The {@code ChangeChessQuestionHasSeenRequest} includes an identifier
 * field for the chess question, which is used to update its "seen" status in
 * the system.
 * </p>
 *
 * @param id The unique identifier of the chess question whose "seen" status
 * is to be updated. This field is mandatory, must be a positive integer, and
 * should not exceed 1000.
 *
 * <p>
 * Includes Java validation annotations to ensure that the necessary data
 * is provided and adheres to the specified constraints.
 * </p>
 *
 * @author Santiago Paz
 */
public record ChangeChessQuestionHasSeenRequest(
      @NotNull(message = ValidationConstants.ID_NOT_NULL_MESSAGE)
      @Positive(message = ValidationConstants.ID_POSITIVE_MESSAGE)
      @Max(
            value = ValidationConstants.MAX_ID,
            message = ValidationConstants.ID_MAX_MESSAGE
      )
      int id
) {
}
