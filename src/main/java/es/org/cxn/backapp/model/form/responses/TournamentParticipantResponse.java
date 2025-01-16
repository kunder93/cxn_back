
package es.org.cxn.backapp.model.form.responses;

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

import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity.TournamentCategory;

import java.math.BigInteger;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a tournament participant response.
 * <p>
 * This class is used to transfer data from the service layer to the
 * presentation layer, encapsulating the details of a tournament participant.
 * It includes the FIDE ID, name, club, birth date,byes, and tournament category of
 * the participant.
 * </p>
 *
 * <p>
 * The DTO is immutable and is used primarily to return participant information
 * in response to client requests.
 * </p>
 *
 * @param fideId The FIDE ID of the tournament participant.
 * @param name The name of the tournament participant.
 * @param club The club the participant is associated with.
 * @param birthDate The birth date of the participant.
 * @param category The tournament category the participant belongs to.
 * @param byes The byes requested by participant.
 */
public record TournamentParticipantResponse(

      BigInteger fideId,

      String name,

      String club,

      LocalDate birthDate,

      TournamentCategory category,

      String byes
) {

}
