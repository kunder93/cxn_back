package es.org.cxn.backapp.model.persistence;

import es.org.cxn.backapp.model.LichessProfileEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lichess_profile")
public class PersistentLichessProfileEntity implements LichessProfileEntity {

    @Id
    @Column(name = "user_dni", nullable = false, length = 10)
    private String userDni;

    @Column(name = "id")
    private String id;

    @Column(name = "username")
    private String username;

    // Blitz statistics
    @Column(name = "blitz_games")
    private Integer blitzGames;

    @Column(name = "blitz_rating")
    private Integer blitzRating;

    @Column(name = "blitz_rd")
    private Integer blitzRd;

    @Column(name = "blitz_prog")
    private Integer blitzProg;

    @Column(name = "blitz_prov")
    private Boolean blitzProv;

    // Bullet statistics
    @Column(name = "bullet_games")
    private Integer bulletGames;

    @Column(name = "bullet_rating")
    private Integer bulletRating;

    @Column(name = "bullet_rd")
    private Integer bulletRd;

    @Column(name = "bullet_prog")
    private Integer bulletProg;

    @Column(name = "bullet_prov")
    private Boolean bulletProv;

    // Ultrabullet statistics
    @Column(name = "ultrabullet_games")
    private Integer ultrabulletGames;

    @Column(name = "ultrabullet_rating")
    private Integer ultrabulletRating;

    @Column(name = "ultrabullet_rd")
    private Integer ultrabulletRd;

    @Column(name = "ultrabullet_prog")
    private Integer ultrabulletProg;

    @Column(name = "ultrabullet_prov")
    private Boolean ultrabulletProv;

    // Classical statistics
    @Column(name = "classical_games")
    private Integer classicalGames;

    @Column(name = "classical_rating")
    private Integer classicalRating;

    @Column(name = "classical_rd")
    private Integer classicalRd;

    @Column(name = "classical_prog")
    private Integer classicalProg;

    @Column(name = "classical_prov")
    private Boolean classicalProv;

    // Rapid statistics
    @Column(name = "rapid_games")
    private Integer rapidGames;

    @Column(name = "rapid_rating")
    private Integer rapidRating;

    @Column(name = "rapid_rd")
    private Integer rapidRd;

    @Column(name = "rapid_prog")
    private Integer rapidProg;

    @Column(name = "rapid_prov")
    private Boolean rapidProv;

    // Puzzle statistics
    @Column(name = "puzzle_games")
    private Integer puzzleGames;

    @Column(name = "puzzle_rating")
    private Integer puzzleRating;

    @Column(name = "puzzle_rd")
    private Integer puzzleRd;

    @Column(name = "puzzle_prog")
    private Integer puzzleProg;

    @Column(name = "puzzle_prov")
    private Boolean puzzleProv;

    @Column(name = "fide_rating")
    private Integer fideRating;

    public Integer getBlitzGames() {
        return blitzGames;
    }

    public Integer getBlitzProg() {
        return blitzProg;
    }

    public Boolean getBlitzProv() {
        return blitzProv;
    }

    public Integer getBlitzRating() {
        return blitzRating;
    }

    public Integer getBlitzRd() {
        return blitzRd;
    }

    public Integer getBulletGames() {
        return bulletGames;
    }

    public Integer getBulletProg() {
        return bulletProg;
    }

    public Boolean getBulletProv() {
        return bulletProv;
    }

    public Integer getBulletRating() {
        return bulletRating;
    }

    public Integer getBulletRd() {
        return bulletRd;
    }

    public Integer getClassicalGames() {
        return classicalGames;
    }

    public Integer getClassicalProg() {
        return classicalProg;
    }

    public Boolean getClassicalProv() {
        return classicalProv;
    }

    public Integer getClassicalRating() {
        return classicalRating;
    }

    public Integer getClassicalRd() {
        return classicalRd;
    }

    public Integer getFideRating() {
        return fideRating;
    }

    public String getId() {
        return id;
    }

    public Integer getPuzzleGames() {
        return puzzleGames;
    }

    public Integer getPuzzleProg() {
        return puzzleProg;
    }

    public Boolean getPuzzleProv() {
        return puzzleProv;
    }

    public Integer getPuzzleRating() {
        return puzzleRating;
    }

    public Integer getPuzzleRd() {
        return puzzleRd;
    }

