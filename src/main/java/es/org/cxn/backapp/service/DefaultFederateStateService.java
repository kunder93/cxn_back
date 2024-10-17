/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.exceptions.FederateStateServiceException;
import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.persistence.ImageExtension;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;
import es.org.cxn.backapp.repository.FederateStateEntityRepository;

/**
 * Default implementation of the {@link FederateStateService} interface.
 *
 * <p>
 * This service handles operations related to federate state management,
 * including confirming federate status, federating members, and retrieving
 * federate state data.
 * </p>
 *
 * <p>
 * This class is marked as a Spring {@code @Service}, making it a candidate for
 * component scanning and dependency injection.
 * </p>
 *
 * <p>
 * All methods in this class may throw exceptions related to federate state
 * operations, which should be handled appropriately by the caller.
 * </p>
 *
 * @author Santiago Paz
 */
@Service
public final class DefaultFederateStateService implements FederateStateService {

    /**
     * Repository for managing federate state entities.
     * <p>
     * This field is used to perform CRUD operations on federate state data stored
     * in the database. It is injected through the constructor and is required for
     * the service's operations.
     * </p>
     */
    private final FederateStateEntityRepository federateStateRepository;

    /**
     * Service for managing user-related operations.
     * <p>
     * This field is responsible for retrieving user data and performing actions
     * related to user management. It is injected through the constructor and is
     * necessary for obtaining user information required for federate state
     * operations.
     * </p>
     */
    private final UserService userService;

    /**
     * Constructs a new instance of {@link DefaultFederateStateService}.
     *
     * <p>
     * This constructor initializes the federate state repository and user service.
     * It performs null checks on the provided parameters to ensure that valid
     * references are provided, throwing an IllegalArgumentException if any
     * parameter is null.
     * </p>
     *
     * @param repoFederate the repository used for accessing federate state entities
     * @param userServ     the service used for user-related operations
     * @throws IllegalArgumentException if either {@code repoFederate} or
     *                                  {@code userServ} is {@code null}
     */
    public DefaultFederateStateService(final FederateStateEntityRepository repoFederate, final UserService userServ) {
        super();
        federateStateRepository = checkNotNull(repoFederate, "Received a null pointer as federate state repository");
        userService = checkNotNull(userServ, "Received a null pointer as user service");
    }

    @Override
    public PersistentFederateStateEntity changeAutoRenew(String userEmail)
            throws UserServiceException, FederateStateServiceException {
        final var user = userService.findByEmail(userEmail);
        final var userDni = user.getDni();
        var federateStateOptional = federateStateRepository.findById(userDni);
        if (federateStateOptional.isEmpty()) {
            throw new FederateStateServiceException("No federate state found for user with dni: " + userDni);
        } else {
            var federateState = federateStateOptional.get();
            if (federateState.getState() != FederateState.FEDERATE) {
                throw new FederateStateServiceException(
                        "Federate state is not : FEDERATE for user with dni: " + userDni);
            } else {
                federateState.setAutomaticRenewal(!federateState.isAutomaticRenewal());
                return federateStateRepository.save(federateState);
            }
        }
    }

    /**
     * Confirms or cancel the federate status for the user identified by the given
     * email.
     *
     * @param userDni the dni of the user
     * @return the updated {@link PersistentFederateStateEntity} for the user
     * @throws FederateStateServiceException if the federate state cannot be
     *                                       confirmed
     * @throws UserServiceException          if there is an issue retrieving the
     *                                       user
     */
    @Override
    public PersistentFederateStateEntity confirmCancelFederate(final String userDni)
            throws FederateStateServiceException, UserServiceException {
        final var federateStateOptional = federateStateRepository.findById(userDni);
        if (federateStateOptional.isEmpty()) {
            throw new FederateStateServiceException("No federate state found for user with dni: " + userDni);
        } else {
            var federateStateEntity = federateStateOptional.get();
            final var entityState = federateStateEntity.getState();
            if (entityState == FederateState.IN_PROGRESS) {
                federateStateEntity.setState(FederateState.FEDERATE);
            }
            if (entityState == FederateState.FEDERATE) {
                federateStateEntity.setState(FederateState.NO_FEDERATE);
            }
            if (entityState == FederateState.NO_FEDERATE) {
                throw new FederateStateServiceException("Cannot change NO FEDERATE status.");
            }

            return federateStateRepository.save(federateStateEntity);

        }
    }

