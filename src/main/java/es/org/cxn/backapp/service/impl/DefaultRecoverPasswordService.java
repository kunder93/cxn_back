
package es.org.cxn.backapp.service.impl;

import java.io.IOException;

import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import es.org.cxn.backapp.service.EmailService;
import es.org.cxn.backapp.service.RecoverPasswordService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.exceptions.RecoverPasswordServiceException;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import jakarta.mail.MessagingException;

@Service
public final class DefaultRecoverPasswordService implements RecoverPasswordService {

    private final OneTimeTokenService oneTimeTokenService;
    private final EmailService emailService;
    private final UserService userService;

    public DefaultRecoverPasswordService(final OneTimeTokenService oneTimeTokenServ, final EmailService emailServ,
            final UserService userServ) {
        oneTimeTokenService = Preconditions.checkNotNull(oneTimeTokenServ,
                "Received a null pointer as one time token service.");
        emailService = Preconditions.checkNotNull(emailServ, "Received a null pointer as email service.");
        userService = Preconditions.checkNotNull(userServ, "Received a null pointer as user service.");
    }

    private String normalizeEmail(String email) {
        return email != null ? email.trim().toLowerCase() : null;
    }

    @Override
    public void resetPassword(String token, String newPassword, String dni) throws RecoverPasswordServiceException {
        OneTimeToken verifiedToken = oneTimeTokenService.consume(new OneTimeTokenAuthenticationToken(token));
        // Verify token not null.
        if (verifiedToken == null) {
            throw new RecoverPasswordServiceException("Token inválido o expirado");
        }
        try {
            final var userByDni = userService.findByDni(dni);
            if (!userByDni.getEmail().equals(verifiedToken.getUsername())) {
                throw new RecoverPasswordServiceException("Diferent token and/or user dni.");
            }
            userService.recoverPassword(verifiedToken.getUsername(), newPassword);
        } catch (UserServiceException e) {
            throw new RecoverPasswordServiceException("User not found.");
        }

    }

    @Override
    public void sendToken(String email, String userDni) throws RecoverPasswordServiceException {
        try {
            final var normEmail = normalizeEmail(email);
            final var userByEmail = userService.findByEmail(normEmail);
            final var userByDni = userService.findByDni(userDni);
            if (!userByEmail.equals(userByDni)) {
                throw new RecoverPasswordServiceException("User dni and user email not equals.");
            }
            OneTimeToken token = oneTimeTokenService.generate(new GenerateOneTimeTokenRequest(normEmail));
            emailService.sendRecoverPasswordEmail(normEmail, userDni, token.getTokenValue());
        } catch (MessagingException | IOException | UserServiceException e) {
            throw new RecoverPasswordServiceException("Cannot send token.");
        }
    }

}
