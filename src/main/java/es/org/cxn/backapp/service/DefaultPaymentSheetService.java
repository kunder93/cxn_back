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

import static com.google.common.base.Preconditions.checkNotNull;

import es.org.cxn.backapp.exceptions.PaymentSheetServiceException;
import es.org.cxn.backapp.model.persistence.PersistentFoodHousingEntity;
import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;
import es.org.cxn.backapp.model.persistence.PersistentSelfVehicleEntity;
import es.org.cxn.backapp.repository.FoodHousingRepository;
import es.org.cxn.backapp.repository.InvoiceEntityRepository;
import es.org.cxn.backapp.repository.PaymentSheetEntityRepository;
import es.org.cxn.backapp.repository.RegularTransportRepository;
import es.org.cxn.backapp.repository.SelfVehicleRepository;
import es.org.cxn.backapp.repository.UserEntityRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Default implementation of the {@link PaymentSheetService}.
 *
 * @author Santiago Paz.
 *
 */
@Service
public final class DefaultPaymentSheetService implements PaymentSheetService {

  /**
   * User not found message exception.
   */
  public static final String USER_NOT_FOUND_MESSAGE =
        "User with email not found.";

  /**
   * Payment sheet not found message exception.
   */
  public static final String PAYMENT_SHEET_NOT_FOUND_MESSAGE =
        "Payment sheet with this identifier not found.";

  /**
   * Payment sheet already have food housing message exception.
   */
  public static final String PAYMENT_SHEET_FOOD_HOUSING_EXISTS =
        " This payment sheet have already food-housing.";
  /**
   * Invoice not found message exception.
   */
  public static final String INVOICE_NOT_FOUND_MESSAGE =
        "Invoice with number and series not found.";

  /**
   * Self vehicle not found message exception.
   */
  public static final String SELF_VEHICLE_NOT_FOUND_MESSAGE =
        "Self vehicle not found in this payment sheet.";

  /**
   * Food housing not found message exception.
   */
  public static final String FOOD_HOUSING_NOT_FOUND_MESSAGE =
        "Food housing not found in this payment sheet.";

  /**
   * Regular transport not found message exception.
   */
  public static final String REGULAR_TRANSPORT_NOT_FOUND_MESSAGE =
        "Regular transport not found";

  /**
   * Regular transport not in payment sheet message exception.
   */
  public static final String REGULAR_TRANSPORT_NOT_IN_PAYMENT_SHEET_MESSAGE =
        "This payment sheet not contains this regular transport.";

  /**
   * Repository for the payment sheet repository entities handled by the
   * service.
   */
  private final PaymentSheetEntityRepository paymentSheetRepository;
  /**
   * Repository for the user entities handled by the service.
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
   * Repository for the self vehicle entities handled by the service.
   */
  private final SelfVehicleRepository selfVehicleRepository;

  /**
   * Repository for the food housing entities handled by the service.
   */
  private final FoodHousingRepository foodHousingRepository;

  /**
   * Constructs an entities service with the specified repositories.
   *
   * @param paymentSheetRepo The payment sheet repository.
   * @param userRepo         The user repository.
   * @param invoiceRepo      The invoices repository.
   * @param rgtRepository    The regularTransport repository.
   * @param selfVehicleRepo  The selfVehicle repository.
   * @param foodHousingRepo  The foodHousing repository.
   */
  public DefaultPaymentSheetService(
        final PaymentSheetEntityRepository paymentSheetRepo,
        final UserEntityRepository userRepo,
        final InvoiceEntityRepository invoiceRepo,
        final RegularTransportRepository rgtRepository,
        final SelfVehicleRepository selfVehicleRepo,
        final FoodHousingRepository foodHousingRepo
  ) {
    super();

    paymentSheetRepository = checkNotNull(
          paymentSheetRepo, "Received a null pointer as paymentSheet repository"
    );
    userRepository =
          checkNotNull(userRepo, "Received a null pointer as User repository");
    invoiceRepository = checkNotNull(
          invoiceRepo, "Received a null pointer as invoice repository"
    );
    regularTransportRepository = checkNotNull(
          rgtRepository,
          "Received a null pointer as regularTransport repository"
    );
    selfVehicleRepository = checkNotNull(
          selfVehicleRepo, "Received a null pointer as selfVehicle repository"
    );
    foodHousingRepository = checkNotNull(
          foodHousingRepo, "Received a null pointer as foodHousing repository"
    );

  }