    /**
     * Federates the member identified by the given email and saves the provided DNI
     * images.
     *
     * @param userEmail    the email of the user
     * @param frontDniFile the front side of the DNI image
     * @param backDniFile  the back side of the DNI image
     * @param autoRenewal  indicates if the federate state should be set to
     *                     auto-renew
     * @return the updated {@link PersistentFederateStateEntity} for the user
     * @throws IOException                   if there is an issue with file
     *                                       operations
     * @throws UserServiceException          if there is an issue retrieving the
     *                                       user
     * @throws FederateStateServiceException if the federate state cannot be updated
     */
    @Override
    public PersistentFederateStateEntity federateMember(final String userEmail, final MultipartFile frontDniFile,
            final MultipartFile backDniFile, final boolean autoRenewal)
            throws IOException, UserServiceException, FederateStateServiceException {

        // Retrieve the user by email
        final var user = userService.findByEmail(userEmail);
        final var userDni = user.getDni();

        // Retrieve the federate state for the user by their DNI
        final var federateStateOptional = federateStateRepository.findById(userDni);

        // Base directory to store DNI files
        final var baseDirectory = "C:\\Users\\Santi\\Desktop\\AlmacenDnis\\";

        if (federateStateOptional.isEmpty()) {
            throw new FederateStateServiceException("No federate state found for user with dni: " + userDni);
        }

        var federateStateEntity = federateStateOptional.get();

        // Define user-specific directory based on their DNI
        String userDirectory = baseDirectory + userDni + "\\";

        /**
         * Federate status NO_FEDERATE, update DNI date, save new DNI files and change
         * state to IN_PROGRESS.
         */
        if (federateStateEntity.getState() == FederateState.NO_FEDERATE) {
            federateStateEntity.setAutomaticRenewal(autoRenewal);
            federateStateEntity.setDniLastUpdate(LocalDate.now());
            federateStateEntity.setState(FederateState.IN_PROGRESS);

            try {
                // Save the front and back DNI images with new filenames
                String frontUrl = saveFile(frontDniFile, userDni, "frontal");
                String backUrl = saveFile(backDniFile, userDni, "trasera");

                // Update the federate entity with the new file paths
                federateStateEntity.setDniFrontImageUrl(frontUrl);
                federateStateEntity.setDniBackImageUrl(backUrl);

                return federateStateRepository.save(federateStateEntity);
            } catch (Exception e) {
                throw new FederateStateServiceException("Error saving DNI images: " + e.getMessage());
            }
        } else if (federateStateEntity.getState() == FederateState.FEDERATE) {
            // If already federated, just update the files and state
            federateStateEntity.setAutomaticRenewal(autoRenewal);
            federateStateEntity.setDniLastUpdate(LocalDate.now());

            try {
                // Save the front and back DNI images with new filenames
                String frontUrl = saveFile(frontDniFile, userDni, "frontal");
                String backUrl = saveFile(backDniFile, userDni, "trasera");

                // Update the federate entity with the new file paths
                federateStateEntity.setDniFrontImageUrl(frontUrl);
                federateStateEntity.setDniBackImageUrl(backUrl);

                federateStateEntity.setState(FederateState.IN_PROGRESS);

                return federateStateRepository.save(federateStateEntity);
            } catch (Exception e) {
                throw new FederateStateServiceException("Error saving DNI images: " + e.getMessage());
            }
        } else {
            throw new FederateStateServiceException("Bad state. User dni: " + userDni);
        }
    }

    /**
     * Retrieves all federate state entities.
     *
     * @return a list of all {@link PersistentFederateStateEntity}
     */
    @Override
    public List<PersistentFederateStateEntity> getAll() {
        return federateStateRepository.findAll();
    }

