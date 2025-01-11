/**
 * Contains classes and enums related to the persistence of payment data.
 * <p>
 * This package provides the following components:
 * </p>
 * <ul>
 * <li>{@link es.org.cxn.backapp.model.persistence.payments.PaymentsCategory} -
 * Enum defining the various categories of payments, such as membership,
 * federation, and others.</li>
 * <li>{@link es.org.cxn.backapp.model.persistence.payments.PaymentsState} -
 * Enum representing the possible states of a payment, including unpaid, paid,
 * and cancelled.</li>
 * <li>{@link es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity}
 * - Entity class representing payment records in the database, including fields
 * for payment details and associated user information.</li>
 * </ul>
 *
 * <p>
 * The classes in this package are used to model and manage payment-related data
 * in the persistence layer of the application. They enable the application to
 * categorize, track, and process payments efficiently.
 * </p>
 *
 * @author Santi
 */
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
