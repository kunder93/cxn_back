
package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;

/**
 * A FoodHousingEntity interface representing food and housing accommodations,
 * including details about the duration of stay, pricing, and associated
 * invoices.
 *
 * @author Santiago Paz Perez.
 */
public interface FoodHousingEntity extends Serializable {

    /**
     * Retrieves the number of days associated with the food housing entity.
     *
     * @return The amount of days.
     */
    Integer getAmountDays();

    /**
     * Retrieves the price per day for the food housing entity.
     *
     * @return The price per day.
     */
    BigDecimal getDayPrice();

    /**
     * Retrieves the identifier of the food housing entity.
     *
     * @return The food housing identifier.
     */
    Integer getIdentifier();

    /**
     * Retrieves the set of invoices associated with the food housing entity.
     *
     * @return A set of invoices that the food housing entity owns.
     */
    Set<PersistentInvoiceEntity> getInvoices();

    /**
     * Retrieves the payment sheet associated with the food housing entity.
     *
     * @return The payment sheet entity associated with the food housing.
     */
    PersistentPaymentSheetEntity getPaymentSheet();

    /**
     * Indicates whether the food housing includes an overnight stay.
     *
     * @return The overnight value, true if overnight is included, false otherwise.
     */
    Boolean isOvernight();

    /**
     * Sets the number of days for the food housing entity.
     *
     * @param amountDays The amount of days.
     */
    void setAmountDays(Integer amountDays);

    /**
     * Sets the price per day for the food housing entity.
     *
     * @param dayPrice The price per day.
     */
    void setDayPrice(BigDecimal dayPrice);

    /**
     * Sets the identifier of the food housing entity.
     *
     * @param value The food housing identifier.
     */
    void setIdentifier(Integer value);

    /**
     * Sets the invoices associated with the food housing entity.
     *
     * @param invoices The set of invoices associated with this food housing entity.
     */
    void setInvoices(Set<PersistentInvoiceEntity> invoices);

    /**
     * Sets whether the food housing includes an overnight stay.
     *
     * @param overnight The overnight value.
     */
    void setOvernight(Boolean overnight);

    /**
     * Sets the payment sheet entity for the food housing.
     *
     * @param paymentSheet The payment sheet associated with this food housing
     *                     entity.
     */
    void setPaymentSheet(PersistentPaymentSheetEntity paymentSheet);

}
