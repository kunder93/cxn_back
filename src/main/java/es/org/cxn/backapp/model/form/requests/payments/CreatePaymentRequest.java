package es.org.cxn.backapp.model.form.requests.payments;

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

import java.math.BigDecimal;

import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;

/**
 * Represents a request to create a payment. This record encapsulates all the
 * necessary information required to create a new payment.
 *
 * @param userDni     The DNI of the user making the payment. Must not be null
 *                    or empty.
 * @param title       The title of the payment. Describes the purpose or reason
 *                    for the payment.
 * @param description A brief description of the payment. Provides additional
 *                    details about the payment.
 * @param category    The category of the payment. Determines the type or
 *                    classification of the payment.
 * @param amount      The amount of the payment. Must be a positive decimal
 *                    value.
 */
public record CreatePaymentRequest(String userDni, String title, String description, PaymentsCategory category,
        BigDecimal amount) {

}
