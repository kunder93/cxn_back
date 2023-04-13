package es.org.cxn.backapp.model.form.responses;

import java.io.Serializable;

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
public class FoodHousingDataResponse implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3110452922215007099L;

    /**
     * The regular transport Category
     */
    private Integer amountDays;
    /**
     * The regular transport description.
     */
    private double dayPrice;

    private boolean overnight;

    /**
     * Main empty constructor.
     */
    public FoodHousingDataResponse() {
        super();
    }

    /**
     * @param amountDays
     * @param dayPrice
     * @param overnight
     */
    public FoodHousingDataResponse(
            Integer amountDays, double dayPrice, boolean overnight
    ) {
        super();
        this.amountDays = amountDays;
        this.dayPrice = dayPrice;
        this.overnight = overnight;
    }

    public Integer getAmountDays() {
        return amountDays;
    }

    public double getDayPrice() {
        return dayPrice;
    }

    public boolean isOvernight() {
        return overnight;
    }

    public void setAmountDays(Integer amountDays) {
        this.amountDays = amountDays;
    }

    public void setDayPrice(double dayPrice) {
        this.dayPrice = dayPrice;
    }

    public void setOvernight(boolean overnight) {
        this.overnight = overnight;
    }

}