    public Integer getRapidGames() {
        return rapidGames;
    }

    public Integer getRapidProg() {
        return rapidProg;
    }

    public Boolean getRapidProv() {
        return rapidProv;
    }

    public Integer getRapidRating() {
        return rapidRating;
    }

    public Integer getRapidRd() {
        return rapidRd;
    }

    public Integer getUltrabulletGames() {
        return ultrabulletGames;
    }

    public Integer getUltrabulletProg() {
        return ultrabulletProg;
    }

    public Boolean getUltrabulletProv() {
        return ultrabulletProv;
    }

    public Integer getUltrabulletRating() {
        return ultrabulletRating;
    }

    public Integer getUltrabulletRd() {
        return ultrabulletRd;
    }

    // Getters and Setters
    public String getUserDni() {
        return userDni;
    }

    public String getUsername() {
        return username;
    }

    public void setBlitzGames(Integer blitzGames) {
        this.blitzGames = blitzGames;
    }

    public void setBlitzProg(Integer blitzProg) {
        this.blitzProg = blitzProg;
    }

    public void setBlitzProv(Boolean blitzProv) {
        this.blitzProv = blitzProv;
    }

    public void setBlitzRating(Integer blitzRating) {
        this.blitzRating = blitzRating;
    }

    public void setBlitzRd(Integer blitzRd) {
        this.blitzRd = blitzRd;
    }

    public void setBulletGames(Integer bulletGames) {
        this.bulletGames = bulletGames;
    }

    public void setBulletProg(Integer bulletProg) {
        this.bulletProg = bulletProg;
    }

    public void setBulletProv(Boolean bulletProv) {
        this.bulletProv = bulletProv;
    }

    public void setBulletRating(Integer bulletRating) {
        this.bulletRating = bulletRating;
    }

    public void setBulletRd(Integer bulletRd) {
        this.bulletRd = bulletRd;
    }

    public void setClassicalGames(Integer classicalGames) {
        this.classicalGames = classicalGames;
    }

    public void setClassicalProg(Integer classicalProg) {
        this.classicalProg = classicalProg;
    }

    public void setClassicalProv(Boolean classicalProv) {
        this.classicalProv = classicalProv;
    }

    public void setClassicalRating(Integer classicalRating) {
        this.classicalRating = classicalRating;
    }

    public void setClassicalRd(Integer classicalRd) {
        this.classicalRd = classicalRd;
    }

    public void setFideRating(Integer fideRating) {
        this.fideRating = fideRating;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPuzzleGames(Integer puzzleGames) {
        this.puzzleGames = puzzleGames;
    }

    public void setPuzzleProg(Integer puzzleProg) {
        this.puzzleProg = puzzleProg;
    }

    public void setPuzzleProv(Boolean puzzleProv) {
        this.puzzleProv = puzzleProv;
    }

    public void setPuzzleRating(Integer puzzleRating) {
        this.puzzleRating = puzzleRating;
    }

    public void setPuzzleRd(Integer puzzleRd) {
        this.puzzleRd = puzzleRd;
    }

    public void setRapidGames(Integer rapidGames) {
        this.rapidGames = rapidGames;
    }

    public void setRapidProg(Integer rapidProg) {
        this.rapidProg = rapidProg;
    }

    public void setRapidProv(Boolean rapidProv) {
        this.rapidProv = rapidProv;
    }

    public void setRapidRating(Integer rapidRating) {
        this.rapidRating = rapidRating;
    }

    public void setRapidRd(Integer rapidRd) {
        this.rapidRd = rapidRd;
    }

    public void setUltrabulletGames(Integer ultrabulletGames) {
        this.ultrabulletGames = ultrabulletGames;
    }

    public void setUltrabulletProg(Integer ultrabulletProg) {
        this.ultrabulletProg = ultrabulletProg;
    }

    public void setUltrabulletProv(Boolean ultrabulletProv) {
        this.ultrabulletProv = ultrabulletProv;
    }

    public void setUltrabulletRating(Integer ultrabulletRating) {
        this.ultrabulletRating = ultrabulletRating;
    }

    public void setUltrabulletRd(Integer ultrabulletRd) {
        this.ultrabulletRd = ultrabulletRd;
    }

    public void setUserDni(String userDni) {
        this.userDni = userDni;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
