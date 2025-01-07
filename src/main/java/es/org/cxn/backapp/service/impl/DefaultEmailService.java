package es.org.cxn.backapp.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
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

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderAddress;

    /**
     * Main service constructor.
     *
     * @param mailSender The mail java mail sender implementation.
     */
    public DefaultEmailService(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private String loadEmailTemplate(final String templatePath) throws IOException {
        final ClassPathResource resource = new ClassPathResource(templatePath);
        try (var inputStream = resource.getInputStream();
                var reader = new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8);
                var bufferedReader = new java.io.BufferedReader(reader)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    @Override
    public void sendChangeEmail(final String oldEmail, final String newEmail, final String memberName)
            throws MessagingException, IOException {
        sendEmail(oldEmail, "CXN: Cambio de correo", "mailTemplates/ChangeEmailMessage.html",
                Map.of("name", memberName, "oldEmail", oldEmail, "newEmail", newEmail));
        sendEmail(newEmail, "CXN: Cambio de correo", "mailTemplates/ChangeEmailMessage.html",
                Map.of("name", memberName, "oldEmail", oldEmail, "newEmail", newEmail));
    }

    private void sendEmail(final String toEmail, final String subject, final String templatePath,
            Map<String, String> placeholders) throws MessagingException, IOException {
        final var message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress(senderAddress));
        message.setRecipients(RecipientType.TO, toEmail);
        message.setSubject(subject);

        final String htmlTemplate = loadEmailTemplate(templatePath);
        final String formattedHtml = StringSubstitutor.replace(htmlTemplate, placeholders);

        message.setContent(formattedHtml, "text/html; charset=utf-8");
        mailSender.send(message);
    }

    @Override
    public void sendPaymentConfirmation(final String toEmail, final String memberName, final String paymentQuantity,
            final String reason) throws MessagingException, IOException {
        sendEmail(toEmail, "CXN: Confirmación pago\"", "mailTemplates/PaymentConfirmedEmail.html",
                Map.of("name", memberName, "motivo", reason, "cantidad", paymentQuantity));
    }

    @Override
    public void sendSignUp(final String toEmail, final String memberName, final String body)
            throws MessagingException, IOException {
        sendEmail(toEmail, "Hola, " + memberName + "!", "mailTemplates/SignUpWelcomeEmail.html",
                Map.of("name", memberName));
    }

    @Override
    public void sendWelcome(final String toEmail, final String memberName) throws MessagingException, IOException {
        sendEmail(toEmail, "CXN: Ya eres socio", "mailTemplates/AcceptedMemberEmail.html", Map.of("name", memberName));
    }

}
