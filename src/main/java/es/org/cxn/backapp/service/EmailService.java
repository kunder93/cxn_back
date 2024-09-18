
package es.org.cxn.backapp.service;

import jakarta.mail.MessagingException;

/**
 * Interface for sending emails.
 * Provides a method for sending simple emails.
 */
public interface EmailService {

  /**
   * Sends a sign up email.
   *
   * @param toEmail the recipient's email address
   * @param subject the subject of the email
   * @param body    the body of the email
   * @throws MessagingException When message fails.
   */
  void sendSignUpEmail(String toEmail, String subject, String body)
        throws MessagingException;
}
