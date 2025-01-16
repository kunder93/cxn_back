package es.org.cxn.backapp.model;

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
 * Represents a Lichess profile entity with various game statistics for
 * different game modes. This interface provides getter methods for retrieving
 * Lichess profile data related to blitz and bullet games.
 */
public interface LichessProfileEntity {

    /**
     * Gets the number of blitz games played by the user.
     *
     * @return the number of blitz games played
     */
    Integer getBlitzGames();

    /**
     * Gets the blitz progress of the user, which indicates rating progression.
     *
     * @return the blitz progress
     */
    Integer getBlitzProg();

    /**
     * Gets the user's blitz rating.
     *
     * @return the blitz rating
     */
    Integer getBlitzRating();

    /**
     * Gets the rating deviation (RD) for blitz, which indicates the accuracy of the
     * rating.
     *
     * @return the blitz rating deviation
     */
    Integer getBlitzRd();

    /**
     * Gets the number of bullet games played by the user.
     *
     * @return the number of bullet games played
     */
    Integer getBulletGames();

    /**
     * Gets the bullet progress of the user, which indicates rating progression.
     *
     * @return the bullet progress
     */
    Integer getBulletProg();

    /**
     * Gets the user's bullet rating.
     *
     * @return the bullet rating
     */
    Integer getBulletRating();

    /**
     * Gets the rating deviation (RD) for bullet, which indicates the accuracy of
     * the rating.
     *
     * @return the bullet rating deviation
     */
    Integer getBulletRd();

    /**
     * Gets the number of classical games played by the user.
     *
     * @return the number of classical games played
     */
    Integer getClassicalGames();

    /**
     * Gets the classical progress of the user, which indicates rating progression.
     *
     * @return the classical progress
     */
    Integer getClassicalProg();

    /**
     * Gets the user's classical rating.
     *
     * @return the classical rating
     */
    Integer getClassicalRating();

    /**
     * Gets the rating deviation (RD) for classical, which indicates the accuracy of
     * the rating.
     *
     * @return the classical rating deviation
     */
    Integer getClassicalRd();

    /**
     * Gets the unique identifier for the Lichess profile.
     *
     * @return the profile's unique identifier (ID)
     */
    String getIdentifier();

    /**
     * Gets the number of puzzle games solved by the user.
     *
     * @return the number of puzzle games solved
     */
    Integer getPuzzleGames();

    /**
     * Gets the puzzle progress of the user, which indicates rating progression in
     * solving puzzles.
     *
     * @return the puzzle progress
     */
    Integer getPuzzleProg();

    /**
     * Gets the user's puzzle rating.
     *
     * @return the puzzle rating
     */
    Integer getPuzzleRating();

    /**
     * Gets the rating deviation (RD) for puzzles, which indicates the accuracy of
     * the puzzle rating.
     *
     * @return the puzzle rating deviation
     */
    Integer getPuzzleRd();

    /**
     * Gets the number of rapid games played by the user.
     *
     * @return the number of rapid games played
     */
    Integer getRapidGames();

    /**
     * Gets the rapid progress of the user, which indicates rating progression.
     *
     * @return the rapid progress
     */
    Integer getRapidProg();

    /**
     * Gets the user's rapid rating.
     *
     * @return the rapid rating
     */
    Integer getRapidRating();

    /**
     * Gets the rating deviation (RD) for rapid, which indicates the accuracy of the
     * rating.
     *
     * @return the rapid rating deviation
     */
    Integer getRapidRd();

    /**
     * Retrieves the date and time when the profile was last updated.
     *
     * @return the {@link LocalDateTime} representing the last update timestamp.
     */
    LocalDateTime getUpdatedAt();

    /**
     * Gets the user's unique identifier (DNI).
     *
     * @return the user's DNI
     */
    String getUserDni();

    /**
     * Gets the username associated with the Lichess profile.
     *
     * @return the username
     */
    String getUsername();

    /**
     * Checks if the user's blitz rating is provisional.
     *
     * @return true if the blitz rating is provisional, false otherwise
     */
    Boolean isBlitzProv();

    /**
     * Checks if the user's bullet rating is provisional.
     *
     * @return true if the bullet rating is provisional, false otherwise
     */
    Boolean isBulletProv();

    /**
     * Checks if the user's classical rating is provisional.
     *
     * @return true if the classical rating is provisional, false otherwise
     */
    Boolean isClassicalProv();

    /**
     * Checks if the user's puzzle rating is provisional.
     *
     * @return true if the puzzle rating is provisional, false otherwise
     */
    Boolean isPuzzleProv();

    /**
     * Checks if the user's rapid rating is provisional.
     *
     * @return true if the rapid rating is provisional, false otherwise
     */
    Boolean isRapidProv();

