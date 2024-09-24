package es.org.cxn.backapp.controller.entity;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.org.cxn.backapp.exceptions.LichessAuthServiceException;
import es.org.cxn.backapp.service.LichessAuthService;

/**
 * Controller class for handling Lichess OAuth2 authorization. It provides
 * endpoints to handle Lichess authentication and the exchange of authorization
 * codes for access tokens.
 */
@RestController
@RequestMapping("/api/{userEmail}/lichessAuth")
public class LichessAuthController {

    /**
     * The controller logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LichessAuthController.class);
    /**
     * Lichess authentication and authorization service.
     */
    LichessAuthService lichessAuthService;

    /**
     * Main Constructor.
     *
     * @param lichessAuthServ The provided service instance.
     */
    public LichessAuthController(final LichessAuthService lichessAuthServ) {
        lichessAuthService = lichessAuthServ;
    }

    /**
     * Handles the callback from Lichess with the authorization code. This method
     * retrieves the authorization code, exchanges it for an access token, and logs
     * relevant information.
     *
     * @param userEmail         the email of the authenticated user
     * @param authorizationCode the authorization code received from Lichess
     * @param state             the state parameter to prevent CSRF attacks
     * @return token saved.
     * @throws LichessAuthServiceException if an error occurs during the token
     *                                     exchange process
     */
    @GetMapping()
    public ResponseEntity<String> handleLichessCallback(@PathVariable final String userEmail,
            @RequestParam("code") final String authorizationCode, @RequestParam final String state)
            throws LichessAuthServiceException {
        final String clientId = "xadreznaron.es";

        LOGGER.info("Received userEmail: {}", userEmail); // Log para ver el email capturado
        LOGGER.info("Received authorization code: {}", authorizationCode);
        LOGGER.info("Received state: {}", state);

        final String redirectUri = "http://localhost:8080/api/" + userEmail + "/lichessAuth";

        final String codeVerifier = lichessAuthService.getCodeVerifier(userEmail);

        final String accessToken = requestAccessToken(authorizationCode, codeVerifier, redirectUri, clientId,
                userEmail);

        if (accessToken != null) {
            return new ResponseEntity<>(accessToken, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Sends a request to Lichess to exchange the authorization code for an access
     * token. This method handles the construction of the request, parsing the
     * response, and saving the token information.
     *
     * @param code         the authorization code received from Lichess
     * @param codeVerifier the code verifier used in the PKCE flow
     * @param redirectUri  the redirect URI used for the authorization
     * @param clientId     the client ID of the application
     * @param userEmail    the email of the authenticated user
     * @return the access token if the exchange was successful, null otherwise
     * @throws LichessAuthServiceException if an error occurs during the token
     *                                     request or parsing the response
     */
    private String requestAccessToken(final String code, final String codeVerifier, final String redirectUri,
            final String clientId, final String userEmail) throws LichessAuthServiceException {
        final RestTemplate restTemplate = new RestTemplate();
        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("code_verifier", codeVerifier);
        body.add("redirect_uri", redirectUri);
        body.add("client_id", clientId);

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        LOGGER.info("HTTP request to Lichess to retrieve the access token:");
        LOGGER.info("code: {}", code);
        LOGGER.info("code_verifier: {}", codeVerifier);
        LOGGER.info("redirect_uri: {}", redirectUri);
        LOGGER.info("client_id: {}", clientId);

        String accessToken = null;

        try {
            final ResponseEntity<String> response = restTemplate.exchange("https://lichess.org/api/token",
                    HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                final ObjectMapper objectMapper = new ObjectMapper();
                final JsonNode root = objectMapper.readTree(response.getBody());

                accessToken = root.path("access_token").asText();
                final String tokenType = root.path("token_type").asText();
                final int expiresIn = root.path("expires_in").asInt();

                LOGGER.info("Token Type: {}", tokenType);
                LOGGER.info("Access Token: {}", accessToken);
                LOGGER.info("Expires In: {} seconds", expiresIn);

                // Calculate expiration date
                final LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(expiresIn);

                // Save token information
                lichessAuthService.saveAuthToken(tokenType, accessToken, expirationDate, userEmail);
            } else {
                LOGGER.error("Failed to retrieve access token. Status code: {}", response.getStatusCode());
            }
        } catch (IOException e) {
            LOGGER.error("Error parsing the token response", e);
        }

        return accessToken;
    }

    /**
     * Stores the code verifier received during the OAuth2 authorization process.
     * This method verifies the authenticated user and saves the code verifier for
     * later use.
     *
     * @param userEmail    the email of the authenticated user
     * @param codeVerifier the code verifier to be stored
     * @return an HTTP response indicating whether the operation was successful
     */
    @PostMapping()
    public ResponseEntity<HttpStatus> storeCodeVerifier(@PathVariable final String userEmail,
            @RequestBody final String codeVerifier) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String userName = authentication.getName();

        LOGGER.info("RECIVIDO PARA GUARDAR EL CODE VERIFIER: {}", codeVerifier);
        LOGGER.info("Request userEmail: {}", userEmail);
        LOGGER.info("Authenticated user name (email) : {}", userName);

        try {
            lichessAuthService.saveOAuthRequest(userName, codeVerifier.trim());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
