package es.org.cxn.backapp.model.form.responses;

import java.time.LocalDate;

import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.FederateStateEntity;

/**
 * Represents a response containing federate state information for a user.
 *
 * <p>
 * This record encapsulates the user's federate state, auto-renewal status, and
 * the last update date for their DNI.
 * </p>
 *
 * @param state         the federate state of the user
 * @param autoRenew     indicates whether the user's federate status is set to
 *                      auto-renew
 * @param dniLastUpdate the last update date for the user's DNI
 */
public record FederateStateResponse(FederateState state, Boolean autoRenew, LocalDate dniLastUpdate) {

    /**
     * Constructs a {@link FederateStateResponse} from a
     * {@link FederateStateEntity}.
     *
     * @param entity the federate state entity containing user data
     */
    public FederateStateResponse(final FederateStateEntity entity) {
        this(entity.getState(), entity.isAutomaticRenewal(), entity.getDniLastUpdate());
    }
}
