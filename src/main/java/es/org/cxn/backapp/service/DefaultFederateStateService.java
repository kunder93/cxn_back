package es.org.cxn.backapp.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.exceptions.FederateStateServiceException;
import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.persistence.ImageExtension;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;
import es.org.cxn.backapp.repository.FederateStateEntityRepository;

/**
 * Service implementation for handling operations related to the federate state
 * of users. Provides methods to federate members, confirm federate
 * cancellation, change auto-renewal status, and manage the storage of DNI
 * images.
 *
 * This class interacts with the FederateStateEntityRepository to persist
 * federate state changes and the UserService to retrieve user details.
 *
 * @see es.org.cxn.backapp.service.FederateStateService
 * @see es.org.cxn.backapp.repository.FederateStateEntityRepository
 * @see es.org.cxn.backapp.service.UserService
 *
 */
@Service
public final class DefaultFederateStateService implements FederateStateService {

    /**
     * The location where DNI images are stored, injected from the application
     * properties.
     */
    @Value("${image.location.dnis}")
    private String imageLocationDnis;

    /**
     * Repository for managing federate state persistence operations. This
     * repository provides methods to interact with the federate state entities
     * stored in the database.
     *
     * @see es.org.cxn.backapp.repository.FederateStateEntityRepository
     */
    private final FederateStateEntityRepository federateStateRepository;

    /**
     * Service for retrieving user details and managing user-related operations.
     * This service is used to fetch user data, including the user's email and DNI,
     * which are essential for federate state operations.
     *
     * @see es.org.cxn.backapp.service.UserService
     */
    private final UserService userService;

    /**
     * Constructs the DefaultFederateStateService with a repository and user
     * service.
     *
     * @param repoFederate The repository for managing federate states.
     * @param userServ     The user service for retrieving user details.
     * @throws NullPointerException if the provided repository or user service is
     *                              null.
     */
    public DefaultFederateStateService(final FederateStateEntityRepository repoFederate, final UserService userServ) {
        super();
        federateStateRepository = checkNotNull(repoFederate, "Received a null pointer as federate state repository");
        userService = checkNotNull(userServ, "Received a null pointer as user service");
    }

    /**
     * Toggles the auto-renewal status of a federate state for a given user.
     *
     * @param userEmail The email of the user whose auto-renewal status is to be
     *                  changed.
     * @return The updated federate state entity.
     * @throws UserServiceException          If the user is not found.
     * @throws FederateStateServiceException If the user is not in a federate state.
     */
    @Override
    public PersistentFederateStateEntity changeAutoRenew(final String userEmail)
            throws UserServiceException, FederateStateServiceException {
        final UserEntity user = userService.findByEmail(userEmail);
        final String userDni = user.getDni();
        final Optional<PersistentFederateStateEntity> federateStateOptional = federateStateRepository.findById(userDni);
        if (federateStateOptional.isEmpty()) {
            throw new FederateStateServiceException("No federate state found for user with dni: " + userDni);
        } else {
            final PersistentFederateStateEntity federateState = federateStateOptional.get();
            final FederateState state = federateState.getState();
            if (state.equals(FederateState.FEDERATE)) {
                federateState.setAutomaticRenewal(!federateState.isAutomaticRenewal());
                return federateStateRepository.save(federateState);
            } else {
                throw new FederateStateServiceException(
                        "Federate state is not : FEDERATE for user with dni: " + userDni);
            }
        }
    }

