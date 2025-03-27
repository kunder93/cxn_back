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

import es.org.cxn.backapp.service.dto.UserTeamInfoDto;

/**
 * Record representing a team participant response.
 *
 * @param dni           The unique identification number of the participant.
 * @param email         The email address of the participant.
 * @param name          The first name of the participant.
 * @param firstSurname  The first surname of the participant.
 * @param secondSurname The second surname of the participant.
 * @param birthDate     The birth date of the participant in string format.
 * @param gender        The gender of the participant.
 */
public record TeamParticipantResponse(String dni, String email, String name, String firstSurname, String secondSurname,
        String birthDate, String gender) {
    /**
     * Constructs a {@code TeamParticipantResponse} from a {@code UserEntity}.
     *
     * @param user The user entity containing participant details.
     */
    public TeamParticipantResponse(UserTeamInfoDto user) {
        this(user.dni(), user.email(), user.name(), user.firstSurname(), user.secondSurname(), user.birthDate(),
                user.gender());
    }
}
