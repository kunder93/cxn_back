
package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;
import es.org.cxn.backapp.model.persistence.PersistentRegularTransportEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
   * Main empty constructor.
   */
  public PaymentSheetResponse() {
    super();
  }

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
      if (!paymentSheetEntity.getRegularTransports().isEmpty()) {
        var regularTransports = paymentSheetEntity.getRegularTransports();
        List<PersistentRegularTransportEntity> lista = new ArrayList<>();
        lista.addAll(regularTransports);
        regularTransportList = new RegularTransportListResponse(lista);
      }
    }
  }

  /**
   * Get payment sheet regular transport list.
   *
   * @return The regular transport list.
   */
  public RegularTransportListResponse getRegularTransportList() {
    return regularTransportList;
  }

  /**
   * Set payment sheet regular transport list.
   *
   * @param regularTransportList The regular transport list.
   */
  public void setRegularTransportList(
        final RegularTransportListResponse regularTransportList
  ) {
    this.regularTransportList = regularTransportList;
  }

  /**
   * Return payment sheet food housing.
   *
   * @return the food housing.
   */
  public FoodHousingDataResponse getFoodHousing() {
    return foodHousing;
  }

  /**
   * Get payment sheet postal user address.
   *
   * @return The postal code.
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * Set payment sheet user postal code address.
   *
   * @param postalCode The postal code.
   */
  public void setPostalCode(final String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * Get payment sheet user apartment number address.
   *
   * @return The apartment number.
   */
  public String getApartmentNumber() {
    return apartmentNumber;
  }

  /**
   * Set payment sheet user apartment number address.
   *
   * @param apartmentNumber The apartment number.
   */
  public void setApartmentNumber(final String apartmentNumber) {
    this.apartmentNumber = apartmentNumber;
  }

  /**
   * Get payment sheet user building address.
   *
   * @return The building.
   */
  public String getBuilding() {
    return building;
  }

  /**
   * Set payment sheet user building address.
   *
   * @param building The building.
   */
  public void setBuilding(final String building) {
    this.building = building;
  }

  /**
   * Get payment sheet user street address.
   *
   * @return The street.
   */
  public String getStreet() {
    return street;
  }

  /**
   * Set payment sheet user street address.
   *
   * @param street The street.
   */
  public void setStreet(final String street) {
    this.street = street;
  }

  /**
   * The payment sheet user city address.
   *
   * @return The city.
   */
  public String getCity() {
    return city;
  }

  /**
   * Set payment sheet user city address.
   *
   * @param city The city.
   */
  public void setCity(final String city) {
    this.city = city;
  }

  /**
   * Get payment sheet user country name address.
   *
   * @return The country name.
   */
  public String getCountryName() {
    return countryName;
  }

  /**
   * Set payment sheet user country name address.
   *
   * @param countryName The country name.
   */
  public void setCountryName(final String countryName) {
    this.countryName = countryName;
  }

  /**
   * Get payment sheet user country subdivision name address.
   *
   * @return The country subdivision name.
   */
  public String getCountrySubdivisionName() {
    return countrySubdivisionName;
  }

  /**
   * Set payment sheet user country subdivision name address.
   *
   * @param countrySubdivisionName The country subdivision name.
   */
  public void setCountrySubdivisionName(final String countrySubdivisionName) {
    this.countrySubdivisionName = countrySubdivisionName;
  }

  /**
   * Set payment sheet food housing response.
   *
   * @param foodHousing The food housing response.
   */
  public void setFoodHousing(final FoodHousingDataResponse foodHousing) {
    this.foodHousing = foodHousing;
  }

  /**
   * Get payment sheet self vehicle data response.
   *
   * @return The self vehicle data response.
   */
  public SelfVehicleDataResponse getSelfVehicle() {
    return selfVehicle;
  }

  /**
   * Set payment sheet self vehicle data response.
   *
   * @param selfVehicle The self vehicle data response.
   */
  public void setSelfVehicle(final SelfVehicleDataResponse selfVehicle) {
    this.selfVehicle = selfVehicle;
  }

  /**
   * Get payment sheet identifier.
   *
   * @return The payment sheet identifier.
   */
  public Integer getPaymentSheetIdentifier() {
    return paymentSheetIdentifier;
  }

  /**
   * Get payment sheet user name.
   *
   * @return The user name.
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Get payment sheet user first surname.
   *
   * @return The payment sheet user first surname.
   */
  public String getUserFirstSurname() {
    return userFirstSurname;
  }

  /**
   * Get payment sheet user second surname.
   *
   * @return The payment sheet user second surname.
   */
  public String getUserSecondSurname() {
    return userSecondSurname;
  }

  /**
   * Get payment sheet user dni.
   *
   * @return The payment sheet user dni.
   */
  public String getUserDNI() {
    return userDNI;
  }

  /**
   * Get payment sheet event reason.
   *
   * @return The payment sheet event reason.
   */
  public String getReason() {
    return reason;
  }

  /**
   * Get payment sheet event place.
   *
   * @return The payment sheet event place.
   */
  public String getPlace() {
    return place;
  }

  /**
   * Get payment sheet event start date.
   *
   * @return The payment sheet event start date.
   */
  public LocalDate getStartDate() {
    return startDate;
  }

  /**
   * Get payment sheet event end date.
   *
   * @return The payment sheet event end date.
   */
  public LocalDate getEndDate() {
    return endDate;
  }

  /**
   * Set payment sheet identifier.
   *
   * @param paymentSheetIdentifier The payment sheet identifier.
   */
  public void setPaymentSheetIdentifier(final Integer paymentSheetIdentifier) {
    this.paymentSheetIdentifier = paymentSheetIdentifier;
  }

  /**
   * Set payment sheet user name.
   *
   * @param userName The payment sheet user name.
   */
  public void setUserName(final String userName) {
    this.userName = userName;
  }

  /**
   * Set payment sheet user first surname.
   *
   * @param userFirstSurname The payment sheet user first surname.
   */
  public void setUserFirstSurname(final String userFirstSurname) {
    this.userFirstSurname = userFirstSurname;
  }

  /**
   * Set payment sheet user second surname.
   *
   * @param userSecondSurname The payment sheet user second surname.
   */
  public void setUserSecondSurname(final String userSecondSurname) {
    this.userSecondSurname = userSecondSurname;
  }

  /**
   * Set payment sheet user dni.
   *
   * @param userDNI The payment sheet user dni.
   */
  public void setUserDNI(final String userDNI) {
    this.userDNI = userDNI;
  }

  /**
   * Set payment sheet event reason.
   *
   * @param reason The payment sheet event reason.
   */
  public void setReason(final String reason) {
    this.reason = reason;
  }

  /**
   * Set the payment sheet event place.
   *
   * @param place The payment sheet event place.
   */
  public void setPlace(final String place) {
    this.place = place;
  }

  /**
   * Set payment sheet event start date.
   *
   * @param startDate The payment sheet event start date.
   */
  public void setStartDate(final LocalDate startDate) {
    this.startDate = startDate;
  }

  /**
   * Set payment sheet event end date.
   *
   * @param endDate The payment sheet event end date.
   */
  public void setEndDate(final LocalDate endDate) {
    this.endDate = endDate;
  }

}
