
package es.org.cxn.backapp.service;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link EmailService} for sending emails.
 * Provides a method to send simple emails using {@link JavaMailSender}.
 */
@Service
public class DefaultEmailService implements EmailService {

  private final JavaMailSender mailSender;

  /**
   * Constructs a {@code DefaultEmailService} with the specified mail sender
   * and environment.
   *
   * @param mailSender the {@link JavaMailSender} used to send emails.
   */
  public DefaultEmailService(final JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * Sends a simple email.
   * <p>
   * The 'from' address is taken from the {@code spring.mail.username} property
   * in the application's configuration. The recipient, subject, and body of
   * the email are provided as parameters.
   * </p>
   *
   * @param toEmail the recipient's email address
   * @param subject the subject of the email
   * @param body    the body of the email
   * @throws MessagingException
   */
  @Override
  public void sendSignUpEmail(
        final String toEmail, final String subject, final String body
  ) throws MessagingException {
    var message = mailSender.createMimeMessage();

    message.setFrom(new InternetAddress("principal@xadreznaron.es"));
    message.setRecipients(RecipientType.TO, toEmail);
    message.setSubject("Hola, " + subject + "!\n\n");
    var htmlTemplate =
          """
                        <html>
                          <body style="font-family: Arial, sans-serif; color: #333;">
                            <div style="max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; background-color: #f9f9f9;">
                              <h1 style="text-align: center; color: #4CAF50;">¡Bienvenido al Círculo Xadrez Narón!</h1>
                              <p>Hola <strong>%s</strong>,</p>
                              <p>Gracias por unirte a nuestro club de ajedrez. Estamos emocionados de tenerte como nuevo miembro.</p>

                              <h2 style="color: #4CAF50;">Confirmación de tu solicitud de socio</h2>
                              <p>Hemos recibido tu solicitud para hacerte socio del <strong>Círculo Xadrez Narón</strong>. Estamos procesando tu activación y te rogamos que tengas un poco de paciencia mientras completamos este proceso.</p>

                              <p>En cuanto tu cuenta sea activada, recibirás un correo de confirmación con los próximos pasos para disfrutar de todas las actividades y eventos de nuestro club.</p>

                              <p style="text-align: center;">
                                <a href="https://www.xadreznaron.es" style="text-decoration: none; color: white; background-color: #4CAF50; padding: 10px 20px; border-radius: 5px;">Visita nuestro sitio web</a>
                              </p>

                              <p>Si tienes alguna pregunta mientras tanto, no dudes en ponerte en contacto con nosotros respondiendo a este correo o visitando nuestra página de contacto en nuestro sitio web.</p>

                              <p>Atentamente,</p>
                              <p><strong>El equipo del Círculo Xadrez Narón</strong></p>
                              <hr>
                             <p style="font-size: 12px; text-align: center; color: #999;">
                «Esta comunicación va dirigida de manera exclusiva a su destinatario y puede contener información confidencial y/o sujeta a secreto profesional, cuya divulgación no está permitida por la Ley. Si ha recibido este mensaje por error, le rogamos que a la menor brevedad posible se comunique mediante correo electrónico remitido a nuestra dirección y proceda a su eliminación, así como cualquier documento adjunto al mismo».
            </p>
            <p style="font-size: 12px; text-align: center; color: #999;">
                «En cumplimiento del RGPD en materia de protección de datos personales, le informamos de que recibe este email por haber solicitado su alta como socio en el Club Círculo Xadrez Narón.
                El responsable del tratamiento de sus datos es Círculo Xadrez Narón, con CIF G-15.227.556 y domicilio fiscal en R/Manoel Antonio 9, 15570 Narón (A Coruña).
                La finalidad para la que se recogen sus datos es gestionar su solicitud de alta como socio del club y remitirle comunicaciones relacionadas con dicha membresía.
                Su información permanecerá en nuestra base de datos durante el tiempo necesario para gestionar su alta y no será cedida a terceros salvo obligación legal.
                Puede ejercer sus derechos de acceso, rectificación, cancelación, oposición o limitación del tratamiento poniéndose en contacto con nosotros a través de principal@xadreznaron.es, xadreznaron.es o en las oficinas del club».
            </p>
                            </div>
                          </body>
                        </html>
                        """
                .formatted(subject);

    // Set the email's content to be the HTML template
    message.setContent(htmlTemplate, "text/html; charset=utf-8");

    mailSender.send(message);
  }
}
