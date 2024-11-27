package es.org.cxn.backapp.service.dto;

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
