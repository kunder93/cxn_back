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

import java.time.LocalDate;

import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;

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
     * Getter for associated payment entity.
     *
     * @return The federate state associated payment.
     */
    PersistentPaymentsEntity getPayment();

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
     * Setter for federate state payment.
     *
     * @param value The new federate state associated payment.
     */
    void setPayment(PersistentPaymentsEntity value);

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
