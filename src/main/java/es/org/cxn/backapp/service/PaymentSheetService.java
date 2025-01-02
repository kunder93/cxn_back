
package es.org.cxn.backapp.service;

import es.org.cxn.backapp.model.PaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.service.exceptions.PaymentSheetServiceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for the PaymentSheet entity domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 * </p>
 *
 * @author Santiago Paz Perez.
 */
public interface PaymentSheetService {

  /**
   * Add new payment sheet entity.
   *
   * @param reason    The reason of payment sheet event.
   * @param place     The place of payment sheet event.
   * @param startDate The start date of payment sheet event.
   * @param endDate   The end date of payment sheet event.
   * @param userEmail The user owner email of payment sheet.
   * @return The entity that has been added.
   * @throws PaymentSheetServiceException If fails.
   */
  PaymentSheetEntity add(
        String reason, String place, LocalDate startDate, LocalDate endDate,
        String userEmail
  ) throws PaymentSheetServiceException;

  /**
   * Find a payment sheet entity.
   *
   * @param identifier The payment sheet identifier (id).
   * @return The payment sheet entity with the identifier.
   * @throws PaymentSheetServiceException If fails.
   */
  PersistentPaymentSheetEntity findById(Integer identifier)
        throws PaymentSheetServiceException;

  /**
   * Remove or delete payment sheet entity.
   *
   * @param identifier The payment sheet identifier.
   * @throws PaymentSheetServiceException If fails.
   */
  void remove(Integer identifier) throws PaymentSheetServiceException;

  /**
   * Update data from existing payment sheet entity.
   *
   * @param identifier        The payment sheet identifier.
   * @param reason    The payment sheet event reason.
   * @param place     The payment sheet event place.
   * @param startDate The payment sheet start date.
   * @param endDate   The payment sheet end date.
   * @return The updated payment sheet entity.
   * @throws PaymentSheetServiceException If fails.
   */
  PersistentPaymentSheetEntity updatePaymentSheet(
        Integer identifier, String reason, String place, LocalDate startDate,
        LocalDate endDate
  ) throws PaymentSheetServiceException;

  /**
   * Retrieves all existing payment sheets.
   * <p>
   * This method returns a list of all payment sheet entities currently stored
   * in the system.
   * </p>
   *
   * @return A list of {@link PersistentPaymentSheetEntity} representing all
   *         existing payment sheets.
   */
  List<PersistentPaymentSheetEntity> getPaymentSheets();

  /**
   * Create regularTransport entity and add it to payment sheet.
   *
   * @param paymentSheetId              The payment sheet identifier.
   * @param regularTransportCategory    The regular transport category.
   * @param regularTransportDescription The regular transport description.
   * @param invoiceNumber               The invoice number.
   * @param invoiceSeries               The invoice series.
   * @return The regular transport entity created.
   * @throws PaymentSheetServiceException If fails.
   */
  PersistentPaymentSheetEntity addRegularTransportToPaymentSheet(
        Integer paymentSheetId, String regularTransportCategory,
        String regularTransportDescription, Integer invoiceNumber,
        String invoiceSeries
  ) throws PaymentSheetServiceException;

  /**
   * Create selfVehicle entity and add it to payment sheet.
   *
   * @param paymentSheetId The payment sheet identifier.
   * @param places         The self vehicle places visited.
   * @param distance       The total distance traveled.
   * @param kmPrice        The price for one kilometer.
   * @return PersistentPaymentSheetEntity payment sheet with self vehicle added
   * @throws PaymentSheetServiceException If fails.
   */
  PersistentPaymentSheetEntity addSelfVehicleToPaymentSheet(
        Integer paymentSheetId, String places, BigDecimal distance,
        BigDecimal kmPrice
  ) throws PaymentSheetServiceException;

  /**
   * Create FoodHousing entity and add it to payment sheet.
   *
   * @param paymentSheetId The payment sheet identifier.
   * @param amountDays     The amount of days that food or housing last.
   * @param dayPrice       The price per day.
   * @param overnight      If sleeps true else false.
   * @return The payment sheet entity with food housing.
   * @throws PaymentSheetServiceException If fails.
   */
  PersistentPaymentSheetEntity addFoodHousingToPaymentSheet(
        Integer paymentSheetId, Integer amountDays, BigDecimal dayPrice,
        boolean overnight
  ) throws PaymentSheetServiceException;

  /**
   * Remove selfVehicle entity from payment sheet.
   *
   * @param paymentSheetId The payment sheet identifier.
   * @throws PaymentSheetServiceException If fails.
   */
  void removeSelfVehicle(Integer paymentSheetId)
        throws PaymentSheetServiceException;

  /**
   * Remove foodHousing entity from payment sheet.
   *
   * @param paymentSheetId The payment sheet identifier.
   * @throws PaymentSheetServiceException If fails.
   */
  void removeFoodHousing(Integer paymentSheetId)
        throws PaymentSheetServiceException;

  /**
   * Remove regularTransport entity from payment sheet.
   *
   * @param paymentSheetId     The payment sheet identifier.
   * @param regularTransportId The regular transport identifier.
   * @throws PaymentSheetServiceException If fails.
   */
  void removeRegularTransportFromPaymentSheet(
        Integer paymentSheetId, Integer regularTransportId
  ) throws PaymentSheetServiceException;

  /**
   * Delete payment sheet.
   *
   * @param paymentSheetId The payment sheet identifier.
   * @throws PaymentSheetServiceException When paymentSheet with id not found.
   */
  void deletePaymentSheet(Integer paymentSheetId)
        throws PaymentSheetServiceException;

}
