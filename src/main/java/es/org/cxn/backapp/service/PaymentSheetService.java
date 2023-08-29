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

import es.org.cxn.backapp.exceptions.PaymentSheetServiceException;
import es.org.cxn.backapp.model.PaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentFoodHousingEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;
import es.org.cxn.backapp.model.persistence.PersistentSelfVehicleEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for the PaymentSheet entity domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
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
   * @return The updated payment sheet entity
   * @throws PaymentSheetServiceException If fails.
   */
  PersistentPaymentSheetEntity updatePaymentSheet(
        Integer identifier, String reason, String place, LocalDate startDate,
        LocalDate endDate
  ) throws PaymentSheetServiceException;

  /**
   * @return Get a list with all existing payment sheets.
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
  PersistentRegularTransportEntity addRegularTransportToPaymentSheet(
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
   * @return Self vehicle entity created.
   * @throws PaymentSheetServiceException If fails.
   */
  PersistentSelfVehicleEntity addSelfVehicleToPaymentSheet(
        Integer paymentSheetId, String places, float distance, double kmPrice
  ) throws PaymentSheetServiceException;

  /**
   * Create FoodHousing entity and add it to payment sheet.
   *
   * @param paymentSheetId The payment sheet identifier.
   * @param amountDays     The amount of days that food or housing last.
   * @param dayPrice       The price per day.
   * @param overnight      If sleeps true else false.
   * @return The FoodHousing entity created.
   * @throws PaymentSheetServiceException If fails.
   */
  PersistentFoodHousingEntity addFoodHousingToPaymentSheet(
        Integer paymentSheetId, Integer amountDays, float dayPrice,
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

}