    /**
     * Confirms the cancellation of a user's federate status. If the federate state
     * is 'IN_PROGRESS', it changes to 'FEDERATE'. If the state is 'FEDERATE', it
     * changes to 'NO_FEDERATE'.
     *
     * @param userDni The DNI of the user whose federate state is to be updated.
     * @return The updated federate state entity.
     * @throws FederateStateServiceException If the state is already 'NO_FEDERATE'
     *                                       or if no federate state is found.
     * @throws UserServiceException          If the user is not found.
     */
    @Override
    public PersistentFederateStateEntity confirmCancelFederate(final String userDni)
            throws FederateStateServiceException, UserServiceException {
        final var federateStateOptional = federateStateRepository.findById(userDni);
        if (federateStateOptional.isEmpty()) {
            throw new FederateStateServiceException("No federate state found for user with dni: " + userDni);
        } else {
            final var federateStateEntity = federateStateOptional.get();
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
     * Federates a member by storing their DNI images and updating their federate
     * state.
     *
     * @param userEmail    The email of the user to federate.
     * @param frontDniFile The front image of the user's DNI.
     * @param backDniFile  The back image of the user's DNI.
     * @param autoRenewal  Whether the user opts for auto-renewal of their federate
     *                     status.
     * @return The updated federate state entity.
     * @throws UserServiceException          If the user is not found or the image
     *                                       extension is invalid.
     * @throws FederateStateServiceException If the federate state is invalid or the
     *                                       images cannot be saved.
     */
    @Override
    public PersistentFederateStateEntity federateMember(final String userEmail, final MultipartFile frontDniFile,
            final MultipartFile backDniFile, final boolean autoRenewal)
            throws UserServiceException, FederateStateServiceException {
        final var user = userService.findByEmail(userEmail);
        final var userDni = user.getDni();
        final var federateStateOptional = federateStateRepository.findById(userDni);

        if (federateStateOptional.isEmpty()) {
            throw new FederateStateServiceException("No federate state found for user with dni: " + userDni);
        }

        final var federateStateEntity = federateStateOptional.get();
        final var baseDirectory = imageLocationDnis;
        // Define user-specific directory based on their DNI using Paths.get()
        final Path userDirectoryPath = Paths.get(baseDirectory, userDni);
        if (!Files.exists(userDirectoryPath)) {
            try {
                Files.createDirectories(userDirectoryPath);
            } catch (IOException e) {
                throw new FederateStateServiceException("Error creating directory: " + e.getMessage(), e);
            }
        }

        if (federateStateEntity.getState() == FederateState.NO_FEDERATE) {
            federateStateEntity.setAutomaticRenewal(autoRenewal);
            federateStateEntity.setDniLastUpdate(LocalDate.now());
            federateStateEntity.setState(FederateState.IN_PROGRESS);

            try {
                final String frontUrl = saveFile(frontDniFile, userDni, "frontal");
                final String backUrl = saveFile(backDniFile, userDni, "trasera");

                federateStateEntity.setDniFrontImageUrl(frontUrl);
                federateStateEntity.setDniBackImageUrl(backUrl);

                return federateStateRepository.save(federateStateEntity);
            } catch (IOException e) {
                throw new FederateStateServiceException("Error saving DNI images: " + e.getMessage(), e);
            }
        } else if (federateStateEntity.getState() == FederateState.FEDERATE) {
            federateStateEntity.setAutomaticRenewal(autoRenewal);
            federateStateEntity.setDniLastUpdate(LocalDate.now());

            try {
                final String frontUrl = saveFile(frontDniFile, userDni, "frontal");
                final String backUrl = saveFile(backDniFile, userDni, "trasera");

                federateStateEntity.setDniFrontImageUrl(frontUrl);
                federateStateEntity.setDniBackImageUrl(backUrl);

                federateStateEntity.setState(FederateState.IN_PROGRESS);

                return federateStateRepository.save(federateStateEntity);
            } catch (IOException e) {
                throw new FederateStateServiceException("Error saving DNI images: " + e.getMessage(), e);
            }
        } else {
            throw new FederateStateServiceException("Bad state. User dni: " + userDni);
        }
    }

    /**
     * Retrieves all federate state entities.
     *
     * @return A list of all federate state entities.
     */
    @Override
    public List<PersistentFederateStateEntity> getAll() {
        return federateStateRepository.findAll();
    }

    /**
     * Retrieves the federate state data for a given user.
     *
     * @param userEmail The email of the user.
     * @return The federate state entity for the user.
     * @throws UserServiceException          If the user is not found.
     * @throws FederateStateServiceException If no federate state is found for the
     *                                       user.
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
     * Saves a DNI image file to the specified location.
     *
     * @param file         The MultipartFile representing the DNI image.
     * @param dni          The DNI of the user.
     * @param kindDniImage A descriptor (e.g., "frontal", "trasera") indicating the
     *                     type of image.
     * @return The URL of the saved file.
     * @throws IOException          If there is an error while saving the file.
     * @throws UserServiceException If the file name or extension is invalid.
     */
    private String saveFile(final MultipartFile file, final String dni, final String kindDniImage)
            throws IOException, UserServiceException {

        final String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new UserServiceException("Invalid file name: " + originalFileName);
        }

        final String[] tokens = originalFileName.split("\\.");
        final String fileExtension = tokens[tokens.length - 1].toLowerCase();

        final ImageExtension imageExtension = ImageExtension.fromString(fileExtension);
        if (imageExtension == null) {
            throw new UserServiceException("Invalid image extension: " + fileExtension);
        }

        // Create the directory using Paths and File.separator
        final Path userDirectoryPath = Paths.get(imageLocationDnis, dni);
        if (!Files.exists(userDirectoryPath)) {
            Files.createDirectories(userDirectoryPath);
        }

        final String sanitizedFileName = kindDniImage + "." + fileExtension;

        final Path filePath = userDirectoryPath.resolve(sanitizedFileName);

        final File dest = filePath.toFile();
        file.transferTo(dest);

        return filePath.toString();
    }

    /**
     * Updates the DNI images for a federated user.
     *
     * @param userEmail    The email of the user.
     * @param frontDniFile The new front image of the user's DNI.
     * @param backDniFile  The new back image of the user's DNI.
     * @return The updated federate state entity.
     * @throws UserServiceException          If the user is not found or the image
     *                                       extension is invalid.
     * @throws FederateStateServiceException If the user is not in a federate state
     *                                       or the images cannot be updated.
     */
    @Override
    public PersistentFederateStateEntity updateDni(final String userEmail, final MultipartFile frontDniFile,
            final MultipartFile backDniFile) throws UserServiceException, FederateStateServiceException {
        final var userEntity = userService.findByEmail(userEmail);
        final var userDni = userEntity.getDni();

        final var federateStateOptional = federateStateRepository.findById(userDni);

        if (federateStateOptional.isPresent()) {
            final var federateStateEntity = federateStateOptional.get();
            final var state = federateStateEntity.getState();
            if (state.equals(FederateState.FEDERATE)) {
                federateStateEntity.setDniLastUpdate(LocalDate.now());
                try {
                    final String frontUrl = saveFile(frontDniFile, userDni, "frontal");
                    final String backUrl = saveFile(backDniFile, userDni, "trasera");

                    federateStateEntity.setDniFrontImageUrl(frontUrl);
                    federateStateEntity.setDniBackImageUrl(backUrl);

                    return federateStateRepository.save(federateStateEntity);
                } catch (IOException e) {
                    throw new FederateStateServiceException("Error updating DNI images: " + e.getMessage(), e);
                }
            } else {
                throw new FederateStateServiceException("User is not in a federate state.");
            }
        } else {
            throw new FederateStateServiceException("No federate state found for user with dni: " + userDni);
        }
    }

}
