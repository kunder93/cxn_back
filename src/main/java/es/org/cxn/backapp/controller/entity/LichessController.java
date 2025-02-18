package es.org.cxn.backapp.controller.entity;

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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.org.cxn.backapp.model.form.responses.LichessProfileListResponse;
import es.org.cxn.backapp.model.form.responses.LichessProfileResponse;
import es.org.cxn.backapp.service.LichessService;
import es.org.cxn.backapp.service.dto.LichessProfileDto;
import es.org.cxn.backapp.service.dto.LichessSaveProfileDto;
import es.org.cxn.backapp.service.dto.LichessSaveProfileDto.SaveGameStatistics;
import es.org.cxn.backapp.service.exceptions.LichessServiceException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Controller class for handling Lichess OAuth2 authorization. It provides
 * endpoints to handle Lichess authentication and the exchange of authorization
 * codes for access tokens.
 */
@RestController
@RequestMapping("/api")
public class LichessController {

    /**
     * The controller logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LichessController.class);

    /**
     * Minimum size for the code verifier as defined by RFC 7636. The code verifier
     * must be at least 43 characters long.
     */
    private static final int CODE_VERIFIER_MIN_SIZE = 43;

    /**
     * Maximum size for the code verifier as defined by RFC 7636. The code verifier
     * must not exceed 128 characters in length.
     */
    private static final int CODE_VERIFIER_MAX_SIZE = 128;

    /**
     * Maximum length for auth code.
     */
    private static final int MAX_LENGTH_AUTH_CODE = 43;

    /**
     * Maximum length for state code.
     */
    private static final int MAX_LENGTH_STATE = 128;

    /**
     * Holds the environment configuration of the application.
     *
     * <p>
     * This field allows access to the current environment properties and profiles,
     * enabling the application to differentiate between environments such as
     * development, staging, and production.
     * </p>
     *
     * <p>
     * It is useful for checking active profiles, loading environment-specific
     * properties, and configuring beans or services based on the environment
     * settings.
     * </p>
     */
    private final Environment environment;

    /**
     * Lichess authentication and authorization service.
     */
    private final LichessService lichessService;

    /**
     * Main Constructor.
     *
     * @param lichessServ The provided service instance.
     * @param env         The Spring environment.
     */
    public LichessController(final LichessService lichessServ, final Environment env) {
        lichessService = lichessServ;
        environment = env;
    }

    /**
     * Converts a Lichess profile Data Transfer Object (DTO) from the service layer
     * to a controller response format. This method encapsulates the transformation
     * of the service DTO into a LichessProfileResponse record, including the user's
     * name, Lichess username, last update time, and game statistics for various
     * game types.
     *
     * @param serviceDto The LichessProfileDto object containing the user's profile
     *                   information and game statistics from the service layer.
     * @return A LichessProfileResponse object representing the user's profile,
     *         including their Lichess username and game statistics for Blitz,
     *         Bullet, Rapid, Classical, and Puzzle game types.
     */
    private LichessProfileResponse fromLichessProfileServiceDtoToControllerResponse(
            final LichessProfileDto serviceDto) {
        return new LichessProfileResponse(serviceDto.completeUserName(), // nombre completo

                serviceDto.username(), // nombre de usuario en Lichess
                serviceDto.updatedAt(), // última actualización
                new LichessProfileResponse.Game(serviceDto.blitz().rating(), // Elo Blitz
                        serviceDto.blitz().games(), // total de juegos Blitz
                        serviceDto.blitz().prov() // si el Elo es provisional
                ), new LichessProfileResponse.Game(serviceDto.bullet().rating(), // Elo Bullet
                        serviceDto.bullet().games(), // total de juegos Bullet
                        serviceDto.bullet().prov() // si el Elo es provisional
                ), new LichessProfileResponse.Game(serviceDto.rapid().rating(), // Elo Rapid
                        serviceDto.rapid().games(), // total de juegos Rapid
                        serviceDto.rapid().prov() // si el Elo es provisional
                ), new LichessProfileResponse.Game(serviceDto.classical().rating(), // Elo Classical
                        serviceDto.classical().games(), // total de juegos Classical
                        serviceDto.classical().prov() // si el Elo es provisional
                ), new LichessProfileResponse.Game(serviceDto.puzzle().rating(), // Elo Puzzle
                        serviceDto.puzzle().games(), // total de juegos Puzzle
                        serviceDto.puzzle().prov() // si el Elo es provisional
                ));
    }

