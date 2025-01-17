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

import java.time.LocalDateTime;

/**
 * Represents the response for a Lichess profile, containing the user's general
 * information and game statistics.
 *
 * @param userName        The user's name.
 * @param lichessUserName The user's nick on Lichess.
 * @param lastUpdate      The Lichess profile last update.
 * @param blitzGame       The game information for Blitz games.
 * @param bulletGame      The game information for Bullet games.
 * @param rapidGame       The game information for Rapid games.
 * @param classicalGame   The game information for Classical games.
 * @param puzzleGame      The game information for Puzzle games.
 */
public record LichessProfileResponse(String userName, String lichessUserName, LocalDateTime lastUpdate, Game blitzGame,
        Game bulletGame, Game rapidGame, Game classicalGame, Game puzzleGame) {

    /**
     * Represents the game statistics for a specific game type.
     *
     * @param elo           The Elo rating of the user for this game type.
     * @param amountOfGames The total number of games played.
     * @param isProvisional Indicates whether the rating is provisional.
     */
    public record Game(int elo, int amountOfGames, Boolean isProvisional) {
    }
}
