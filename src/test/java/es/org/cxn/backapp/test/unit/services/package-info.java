/**
 * Provides unit tests for the services in the application.
 *
 * This package contains test classes for verifying the functionality of
 * different service components. These tests are meant to ensure that the
 * service layers function correctly and handle various edge cases.
 *
 * <p>The specific test classes included in this package are:
 * <ul>
 *     <li>{@link ChessQuestionServiceTest} - Tests for the {@link
 *         es.org.cxn.backapp.services.ChessQuestionService} class.
 *         Ensures that the chess question service behaves as expected,
 *         including creating, retrieving, and managing chess questions.</li>
 *     <li>{@link UserServiceTest} - Tests for the {@link
 *         es.org.cxn.backapp.service.impl.services.DefaultUserService} class. Verifies
 *         the correctness of user-related operations such as user creation,
 *         updating user details, and user authentication.</li>
 *     <li>{@link PaymentSheetServiceTest} - Tests for the {@link
 *         es.org.cxn.backapp.services.PaymentSheetService} class. Ensures
 *         the payment sheet service functions correctly, including handling
 *         payment processing and generating payment sheets.</li>
 * </ul>
 * </p>
 *
 * All tests in this package are designed to be unit tests, focusing on
 * individual units of functionality and ensuring that each service performs
 * as expected in isolation.
 */

package es.org.cxn.backapp.test.unit.services;

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
