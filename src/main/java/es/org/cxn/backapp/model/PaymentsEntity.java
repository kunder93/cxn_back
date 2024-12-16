package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PaymentsState;

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
    PaymentsCategory getCategory();

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
    PaymentsState getState();

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

    /**
     * Sets the monetary amount of the payment.
     *
     * @param amount the monetary amount to set.
     */
    void setAmount(BigDecimal amount);

    /**
     * Sets the category of the payment.
     *
     * @param category the category to set.
     */
    void setCategory(PaymentsCategory category);

    /**
     * Sets the creation timestamp of the payment.
     *
     * @param createdAt the creation timestamp to set.
     */
    void setCreatedAt(LocalDateTime createdAt);

    /**
     * Sets the description of the payment.
     *
     * @param description the description to set.
     */
    void setDescription(String description);

    /**
     * Sets the completion timestamp of the payment.
     *
     * @param paidAt the completion timestamp to set.
     */
    void setPaidAt(LocalDateTime paidAt);

    /**
     * Sets the state of the payment.
     *
     * @param state the state to set.
     */
    void setState(PaymentsState state);

    /**
     * Sets the title of the payment.
     *
     * @param title the title to set.
     */
    void setTitle(String title);

    /**
     * Sets the user DNI associated with the payment.
     *
     * @param userDni the DNI of the associated user to set.
     */
    void setUserDni(String userDni);

}
