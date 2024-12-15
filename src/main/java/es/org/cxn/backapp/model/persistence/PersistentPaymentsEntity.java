package es.org.cxn.backapp.model.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import es.org.cxn.backapp.model.ActivityEntity;
import es.org.cxn.backapp.model.PaymentsEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersistentPaymentsEntity is a JPA entity representing a payment record in the
 * database. This class implements the PaymentsEntity interface and maps to the
 * "payments" table.
 *
 * <p>
 * Attributes of this entity include an identifier, title, description, amount,
 * and more. Each attribute corresponds to a column in the database table.
 *
 * <p>
 * This class is annotated with the {@code @Entity} annotation and managed by
 * the JPA persistence provider. It uses Lombok's {@code @Data} to automatically
 * generate getters, setters, and other common methods.
 *
 * <p>
 * This class is licensed under the MIT License.
 *
 * @see ActivityEntity
 */
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersistentPaymentsEntity implements PaymentsEntity {

    /**
     * Serial version UID for serialization compatibility.
     */
    @Transient
    private static final long serialVersionUID = -6782220123937248455L;

    /**
     * The unique identifier for each payment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    /**
     * Title of the payment.
     */
    @Column(name = "title", nullable = false, length = 80)
    private String title;

    /**
     * Detailed description of the payment.
     */
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * State of the payment.
     */
    @Column(name = "state", nullable = false, length = 40)
    private String state;

    /**
     * Category of the payment.
     */
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    /**
     * Date and time when the payment was made.
     */
    @Column(name = "paid_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime paidAt;

    /**
     * Monetary amount of the payment.
     */
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * The DNI of the user associated with the payment.
     */
    @Column(name = "user_dni", nullable = false, length = 20)
    private String userDni;

    /**
     * The payment time when is created.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