  @Override
  public PersistentPaymentSheetEntity add(
        final String reason, final String place, final LocalDate startDate,
        final LocalDate endDate, final String userEmail
  ) throws PaymentSheetServiceException {

    final var user = userRepository.findByEmail(userEmail);
    if (user.isEmpty()) {
      throw new PaymentSheetServiceException(USER_NOT_FOUND_MESSAGE);
    } else {
      final var paymentSheet = PersistentPaymentSheetEntity.builder()
            .reason(reason).place(place).startDate(startDate).endDate(endDate)
            .userOwner(user.get()).build();

      return paymentSheetRepository.save(paymentSheet);
    }
  }

  @Override
  public PersistentPaymentSheetEntity findById(final Integer id)
        throws PaymentSheetServiceException {

    final var paymentSheet = paymentSheetRepository.findById(id);
    if (paymentSheet.isEmpty()) {
      throw new PaymentSheetServiceException(PAYMENT_SHEET_NOT_FOUND_MESSAGE);
    }
    return paymentSheet.get();
  }

  @Override
  public void remove(final Integer id) throws PaymentSheetServiceException {
    final var paymentSheetOpt = paymentSheetRepository.findById(id);
    if (paymentSheetOpt.isEmpty()) {
      throw new PaymentSheetServiceException(PAYMENT_SHEET_NOT_FOUND_MESSAGE);
    }
    final var paymentSheetEntity = paymentSheetOpt.get();
    paymentSheetRepository.delete(paymentSheetEntity);
  }

  @Override
  public List<PersistentPaymentSheetEntity> getPaymentSheets() {
    return paymentSheetRepository.findAll();
  }

  @Override
  public PersistentPaymentSheetEntity updatePaymentSheet(
        final Integer identifier, final String reason, final String place,
        final LocalDate startDate, final LocalDate endDate
  ) throws PaymentSheetServiceException {

    final var paymentSheetOptional =
          paymentSheetRepository.findById(identifier);
    if (paymentSheetOptional.isEmpty()) {
      throw new PaymentSheetServiceException(PAYMENT_SHEET_NOT_FOUND_MESSAGE);
    } else {
      final var paymentSheet = paymentSheetOptional.get();
      paymentSheet.setReason(reason);
      paymentSheet.setPlace(place);
      paymentSheet.setEndDate(endDate);
      paymentSheet.setStartDate(startDate);

      return paymentSheetRepository.save(paymentSheet);
    }
  }

  @Override
  public PersistentRegularTransportEntity addRegularTransportToPaymentSheet(
        final Integer paymentSheetId, final String regularTransportCategory,
        final String regularTransportDescription, final Integer invoiceNumber,
        final String invoiceSeries
  ) throws PaymentSheetServiceException {

    final var paymentSheetEntity =
          paymentSheetRepository.findById(paymentSheetId);
    if (paymentSheetEntity.isEmpty()) {
      throw new PaymentSheetServiceException(PAYMENT_SHEET_NOT_FOUND_MESSAGE);
    }
    final var invoiceEntityList =
          invoiceRepository.findByNumberAndSeries(invoiceNumber, invoiceSeries);
    if (invoiceEntityList.size() != 1) {
      throw new PaymentSheetServiceException(INVOICE_NOT_FOUND_MESSAGE);
    }
    final var invoiceEntity = invoiceEntityList.get(0);

    final var transportEntity = PersistentRegularTransportEntity.builder()
          .category(regularTransportCategory)
          .description(regularTransportDescription)
          .transportInvoice(invoiceEntity)
          .paymentSheet(paymentSheetEntity.get()).build();

    return regularTransportRepository.save(transportEntity);
  }

