
package es.org.cxn.backapp.model.persistence;

import java.math.BigInteger;
import java.time.LocalDate;

import es.org.cxn.backapp.model.TournamentParticipantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Entity representing a participant in a tournament.
 * <p>
 * This class uses JPA annotations to define the persistence configuration for a
 * tournament participant entity.
 * </p>
 *
 * <p>
 * The entity is mapped to the `tournamentParticipant` table in the database. It
 * includes fields for the participant's FIDE ID, name, club, birth date,
 * category, and byes. The {@link TournamentCategory} enum defines the available
 * categories a participant can be assigned to.
 * </p>
 *
 * <p>
 * This class is annotated with Lombok's {@code @Data} for generating
 * boilerplate code such as getters, setters, and {@code toString()} methods,
 * and {@code @Builder} for providing a builder pattern. It also includes
 * {@code @AllArgsConstructor} for a constructor that initializes all fields and
 * {@code @NoArgsConstructor} for a no-argument constructor.
 * </p>
 *
 * <p>
 * Note: This class implements the {@link TournamentParticipantEntity}
 * interface.
 * </p>
 *
 * <p>
 * The class also contains a transient field for serialization purposes.
 * </p>
 *
 * <p>
 * <strong>Author:</strong> Santiago Paz Perez
 * </p>
 *
 * <p>
 * <strong>License:</strong> MIT License (see license details at the top of the
 * file)
 * </p>
 */
@Entity(name = "TournamentParticipant")
@Table(name = "tournamentparticipant")
@Data
@Builder
@AllArgsConstructor
public class PersistentTournamentParticipantEntity implements TournamentParticipantEntity {

    /**
     * Enumeration of the possible tournament categories. Each category is based on
     * the participant's age.
     */
    public enum TournamentCategory {
        /**
         * Category for players aged 14 years or less.
         */
        SUB14,
        /**
         * Category for players aged 12 years or less.
         */
        SUB12,
        /**
         * Category for players aged 10 years or less.
         */
        SUB10,
        /**
         * Category for players aged 8 years or less.
         */
        SUB8
    }

    /**
     * Serialization ID used to ensure the class's binary compatibility. This field
     * is marked as {@code transient} since it is not intended to be persisted.
     */
    @Transient
    private static final long serialVersionUID = 1393244932130122291L;

    /**
     * The unique FIDE ID of the tournament participant. This serves as the primary
     * identifier for each participant.
     */
    @Id
    @Column(name = "fide_id")
    private BigInteger fideId;

    /**
     * The name of the participant. This field is mandatory and must be unique.
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * The club to which the participant belongs. This field is mandatory, but it
     * does not need to be unique.
     */
    @Column(name = "club", nullable = false)
    private String club;

    /**
     * The participant's birth date. This field is mandatory and represents the
     * participant's date of birth.
     */
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    /**
     * The category in which the participant is competing. This field is mandatory.
     * <p>
     * The category is defined using the {@link TournamentCategory} enum, which
     * includes categories such as SUB14, SUB12, SUB10, and SUB8 based on the
     * participant's age.
     * </p>
     */
    @Column(name = "category", nullable = false)
    private TournamentCategory category;

    /**
     * The byes requested by the participant.
     * <p>
     * Byes allow a participant to skip certain rounds in the tournament. This field
     * is optional.
     * </p>
     */
    @Column(name = "byes", nullable = true)
    private String byes;

    /**
     * Default no-argument constructor for the PersistentTournamentParticipantEntity
     * class.
     * <p>
     * This constructor is provided for JPA and other frameworks that may require a
     * no-argument constructor for creating instances of this entity.
     * </p>
     */
    public PersistentTournamentParticipantEntity() {
        // void constructor
    }
}
