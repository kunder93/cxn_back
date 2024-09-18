
package es.org.cxn.backapp.test.unit.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.org.cxn.backapp.service.DefaultEmailService;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

class EmailServiceTest {

  @Mock
  private JavaMailSender mailSender;

  @Mock
  private MimeMessage mimeMessage;

  @InjectMocks
  private DefaultEmailService emailService;

  @BeforeEach
  public void setUp() {
    // Initialize mocks
    MockitoAnnotations.openMocks(this);

    // Configure the mock mailSender to return the mocked MimeMessage
    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
  }

  @Test
  void testSendSignUpEmail() throws MessagingException {
    // Given
    var toEmail = "test@example.com";
    var subject = "Test Subject";
    var body = "This is a test email.";

    // When
    emailService.sendSignUpEmail(toEmail, subject, body);

    // Then
    verify(mailSender, times(1)).createMimeMessage();
    verify(mimeMessage, times(1))
          .setFrom(new InternetAddress("principal@xadreznaron.es"));
    verify(mimeMessage, times(1)).setRecipients(RecipientType.TO, toEmail);
    verify(mimeMessage, times(1)).setSubject("Hola, " + subject + "!\n\n");
    verify(mimeMessage, times(1))
          .setContent(any(String.class), eq("text/html; charset=utf-8"));
    verify(mailSender, times(1)).send(mimeMessage);
  }
}