  @Override
  public PersistentSelfVehicleEntity addSelfVehicleToPaymentSheet(
        final Integer paymentSheetId, final String places, final float distance,
        final double kmPrice
  ) throws PaymentSheetServiceException {

    final var paymentSheetOptionalEntity =
          paymentSheetRepository.findById(paymentSheetId);
    if (paymentSheetOptionalEntity.isEmpty()) {
      throw new PaymentSheetServiceException(PAYMENT_SHEET_NOT_FOUND_MESSAGE);
    }
    final var paymentSheetEntity = paymentSheetOptionalEntity.get();
    final var selfVehicle = PersistentSelfVehicleEntity.builder().places(places)
          .distance(distance).kmPrice(kmPrice).paymentSheet(paymentSheetEntity)
          .build();
    return selfVehicleRepository.save(selfVehicle);
  }

  @Override
  public PersistentFoodHousingEntity addFoodHousingToPaymentSheet(
        final Integer paymentSheetId, final Integer amountDays,
        final float dayPrice, final boolean overnight
  ) throws PaymentSheetServiceException {

    final var paymentSheetOptionalEntity =
          paymentSheetRepository.findById(paymentSheetId);
    if (paymentSheetOptionalEntity.isEmpty()) {
      throw new PaymentSheetServiceException(PAYMENT_SHEET_NOT_FOUND_MESSAGE);
    }
    final var paymentSheetEntity = paymentSheetOptionalEntity.get();
    if (paymentSheetEntity.getFoodHousing() != null) {
      throw new PaymentSheetServiceException(PAYMENT_SHEET_FOOD_HOUSING_EXISTS);
    }
    final var foodHousing = PersistentFoodHousingEntity.builder()
          .amountDays(amountDays).dayPrice(dayPrice).overnight(overnight)
          .paymentSheet(paymentSheetEntity).build();
    return foodHousingRepository.save(foodHousing);
  }

  @Override
  public void removeSelfVehicle(final Integer paymentSheetId)
        throws PaymentSheetServiceException {

    final var paymentSheetOptionalEntity =
          paymentSheetRepository.findById(paymentSheetId);
    if (paymentSheetOptionalEntity.isEmpty()) {
      throw new PaymentSheetServiceException(PAYMENT_SHEET_NOT_FOUND_MESSAGE);
    }
    final var paymentSheetEntity = paymentSheetOptionalEntity.get();
    if (paymentSheetEntity.getSelfVehicle() == null) {
      throw new PaymentSheetServiceException(SELF_VEHICLE_NOT_FOUND_MESSAGE);
    }
    selfVehicleRepository.delete(paymentSheetEntity.getSelfVehicle());
  }

  @Override
  public void removeFoodHousing(final Integer paymentSheetId)
        throws PaymentSheetServiceException {

    final var paymentSheetOptionalEntity =
          paymentSheetRepository.findById(paymentSheetId);
    if (paymentSheetOptionalEntity.isEmpty()) {
      throw new PaymentSheetServiceException(PAYMENT_SHEET_NOT_FOUND_MESSAGE);
    }
    final var paymentSheetEntity = paymentSheetOptionalEntity.get();
    if (paymentSheetEntity.getFoodHousing() == null) {
      throw new PaymentSheetServiceException(FOOD_HOUSING_NOT_FOUND_MESSAGE);
    }
    foodHousingRepository.delete(paymentSheetEntity.getFoodHousing());
  }

  @Override
  public void removeRegularTransportFromPaymentSheet(
        final Integer paymentSheetId, final Integer regularTransportId
  ) throws PaymentSheetServiceException {

    final var paymentSheetOptionalEntity =
          paymentSheetRepository.findById(paymentSheetId);
    if (paymentSheetOptionalEntity.isEmpty()) {
      throw new PaymentSheetServiceException(PAYMENT_SHEET_NOT_FOUND_MESSAGE);
    }
    final var regularTransportOptional =
          regularTransportRepository.findById(regularTransportId);
    if (regularTransportOptional.isEmpty()) {
      throw new PaymentSheetServiceException(
            REGULAR_TRANSPORT_NOT_FOUND_MESSAGE
      );
    }
    final var paymentSheetEntity = paymentSheetOptionalEntity.get();
    if (!paymentSheetEntity.getRegularTransports()
          .contains(regularTransportOptional.get())) {
      throw new PaymentSheetServiceException(
            REGULAR_TRANSPORT_NOT_IN_PAYMENT_SHEET_MESSAGE
      );
    }
    regularTransportRepository.delete(regularTransportOptional.get());
  }
}
