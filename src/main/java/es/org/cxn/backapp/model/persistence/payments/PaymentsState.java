package es.org.cxn.backapp.model.persistence.payments;

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

/**
 * Enum representing the possible states of a payment. The state can be one of
 * the following:
 * <ul>
 * <li>{@link #UNPAID}</li>
 * <li>{@link #PAID}</li>
 * <li>{@link #CANCELLED}</li>
 * </ul>
 */
public enum PaymentsState {
    /**
     * The payment has not been completed yet. This state represents an unpaid
     * payment.
     */
    UNPAID("unpaid"),

    /**
     * The payment has been successfully completed. This state represents a paid
     * payment.
     */
    PAID("paid"),

    /**
     * The payment has been cancelled and will not be processed. This state
     * represents a cancelled payment.
     */
    CANCELLED("cancelled");

    /**
     * The string value associated with the payment state.
     * <p>
     * This field represents the underlying string identifier for each payment
     * state. It is used for serialization, storage, or mapping purposes where a
     * string representation of the state is required.
     * </p>
     */
    private final String value;

    /**
     * Constructs a {@link PaymentsState} with the specified string value.
     * <p>
     * This constructor initializes the payment state with a unique string value
     * that serves as its identifier. It is used internally to define the constants
     * of this enum.
     * </p>
     *
     * @param val the string value associated with the payment state.
     */
    PaymentsState(final String val) {
        this.value = val;
    }

    /**
     * Returns the enum constant corresponding to the given string value.
     *
     * @param value the string value to map to an enum constant.
     * @return the corresponding {@link PaymentsState}.
     * @throws IllegalArgumentException if the value does not match any state.
     */
    public static PaymentsState fromValue(final String value) {
        for (final PaymentsState state : PaymentsState.values()) {
            if (state.value.equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }

    /**
     * Returns the string value associated with the state.
     *
     * @return the state value.
     */
    public String getValue() {
        return value;
    }
}
