
package es.org.cxn.backapp.service;

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

import java.io.IOException;

import jakarta.mail.MessagingException;

/**
 * Interface for sending emails. Provides a method for sending simple emails.
 */
public interface EmailService {

    /**
     * Sends a message to old and new member email.
     *
     * @param oldEmail   The member's old email.
     * @param newEmail   The member's new email.
     * @param memberName The member's complete name.
     * @throws MessagingException When message fails.
     * @throws IOException        When fails cause cannot load mail template file.
     */
    void sendChangeEmail(String oldEmail, String newEmail, String memberName) throws MessagingException, IOException;

    /**
     * Send email to user who has been deleted.
     *
     * @param toEmail    Email from user.
     * @param memberName The member complete name.
     * @throws MessagingException When message fails.
     * @throws IOException        When fails cause cannot load mail template file.
     */
    void sendDeletedUser(String toEmail, String memberName) throws MessagingException, IOException;

    void sendGeneratedPayment(final String toEmail, final String memberName, final String paymentTitle,
            String paymentDescription, final String paymentAmount) throws MessagingException, IOException;

    /**
     * Sends a confirmation payment received email to member.
     *
     * @param toEmail         the recipient's email address
     * @param memberName      the name of the member
     * @param paymentQuantity the amount of the payment.
     * @param reason          the reason or description of payment.
     * @throws MessagingException When message fails.
     * @throws IOException        When fails cause cannot load mail template file.
     */
    void sendPaymentConfirmation(String toEmail, String memberName, String paymentQuantity, String reason)
            throws MessagingException, IOException;

    /**
     * Sends a sign up email.
     *
     * @param toEmail the recipient's email address
     * @param subject the subject of the email
     * @param body    the body of the email
     * @throws MessagingException When message fails.
     * @throws IOException        When fails cause cannot load mail template file.
     */
    void sendSignUp(String toEmail, String subject, String body) throws MessagingException, IOException;

    /**
     * Sends a unsubscribe email.
     *
     * @param toEmail    the recipient's email address
     * @param memberName the complete name of the member
     * @throws MessagingException When message fails.
     * @throws IOException        When fails cause cannot load mail template file.
     */
    void sendUnsubscribe(String toEmail, String memberName) throws MessagingException, IOException;

    /**
     * Sends a welcome email to newly approved members.
     *
     * @param toEmail    the recipient's email address
     * @param memberName the name of the member
     * @throws MessagingException When message fails.
     * @throws IOException        When fails cause cannot load mail template file.
     */
    void sendWelcome(String toEmail, String memberName) throws MessagingException, IOException;

}