    /**
     * Converts a list of {@link LichessProfileDto} objects to a
     * {@link LichessProfileListResponse}.
     *
     * This method maps each {@link LichessProfileDto} to a
     * {@link LichessProfileResponse}, encapsulating user details and their
     * corresponding game statistics. It transforms the necessary fields from the
     * DTOs to the response format used by the controller.
     *
     * @param profilesListDto A list of {@link LichessProfileDto} objects
     *                        representing the Lichess profiles to be converted.
     * @return A {@link LichessProfileListResponse} containing a list of
     *         {@link LichessProfileResponse} objects derived from the input DTOs.
     */
    private LichessProfileListResponse fromServiceDtoToControllerResponseList(
            final List<LichessProfileDto> profilesListDto) {
        final var responsesList = profilesListDto.stream().map(dto -> new LichessProfileResponse(dto.completeUserName(),
                dto.identifier(), dto.updatedAt(),
                new LichessProfileResponse.Game(dto.blitz().rating(), dto.blitz().games(), dto.blitz().prov()),
                new LichessProfileResponse.Game(dto.bullet().rating(), dto.bullet().games(), dto.bullet().prov()),
                new LichessProfileResponse.Game(dto.rapid().rating(), dto.rapid().games(), dto.rapid().prov()),
                new LichessProfileResponse.Game(dto.classical().rating(), dto.classical().games(),
                        dto.classical().prov()),
                new LichessProfileResponse.Game(dto.puzzle().rating(), dto.puzzle().games(), dto.puzzle().prov())))
                .toList();
        return new LichessProfileListResponse(responsesList);
    }

