
package es.org.cxn.backapp.service;

import java.io.IOException;

import jakarta.mail.MessagingException;

/**
 * Interface for sending emails. Provides a method for sending simple emails.
 */
public interface EmailService {

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
    void sendPaymentConfirmationEmail(final String toEmail, final String memberName, final String paymentQuantity,
            final String reason) throws MessagingException, IOException;

    /**
     * Sends a sign up email.
     *
     * @param toEmail the recipient's email address
     * @param subject the subject of the email
     * @param body    the body of the email
     * @throws MessagingException When message fails.
     * @throws IOException        When fails cause cannot load mail template file.
     */
    void sendSignUpEmail(String toEmail, String subject, String body) throws MessagingException, IOException;

    /**
     * Sends a welcome email to newly approved members.
     *
     * @param toEmail    the recipient's email address
     * @param memberName the name of the member
     * @throws MessagingException When message fails.
     * @throws IOException        When fails cause cannot load mail template file.
     */
    void sendWelcomeEmail(String toEmail, String memberName) throws MessagingException, IOException;

}
