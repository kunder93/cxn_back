package es.org.cxn.backapp.config;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import es.org.cxn.backapp.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles the successful generation of a One-Time Token (OTT) for
 * authentication via a Magic Link. This class sends the generated token to the
 * user's email and redirects to a confirmation page.
 *
 * Implements {@link OneTimeTokenGenerationSuccessHandler} to define custom
 * behavior upon token generation.
 *
 * @author Círculo Xadrez Narón
 * @since 2025
 */
@Component
public class MagicLinkOneTimeTokenGenerationSuccessHandler implements OneTimeTokenGenerationSuccessHandler {

    /**
     * Logger instance for logging events and errors related to One-Time Token
     * generation.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MagicLinkOneTimeTokenGenerationSuccessHandler.class);

    /**
     * Service responsible for sending recovery emails with the generated One-Time
     * Token.
     */
    private final EmailService emailService;

    /**
     * Handler that redirects the user after a One-Time Token has been successfully
     * generated.
     */
    private final OneTimeTokenGenerationSuccessHandler redirectHandler;

    /**
     * Constructs the handler with the required dependencies.
     *
     * @param emailService the service used to send emails
     */
    public MagicLinkOneTimeTokenGenerationSuccessHandler(final EmailService emailService) {
        this.emailService = emailService;
        this.redirectHandler = new RedirectOneTimeTokenGenerationSuccessHandler("/ott/sent");
    }

    /**
     * Handles the token generation success event by sending a magic link to the
     * user and redirecting.
     *
     * @param request      the HTTP request
     * @param response     the HTTP response
     * @param oneTimeToken the generated One-Time Token
     *
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet-related error occurs
     */
    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
            final OneTimeToken oneTimeToken) throws IOException, ServletException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(UrlUtils.buildFullRequestUrl(request))
                .replacePath(request.getContextPath()).replaceQuery(null).fragment(null).path("/login/ott")
                .queryParam("token", oneTimeToken.getTokenValue());

        String magicLink = builder.toUriString();
        String email = oneTimeToken.getUsername();

        try {
            this.emailService.sendRecoverPasswordEmail(email, email, magicLink);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send recovery email to {}: {}", email, e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error("IO error while sending recovery email to {}: {}", email, e.getMessage(), e);
        }

        this.redirectHandler.handle(request, response, oneTimeToken);
    }

}
