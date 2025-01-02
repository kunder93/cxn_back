
package es.org.cxn.backapp.model;

import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * A FoodHousingEntity interface representing food and housing accommodations,
 * including details about the duration of stay, pricing, and associated
 * invoices.
 *
 * @author Santiago Paz Perez.
 */
public interface FoodHousingEntity extends Serializable {

  /**
   * Retrieves the identifier of the food housing entity.
   *
   * @return The food housing identifier.
   */
  Integer getId();

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
   * Indicates whether the food housing includes an overnight stay.
   *
   * @return The overnight value, true if overnight is included, false
   * otherwise.
   */
  Boolean getOvernight();

  /**
   * Sets the identifier of the food housing entity.
   *
   * @param id The food housing identifier.
   */
  void setId(Integer id);

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
   * Sets whether the food housing includes an overnight stay.
   *
   * @param overnight The overnight value.
   */
  void setOvernight(Boolean overnight);

  /**
   * Retrieves the payment sheet associated with the food housing entity.
   *
   * @return The payment sheet entity associated with the food housing.
   */
  PersistentPaymentSheetEntity getPaymentSheet();

  /**
   * Retrieves the set of invoices associated with the food housing entity.
   *
   * @return A set of invoices that the food housing entity owns.
   */
  Set<PersistentInvoiceEntity> getInvoices();

  /**
   * Sets the payment sheet entity for the food housing.
   *
   * @param paymentSheet The payment sheet associated with this food housing
   * entity.
   */
  void setPaymentSheet(PersistentPaymentSheetEntity paymentSheet);

  /**
   * Sets the invoices associated with the food housing entity.
   *
   * @param invoices The set of invoices associated with this food housing
   * entity.
   */
  void setInvoices(Set<PersistentInvoiceEntity> invoices);

}
