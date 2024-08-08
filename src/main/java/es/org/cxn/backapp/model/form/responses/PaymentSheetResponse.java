
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form used by controller as response for requesting one payment
 * sheet.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class PaymentSheetResponse implements Serializable {

  /**
   * Serial UID.
   */
  private static final long serialVersionUID = -3183089459011952705L;

  /**
   * The payment sheet identifier.
   */
  private Integer paymentSheetIdentifier;
  /**
   * Payment sheet owner user name.
   */
  private String userName;
  /**
   * Payment sheet owner user first surname.
   */
  private String userFirstSurname;
  /**
   * Payment sheet owner user second surname.
   */
  private String userSecondSurname;
  /**
   * Payment sheet owner user DNI.
   */
  private String userDNI;
  /**
   * Payment sheet event reason.
   */
  private String reason;
  /**
   * Payment sheet event place.
   */
  private String place;
  /**
   * Payment sheet event start date.
   */
  private LocalDate startDate;
  /**
   * Payment sheet event end date.
   */
  private LocalDate endDate;
  /**
   * Self vehicle data.
   */
  private SelfVehicleDataResponse selfVehicle;
  /**
   * Food housing data.
   */
  private FoodHousingDataResponse foodHousing;
  /**
   * Regular transport list.
   */
  private RegularTransportListResponse regularTransportList;
  /**
   * User address postal code.
   */
  private String postalCode;
  /**
   * User address apartment number.
   */
  private String apartmentNumber;
  /**
   * User address building.
   */
  private String building;
  /**
   * User address street.
   */
  private String street;
  /**
   * User address city.
   */
  private String city;
  /**
   * User address country name.
   */
  private String countryName;
  /**
   * User address country subdivision name.
   */
  private String countrySubdivisionName;

  /**
   * Constructor with Payment sheet entity.
   *
   * @param paymentSheetEntity The payment sheet entity.
   */
  public PaymentSheetResponse(
        final PersistentPaymentSheetEntity paymentSheetEntity
  ) {
    super();
    this.paymentSheetIdentifier = paymentSheetEntity.getId();
    this.userName = paymentSheetEntity.getUserOwner().getName();
    this.userFirstSurname = paymentSheetEntity.getUserOwner().getFirstSurname();
    this.userSecondSurname =
          paymentSheetEntity.getUserOwner().getSecondSurname();
    this.userDNI = paymentSheetEntity.getUserOwner().getDni();
    this.reason = paymentSheetEntity.getReason();
    this.place = paymentSheetEntity.getPlace();
    this.startDate = paymentSheetEntity.getStartDate();
    this.endDate = paymentSheetEntity.getEndDate();
    this.postalCode =
          paymentSheetEntity.getUserOwner().getAddress().getPostalCode();
    this.apartmentNumber =
          paymentSheetEntity.getUserOwner().getAddress().getApartmentNumber();
    this.building =
          paymentSheetEntity.getUserOwner().getAddress().getBuilding();
    this.street = paymentSheetEntity.getUserOwner().getAddress().getStreet();
    this.city = paymentSheetEntity.getUserOwner().getAddress().getCity();
    this.countryName = paymentSheetEntity.getUserOwner().getAddress()
          .getCountry().getShortName();
    this.countrySubdivisionName = paymentSheetEntity.getUserOwner().getAddress()
          .getCountrySubdivision().getName();
    if (paymentSheetEntity.getSelfVehicle() != null) {
      this.selfVehicle = new SelfVehicleDataResponse(
            paymentSheetEntity.getSelfVehicle().getPlaces(),
            paymentSheetEntity.getSelfVehicle().getDistance(),
            paymentSheetEntity.getSelfVehicle().getKmPrice()
      );
    }
    if (paymentSheetEntity.getFoodHousing() != null) {
      this.foodHousing = new FoodHousingDataResponse(
            paymentSheetEntity.getFoodHousing().getAmountDays(),
            paymentSheetEntity.getFoodHousing().getDayPrice(),
            paymentSheetEntity.getFoodHousing().getOvernight()
      );
    }
    if (!paymentSheetEntity.getRegularTransports().isEmpty()) {
      final var regularTransports = paymentSheetEntity.getRegularTransports();
      final List<PersistentRegularTransportEntity> lista = new ArrayList<>();
      lista.addAll(regularTransports);
      regularTransportList = new RegularTransportListResponse(lista);
    }

  }
}
