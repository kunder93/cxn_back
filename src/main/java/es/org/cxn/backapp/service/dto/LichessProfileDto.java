package es.org.cxn.backapp.service.dto;

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

import java.time.LocalDateTime;

/**
 * DTO that represents the Lichess profile of a user. It contains various
 * statistics such as blitz, bullet, classical, rapid, puzzle, as well as the
 * user's FIDE rating and associated email.
 *
 * USED FOR GIVE DATA FROM LICHESS PROFILE.
 *
 * @param completeUserName The complete user name with name, first surname and
 *                         second surname.
 * @param identifier       The unique identifier of the Lichess profile.
 * @param username         The username of the Lichess profile.
 * @param updatedAt        The updatedAt local time date for lichess user
 *                         profile.
 * @param blitz            The statistics for blitz games.
 * @param bullet           The statistics for bullet games.
 * @param classical        The statistics for classical games.
 * @param rapid            The statistics for rapid games.
 * @param puzzle           The statistics for puzzles.
 *
 */
public record LichessProfileDto(String completeUserName, String identifier, String username, LocalDateTime updatedAt,
        GameStatistics blitz, GameStatistics bullet, GameStatistics classical, GameStatistics rapid,
        GameStatistics puzzle) {

    /**
     * Represents game statistics for a particular category (e.g., blitz, bullet).
     * Contains details such as the number of games played, rating, rating deviation
     * (rd), progress, and whether the rating is provisional.
     *
     * @param games            The number of games played.
     * @param rating           The rating of the player in this category.
     * @param ratingDerivation The rating deviation (rd), representing uncertainty
     *                         in the rating.
     * @param prog             The progress of the rating.
     * @param prov             Whether the rating is provisional (true if
     *                         provisional, false otherwise).
     */
    public record GameStatistics(Integer games, Integer rating, Integer ratingDerivation, Integer prog, Boolean prov) {
    }
}
