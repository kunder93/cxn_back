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
 * <p>
 * All fields are validated with the following constraints:
 * </p>
 * <ul>
 * <li>{@code name}: must not be {@code null}, blank, and must have a maximum
 * length defined by {@link ValidationConstants#MAX_TEAM_NAME_LENGTH}.</li>
 * <li>{@code description}: must not be {@code null}, blank, and must have a
 * maximum length defined by
 * {@link ValidationConstants#MAX_TEAM_DESCRIPTION_LENGTH}.</li>
 * <li>{@code category}: must not be {@code null}, blank, and must have a
 * maximum length defined by
 * {@link ValidationConstants#MAX_TEAM_CATEGORY_LENGTH}.</li>
 * </ul>
 *
 * @param name        the name of the team.
 * @param description the description of the team.
 * @param category    the category the team belongs to.
 */
public record CreateTeamRequest(@NotNull
@NotBlank
@Size(max = ValidationConstants.MAX_TEAM_NAME_LENGTH) String name,
        @NotNull
        @NotBlank
        @Size(max = ValidationConstants.MAX_TEAM_DESCRIPTION_LENGTH) String description,
        @NotNull
        @NotBlank
        @Size(max = ValidationConstants.MAX_TEAM_CATEGORY_LENGTH) String category) {
}
