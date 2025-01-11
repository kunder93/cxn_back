
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

import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity.TournamentCategory;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigInteger;
import java.time.LocalDate;

import org.springframework.validation.annotation.Validated;

/**
 * Data Transfer Object (DTO) for adding a tournament participant.
 * <p>
 * This class represents the form used by the controller to receive data for
 * adding a tournament participant.
 * It includes Java validation annotations to ensure the controller receives
 * valid and complete data.
 * </p>
 * <p>
 * The DTO includes fields for various tournament participant attributes such
 * as FIDE_ID, name, club, birthDate,byes and category.
 * All fields are required to ensure data integrity and completeness.
 * </p>
 *
 * @param fideId The FIDE ID of the tournament participant, which must be a
 *               positive number not greater than 9999999.
 * @param name The name of the tournament participant, with a maximum length
 *             of 50 characters.
 * @param club The name of the participant's club, with a maximum length of
 *             30 characters.
 * @param birthDate The birth date of the participant, which cannot be a
 *                  future date.
 * @param byes The byes requested by participant.
 * @param category The tournament category the participant is competing in.
 */
@Validated
public record AddTournamentParticipantRequest(

      @NotNull(message = "FIDE ID is required") @Max(
            value = AddTournamentParticipantRequest.MAX_FIDE_ID,
            message = "FIDE ID cannot be greater than 9999999"
      ) @Positive(message = "FIDE ID must be positive.")
      BigInteger fideId,

      @NotBlank(message = "Name is required") @Size(
            max = AddTournamentParticipantRequest.MAX_NAME_LENGTH,
            message = "Name cannot be longer than 50 characters"
      )
      String name,

      @NotBlank(message = "Club is required") @Size(
            max = AddTournamentParticipantRequest.MAX_CLUB_LENGTH,
            message = "Club name cannot be longer than 30 characters"
      )
      String club,

      @NotNull(message = "Birth date is required")
      @PastOrPresent(message = "Birth date cannot be in the future")
      LocalDate birthDate,

      @NotNull(message = "Category is required")
      TournamentCategory category,

      String byes
) {
  /**
   * Max fide id number field.
   */
  public static final int MAX_FIDE_ID = 9_999_999;
  /**
   * Max char name length.
   */
  public static final int MAX_NAME_LENGTH = 50;
  /**
   * Max club name length.
   */
  public static final int MAX_CLUB_LENGTH = 30;
}
