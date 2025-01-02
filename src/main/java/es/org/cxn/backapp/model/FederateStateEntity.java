package es.org.cxn.backapp.model;

import java.time.LocalDate;

/**
 * Represents a Federate State entity with various details about a user's
 * federative status. This includes fields related to the user's DNI, DNI front
 * image URL, DNI back image URL, automatic renewal status, and the last update
 * timestamp of the user's DNI information.
 * <p>
 * It provides methods to access and modify these fields.
 * </p>
 *
 * @author Santiago Paz Perez
 */
public interface FederateStateEntity extends java.io.Serializable {

    /**
     * Gets the URL of the back image of the user's DNI.
     *
     * @return The URL of the back image of the user's DNI as a {@link String}.
     */
    String getDniBackImageUrl();

    /**
     * Gets the URL of the front image of the user's DNI.
     *
     * @return The URL of the front image of the user's DNI as a {@link String}.
     */
    String getDniFrontImageUrl();

    /**
     * Gets the timestamp of when the user's DNI information was last updated.
     *
     * @return The timestamp of the last update as a
     *         {@link java.time.LocalDateTime}.
     */
    LocalDate getDniLastUpdate();

    /**
     * Gets the federate state of the user.
     *
     * @return The federate state as a {@link FederateState}.
     */
    FederateState getState();

    /**
     * Gets the user's DNI associated with the federate state.
     *
     * @return The user's DNI as a {@link String}.
     */
    String getUserDni();

    /**
     * Checks if the user's membership has automatic renewal enabled.
     *
     * @return {@code true} if automatic renewal is enabled, {@code false}
     *         otherwise.
     */
    boolean isAutomaticRenewal();

    /**
     * Sets the automatic renewal status for the user's membership.
     *
     * @param automaticRenewal {@code true} to enable automatic renewal,
     *                         {@code false} to disable it.
     */
    void setAutomaticRenewal(boolean automaticRenewal);

    /**
     * Sets the URL of the back image of the user's DNI.
     *
     * @param dniBackImageUrl The URL of the back image of the user's DNI as a
     *                        {@link String}.
     */
    void setDniBackImageUrl(String dniBackImageUrl);

    /**
     * Sets the URL of the front image of the user's DNI.
     *
     * @param dniFrontImageUrl The URL of the front image of the user's DNI as a
     *                         {@link String}.
     */
    void setDniFrontImageUrl(String dniFrontImageUrl);

    /**
     * Sets the timestamp of when the user's DNI information was last updated.
     *
     * @param dniLastUpdate The timestamp of the last update as a
     *                      {@link java.time.LocalDateTime}.
     */
    void setDniLastUpdate(LocalDate dniLastUpdate);

    /**
     * Sets the federate state of the user.
     *
     * @param state The federate state as a {@link FederateState}.
     */
    void setState(FederateState state);

    /**
     * Sets the user's DNI associated with the federate state.
     *
     * @param userDni The user's DNI as a {@link String}.
     */
    void setUserDni(String userDni);

}
