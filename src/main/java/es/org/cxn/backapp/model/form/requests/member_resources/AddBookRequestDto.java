
package es.org.cxn.backapp.model.form.requests.member_resources;

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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddBookRequestDto(@NotNull(message = ValidationValues.ISBN_NOT_NULL) Long isbn,

        @NotNull(message = ValidationValues.TITLE_NOT_NULL)
        @Size(min = ValidationValues.MIN_TITLE_LENGTH, max = ValidationValues.MAX_TITLE_LENGTH,
                message = ValidationValues.TITLE_SIZE) String title,

        String description,

        @Size(min = ValidationValues.MIN_GENDER_LENGTH, max = ValidationValues.MAX_GENDER_LENGTH,
                message = ValidationValues.GENDER_SIZE) String genre,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") LocalDate publishDate,

        @Size(min = ValidationValues.MIN_LANGUAGE_LENGTH, max = ValidationValues.MAX_LANGUAGE_LENGTH,
                message = ValidationValues.LANGUAGE_SIZE) String language,

        @NotNull(message = ValidationValues.AUTHORS_LIST_NOT_NULL)
        @Size(min = ValidationValues.MIN_AUTHORS_LIST_SIZE,
                message = ValidationValues.AUTHORS_LIST_SIZE) List<AuthorRequest> authors) {

    public AddBookRequestDto {
        // Create a defensive copy of the authorsList to avoid storing external mutable
        // references
        authors = List.copyOf(authors);
    }

    @Override
    public List<AuthorRequest> authors() {
        // Return an unmodifiable view of the list
        return Collections.unmodifiableList(authors);
    }
}
