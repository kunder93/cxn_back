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

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import es.org.cxn.backapp.exceptions.PaymentSheetServiceException;
import es.org.cxn.backapp.model.persistence.PersistentFoodHousingEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;
import es.org.cxn.backapp.model.persistence.PersistentSelfVehicleEntity;
import es.org.cxn.backapp.repository.CompanyEntityRepository;
import es.org.cxn.backapp.repository.FoodHousingRepository;
import es.org.cxn.backapp.repository.InvoiceEntityRepository;
import es.org.cxn.backapp.repository.PaymentSheetEntityRepository;
import es.org.cxn.backapp.repository.RegularTransportRepository;
import es.org.cxn.backapp.repository.SelfVehicleRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;

/**
 * Default implementation of the {@link PaymentSheetService}.
 *
 * @author Santiago Paz.
 *
 */
@Service
public final class DefaultPaymentSheetService implements PaymentSheetService {

    /**
     * Invoice exist message for exception.
     */
    public static final String USER_NOT_FOUND_MESSAGE = "User with email not found.";

    /**
     * Payment sheet not found message for exception.
     */
    public static final String PAYMENT_SHEET_NOT_FOUND_MESSAGE = "Payment sheet with this identifier not found.";

    /**
     * Invoice not found message for exception.
     */
    public static final String INVOICE_NOT_FOUND_MESSAGE = "Invoice with number and series not found.";

    /**
     * Invoice not found message for exception.
     */
    public static final String SELF_VEHICLE_NOT_FOUND_MESSAGE = "Self vehicle not found in this payment sheet.";

    /**
     * Invoice not found message for exception.
     */
    public static final String FOOD_HOUSING_NOT_FOUND_MESSAGE = "Food housing not found in this payment sheet.";

    /**
     * Invoice not found message for exception.
     */
    public static final String REGULAR_TRANSPORT_NOT_FOUND_MESSAGE = "Regular transport not found";

    /**
     * Invoice not found message for exception.
     */
    public static final String REGULAR_TRANSPORT_NOT_IN_THIS_PAYMENT_SHEET_MESSAGE = "This payment sheet not contains this regular transport.";

    /**
     * Repository for the invoice entities handled by the service.
     */
    private final PaymentSheetEntityRepository paymentSheetRepository;
    /**
     * Repository for the invoice entities handled by the service.
     */
    private final UserEntityRepository userRepository;
    /**
     * Repository for the invoice entities handled by the service.
     */
    private final InvoiceEntityRepository invoiceRepository;
    /**
     * Repository for the regular transport entities handled by the service.
     */
    private final RegularTransportRepository regularTransportRepository;

    /**
     * Repository for the regular transport entities handled by the service.
     */
    private final SelfVehicleRepository selfVehicleRepository;

    /**
     * Repository for the regular transport entities handled by the service.
     */
    private final FoodHousingRepository foodHousingRepository;

    /**
     * Constructs an entities service with the specified repositories.
     *
     * @param repoComp The company repository{@link CompanyEntityRepository}
     */
    public DefaultPaymentSheetService(
            final PaymentSheetEntityRepository paymentSheetRepo,
            UserEntityRepository userRepo, InvoiceEntityRepository invoiceRepo,
            RegularTransportRepository rgtRepository,
            SelfVehicleRepository selfVehicleRepo,
            FoodHousingRepository foodHousingRepo
    ) {
        super();

        paymentSheetRepository = checkNotNull(
                paymentSheetRepo,
                "Received a null pointer as paymentSheet repository"
        );
        userRepository = checkNotNull(
                userRepo, "Received a null pointer as User repository"
        );
        invoiceRepository = checkNotNull(
                invoiceRepo, "Received a null pointer as User repository"
        );
        regularTransportRepository = checkNotNull(
                rgtRepository, "Received a null pointer as User repository"
        );
        selfVehicleRepository = checkNotNull(
                selfVehicleRepo, "Received a null pointer as User repository"
        );
        foodHousingRepository = checkNotNull(
                foodHousingRepo, "Received a null pointer as User repository"
        );

    }

