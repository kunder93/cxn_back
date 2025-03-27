package es.org.cxn.backapp.service.dto;

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

import java.util.List;

import es.org.cxn.backapp.model.TeamEntity;

/**
 * Data Transfer Object (DTO) for representing team information.
 *
 * @param name        The name of the team.
 * @param description The description of the team.
 * @param category    The category of the team.
 * @param users       A list of users associated with the team.
 */
public record TeamInfoDto(String name, String description, String category, List<UserTeamInfoDto> users) {

    /**
     * Constructs a new {@code TeamInfoDto} from a given {@code TeamEntity}.
     *
     * @param team The {@link TeamEntity} containing the team details.
     * @throws NullPointerException if {@code team} is {@code null}.
     */
    public TeamInfoDto(final TeamEntity team) {
        this(team.getName(), team.getDescription(), team.getCategory(),
                team.getUsersAssigned().stream()
                        .map(u -> new UserTeamInfoDto(u.getDni(), u.getEmail(), u.getProfile().getName(),
                                u.getProfile().getFirstSurname(), u.getProfile().getSecondSurname(),
                                u.getProfile().getGender(), u.getProfile().getBirthDate().toString(),
                                u.getTeamAssigned() == null ? null : u.getTeamAssigned().getName(),
                                u.getTeamPreferred() == null ? null : u.getTeamPreferred().getName()))
                        .toList());
    }
}
