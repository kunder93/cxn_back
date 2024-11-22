package es.org.cxn.backapp.model.form.responses;

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
 * @param federateStateMembersList a list of federate state responses
 */
public record FederateStateExtendedResponseList(List<FederateStateExtendedResponse> federateStateMembersList) {

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
