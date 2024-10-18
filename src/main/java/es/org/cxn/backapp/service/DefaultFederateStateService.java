package es.org.cxn.backapp.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.exceptions.FederateStateServiceException;
import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.persistence.ImageExtension;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;
import es.org.cxn.backapp.repository.FederateStateEntityRepository;

@Service
public final class DefaultFederateStateService implements FederateStateService {

    @Value("${image.location.dnis}")
    private String imageLocationDnis;

    private final FederateStateEntityRepository federateStateRepository;
    private final UserService userService;

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

    @Override
    public PersistentFederateStateEntity federateMember(final String userEmail, final MultipartFile frontDniFile,
            final MultipartFile backDniFile, final boolean autoRenewal)
            throws IOException, UserServiceException, FederateStateServiceException {

        final var user = userService.findByEmail(userEmail);
        final var userDni = user.getDni();
        final var federateStateOptional = federateStateRepository.findById(userDni);

        final var baseDirectory = imageLocationDnis;

        if (federateStateOptional.isEmpty()) {
            throw new FederateStateServiceException("No federate state found for user with dni: " + userDni);
        }

        var federateStateEntity = federateStateOptional.get();

        // Define user-specific directory based on their DNI using Paths.get()
        Path userDirectoryPath = Paths.get(baseDirectory, userDni);
        if (!Files.exists(userDirectoryPath)) {
            Files.createDirectories(userDirectoryPath);
        }

        if (federateStateEntity.getState() == FederateState.NO_FEDERATE) {
            federateStateEntity.setAutomaticRenewal(autoRenewal);
            federateStateEntity.setDniLastUpdate(LocalDate.now());
            federateStateEntity.setState(FederateState.IN_PROGRESS);

            try {
                String frontUrl = saveFile(frontDniFile, userDni, "frontal");
                String backUrl = saveFile(backDniFile, userDni, "trasera");

                federateStateEntity.setDniFrontImageUrl(frontUrl);
                federateStateEntity.setDniBackImageUrl(backUrl);

                return federateStateRepository.save(federateStateEntity);
            } catch (Exception e) {
                throw new FederateStateServiceException("Error saving DNI images: " + e.getMessage());
            }
        } else if (federateStateEntity.getState() == FederateState.FEDERATE) {
            federateStateEntity.setAutomaticRenewal(autoRenewal);
            federateStateEntity.setDniLastUpdate(LocalDate.now());

            try {
                String frontUrl = saveFile(frontDniFile, userDni, "frontal");
                String backUrl = saveFile(backDniFile, userDni, "trasera");

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

    @Override
    public List<PersistentFederateStateEntity> getAll() {
        return federateStateRepository.findAll();
    }

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

    private String saveFile(final MultipartFile file, final String dni, final String kindDniImage)
            throws IOException, UserServiceException {

        final String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new UserServiceException("Invalid file name: " + originalFileName);
        }

        String[] tokens = originalFileName.split("\\.");
        String fileExtension = tokens[tokens.length - 1].toLowerCase();

        ImageExtension imageExtension = ImageExtension.fromString(fileExtension);
        if (imageExtension == null) {
            throw new UserServiceException("Invalid image extension: " + fileExtension);
        }

        // Create the directory using Paths and File.separator
        Path userDirectoryPath = Paths.get(imageLocationDnis, dni);
        if (!Files.exists(userDirectoryPath)) {
            Files.createDirectories(userDirectoryPath);
        }

        String sanitizedFileName = kindDniImage + "." + fileExtension;

        Path filePath = userDirectoryPath.resolve(sanitizedFileName);

        File dest = filePath.toFile();
        file.transferTo(dest);

        System.out.println("Saving file to: " + filePath.toString());

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
                throw new FederateStateServiceException("User is not in a federate state.");
            } else {
                federateStateEntity.setDniLastUpdate(LocalDate.now());
                try {
                    String frontUrl = saveFile(frontDniFile, userDni, "frontal");
                    String backUrl = saveFile(backDniFile, userDni, "trasera");

                    federateStateEntity.setDniFrontImageUrl(frontUrl);
                    federateStateEntity.setDniBackImageUrl(backUrl);

                    return federateStateRepository.save(federateStateEntity);
                } catch (Exception e) {
                    throw new FederateStateServiceException("Error updating DNI images: " + e.getMessage());
                }
            }
        } else {
            throw new FederateStateServiceException("No federate state found for user with dni: " + userDni);
        }
    }
}
