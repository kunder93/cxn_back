package es.org.cxn.backapp.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

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

    /**
     * The java mail sender.
     */
    private final JavaMailSender mailSender;

    /**
     * Default mail service constructor.
     *
     * @param value Provided java mail sender.
     */
    public DefaultEmailService(final JavaMailSender value) {
        this.mailSender = value;
    }

    /**
     * Loads the email template from the resources directory.
     *
     * @return the content of the HTML template as a String
     * @throws IOException if there is an error reading the file
     */
    private String loadEmailTemplate() throws IOException {
        final ClassPathResource resource = new ClassPathResource("mailTemplates/SignUpWelcomeEmail.html");

        // Use InputStream instead of Files.lines to handle resources inside the JAR
        try (var inputStream = resource.getInputStream();
                var reader = new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8);
                var bufferedReader = new java.io.BufferedReader(reader)) {

            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendSignUpEmail(final String toEmail, final String subject, final String body)
            throws MessagingException, IOException {
        final var message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("principal@xadreznaron.es"));
        message.setRecipients(RecipientType.TO, toEmail);
        message.setSubject("Hola, " + subject + "!");

        // Load HTML template from file
        final String htmlTemplate = loadEmailTemplate();

        // Replace the placeholder with the subject (name)
        final String formattedHtml = String.format(htmlTemplate, subject);

        // Set the email's content to be the HTML template
        message.setContent(formattedHtml, "text/html; charset=utf-8");

        mailSender.send(message);
    }
}
