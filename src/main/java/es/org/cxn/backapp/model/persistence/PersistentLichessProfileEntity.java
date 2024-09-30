package es.org.cxn.backapp.model.persistence;

import java.time.LocalDateTime;

import es.org.cxn.backapp.model.LichessProfileEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Persistent entity representing a Lichess profile in the database. Contains
 * various game statistics such as Blitz, Bullet, Classical, Rapid, and Puzzle.
 * Implements the {@link LichessProfileEntity} interface.
 */
@Entity
@Table(name = "lichess_profile")
public class PersistentLichessProfileEntity implements LichessProfileEntity {

    /**
     * The user's unique identifier (DNI), used as the primary key.
     */
    @Id
    @Column(name = "user_dni", nullable = false, length = 10)
    private String userDni;

    /**
     * The user's Lichess profile ID.
     */
    @Column(name = "id")
    private String id;

    /**
     * The username associated with the Lichess profile.
     */
    @Column(name = "username")
    private String username;

    // Blitz statistics

    /**
     * The number of blitz games played by the user.
     */
    @Column(name = "blitz_games")
    private Integer blitzGames;

    /**
     * The user's blitz rating.
     */
    @Column(name = "blitz_rating")
    private Integer blitzRating;

    /**
     * The blitz rating deviation (RD), which indicates the accuracy of the rating.
     */
    @Column(name = "blitz_rd")
    private Integer blitzRd;

    /**
     * The blitz progress, which indicates the user's rating progression.
     */
    @Column(name = "blitz_prog")
    private Integer blitzProg;

    /**
     * Whether the user's blitz rating is provisional.
     */
    @Column(name = "blitz_prov")
    private Boolean blitzProv;

    // Bullet statistics

    /**
     * The number of bullet games played by the user.
     */
    @Column(name = "bullet_games")
    private Integer bulletGames;

    /**
     * The user's bullet rating.
     */
    @Column(name = "bullet_rating")
    private Integer bulletRating;

    /**
     * The bullet rating deviation (RD), which indicates the accuracy of the rating.
     */
    @Column(name = "bullet_rd")
    private Integer bulletRd;

    /**
     * The bullet progress, which indicates the user's rating progression.
     */
    @Column(name = "bullet_prog")
    private Integer bulletProg;

    /**
     * Whether the user's bullet rating is provisional.
     */
    @Column(name = "bullet_prov")
    private Boolean bulletProv;

    // Classical statistics

    /**
     * The number of classical games played by the user.
     */
    @Column(name = "classical_games")
    private Integer classicalGames;

    /**
     * The user's classical rating.
     */
    @Column(name = "classical_rating")
    private Integer classicalRating;

    /**
     * The classical rating deviation (RD), which indicates the accuracy of the
     * rating.
     */
    @Column(name = "classical_rd")
    private Integer classicalRd;

    /**
     * The classical progress, which indicates the user's rating progression.
     */
    @Column(name = "classical_prog")
    private Integer classicalProg;

    /**
     * Whether the user's classical rating is provisional.
     */
    @Column(name = "classical_prov")
    private Boolean classicalProv;

    // Rapid statistics

    /**
     * The number of rapid games played by the user.
     */
    @Column(name = "rapid_games")
    private Integer rapidGames;

    /**
     * The user's rapid rating.
     */
    @Column(name = "rapid_rating")
    private Integer rapidRating;

    /**
     * The rapid rating deviation (RD), which indicates the accuracy of the rating.
     */
    @Column(name = "rapid_rd")
    private Integer rapidRd;

    /**
     * The rapid progress, which indicates the user's rating progression.
     */
    @Column(name = "rapid_prog")
    private Integer rapidProg;

    /**
     * Whether the user's rapid rating is provisional.
     */
    @Column(name = "rapid_prov")
    private Boolean rapidProv;

    // Puzzle statistics

    /**
     * The number of puzzle games played by the user.
     */
    @Column(name = "puzzle_games")
    private Integer puzzleGames;

    /**
     * The user's puzzle rating.
     */
    @Column(name = "puzzle_rating")
    private Integer puzzleRating;

    /**
     * The puzzle rating deviation (RD), which indicates the accuracy of the rating.
     */
    @Column(name = "puzzle_rd")
    private Integer puzzleRd;

    /**
     * The puzzle progress, which indicates the user's rating progression in solving
     * puzzles.
     */
    @Column(name = "puzzle_prog")
    private Integer puzzleProg;

    /**
     * Whether the user's puzzle rating is provisional.
     */
    @Column(name = "puzzle_prov")
    private Boolean puzzleProv;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Blitz games getters.
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getBlitzGames() {
        return blitzGames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getBlitzProg() {
        return blitzProg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBlitzProv() {
        return blitzProv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getBlitzRating() {
        return blitzRating;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getBlitzRd() {
        return blitzRd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getBulletGames() {
        return bulletGames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getBulletProg() {
        return bulletProg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBulletProv() {
        return bulletProv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getBulletRating() {
        return bulletRating;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getBulletRd() {
        return bulletRd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getClassicalGames() {
        return classicalGames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getClassicalProg() {
        return classicalProg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getClassicalProv() {
        return classicalProv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getClassicalRating() {
        return classicalRating;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getClassicalRd() {
        return classicalRd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPuzzleGames() {
        return puzzleGames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPuzzleProg() {
        return puzzleProg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getPuzzleProv() {
        return puzzleProv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPuzzleRating() {
        return puzzleRating;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPuzzleRd() {
        return puzzleRd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getRapidGames() {
        return rapidGames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getRapidProg() {
        return rapidProg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getRapidProv() {
        return rapidProv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getRapidRating() {
        return rapidRating;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getRapidRd() {
        return rapidRd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserDni() {
        return userDni;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlitzGames(final Integer value) {
        blitzGames = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlitzProg(final Integer value) {
        blitzProg = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlitzProv(final Boolean value) {
        blitzProv = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlitzRating(final Integer value) {
        blitzRating = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlitzRd(final Integer value) {
        blitzRd = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBulletGames(final Integer value) {
        bulletGames = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBulletProg(final Integer value) {
        bulletProg = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBulletProv(final Boolean value) {
        bulletProv = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBulletRating(final Integer value) {
        bulletRating = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBulletRd(final Integer value) {
        bulletRd = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClassicalGames(final Integer value) {
        classicalGames = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClassicalProg(final Integer value) {
        classicalProg = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClassicalProv(final Boolean value) {
        classicalProv = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClassicalRating(final Integer value) {
        classicalRating = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClassicalRd(final Integer value) {
        classicalRd = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(final String value) {
        id = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPuzzleGames(final Integer value) {
        puzzleGames = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPuzzleProg(final Integer value) {
        puzzleProg = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPuzzleProv(final Boolean value) {
        puzzleProv = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPuzzleRating(final Integer value) {
        puzzleRating = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPuzzleRd(final Integer value) {
        puzzleRd = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRapidGames(final Integer value) {
        rapidGames = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRapidProg(final Integer value) {
        rapidProg = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRapidProv(final Boolean value) {
        rapidProv = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRapidRating(final Integer value) {
        rapidRating = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRapidRd(final Integer value) {
        rapidRd = value;
    }

    /**
     * {inheritDoc}
     */
    @Override
    public void setUpdatedAt(LocalDateTime value) {
        updatedAt = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserDni(String value) {
        userDni = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUsername(String value) {
        username = value;
    }

}
