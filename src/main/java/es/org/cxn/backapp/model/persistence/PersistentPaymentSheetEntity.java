
package es.org.cxn.backapp.model.persistence;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import es.org.cxn.backapp.model.PaymentSheetEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * Entity representing a payment sheet in the system.
 * <p>
 * This class maps to the "paymentsheet" table in the database and contains
 * details about a payment sheet, including its reason, place, dates, and
 * associated entities like user and different types of transport and housing.
 * </p>
 *
 * @author Santi
 */
@Entity(name = "PaymentSheet")
@Table(name = "paymentsheet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PersistentPaymentSheetEntity implements PaymentSheetEntity {

    /**
     * The serial UID.
     */
    @Transient
    private static final long serialVersionUID = -8238192117130392730L;

    /**
     * Unique identifier for the payment sheet.
     * <p>
     * This is the primary key of the payment sheet entity, automatically generated
     * by the database.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    /**
     * Reason for the payment sheet.
     * <p>
     * Provides a description or reason related to the payment sheet.
     * </p>
     */
    @Column(name = "reason", nullable = false, unique = false)
    @NonNull
    private String reason;

    /**
     * Place where the payment sheet is associated.
     * <p>
     * Indicates the location or place related to the payment sheet.
     * </p>
     */
    @Column(name = "place", nullable = false, unique = false)
    @NonNull
    private String place;

    /**
     * Start date of the payment sheet's validity or period.
     * <p>
     * Represents the beginning date of the payment sheet's period.
     * </p>
     */
    @Column(name = "start_date", nullable = false, unique = false)
    @NonNull
    private LocalDate startDate;

    /**
     * End date of the payment sheet's validity or period.
     * <p>
     * Represents the ending date of the payment sheet's period.
     * </p>
     */
    @Column(name = "end_date", nullable = false, unique = false)
    @NonNull
    private LocalDate endDate;

    /**
     * User associated with the payment sheet.
     * <p>
     * Represents the user who owns or is associated with this payment sheet. It is
     * a many-to-one relationship indicating that multiple payment sheets can be
     * associated with one user.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_dni", nullable = false)
    @NonNull
    private PersistentUserEntity userOwner;

    /**
     * Regular transports associated with the payment sheet.
     * <p>
     * This is a one-to-many relationship indicating that payment sheet can have
     * multiple regular transports associated with it. The cascade and orphanRemoval
     * settings ensure that changes are propagated and orphan records are removed.
     * </p>
     */
    @OneToMany(mappedBy = "paymentSheet", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PersistentRegularTransportEntity> regularTransports = new HashSet<>();

    /**
     * Self vehicle associated with the payment sheet.
     * <p>
     * This is a one-to-one relationship indicating that each payment sheet can have
     * one associated self vehicle. The mappedBy attribute indicates that the other
     * side of this relationship is managed by the "paymentSheet" field in the
     * PersistentSelfVehicleEntity class.
     * </p>
     */
    @OneToOne(mappedBy = "paymentSheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private PersistentSelfVehicleEntity selfVehicle;

    /**
     * Food and housing details associated with the payment sheet.
     * <p>
     * This is a one-to-one relationship indicating that each payment sheet can have
     * one associated food and housing entity. The mappedBy attribute indicates that
     * the other side of this relationship is managed by the "paymentSheet" field in
     * the PersistentFoodHousingEntity class.
     * </p>
     */
    @OneToOne(mappedBy = "paymentSheet", cascade = CascadeType.ALL, orphanRemoval = true)
    private PersistentFoodHousingEntity foodHousing;

    /**
     * Adds a regular transport to the payment sheet.
     * <p>
     * This method updates the bidirectional relationship between the payment sheet
     * and the regular transport.
     * </p>
     *
     * @param transport the regular transport entity to add
     */
    public void addRegularTransport(final PersistentRegularTransportEntity transport) {
        this.regularTransports.add(transport);
        transport.setPaymentSheet(this);
    }

    /**
     * Removes a regular transport from the payment sheet.
     * <p>
     * This method updates the bidirectional relationship between the payment sheet
     * and the regular transport.
     * </p>
     *
     * @param transport the regular transport entity to remove
     */
    public void deleteRegularTransport(final PersistentRegularTransportEntity transport) {
        this.regularTransports.remove(transport);
        transport.setPaymentSheet(null);
    }
}
