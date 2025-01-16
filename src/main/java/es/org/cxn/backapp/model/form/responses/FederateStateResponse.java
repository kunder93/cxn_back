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
