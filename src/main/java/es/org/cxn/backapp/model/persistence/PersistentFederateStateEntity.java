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

package es.org.cxn.backapp.model.persistence;

import java.time.LocalDate;

import es.org.cxn.backapp.model.FederateState;
import es.org.cxn.backapp.model.FederateStateEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entity representing federative user data in the system.
 *
 * <p>
 * This class uses JPA annotations for mapping the entity to the database. It is
 * equipped with Lombok annotations to generate getters, setters, and other
 * boilerplate code.
 * </p>
 *
 * <p>
 * Each instance of this class corresponds to a record in the
 * "user_federative_data" table, storing information about a user's federate
 * state, including their DNI images, renewal status, last update date, and
 * associated user.
 * </p>
 *
 * @author Santiago Paz Perez
 */
@Entity
@Table(name = "user_federative_data")
@Data
public class PersistentFederateStateEntity implements FederateStateEntity {

    /**
     * The length of the user's DNI (Documento Nacional de Identidad). This constant
     * is used to define the maximum allowed length for the user's DNI field in the
     * database and throughout the application. The standard length for a DNI in
     * this system is 10 characters.
     */
    private static final int USER_DNI_LENGTH = 10;
    /**
     * The maximum length allowed for the URL of the DNI images. This constant
     * defines the maximum allowed length for the URLs of the front and back images
     * of the user's DNI. These URLs are stored in the database and must conform to
     * a standard length, which is set to 150 characters for this system.
     */
    private static final int IMAGE_URL_LENGTH = 150;

    /**
     * Serial uid for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier for this federative user data, corresponding to the
     * user's DNI.
     *
     */
    @Id
    @Column(name = "user_dni", nullable = false, unique = true, length = USER_DNI_LENGTH)
    private String userDni;

    /**
     * URL of the front side image of the user's DNI.
     *
     */
    @Column(name = "dni_front_image_url", nullable = false, length = IMAGE_URL_LENGTH)
    private String dniFrontImageUrl;

    /**
     * URL of the back side image of the user's DNI.
     *
     */
    @Column(name = "dni_back_image_url", nullable = false, length = IMAGE_URL_LENGTH)
    private String dniBackImageUrl;

    /**
     * Indicates whether the user's membership is automatically renewed.
     *
     */
    @Column(name = "automatic_renewal", nullable = false)
    private boolean automaticRenewal;

    /**
     * The date when the user's DNI data was last updated.
     *
     */
    @Column(name = "dni_last_update", nullable = false)
    private LocalDate dniLastUpdate;

    /**
     * The current state of the user's federate status.
     *
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private FederateState state;

    /**
     * The user associated with this federative data.
     *
     */
    @OneToOne
    @JoinColumn(name = "user_dni", referencedColumnName = "dni")
    private PersistentUserEntity user;

    /**
     * Default constructor. This is used by JPA for entity instantiation.
     */
    public PersistentFederateStateEntity() {
        // Default constructor
    }

    /**
     * Constructs a {@link PersistentFederateStateEntity} with the specified
     * parameters.
     *
     * @param usrDni             the unique identifier (DNI) for the user
     * @param frontImageUrl      the URL of the front side image of the user's DNI
     * @param backImageUrl       the URL of the back side image of the user's DNI
     * @param isAutomaticRenewal whether the user's membership is automatically
     *                           renewed
     * @param lastUpdate         the date when the user's DNI data was last updated
     * @param federateStatus     the current state of the user's federate status
     */
    public PersistentFederateStateEntity(final String usrDni, final String frontImageUrl, final String backImageUrl,
            final boolean isAutomaticRenewal, final LocalDate lastUpdate, final FederateState federateStatus) {
        this.userDni = usrDni;
        this.dniFrontImageUrl = frontImageUrl;
        this.dniBackImageUrl = backImageUrl;
        this.automaticRenewal = isAutomaticRenewal;
        this.dniLastUpdate = lastUpdate;
        this.state = federateStatus;
    }
}
