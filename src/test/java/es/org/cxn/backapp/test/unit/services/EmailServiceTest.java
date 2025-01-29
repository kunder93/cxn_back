
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import es.org.cxn.backapp.service.impl.DefaultEmailService;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

class EmailServiceTest {

    /**
     * Mock sender, not real sending.
     */
    @Mock
    private JavaMailSender mailSender;

    /**
     * Mock mime messages.
     */
    @Mock
    private MimeMessage mimeMessage;

    /**
     * Service where inject mocks.
     */
    @InjectMocks
    private DefaultEmailService emailService;

    @BeforeEach
    public void setUp()
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Configure the mock mailSender to return the mocked MimeMessage
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Inject the value for senderAddress
        Field senderAddressField = DefaultEmailService.class.getDeclaredField("senderAddress");
        senderAddressField.setAccessible(true);
        senderAddressField.set(emailService, "principal@xadreznaron.es");
    }

    @Test
    void testSendChangeEmail() throws MessagingException, IOException {
        // Given
        String oldEmail = "oldemail@example.com";
        String newEmail = "newemail@example.com";
        String memberName = "John Doe";

        // When
        emailService.sendChangeEmail(oldEmail, newEmail, memberName);

        // Then
        // some invoked twice, once for each email.
        verify(mailSender, times(2)).createMimeMessage();
        verify(mimeMessage, times(2)).setFrom(new InternetAddress("principal@xadreznaron.es"));
        verify(mimeMessage, times(1)).setRecipients(RecipientType.TO, oldEmail);
        verify(mimeMessage, times(2)).setSubject("CXN: Cambio de correo");
        verify(mimeMessage, times(2)).setContent(any(String.class), eq("text/html; charset=utf-8"));

        verify(mimeMessage, times(2)).setFrom(new InternetAddress("principal@xadreznaron.es"));
        verify(mimeMessage, times(1)).setRecipients(RecipientType.TO, newEmail);
        verify(mimeMessage, times(2)).setSubject("CXN: Cambio de correo");
        verify(mimeMessage, times(2)).setContent(any(String.class), eq("text/html; charset=utf-8"));

        verify(mailSender, times(2)).send(mimeMessage);
    }

    @Test
    void testSendPaymentConfirmation() throws MessagingException, IOException {
        // Given
        String toEmail = "recipient@example.com";
        String memberName = "Jane Smith";
        String paymentQuantity = "50€";
        String reason = "Membership Fee";

        // When
        emailService.sendPaymentConfirmation(toEmail, memberName, paymentQuantity, reason);

        // Then
        verify(mailSender, times(1)).createMimeMessage();
        verify(mimeMessage, times(1)).setFrom(new InternetAddress("principal@xadreznaron.es"));
        verify(mimeMessage, times(1)).setRecipients(RecipientType.TO, toEmail);
        verify(mimeMessage, times(1)).setSubject("CXN: Confirmación de pago");
        verify(mimeMessage, times(1)).setContent(any(String.class), eq("text/html; charset=utf-8"));

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendSignUpEmail() throws MessagingException, IOException {
        // Given
        var toEmail = "principal@xadreznaron.es";
        var subject = "Test Subject";
        var body = "This is a test email.";

        // When
        emailService.sendSignUp(toEmail, subject, body);

        // Then
        verify(mailSender, times(1)).createMimeMessage();
        verify(mimeMessage, times(1)).setFrom(new InternetAddress("principal@xadreznaron.es"));
        verify(mimeMessage, times(1)).setContent(any(String.class), eq("text/html; charset=utf-8"));
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendWelcome() throws MessagingException, IOException {
        // Given
        String toEmail = "member@example.com";
        String memberName = "John Doe";

        // When
        emailService.sendWelcome(toEmail, memberName);

        // Then
        verify(mailSender, times(1)).createMimeMessage();
        verify(mimeMessage, times(1)).setFrom(new InternetAddress("principal@xadreznaron.es"));
        verify(mimeMessage, times(1)).setRecipients(RecipientType.TO, toEmail);
        verify(mimeMessage, times(1)).setSubject("CXN: Ya eres socio");
        verify(mimeMessage, times(1)).setContent(any(String.class), eq("text/html; charset=utf-8"));
        verify(mailSender, times(1)).send(mimeMessage);
    }

}
