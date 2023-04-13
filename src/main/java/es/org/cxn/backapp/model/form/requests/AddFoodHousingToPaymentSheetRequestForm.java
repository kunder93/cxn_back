package es.org.cxn.backapp.model.form.requests;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the form used by controller as request of create company.
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller.
 * <p>
 * Includes Java validation annotations, for applying binding validation. This
 * way the controller will make sure it receives all the required data.
 *
 * @author Santiago Paz.
 */
public class AddFoodHousingToPaymentSheetRequestForm implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3100089900015947999L;

    /**
     * The regular transport Category
     */
    private Integer amountDays;
    /**
     * The regular transport description.
     */
    private float dayPrice;

    private Boolean overnight;

    /**
     *
     */
    public AddFoodHousingToPaymentSheetRequestForm() {
        super();
    }

    /**
     * @param amountDays
     * @param dayPrice
     * @param overnight
     */
    public AddFoodHousingToPaymentSheetRequestForm(
            Integer amountDays, float dayPrice, Boolean overnight
    ) {
        super();
        this.amountDays = amountDays;
        this.dayPrice = dayPrice;
        this.overnight = overnight;
    }

    public Integer getAmountDays() {
        return amountDays;
    }

    public float getDayPrice() {
        return dayPrice;
    }

    public Boolean getOvernight() {
        return overnight;
    }

    public void setAmountDays(Integer amountDays) {
        this.amountDays = amountDays;
    }

    public void setDayPrice(float dayPrice) {
        this.dayPrice = dayPrice;
    }

    public void setOvernight(Boolean overnight) {
        this.overnight = overnight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountDays, dayPrice, overnight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        var other = (AddFoodHousingToPaymentSheetRequestForm) obj;
        return Objects.equals(amountDays, other.amountDays)
                && Float.floatToIntBits(dayPrice) == Float
                        .floatToIntBits(other.dayPrice)
                && Objects.equals(overnight, other.overnight);
    }

    @Override
    public String toString() {
        return "AddFoodHousingToPaymentSheetRequestForm [amountDays="
                + amountDays + ", dayPrice=" + dayPrice + ", overnight="
                + overnight + "]";
    }

}
