/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package es.org.cxn.backapp.service;

import java.time.LocalDate;
import java.util.List;

import es.org.cxn.backapp.exceptions.PaymentSheetServiceException;
import es.org.cxn.backapp.model.persistence.PersistentFoodHousingEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;
import es.org.cxn.backapp.model.persistence.PersistentSelfVehicleEntity;

/**
 * Service for the PaymentSheet entity domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago Paz Perez.
 */
public interface PaymentSheetService {

    PersistentPaymentSheetEntity add(
            String reason, String place, LocalDate startDate, LocalDate endDate,
            String userEmail
    ) throws PaymentSheetServiceException;

    PersistentPaymentSheetEntity findById(Integer id)
            throws PaymentSheetServiceException;

    void remove(Integer id) throws PaymentSheetServiceException;

    PersistentPaymentSheetEntity updatePaymentSheet(
            Integer id, String reason, String place, LocalDate startDate,
            LocalDate endDate
    ) throws PaymentSheetServiceException;

    List<PersistentPaymentSheetEntity> getPaymentSheets();

    PersistentRegularTransportEntity addRegularTransportToPaymentSheet(
            Integer paymentSheetId, String regularTransportCategory,
            String regularTransportDescription, Integer invoiceNumber,
            String invoiceSeries
    ) throws PaymentSheetServiceException;

    PersistentSelfVehicleEntity addSelfVehicleToPaymentSheet(
            Integer paymentSheetId, String places, float distance,
            double kmPrice
    ) throws PaymentSheetServiceException;

    PersistentFoodHousingEntity addFoodHousingToPaymentSheet(
            Integer paymentSheetId, Integer amountDays, float dayPrice,
            boolean overnight
    ) throws PaymentSheetServiceException;

    void removeSelfVehicle(Integer paymentSheetId)
            throws PaymentSheetServiceException;

    void removeFoodHousing(Integer paymentSheetId)
            throws PaymentSheetServiceException;

    void removeRegularTransportFromPaymentSheet(
            Integer paymentSheetId, Integer regularTransportId
    ) throws PaymentSheetServiceException;

}