    /**
     * Retrieves the federate data for the user identified by the given email.
     *
     * @param userEmail the email of the user
     * @return the {@link PersistentFederateStateEntity} for the user
     * @throws FederateStateServiceException if the federate state cannot be
     *                                       retrieved
     * @throws UserServiceException          if there is an issue retrieving the
     *                                       user
     */
    @Override
    public PersistentFederateStateEntity getFederateData(final String userEmail)
            throws UserServiceException, FederateStateServiceException {
        final var userEntity = userService.findByEmail(userEmail);
        final var userDni = userEntity.getDni();

        final var federateStateOptional = federateStateRepository.findById(userDni);

        if (federateStateOptional.isPresent()) {
            return federateStateOptional.get();
        } else {
            throw new FederateStateServiceException("No federate state found for user with dni: " + userDni);
        }
    }

    /**
     * Saves the uploaded file to the specified location.
     *
     * @param file         the file to save
     * @param userDni      the DNI of the user (used to create directory)
     * @param kindDniImage the type of image (e.g., "frontal", "trasera")
     * @return the file path where the file is saved
     * @throws IOException          if an I/O error occurs
     * @throws UserServiceException when user not found.
     */
    private String saveFile(final MultipartFile file, final String dni, final String kindDniImage)
            throws IOException, UserServiceException {

        // Get original file name and ensure it has an extension
        final String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new UserServiceException("Invalid file name: " + originalFileName);
        }

        // Split the filename to get the extension
        String[] tokens = originalFileName.split("\\.");
        String fileExtension = tokens[tokens.length - 1].toLowerCase(); // Use lowercase for consistency

        // Validate the file extension (you can implement this method based on your
        // needs)
        ImageExtension imageExtension = ImageExtension.fromString(fileExtension);
        if (imageExtension == null) {
            throw new UserServiceException("Invalid image extension: " + fileExtension);
        }

        // Define the base directory path where images will be saved
        String baseDirectory = "C:\\Users\\Santi\\Desktop\\AlmacenDnis";

        // Create user-specific directory using the DNI as the folder name
        Path userDirectoryPath = Paths.get(baseDirectory, dni);
        if (!Files.exists(userDirectoryPath)) {
            Files.createDirectories(userDirectoryPath);
        }

        // Create file name with format "frontal.extension" or "trasera.extension"
        String sanitizedFileName = kindDniImage + "." + fileExtension;

        // Full path to store the image
        Path filePath = userDirectoryPath.resolve(sanitizedFileName);

        // Save the file to the file system
        File dest = filePath.toFile();
        file.transferTo(dest);

        // Log the file path for debugging (useful for Windows file paths)
        System.out.println("Saving file to: " + filePath.toString());

        // Return the absolute file path
        return filePath.toString();
    }

    @Override
    public PersistentFederateStateEntity updateDni(String userEmail, MultipartFile frontDniFile,
            MultipartFile backDniFile) throws UserServiceException, FederateStateServiceException {
        final var userEntity = userService.findByEmail(userEmail);
        final var userDni = userEntity.getDni();

        final var federateStateOptional = federateStateRepository.findById(userDni);

        if (federateStateOptional.isPresent()) {
            var federateStateEntity = federateStateOptional.get();
            if (federateStateEntity.getState() != FederateState.FEDERATE) {
                throw new FederateStateServiceException(
                        "Federate state found for user with dni: " + userDni + "is not FEDERATE.");
            }
            // Base directory to store DNI files
            final var baseDirectory = "C:\\Users\\Santi\\Desktop\\AlmacenDnis\\";
            // Define user-specific directory based on their DNI
            String userDirectory = baseDirectory + userDni + "\\";

            federateStateEntity.setDniLastUpdate(LocalDate.now());
            // Save the front and back DNI images with new filenames
            try {
                String frontUrl = saveFile(frontDniFile, userDni, "frontal");
                String backUrl = saveFile(backDniFile, userDni, "trasera");

                // Update the federate entity with the new file paths
                federateStateEntity.setDniFrontImageUrl(frontUrl);
                federateStateEntity.setDniBackImageUrl(backUrl);
            } catch (IOException e) {
                throw new FederateStateServiceException("Cannot save dni files for user with dni: " + userDni);
            }
            return federateStateRepository.save(federateStateEntity);
        } else {
            throw new FederateStateServiceException("No federate state found for user with dni: " + userDni);
        }
    }

}