    @Override
    public PersistentPaymentSheetEntity add(
            String reason, String place, LocalDate startDate, LocalDate endDate,
            String userEmail
    ) throws PaymentSheetServiceException {

        var user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new PaymentSheetServiceException(USER_NOT_FOUND_MESSAGE);
        } else {
            var paymentSheet = new PersistentPaymentSheetEntity(
                    reason, place, startDate, endDate, user.get()
            );
            return paymentSheetRepository.save(paymentSheet);
        }
    }

    @Override
    public PersistentPaymentSheetEntity findById(Integer id)
            throws PaymentSheetServiceException {

        var paymentSheet = paymentSheetRepository.findById(id);
        if (paymentSheet.isEmpty()) {
            throw new PaymentSheetServiceException(
                    PAYMENT_SHEET_NOT_FOUND_MESSAGE
            );
        }
        return paymentSheet.get();
    }

    @Override
    public void remove(Integer id) throws PaymentSheetServiceException {
        var paymentSheet = paymentSheetRepository.findById(id);
        if (paymentSheet.isEmpty()) {
            throw new PaymentSheetServiceException(
                    PAYMENT_SHEET_NOT_FOUND_MESSAGE
            );
        }
        paymentSheetRepository.delete(paymentSheet.get());
    }

    @Override
    public List<PersistentPaymentSheetEntity> getPaymentSheets() {
        return paymentSheetRepository.findAll();
    }

    @Override
    public PersistentPaymentSheetEntity updatePaymentSheet(
            Integer id, String reason, String place, LocalDate startDate,
            LocalDate endDate
    ) throws PaymentSheetServiceException {

        var paymentSheetOptional = paymentSheetRepository.findById(id);
        if (paymentSheetOptional.isEmpty()) {
            throw new PaymentSheetServiceException(
                    PAYMENT_SHEET_NOT_FOUND_MESSAGE
            );
        } else {
            var paymentSheet = paymentSheetOptional.get();
            paymentSheet.setReason(reason);
            paymentSheet.setPlace(place);
            paymentSheet.setEndDate(endDate);
            paymentSheet.setStartDate(startDate);

            return paymentSheetRepository.save(paymentSheet);
        }
    }

    @Override
    public PersistentRegularTransportEntity addRegularTransportToPaymentSheet(
            Integer paymentSheetId, String regularTransportCategory,
            String regularTransportDescription, Integer invoiceNumber,
            String invoiceSeries
    ) throws PaymentSheetServiceException {

        var paymentSheetEntity = paymentSheetRepository
                .findById(paymentSheetId);
        if (paymentSheetEntity.isEmpty()) {
            throw new PaymentSheetServiceException(
                    PAYMENT_SHEET_NOT_FOUND_MESSAGE
            );
        }
        var invoiceEntityList = invoiceRepository
                .findByNumberAndSeries(invoiceNumber, invoiceSeries);
        if (invoiceEntityList.size() != Integer.valueOf(1)) {
            throw new PaymentSheetServiceException(INVOICE_NOT_FOUND_MESSAGE);
        }
        var invoiceEntity = invoiceEntityList.get(0);

        var transportEntity = new PersistentRegularTransportEntity(
                regularTransportCategory, regularTransportDescription,
                invoiceEntity, paymentSheetEntity.get()
        );
        return regularTransportRepository.save(transportEntity);
    }

    @Override
    public PersistentSelfVehicleEntity addSelfVehicleToPaymentSheet(
            Integer paymentSheetId, String places, float distance,
            double kmPrice
    ) throws PaymentSheetServiceException {

        var paymentSheetOptionalEntity = paymentSheetRepository
                .findById(paymentSheetId);
        if (paymentSheetOptionalEntity.isEmpty()) {
            throw new PaymentSheetServiceException(
                    PAYMENT_SHEET_NOT_FOUND_MESSAGE
            );
        }
        var paymentSheetEntity = paymentSheetOptionalEntity.get();
        var selfVehicle = new PersistentSelfVehicleEntity(
                places, distance, kmPrice, paymentSheetEntity
        );

        return selfVehicleRepository.save(selfVehicle);
    }

    @Override
    public PersistentFoodHousingEntity addFoodHousingToPaymentSheet(
            Integer paymentSheetId, Integer amountDays, float dayPrice,
            boolean overnight
    ) throws PaymentSheetServiceException {

        var paymentSheetOptionalEntity = paymentSheetRepository
                .findById(paymentSheetId);
        if (paymentSheetOptionalEntity.isEmpty()) {
            throw new PaymentSheetServiceException(
                    PAYMENT_SHEET_NOT_FOUND_MESSAGE
            );
        }
        var paymentSheetEntity = paymentSheetOptionalEntity.get();
        var foodHousing = new PersistentFoodHousingEntity(
                amountDays, dayPrice, overnight, paymentSheetEntity
        );

        return foodHousingRepository.save(foodHousing);
    }

    @Override
    public void removeSelfVehicle(Integer paymentSheetId)
            throws PaymentSheetServiceException {

        var paymentSheetOptionalEntity = paymentSheetRepository
                .findById(paymentSheetId);
        if (paymentSheetOptionalEntity.isEmpty()) {
            throw new PaymentSheetServiceException(
                    PAYMENT_SHEET_NOT_FOUND_MESSAGE
            );
        }
        var paymentSheetEntity = paymentSheetOptionalEntity.get();
        if (paymentSheetEntity.getSelfVehicle() == null) {
            throw new PaymentSheetServiceException(
                    SELF_VEHICLE_NOT_FOUND_MESSAGE
            );
        }
        selfVehicleRepository.delete(paymentSheetEntity.getSelfVehicle());
    }

    @Override
    public void removeFoodHousing(Integer paymentSheetId)
            throws PaymentSheetServiceException {

        var paymentSheetOptionalEntity = paymentSheetRepository
                .findById(paymentSheetId);
        if (paymentSheetOptionalEntity.isEmpty()) {
            throw new PaymentSheetServiceException(
                    PAYMENT_SHEET_NOT_FOUND_MESSAGE
            );
        }
        var paymentSheetEntity = paymentSheetOptionalEntity.get();
        if (paymentSheetEntity.getFoodHousing() == null) {
            throw new PaymentSheetServiceException(
                    FOOD_HOUSING_NOT_FOUND_MESSAGE
            );
        }
        foodHousingRepository.delete(paymentSheetEntity.getFoodHousing());
    }

    @Override
    public void removeRegularTransportFromPaymentSheet(
            Integer paymentSheetId, Integer regularTransportId
    ) throws PaymentSheetServiceException {

        var paymentSheetOptionalEntity = paymentSheetRepository
                .findById(paymentSheetId);
        if (paymentSheetOptionalEntity.isEmpty()) {
            throw new PaymentSheetServiceException(
                    PAYMENT_SHEET_NOT_FOUND_MESSAGE
            );
        }
        var regularTransportOptional = regularTransportRepository
                .findById(regularTransportId);
        if (regularTransportOptional.isEmpty()) {
            throw new PaymentSheetServiceException(
                    REGULAR_TRANSPORT_NOT_FOUND_MESSAGE
            );
        }
        var paymentSheetEntity = paymentSheetOptionalEntity.get();
        if (!paymentSheetEntity.getRegularTransports()
                .contains(regularTransportOptional.get())) {
            throw new PaymentSheetServiceException(
                    REGULAR_TRANSPORT_NOT_IN_THIS_PAYMENT_SHEET_MESSAGE
            );
        }
        regularTransportRepository.delete(regularTransportOptional.get());
    }
}
