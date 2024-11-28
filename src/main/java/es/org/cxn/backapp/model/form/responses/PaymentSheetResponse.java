
package es.org.cxn.backapp.model.form.responses;

import java.time.LocalDate;
import java.util.Objects;

import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;

/**
 * Represents the response form used by the controller for requesting a payment
 * sheet.
 * <p>
 * This record is a Data Transfer Object (DTO) that facilitates communication
 * between the view and the controller, providing an immutable representation of
 * the payment sheet details.
 * </p>
 *
 * @param paymentSheetIdentifier the unique identifier for the payment sheet
 * @param userName               the name of the payment sheet owner
 * @param userFirstSurname       the first surname of the payment sheet owner
 * @param userSecondSurname      the second surname of the payment sheet owner
 * @param userDNI                the DNI of the payment sheet owner
 * @param reason                 the reason for the payment sheet event
 * @param place                  the place of the payment sheet event
 * @param startDate              the start date of the payment sheet event
 * @param endDate                the end date of the payment sheet event
 * @param selfVehicle            the data related to the self vehicle used in
 *                               the payment sheet
 * @param foodHousing            the data related to food and housing expenses
 * @param regularTransportList   the list of regular transport entries
 * @param postalCode             the postal code of the payment sheet owner's
 *                               address
 * @param apartmentNumber        the apartment number of the payment sheet
 *                               owner's address
 * @param building               the building of the payment sheet owner's
 *                               address
 * @param street                 the street of the payment sheet owner's address
 * @param city                   the city of the payment sheet owner's address
 * @param countryName            the name of the country of the payment sheet
 *                               owner's address
 * @param countrySubdivisionName the name of the country subdivision of the
 *                               payment sheet owner's address
 *
 * @author Santiago Paz
 */
public record PaymentSheetResponse(Integer paymentSheetIdentifier, String userName, String userFirstSurname,
        String userSecondSurname, String userDNI, String reason, String place, LocalDate startDate, LocalDate endDate,
        SelfVehicleDataResponse selfVehicle, FoodHousingDataResponse foodHousing,
        RegularTransportListResponse regularTransportList, String postalCode, String apartmentNumber, String building,
        String street, String city, String countryName, String countrySubdivisionName) {

    /**
     * Constructs a {@link PaymentSheetResponse} from a
     * {@link PersistentPaymentSheetEntity}.
     * <p>
     * This static factory method maps the fields of a
     * {@link PersistentPaymentSheetEntity} to the corresponding fields of the
     * {@link PaymentSheetResponse} record. It handles optional values for
     * self-vehicle and food housing data, and collects the regular transport
     * entities into a list.
     * </p>
     *
     * The method performs the following operations:
     * <ul>
     * <li>If the {@code paymentSheetEntity} has a self-vehicle, it creates a
     * {@link SelfVehicleDataResponse} instance with the self-vehicle data.</li>
     * <li>If the {@code paymentSheetEntity} has food housing data, it creates a
     * {@link FoodHousingDataResponse} instance with the food housing data.</li>
     * <li>If the {@code paymentSheetEntity} has regular transports, it creates a
     * {@link RegularTransportListResponse} instance from the list of regular
     * transports.</li>
     * <li>All other fields are directly mapped from the entity to the
     * response.</li>
     * </ul>
     *
     * @param paymentSheetEntity the {@link PersistentPaymentSheetEntity} containing
     *                           payment sheet data. Must not be {@code null}.
     * @return a new {@link PaymentSheetResponse} instance populated with data from
     *         the {@code paymentSheetEntity}.
     *
     * @throws NullPointerException if {@code paymentSheetEntity} is {@code null}.
     */
    public static PaymentSheetResponse fromEntity(final PersistentPaymentSheetEntity paymentSheetEntity) {
        Objects.requireNonNull(paymentSheetEntity, "PaymentSheetEntity must not be null");

        final SelfVehicleDataResponse selfVehicle;
        if (paymentSheetEntity.getSelfVehicle() != null) {
            selfVehicle = new SelfVehicleDataResponse(paymentSheetEntity.getSelfVehicle().getPlaces(),
                    paymentSheetEntity.getSelfVehicle().getDistance(),
                    paymentSheetEntity.getSelfVehicle().getKmPrice());
        } else {
            selfVehicle = null;
        }

        final FoodHousingDataResponse foodHousing;
        if (paymentSheetEntity.getFoodHousing() != null) {
            foodHousing = new FoodHousingDataResponse(paymentSheetEntity.getFoodHousing().getAmountDays(),
                    paymentSheetEntity.getFoodHousing().getDayPrice(),
                    paymentSheetEntity.getFoodHousing().getOvernight());
        } else {
            foodHousing = null;
        }

        final RegularTransportListResponse regularTransportList;
        if (paymentSheetEntity.getRegularTransports() != null && !paymentSheetEntity.getRegularTransports().isEmpty()) {
            regularTransportList = RegularTransportListResponse.fromEntities(paymentSheetEntity.getRegularTransports());
        } else {
            regularTransportList = null;
        }

        return new PaymentSheetResponse(paymentSheetEntity.getIdentifier(), paymentSheetEntity.getUserOwner().getName(),
                paymentSheetEntity.getUserOwner().getFirstSurname(),
                paymentSheetEntity.getUserOwner().getSecondSurname(), paymentSheetEntity.getUserOwner().getDni(),
                paymentSheetEntity.getReason(), paymentSheetEntity.getPlace(), paymentSheetEntity.getStartDate(),
                paymentSheetEntity.getEndDate(), selfVehicle, foodHousing, regularTransportList,
                paymentSheetEntity.getUserOwner().getAddress().getPostalCode(),
                paymentSheetEntity.getUserOwner().getAddress().getApartmentNumber(),
                paymentSheetEntity.getUserOwner().getAddress().getBuilding(),
                paymentSheetEntity.getUserOwner().getAddress().getStreet(),
                paymentSheetEntity.getUserOwner().getAddress().getCity(),
                paymentSheetEntity.getUserOwner().getAddress().getCountry().getShortName(),
                paymentSheetEntity.getUserOwner().getAddress().getCountrySubdivision().getName());
    }

}
