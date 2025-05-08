package es.org.cxn.backapp.model.form.requests.team;

import es.org.cxn.backapp.model.form.requests.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

/**
 * Represents a request to create a new team. This record contains the necessary
 * information to create a team, including its name, description, and category.
 *
 * @param name        the name of the team (must not be blank, not null, and
 *                    have a maximum length of 100 characters)
 * @param description a description of the team (must not be blank, not null,
 *                    and have a maximum length of 255 characters)
 * @param category    the category the team belongs to (must not be blank, not
 *                    null, and have a maximum length of 50 characters)
 */
public record CreateTeamRequest(@NotNull
@NotBlank
@Size(max = ValidationConstants.TEAM_NAME_MAX_LENGTH) String name,
        @NotNull
        @NotBlank
        @Size(max = ValidationConstants.TEAM_DESCRIPTION_MAX_LENGTH) String description,
        @NotNull
        @NotBlank
        @Size(max = ValidationConstants.TEAM_CATEGORY_MAX_LENGTH) String category) {
}
