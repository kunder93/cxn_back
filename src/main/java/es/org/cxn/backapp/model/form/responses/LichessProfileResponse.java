package es.org.cxn.backapp.model.form.responses;

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
