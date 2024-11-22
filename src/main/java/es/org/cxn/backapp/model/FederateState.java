package es.org.cxn.backapp.model;

/**
 * Enum representing the federate status of a player.
 * <p>
 * This enum indicates whether a player is eligible to participate in official
 * chess competitions.
 * </p>
 * <ul>
 * <li>{@link #FEDERATE} - The player is federated and can participate in
 * official chess competitions.</li>
 * <li>{@link #NO_FEDERATE} - The player is not federated and cannot participate
 * in official chess competitions.</li>
 * <li>{@link #IN_PROGRESS} - The player's federate status is in the process of
 * being updated or renewed.</li>
 * </ul>
 */
public enum FederateState {

    /**
     * The player is federated and can participate in official chess competitions.
     */
    FEDERATE,

    /**
     * The player is not federated and cannot participate in official chess
     * competitions.
     */
    NO_FEDERATE,

    /**
     * The player's federate status is in the process of being updated or renewed.
     */
    IN_PROGRESS
}
