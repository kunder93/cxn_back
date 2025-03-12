package es.org.cxn.backapp.model.form.responses.team;

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

import es.org.cxn.backapp.service.dto.TeamInfoDto;

/**
 * Represents a response object for team information, encapsulating team details
 * and members. This record provides an immutable representation of a team's
 * data.
 *
 * @param name        The name of the team.
 * @param description The description of the team.
 * @param category    The category of the team.
 * @param members     The list of team members.
 */
public record TeamInfoResponse(String name, String description, String category,
        List<TeamParticipantResponse> members) {
    /**
     * Constructs a {@code TeamInfoResponse} from a {@code TeamEntity}.
     *
     * @param team The team entity containing team details and members.
     */
    public TeamInfoResponse(TeamInfoDto team) {
        this(team.name(), team.description(), team.category(),
                List.copyOf(team.users().stream().map(TeamParticipantResponse::new).toList()));
    }
}