    /**
     * Sets the number of blitz games played by the user.
     *
     * @param value the number of blitz games played
     */
    void setBlitzGames(Integer value);

    /**
     * Sets the blitz progress of the user, which indicates rating progression.
     *
     * @param value the blitz progress
     */
    void setBlitzProg(Integer value);

    /**
     * Sets whether the user's blitz rating is provisional.
     *
     * @param value true if the blitz rating is provisional, false otherwise
     */
    void setBlitzProv(Boolean value);

    /**
     * Sets the user's blitz rating.
     *
     * @param value the blitz rating
     */
    void setBlitzRating(Integer value);

    /**
     * Sets the rating deviation (RD) for blitz, which indicates the accuracy of the
     * rating.
     *
     * @param value the blitz rating deviation
     */
    void setBlitzRd(Integer value);

    /**
     * Sets the number of bullet games played by the user.
     *
     * @param value the number of bullet games played
     */
    void setBulletGames(Integer value);

    /**
     * Sets the bullet progress of the user, which indicates rating progression.
     *
     * @param value the bullet progress
     */
    void setBulletProg(Integer value);

    /**
     * Sets whether the user's bullet rating is provisional.
     *
     * @param value true if the bullet rating is provisional, false otherwise
     */
    void setBulletProv(Boolean value);

    /**
     * Sets the user's bullet rating.
     *
     * @param value the bullet rating
     */
    void setBulletRating(Integer value);

    /**
     * Sets the rating deviation (RD) for bullet, which indicates the accuracy of
     * the rating.
     *
     * @param value the bullet rating deviation
     */
    void setBulletRd(Integer value);

    /**
     * Sets the number of classical games played by the user.
     *
     * @param value the number of classical games played
     */
    void setClassicalGames(Integer value);

    /**
     * Sets the classical progress of the user, which indicates rating progression.
     *
     * @param value the classical progress
     */
    void setClassicalProg(Integer value);

    /**
     * Sets whether the user's classical rating is provisional.
     *
     * @param value true if the classical rating is provisional, false otherwise
     */
    void setClassicalProv(Boolean value);

    /**
     * Sets the user's classical rating.
     *
     * @param value the classical rating
     */
    void setClassicalRating(Integer value);

    /**
     * Sets the rating deviation (RD) for classical, which indicates the accuracy of
     * the rating.
     *
     * @param value the classical rating deviation
     */
    void setClassicalRd(Integer value);

    /**
     * Sets the unique identifier for the Lichess profile.
     *
     * @param value the profile's unique identifier (ID)
     */
    void setIdentifier(String value);

    /**
     * Sets the number of puzzle games solved by the user.
     *
     * @param value the number of puzzle games solved
     */
    void setPuzzleGames(Integer value);

    /**
     * Sets the puzzle progress of the user, which indicates rating progression in
     * solving puzzles.
     *
     * @param value the puzzle progress
     */
    void setPuzzleProg(Integer value);

    /**
     * Sets whether the user's puzzle rating is provisional.
     *
     * @param value true if the puzzle rating is provisional, false otherwise
     */
    void setPuzzleProv(Boolean value);

    /**
     * Sets the user's puzzle rating.
     *
     * @param value the puzzle rating
     */
    void setPuzzleRating(Integer value);

    /**
     * Sets the rating deviation (RD) for puzzles, which indicates the accuracy of
     * the puzzle rating.
     *
     * @param value the puzzle rating deviation
     */
    void setPuzzleRd(Integer value);

    /**
     * Sets the number of rapid games played by the user.
     *
     * @param value the number of rapid games played
     */
    void setRapidGames(Integer value);

    /**
     * Sets the rapid progress of the user, which indicates rating progression.
     *
     * @param value the rapid progress
     */
    void setRapidProg(Integer value);

    /**
     * Sets whether the user's rapid rating is provisional.
     *
     * @param value true if the rapid rating is provisional, false otherwise
     */
    void setRapidProv(Boolean value);

    /**
     * Sets the user's rapid rating.
     *
     * @param value the rapid rating
     */
    void setRapidRating(Integer value);

    /**
     * Sets the rating deviation (RD) for rapid, which indicates the accuracy of the
     * rating.
     *
     * @param value the rapid rating deviation
     */
    void setRapidRd(Integer value);

    /**
     * Sets the date and time when the profile was last updated.
     *
     * @param value the {@link LocalDateTime} to set as the last update timestamp.
     */
    void setUpdatedAt(LocalDateTime value);

    /**
     * Sets the user's unique identifier (DNI).
     *
     * @param value the user's DNI
     */
    void setUserDni(String value);

    /**
     * Sets the username associated with the Lichess profile.
     *
     * @param value the username
     */
    void setUsername(String value);

}
