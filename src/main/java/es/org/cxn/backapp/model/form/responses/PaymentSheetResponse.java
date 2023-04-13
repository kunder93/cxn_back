package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;
import java.time.LocalDate;

import es.org.cxn.backapp.model.persistence.PersistentPaymentSheetEntity;

/**
 * Represents the form used by controller as response for requesting one
 * invoice.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public class PaymentSheetResponse implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3183089459011952705L;

    private Integer paymentSheetIdentifier;
    private String userName;
    private String userFirstSurname;
    private String userSecondSurname;
    private String userDNI;
    private String userDomicile;
    private String reason;
    private String place;
    private LocalDate startDate;
    private LocalDate endDate;
    private SelfVehicleDataResponse selfVehicle;
    private FoodHousingDataResponse foodHousing;
    private RegularTransportListResponse regularTransportList = new RegularTransportListResponse();

    /**
     * Main empty constructor.
     */
    public PaymentSheetResponse() {
        super();
    }

    public PaymentSheetResponse(
            PersistentPaymentSheetEntity paymentSheetEntity
    ) {
        super();
        this.paymentSheetIdentifier = paymentSheetEntity.getId();
        this.userName = paymentSheetEntity.getUserOwner().getName();
        this.userFirstSurname = paymentSheetEntity.getUserOwner()
                .getFirstSurname();
        this.userSecondSurname = paymentSheetEntity.getUserOwner()
                .getSecondSurname();
        this.userDNI = "FAKE DNI NOT PROVIDED YET";
        this.userDomicile = "FAKE DOMICILE NOT PROVIDED YET";
        this.reason = paymentSheetEntity.getReason();
        this.place = paymentSheetEntity.getPlace();
        this.startDate = paymentSheetEntity.getStartDate();
        this.endDate = paymentSheetEntity.getEndDate();
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

    }

    public RegularTransportListResponse getRegularTransportList() {
        return regularTransportList;
    }

    public void setRegularTransportList(
            RegularTransportListResponse regularTransportList
    ) {
        this.regularTransportList = regularTransportList;
    }

    public FoodHousingDataResponse getFoodHousing() {
        return foodHousing;
    }

    public void setFoodHousing(FoodHousingDataResponse foodHousing) {
        this.foodHousing = foodHousing;
    }

    public SelfVehicleDataResponse getSelfVehicle() {
        return selfVehicle;
    }

    public void setSelfVehicle(SelfVehicleDataResponse selfVehicle) {
        this.selfVehicle = selfVehicle;
    }

    public Integer getPaymentSheetIdentifier() {
        return paymentSheetIdentifier;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserFirstSurname() {
        return userFirstSurname;
    }

    public String getUserSecondSurname() {
        return userSecondSurname;
    }

    public String getUserDNI() {
        return userDNI;
    }

    public String getUserDomicile() {
        return userDomicile;
    }

    public String getReason() {
        return reason;
    }

    public String getPlace() {
        return place;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setPaymentSheetIdentifier(Integer paymentSheetIdentifier) {
        this.paymentSheetIdentifier = paymentSheetIdentifier;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserFirstSurname(String userFirstSurname) {
        this.userFirstSurname = userFirstSurname;
    }

    public void setUserSecondSurname(String userSecondSurname) {
        this.userSecondSurname = userSecondSurname;
    }

    public void setUserDNI(String userDNI) {
        this.userDNI = userDNI;
    }

    public void setUserDomicile(String userDomicile) {
        this.userDomicile = userDomicile;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}
