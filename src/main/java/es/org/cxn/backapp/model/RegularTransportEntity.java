
package es.org.cxn.backapp.model;

import es.org.cxn.backapp.model.persistence.PersistentInvoiceEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;

import java.io.Serializable;

/**
 * A RegularTransport entity interface representing a transport service.
 * This entity includes details about the transport, its category, description,
 * and related invoice and payment sheet information.
 *
 * @author Santiago Paz Perez.
 */
public interface RegularTransportEntity extends Serializable {

  /**
   * Retrieves the identifier of the regular transport.
   *
   * @return The regular transport identifier.
   */
  Integer getId();

  /**
   * Retrieves the category of the regular transport.
   *
   * @return The regular transport category.
   */
  String getCategory();

  /**
   * Retrieves the description of the regular transport.
   *
   * @return The regular transport description.
   */
  String getDescription();

  /**
   * Retrieves the invoice associated with the regular transport.
   *
   * @return The regular transport invoice.
   */
  PersistentInvoiceEntity getTransportInvoice();

  /**
   * Retrieves the payment sheet associated with the regular transport.
   *
   * @return The regular transport payment sheet.
   */
  PersistentPaymentSheetEntity getPaymentSheet();

  /**
   * Sets the identifier of the regular transport.
   *
   * @param id The identifier of the regular transport.
   */
  void setId(Integer id);

  /**
   * Sets the category of the regular transport.
   *
   * @param category The category of the regular transport.
   */
  void setCategory(String category);

  /**
   * Sets the description of the regular transport.
   *
   * @param description The description of the regular transport.
   */
  void setDescription(String description);

  /**
   * Sets the invoice associated with the regular transport.
   *
   * @param transportInvoice The transport invoice entity.
   */
  void setTransportInvoice(PersistentInvoiceEntity transportInvoice);

  /**
   * Sets the payment sheet associated with the regular transport.
   *
   * @param paymentSheet The payment sheet entity.
   */
  void setPaymentSheet(PersistentPaymentSheetEntity paymentSheet);
}