    private LichessSaveProfileDto getLichessProfileAsDto(final String accessToken, final String userDni)
            throws LichessServiceException {
        final String lichessApiUrl = "https://lichess.org/api/account";

        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        final HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            final ResponseEntity<String> response = restTemplate.exchange(lichessApiUrl, HttpMethod.GET, entity,
                    String.class);
            final var statusCode = response.getStatusCode();
            if (statusCode.equals(HttpStatus.OK)) {
                // Convertir la respuesta en JSON a un objeto de Java
                final ObjectMapper objectMapper = new ObjectMapper();
                final JsonNode rootNode = objectMapper.readTree(response.getBody());

                // Extraer y mapear los campos necesarios al DTO
                final String identifier = rootNode.path("id").asText();
                final String username = rootNode.path("username").asText();
                // Obtener estadísticas de diferentes modalidades
                final SaveGameStatistics blitz = mapSaveGameStatistics(rootNode.path("perfs").path("blitz"));
                final SaveGameStatistics bullet = mapSaveGameStatistics(rootNode.path("perfs").path("bullet"));
                final SaveGameStatistics classical = mapSaveGameStatistics(rootNode.path("perfs").path("classical"));
                final SaveGameStatistics rapid = mapSaveGameStatistics(rootNode.path("perfs").path("rapid"));
                final SaveGameStatistics puzzle = mapSaveGameStatistics(rootNode.path("perfs").path("puzzle"));
                // Crear el DTO con la información mapeada
                return new LichessSaveProfileDto(userDni, identifier, username, LocalDateTime.now(), blitz, bullet,
                        classical, rapid, puzzle);
            } else {
                throw new LichessServiceException("Error al obtener el perfil de Lichess.");
            }
        } catch (RestClientException | JsonProcessingException e) {
            throw new LichessServiceException("Error en la llamada a la API de Lichess.", e);
        }
    }

    /**
     * Controller for get all lichess profiles game info.
     *
     * @return Dto with all lichess stored profiles.
     */
    @GetMapping("/getAllLichessProfiles")
    public ResponseEntity<LichessProfileListResponse> getLichessProfiles() {
        final var profilesListDto = lichessService.getLichessProfiles();
        // Create the response object
        final LichessProfileListResponse response = fromServiceDtoToControllerResponseList(profilesListDto);
        // Return the response entity with OK status and the response body
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Retrieves the authenticated user's Lichess profile. This endpoint fetches the
     * user's profile information, including their username and game statistics. If
     * the user is not found or an error occurs during the retrieval process, a Bad
     * Request response is returned.
     *
     * @return A ResponseEntity containing the LichessProfileResponse object with
     *         the user's profile information and an HTTP status of 200 OK if
     *         successful, or a 400 Bad Request if the user is not found or an error
     *         occurs.
     */
    @GetMapping("/getMyLichessProfile")
    public ResponseEntity<LichessProfileResponse> getMyLichessProfile() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String username = authentication.getName();
        try {
            // Directly assign the result of getLichessProfile to lichessProfile
            final LichessProfileDto lichessProfile = lichessService.getLichessProfile(username);
            final LichessProfileResponse response = fromLichessProfileServiceDtoToControllerResponse(lichessProfile);
            return ResponseEntity.ok(response);
        } catch (LichessServiceException e) { // When user with given email not found.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Handles the callback from Lichess with the authorization code. This method
     * retrieves the authorization code, exchanges it for an access token, and logs
     * relevant information. It retrieves the user's Lichess profile and saves it to
     * the database. If successful, it returns an HTML response indicating success;
     * otherwise, it returns an HTML response indicating an error.
     *
     * <p>
     * In case of success, the user is notified that their Lichess profile has been
     * linked successfully, and they are instructed to log out and back into the
     * application to see the changes reflected.
     * </p>
     * <p>
     * If the operation fails, the user is informed of the error and provided with
     * contact information for support.
     * </p>
     *
     * @param userEmail         the email of the authenticated user, must be a valid
     *                          email
     * @param authorizationCode the authorization code received from Lichess, must
     *                          not be blank and has a maximum length of 43
     *                          characters
     * @param state             the state parameter to prevent CSRF attacks, with a
     *                          maximum length of 128 characters
     * @return A ResponseEntity containing an HTML response: - On success: an HTML
     *         page notifying the user that the linking was successful. - On error:
     *         an HTML page notifying the user of the error and providing contact
     *         information.
     * @throws LichessServiceException if an error occurs during the token exchange
     *                                 process or while saving the profile.
     */
    @GetMapping("/{userEmail}/lichessAuth")
    public ResponseEntity<String> handleLichessCallback(@PathVariable
    @Email
    @NotBlank final String userEmail,
            @RequestParam("code")
            @NotBlank
            @Size(max = MAX_LENGTH_AUTH_CODE,
                    message = "MAX SIZE OF AUTH_CODE NOT VALID") final String authorizationCode,
            @RequestParam
            @Size(max = MAX_LENGTH_STATE, message = "MAX SIZE OF STATE NOT VALID") final String state)
            throws LichessServiceException {

        final ResponseEntity<String> responseEntity;

        final String clientId = "xadreznaron.es";
        final String redirectUri = isProdProfile() ? "https://xadreznaron.es:4443/api/" + userEmail + "/lichessAuth"
                : "http://localhost:8080/api/" + userEmail + "/lichessAuth";
        final String codeVerifier = lichessService.getCodeVerifier(userEmail);
        final String accessToken = requestAccessToken(authorizationCode, codeVerifier, redirectUri, clientId,
                userEmail);

        final LichessSaveProfileDto dto = getLichessProfileAsDto(accessToken, userEmail);
        lichessService.saveLichessProfile(dto);

        try {
            if (accessToken != null) {
                final String htmlResponseSuccess = loadResponseTemplateSuccess();
                responseEntity = ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(htmlResponseSuccess);
            } else {
                final String htmlResponseError = loadResponseTemplateError();
                responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_HTML)
                        .body(htmlResponseError);
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

        return responseEntity;
    }

    /**
     * @return Boolean value if environment matches with production profile.
     */
    private boolean isProdProfile() {
        return environment.matchesProfiles("prod");
    }

    /**
     * Utility method to check if the response was successful.
     *
     * @param response the HTTP response
     * @return true if the response status code is in the 2xx range, false otherwise
     */
    private boolean isResponseSuccessful(final ResponseEntity<String> response) {
        final var statusCode = response.getStatusCode();
        return statusCode.is2xxSuccessful();
    }

    /**
     * Load response template for error case from file.
     *
     * @return The response template html for error case.
     * @throws IOException When cannot load the file.
     */
    private String loadResponseTemplateError() throws IOException {
        final ClassPathResource resource = new ClassPathResource("LichessResponseOauthTemplates/error.html");
        // Use InputStream instead of Files.lines to handle resources inside the JAR
        try (var inputStream = resource.getInputStream();
                var reader = new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8);
                var bufferedReader = new java.io.BufferedReader(reader)) {

            return bufferedReader.lines().collect(Collectors.joining("\n"));

        }
    }

    /**
     * Load response template for success case from file.
     *
     * @return The response template html for success case.
     * @throws IOException When cannot load the file.
     */
    private String loadResponseTemplateSuccess() throws IOException {
        final ClassPathResource resource = new ClassPathResource("LichessResponseOauthTemplates/success.html");
        // Use InputStream instead of Files.lines to handle resources inside the JAR
        try (var inputStream = resource.getInputStream();
                var reader = new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8);
                var bufferedReader = new java.io.BufferedReader(reader)) {

            return bufferedReader.lines().collect(Collectors.joining("\n"));

        }
    }

    /**
     * Logs an error if the token retrieval failed.
     *
     * @param response the HTTP response
     */
    private void logError(final ResponseEntity<String> response) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Failed to retrieve access token. Status code: {}", response.getStatusCode());
        }
    }

    /**
     * Logs an exception if an error occurs during parsing.
     *
     * @param exception The IO Exception.
     */
    private void logException(final IOException exception) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Error parsing the token response", exception);
        }
    }

    /**
     * Maps the statistics of a game mode to a GameStatistics object.
     *
     * @param node The JSON node containing the statistics of a game mode
     * @return A GameStatistics object with the mapped data
     */
    private SaveGameStatistics mapSaveGameStatistics(final JsonNode node) {
        final int gameAmount = node.path("games").asInt();
        final int gameRating = node.path("rating").asInt();
        final int gameRd = node.path("rd").asInt();
        final int prog = node.path("prog").asInt();
        final boolean prov = node.path("prov").asBoolean();

        return new SaveGameStatistics(gameAmount, gameRating, gameRd, prog, prov);
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
     * @throws LichessServiceException if an error occurs during the token request
     *                                 or parsing the response
     */
    private String requestAccessToken(final String code, final String codeVerifier, final String redirectUri,
            final String clientId, final String userEmail) throws LichessServiceException {

        final RestTemplate restTemplate = new RestTemplate();

        // Use MultiValueMap interface for body
        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("code_verifier", codeVerifier);
        body.add("redirect_uri", redirectUri);
        body.add("client_id", clientId);

        // Use MultiValueMap for headers
        final MultiValueMap<String, String> headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        String accessToken = null;

        try {
            final ResponseEntity<String> response = restTemplate.exchange("https://lichess.org/api/token",
                    HttpMethod.POST, requestEntity, String.class);

            // Use the encapsulated method to check the status
            if (isResponseSuccessful(response)) {
                final ObjectMapper objectMapper = new ObjectMapper();
                final JsonNode root = objectMapper.readTree(response.getBody());

                accessToken = root.path("access_token").asText();
                final String tokenType = root.path("token_type").asText();
                final int expiresIn = root.path("expires_in").asInt();

                // Calculate expiration date
                final LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(expiresIn);

                // Save token information
                lichessService.saveAuthToken(tokenType, accessToken, expirationDate, userEmail);
            } else {
                logError(response);
            }
        } catch (IOException e) {
            logException(e);
        }

        return accessToken;
    }

    /**
     * Stores the code verifier received during the OAuth2 authorization process.
     * This method verifies the authenticated user and saves the code verifier for
     * later use.
     *
     * @param userEmail    The email of authenticated user
     * @param codeVerifier The code verifier to be stored
     * @return an HTTP response indicating whether the operation was successful
     */
    @PostMapping("/{userEmail}/lichessAuth")
    public ResponseEntity<HttpStatus> storeCodeVerifier(@PathVariable
    @Email
    @NotBlank final String userEmail,
            @RequestBody
            @NotBlank
            @Size(min = CODE_VERIFIER_MIN_SIZE, max = CODE_VERIFIER_MAX_SIZE) final String codeVerifier) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String userName = authentication.getName();
        try {
            lichessService.saveOAuthRequest(userName, codeVerifier.trim());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (LichessServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Updates the Lichess profile of the authenticated user by retrieving profile
     * data from Lichess API and saving it in the application.
     *
     * <p>
     * This endpoint requires the user to be authenticated. It retrieves the user's
     * email from the security context, fetches an access token, and then uses that
     * token to get the user's profile data from Lichess. The profile data is then
     * saved, and the updated profile information is returned in the response.
     * </p>
     *
     * @return {@link ResponseEntity} containing the updated Lichess profile data in
     *         the form of a {@link LichessProfileResponse}
     * @throws ResponseStatusException if the token is expired or any other error
     *                                 occurs while retrieving or saving the profile
     *                                 data
     */
    @PostMapping("/updateLichessProfile")
    public ResponseEntity<LichessProfileResponse> updateLichessProfile() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String userEmail = authentication.getName();

        final String accessToken;
        try {
            accessToken = lichessService.getAuthToken(userEmail);
        } catch (LichessServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        try {
            final LichessSaveProfileDto dto = getLichessProfileAsDto(accessToken, userEmail);
            lichessService.saveLichessProfile(dto);
            final LichessProfileDto lichessProfile = lichessService.getLichessProfile(userEmail);
            final LichessProfileResponse response = fromLichessProfileServiceDtoToControllerResponse(lichessProfile);
            return ResponseEntity.ok(response);
        } catch (LichessServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
