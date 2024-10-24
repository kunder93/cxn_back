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

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.exceptions.FederateStateServiceException;
import es.org.cxn.backapp.exceptions.UserServiceException;
import es.org.cxn.backapp.model.persistence.PersistentFederateStateEntity;

/**
 * Service interface for managing federate state operations.
 *
 * <p>
 * This interface provides the contract for operations related to federate state
 * management, such as creating, updating, retrieving, or deleting federate
 * state data in the application.
 * </p>
 *
 * <p>
 * Implementations of this interface should contain the business logic to manage
 * entities of type {@code PersistentFederateStateEntity}, typically working
 * with repositories to perform database operations.
 * </p>
 *
 * <p>
 * Example of services that might be implemented:
 * </p>
 * <ul>
 * <li>Retrieving federate state data for a given user</li>
 * <li>Updating or renewing a user's federate state information</li>
 * <li>Managing automatic renewal settings</li>
 * </ul>
 *
 * <p>
 * All methods in this interface may throw exceptions related to federate state
 * operations, which should be handled appropriately by the implementing class.
 * </p>
 *
 * @author Santiago Paz Perez
 */
public interface FederateStateService {

    /**
     * Change auto renew property value, from true to false and from false to true.
     *
     * @param userEmail The user email used as identifier.
     * @return The user entity with auto renew value changed.
     * @throws UserServiceException          When no user with email found.
     * @throws FederateStateServiceException When no federate state for user found.
     */
    PersistentFederateStateEntity changeAutoRenew(String userEmail)
            throws UserServiceException, FederateStateServiceException;

    /**
     * Confirms the federate status of a user identified by the given email.
     *
     * @param userDni the dni identifier of the user whose federate status is to be
     *                confirmed
     * @return the updated {@link PersistentFederateStateEntity} representing the
     *         user's federate state
     * @throws FederateStateServiceException if there is an error during the
     *                                       confirmation process
     * @throws UserServiceException          if there is an error related to the
     *                                       user service
     */
    PersistentFederateStateEntity confirmCancelFederate(String userDni)
            throws FederateStateServiceException, UserServiceException;

    /**
     * Federates a member by processing the provided DNI documents.
     *
     * @param userEmail    the email of the user being federated
     * @param frontDniFile the front image of the DNI document
     * @param backDniFile  the back image of the DNI document
     * @param autoRenewal  flag indicating whether automatic renewal is enabled
     * @return the {@link PersistentFederateStateEntity} representing the newly
     *         federated user
     * @throws IOException                   if there is an error handling the
     *                                       uploaded files
     * @throws UserServiceException          if there is an error related to the
     *                                       user service
     * @throws FederateStateServiceException if there is an error during the
     *                                       federate process
     */
    PersistentFederateStateEntity federateMember(String userEmail, MultipartFile frontDniFile,
            MultipartFile backDniFile, boolean autoRenewal) throws UserServiceException, FederateStateServiceException;

    /**
     * Retrieves a list of all federate state entities.
     *
     * @return a list of {@link PersistentFederateStateEntity} representing all
     *         federate states
     */
    List<PersistentFederateStateEntity> getAll();

    /**
     * Retrieves the federate state data for the user identified by the given email.
     *
     * @param userEmail the email of the user whose federate state data is to be
     *                  retrieved
     * @return the {@link PersistentFederateStateEntity} representing the user's
     *         federate state
     * @throws UserServiceException          if there is an error related to the
     *                                       user service
     * @throws FederateStateServiceException if there is an error retrieving the
     *                                       federate state
     */
    PersistentFederateStateEntity getFederateData(String userEmail)
            throws UserServiceException, FederateStateServiceException;

    /**
     * Updates the user's DNI images (both front and back).
     *
     * <p>
     * This method is responsible for updating the front and back DNI images of a
     * user identified by their email. The user's federate state must be
     * {@code FEDERATE} to perform the update. The method also updates the
     * {@code dniLastUpdate} field with the current date.
     * </p>
     *
     * @param userEmail the email of the user whose DNI is being updated
     * @param frontDni  the front image of the new DNI document
     * @param backDni   the back image of the new DNI document
     * @return the updated {@link PersistentFederateStateEntity} representing the
     *         user's federate state
     * @throws UserServiceException          if there is an error related to the
     *                                       user service, such as user not found
     * @throws FederateStateServiceException if the user's federate state is not
     *                                       {@code FEDERATE} or if an error occurs
     *                                       while updating the DNI images
     */
    PersistentFederateStateEntity updateDni(String userEmail, MultipartFile frontDni, MultipartFile backDni)
            throws UserServiceException, FederateStateServiceException;

}
