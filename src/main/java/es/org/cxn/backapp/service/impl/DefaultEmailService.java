
package es.org.cxn.backapp.service.impl;

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
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import es.org.cxn.backapp.service.EmailService;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;

/**
 * Implementation of {@link EmailService} for sending email's. Provides a method
 * to send simple email's using {@link JavaMailSender}.
 */
@Service
public class DefaultEmailService implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEmailService.class);

    /**
     * The JavaMailSender instance used for sending emails.
     */
    private final Optional<JavaMailSender> mailSender;

    /**
     * Main service constructor.
     *
     * @param mailSender The mail java mail sender implementation.Can be null when
     *                   no email service is provided by JavaMail config.
     */
    public DefaultEmailService(final Optional<JavaMailSender> mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Loads an HTML email template from the classpath.
     *
     * @param templatePath The path to the template file on the classpath.
     * @return The content of the email template as a string.
     * @throws IOException If there is an error reading the template file.
     */
    private String loadEmailTemplate(final String templatePath) throws IOException {
        final ClassPathResource resource = new ClassPathResource(templatePath);
        try (var inputStream = resource.getInputStream();
                var reader = new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8);
                var bufferedReader = new java.io.BufferedReader(reader)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Sends an email to notify the user of a change in their email address.
     *
     * @param oldEmail   The old email address.
     * @param newEmail   The new email address.
     * @param memberName The name of the member.
     * @throws MessagingException If there is an error while composing or sending
     *                            the email.
     * @throws IOException        If there is an error reading the email template.
     */
    @Override
    public void sendChangeEmail(final String oldEmail, final String newEmail, final String memberName)
            throws MessagingException, IOException {

        if (mailSender.isEmpty()) {
            LOGGER.warn("Email service is disabled. Skipping email...");

        } else {

            sendEmail(oldEmail, "CXN: Cambio de correo", "mailTemplates/ChangeEmailMessage.html",
                    Map.of("name", memberName, "oldEmail", oldEmail, "newEmail", newEmail));
            sendEmail(newEmail, "CXN: Cambio de correo", "mailTemplates/ChangeEmailMessage.html",
                    Map.of("name", memberName, "oldEmail", oldEmail, "newEmail", newEmail));
        }
    }

    @Override
    public void sendDeletedUser(final String toEmail, final String memberName) throws MessagingException, IOException {
        sendEmail(toEmail, "CXN: Usuario eliminado", "mailTemplates/DeletedMemberEmail.html",
                Map.of("name", memberName));
    }

    /**
     * Sends an email with the specified details.
     *
     * @param toEmail      The recipient's email address.
     * @param subject      The subject of the email.
     * @param templatePath The path to the email template.
     * @param placeholders A map of placeholders to replace in the template.
     * @throws MessagingException If there is an error while composing or sending
     *                            the email.
     * @throws IOException        If there is an error reading the email template.
     */
    private void sendEmail(final String toEmail, final String subject, final String templatePath,
            final Map<String, String> placeholders) throws MessagingException, IOException {
        if (mailSender.isEmpty()) {
            LOGGER.warn("No email send!");
        } else {
            final var message = mailSender.get().createMimeMessage();
            message.setFrom(new InternetAddress("principal@xadreznaron.es", "Xadrez Narón"));
            message.setRecipients(RecipientType.TO, toEmail);
            message.setSubject(subject);

            final String htmlTemplate = loadEmailTemplate(templatePath);
            final String formattedHtml = StringSubstitutor.replace(htmlTemplate, placeholders);

            message.setContent(formattedHtml, "text/html; charset=utf-8");
            mailSender.get().send(message);
        }
    }

    /**
     * Sends a payment confirmation email to the user.
     *
     * @param toEmail         The recipient's email address.
     * @param memberName      The name of the member.
     * @param paymentQuantity The amount paid.
     * @param reason          The reason for the payment.
     * @throws MessagingException If there is an error while composing or sending
     *                            the email.
     * @throws IOException        If there is an error reading the email template.
     */
    @Override
    public void sendPaymentConfirmation(final String toEmail, final String memberName, final String paymentQuantity,
            final String reason) throws MessagingException, IOException {
        sendEmail(toEmail, "CXN: Confirmación de pago", "mailTemplates/PaymentConfirmedEmail.html",
                Map.of("name", memberName, "motivo", reason, "cantidad", paymentQuantity));
    }

    /**
     * Sends a sign-up confirmation email to the user.
     *
     * @param toEmail    The recipient's email address.
     * @param memberName The name of the member.
     * @param body       The body content of the email.
     * @throws MessagingException If there is an error while composing or sending
     *                            the email.
     * @throws IOException        If there is an error reading the email template.
     */
    @Override
    public void sendSignUp(final String toEmail, final String memberName, final String body)
            throws MessagingException, IOException {
        sendEmail(toEmail, "Hola, " + memberName + "!", "mailTemplates/SignUpWelcomeEmail.html",
                Map.of("name", memberName));
    }

    /**
     * Sends a welcome email to the user after they become a member.
     *
     * @param toEmail    The recipient's email address.
     * @param memberName The name of the member.
     * @throws MessagingException If there is an error while composing or sending
     *                            the email.
     * @throws IOException        If there is an error reading the email template.
     */
    @Override
    public void sendWelcome(final String toEmail, final String memberName) throws MessagingException, IOException {
        sendEmail(toEmail, "CXN: Ya eres socio", "mailTemplates/AcceptedMemberEmail.html", Map.of("name", memberName));
    }

}
