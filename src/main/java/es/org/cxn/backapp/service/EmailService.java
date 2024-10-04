
package es.org.cxn.backapp.service;

import java.io.IOException;

import jakarta.mail.MessagingException;

/**
 * Interface for sending emails. Provides a method for sending simple emails.
 */
public interface EmailService {

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
}
