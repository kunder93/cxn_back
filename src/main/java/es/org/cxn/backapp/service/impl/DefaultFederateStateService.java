package es.org.cxn.backapp.service.impl;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;
import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;
import es.org.cxn.backapp.repository.FederateStateEntityRepository;
import es.org.cxn.backapp.service.FederateStateService;
import es.org.cxn.backapp.service.ImageStorageService;
import es.org.cxn.backapp.service.PaymentsService;
import es.org.cxn.backapp.service.UserService;
import es.org.cxn.backapp.service.dto.UserDniImagesDto;
import es.org.cxn.backapp.service.exceptions.FederateStateServiceException;
import es.org.cxn.backapp.service.exceptions.PaymentsServiceException;
import es.org.cxn.backapp.service.exceptions.UserServiceException;
import es.org.cxn.backapp.service.impl.storage.FileLocation;
import jakarta.transaction.Transactional;

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
     * Service for create payments when federate a user.
     */
    private final PaymentsService paymentsService;

    /**
     * The image storage service used for load and save images.
     */
    private final ImageStorageService imageStorageService;

    /**
     * Constructs the DefaultFederateStateService with a repository and user
     * service.
     *
     * @param repoFederate     The repository for managing federate states.
     * @param userServ         The user service for retrieving user details.
     * @param paymentsServ     The payments service for manage payments when
     *                         federate or cancel federate states.
     * @param imageStorageServ The image storage service, load and delete image
     *                         files.
     * @throws NullPointerException if the provided repository or user service is
     *                              null.
     */
    public DefaultFederateStateService(final FederateStateEntityRepository repoFederate, final UserService userServ,
            final PaymentsService paymentsServ, final ImageStorageService imageStorageServ) {
        super();
        federateStateRepository = Objects.requireNonNull(repoFederate,
                "Received a null pointer as federate state repository.");
        userService = Objects.requireNonNull(userServ, "Received a null pointer as user service.");
        paymentsService = Objects.requireNonNull(paymentsServ, "Receivec a null pointer as payments service.");
        imageStorageService = Objects.requireNonNull(imageStorageServ,
                "Receivec a null pointer as image storage service.");
    }

    /**
     * Checks if the given {@link Optional} of {@link PersistentFederateStateEntity}
     * contains a value. If the {@link Optional} is empty, throws a
     * {@link FederateStateServiceException}.
     *
     * @param federateStateOptional the {@link Optional} containing the federate
     *                              state entity to check.
     * @param userDni               the DNI of the user for whom the federate state
     *                              is being checked.
     * @return the {@link PersistentFederateStateEntity} if present in the
     *         {@link Optional}.
     * @throws FederateStateServiceException if the {@link Optional} is empty,
     *                                       indicating that no federate state was
     *                                       found for the user.
     */
    private static PersistentFederateStateEntity getFederateStateOptional(
            final Optional<PersistentFederateStateEntity> federateStateOptional, final String userDni)
            throws FederateStateServiceException {
        if (federateStateOptional.isEmpty()) {
            throw new FederateStateServiceException("No federate state found for user with dni: " + userDni);
        } else {
            return federateStateOptional.get();
        }
    }

    /**
     * Toggles the auto-renewal status for federate member. If member is not
     * federated yet, throw exception.
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

        final PersistentFederateStateEntity federateState = getFederateStateOptional(federateStateOptional, userDni);
        final FederateState state = federateState.getState();
        if (state.equals(FederateState.FEDERATE)) {
            federateState.setAutomaticRenewal(!federateState.isAutomaticRenewal());
            return federateStateRepository.save(federateState);
        } else {
            throw new FederateStateServiceException("Federate state is not : FEDERATE for user with dni: " + userDni);

        }
    }

    /**
     * Confirms the cancellation of a user's federate status. If the federate state
     * is 'IN_PROGRESS', it changes to 'FEDERATE'. If the state is 'FEDERATE', it
     * changes to 'NO_FEDERATE'. Cancel federate proccess removes payment too.
     *
     * @param userDni The DNI of the user whose federate state is to be updated.
     * @return The updated federate state entity.
     * @throws FederateStateServiceException If the state is already 'NO_FEDERATE'
     *                                       or if no federate state is found.
     * @throws UserServiceException          If the user is not found.
     * @throws PaymentsServiceException      When cannot delete associated payment.
     */
    @Override
    @Transactional
    public PersistentFederateStateEntity confirmCancelFederate(final String userDni)
            throws FederateStateServiceException, UserServiceException, PaymentsServiceException {
        final var federateStateOptional = federateStateRepository.findById(userDni);
        final var federateStateEntity = getFederateStateOptional(federateStateOptional, userDni);

        switch (federateStateEntity.getState()) {
        case IN_PROGRESS -> federateStateEntity.setState(FederateState.FEDERATE);
        case FEDERATE -> {
            federateStateEntity.setState(FederateState.NO_FEDERATE);
            final var payment = federateStateEntity.getPayment();
            federateStateEntity.setPayment(null);
            paymentsService.remove(payment.getId());
        }
        case NO_FEDERATE -> throw new FederateStateServiceException("Cannot change NO FEDERATE status.");
        }

        return federateStateRepository.save(federateStateEntity);
    }

    /**
     * Federate a member by storing their DNI images and updating their federate
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
     * @throws PaymentsServiceException      When new payment cannot be generated.
     */
    @Override
    public PersistentFederateStateEntity federateMember(final String userEmail, final MultipartFile frontDniFile,
            final MultipartFile backDniFile, final boolean autoRenewal)
            throws UserServiceException, FederateStateServiceException, PaymentsServiceException {

        final var user = userService.findByEmail(userEmail);
        final var userDni = user.getDni();
        final var federateStateOptional = federateStateRepository.findById(userDni);

        final var federateStateEntity = getFederateStateOptional(federateStateOptional, userDni);

        final PersistentFederateStateEntity updatedEntity;
        if (federateStateEntity.getState() == FederateState.NO_FEDERATE
                || federateStateEntity.getState() == FederateState.FEDERATE) {

            federateStateEntity.setAutomaticRenewal(autoRenewal);
            federateStateEntity.setDniLastUpdate(LocalDate.now());

            try {
                // Use ImageStorageService to save the front and back DNI images
                final String frontUrl = imageStorageService.saveImage(frontDniFile, FileLocation.DNI, user.getDni());
                final String backUrl = imageStorageService.saveImage(backDniFile, FileLocation.DNI, user.getDni());

                federateStateEntity.setDniFrontImageUrl(frontUrl);
                federateStateEntity.setDniBackImageUrl(backUrl);

                if (federateStateEntity.getState() == FederateState.NO_FEDERATE) {
                    federateStateEntity.setState(FederateState.IN_PROGRESS);
                }

                final var createdAssociatedPayment = paymentsService.createPayment(BigDecimal.valueOf(15.00),
                        PaymentsCategory.FEDERATE_PAYMENT, "Coste ficha federativa.", "Pago federar usuario", userDni);
                federateStateEntity.setPayment((PersistentPaymentsEntity) createdAssociatedPayment);
                updatedEntity = federateStateRepository.save(federateStateEntity);
            } catch (IOException e) {
                throw new FederateStateServiceException("Error saving DNI images: " + e.getMessage(), e);
            }

        } else {
            throw new FederateStateServiceException("Bad state. User dni: " + userDni);
        }

        return updatedEntity;
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
     * {@inheritDoc}
     */
    @Override
    public UserDniImagesDto getDniImages(final String userDni) throws FederateStateServiceException {
        final var federateStateOpt = federateStateRepository.findById(userDni);
        if (federateStateOpt.isEmpty()) {
            throw new FederateStateServiceException("User with DNI: " + userDni + " no have federate state.");
        }
        final var federateStateEntity = federateStateOpt.get();
        final String frontDniImageUrl = federateStateEntity.getDniFrontImageUrl();
        final String backDniImageUrl = federateStateEntity.getDniBackImageUrl();

        try {
            final byte[] frontDniImage = imageStorageService.loadImage(frontDniImageUrl);
            final byte[] backDniImage = imageStorageService.loadImage(backDniImageUrl);
            return new UserDniImagesDto(frontDniImage, backDniImage);
        } catch (IOException e) {
            throw new FederateStateServiceException("User with DNI: " + userDni + " DNI images cannot be loaded.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersistentFederateStateEntity getFederateDataByDni(final String userDni)
            throws UserServiceException, FederateStateServiceException {
        final var userEntity = userService.findByDni(userDni);
        return getFederateDataByEmail(userEntity.getEmail());
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
    public PersistentFederateStateEntity getFederateDataByEmail(final String userEmail)
            throws UserServiceException, FederateStateServiceException {
        final var userEntity = userService.findByEmail(userEmail);
        final var userDni = userEntity.getDni();

        final var federateStateOptional = federateStateRepository.findById(userDni);

        return getFederateStateOptional(federateStateOptional, userDni);

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
        final var federateStateEntity = getFederateStateOptional(federateStateOptional, userDni);

        final var state = federateStateEntity.getState();
        if (state.equals(FederateState.FEDERATE)) {
            federateStateEntity.setDniLastUpdate(LocalDate.now());

            try {
                // Use ImageStorageService to save the front and back DNI images
                final String frontUrl = imageStorageService.saveImage(frontDniFile, FileLocation.DNI,
                        userEntity.getDni());
                final String backUrl = imageStorageService.saveImage(backDniFile, FileLocation.DNI,
                        userEntity.getDni());

                federateStateEntity.setDniFrontImageUrl(frontUrl);
                federateStateEntity.setDniBackImageUrl(backUrl);

                return federateStateRepository.save(federateStateEntity);
            } catch (IOException e) {
                throw new FederateStateServiceException("Error updating DNI images: " + e.getMessage(), e);
            }
        } else {
            throw new FederateStateServiceException("User is not in a federate state.");
        }
    }

}
