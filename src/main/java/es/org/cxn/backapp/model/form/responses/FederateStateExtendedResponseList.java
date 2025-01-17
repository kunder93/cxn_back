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

import java.time.LocalDate;
import java.util.List;

import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;

/**
 * Represents a response containing a list of federate state members.
 *
 * <p>
 * This record is used to encapsulate a list of
 * {@link FederateStateExtendedResponse} objects, which represent the federate
 * state information of individual users.
 * </p>
 *
 * @param federateStateMembersList an immutable list of federate state responses
 */
public record FederateStateExtendedResponseList(List<FederateStateExtendedResponse> federateStateMembersList) {

    /**
     * Canonical constructor with a defensive copy to ensure immutability.
     *
     * @param federateStateMembersList the list of federate state responses
     */
    public FederateStateExtendedResponseList(final List<FederateStateExtendedResponse> federateStateMembersList) {
        this.federateStateMembersList = List
                .copyOf(federateStateMembersList == null ? List.of() : federateStateMembersList);
    }

    /**
     * Returns an immutable view of the federate state members list.
     *
     * @return an unmodifiable list of federate state members
     */
    @Override
    public List<FederateStateExtendedResponse> federateStateMembersList() {
        return List.copyOf(federateStateMembersList);
    }

    /**
     * Nested record representing an individual federate state response.
     *
     * <p>
     * This record includes the user's DNI, their federate state, auto-renewal
     * status, and the last update date for their DNI.
     * </p>
     *
     * @param dni           the user's DNI
     * @param state         the federate state of the user
     * @param autoRenew     indicates whether the user's federate status is set to
     *                      auto-renew
     * @param dniLastUpdate the last update date for the user's DNI
     */
    public record FederateStateExtendedResponse(String dni, FederateState state, Boolean autoRenew,
            LocalDate dniLastUpdate) {

        /**
         * Constructor to build a {@link FederateStateExtendedResponse} from a
         * {@link PersistentFederateStateEntity}.
         *
         * @param entity the persistent federate state entity containing user data
         */
        public FederateStateExtendedResponse(final PersistentFederateStateEntity entity) {
            this(entity.getUserDni(), entity.getState(), entity.isAutomaticRenewal(), entity.getDniLastUpdate());
        }
    }

    /**
     * Converts a list of persistent federate state entities into a
     * {@link FederateStateExtendedResponseList}.
     *
     * @param entities a list of {@link PersistentFederateStateEntity} objects to be
     *                 converted
     * @return a new {@link FederateStateExtendedResponseList} containing the
     *         converted responses
     */
    public static FederateStateExtendedResponseList fromEntities(final List<PersistentFederateStateEntity> entities) {
        // Convert the list of entities to a list of FederateStateExtendedResponse
        final List<FederateStateExtendedResponse> responseList = entities.stream()
                .map(FederateStateExtendedResponse::new).toList();
        // Return a new FederateStateExtendedResponseList using the canonical
        // constructor
        return new FederateStateExtendedResponseList(responseList);
    }
}
