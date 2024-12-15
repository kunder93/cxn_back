package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The {@code PaymentsEntity} interface represents a contract for a payment
 * entity in the system. It defines the attributes associated with a payment
 * record that can be implemented by persistent entities.
 *
 * <p>
 * Implementations of this interface should provide the necessary getters and
 * setters for the attributes. The attributes include unique identifiers,
 * descriptive metadata, timestamps, monetary amounts, and user associations.
 * </p>
 *
 * <p>
 * This interface extends {@code Serializable} to ensure that payment objects
 * can be serialized and deserialized when needed, such as during distributed
 * transactions or caching.
 * </p>
 *
 * <p>
 * The attributes defined in this interface include:
 * <ul>
 * <li>Unique identifier for the payment.</li>
 * <li>Title and description of the payment.</li>
 * <li>Timestamps for creation and completion.</li>
 * <li>Payment amount and category.</li>
 * <li>Associated user identifier (e.g., DNI).</li>
 * </ul>
 * </p>
 *
 * <p>
 * All fields are expected to have corresponding database columns in persistent
 * implementations, ensuring consistency between the application layer and the
 * database schema.
 * </p>
 *
 * @author Santiago
 * @version 1.0
 */
public interface PaymentsEntity extends Serializable {

    /**
     * Gets the monetary amount of the payment.
     *
     * @return the monetary amount as a {@code BigDecimal}.
     */
    BigDecimal getAmount();

    /**
     * Gets the category of the payment.
     *
     * @return the category of the payment.
     */
    String getCategory();

    /**
     * Gets the timestamp when the payment was created.
     *
     * @return the creation timestamp of the payment.
     */
    LocalDateTime getCreatedAt();

    /**
     * Gets the description of the payment.
     *
     * @return the description of the payment.
     */
    String getDescription();

    /**
     * Gets the unique identifier of the payment.
     *
     * @return the unique identifier (UUID) of the payment.
     */
    UUID getId();

    /**
     * Gets the timestamp when the payment was completed.
     *
     * @return the completion timestamp of the payment.
     */
    LocalDateTime getPaidAt();

    /**
     * Gets the state of the payment.
     *
     * @return the current state of the payment.
     */
    String getState();

    /**
     * Gets the title of the payment.
     *
     * @return the title of the payment.
     */
    String getTitle();

    /**
     * Gets the user DNI associated with the payment.
     *
     * @return the DNI of the associated user.
     */
    String getUserDni();
}
