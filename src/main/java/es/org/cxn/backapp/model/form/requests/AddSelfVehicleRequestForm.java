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
public class AddSelfVehicleRequestForm implements Serializable {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3112385111515947705L;

    /**
     * The regular transport Category
     */
    private String places;
    /**
     * The regular transport description.
     */
    private float distance;

    private double kmPrice;

    /**
     * @param places
     * @param distance
     * @param kmPrice
     */
    public AddSelfVehicleRequestForm(
            String places, float distance, double kmPrice
    ) {
        super();
        this.places = places;
        this.distance = distance;
        this.kmPrice = kmPrice;
    }

    /**
     *
     */
    public AddSelfVehicleRequestForm() {
        super();
    }

    public String getPlaces() {
        return places;
    }

    public float getDistance() {
        return distance;
    }

    public double getKmPrice() {
        return kmPrice;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setKmPrice(double kmPrice) {
        this.kmPrice = kmPrice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, kmPrice, places);
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
        var other = (AddSelfVehicleRequestForm) obj;
        return Float.floatToIntBits(distance) == Float
                .floatToIntBits(other.distance)
                && Double.doubleToLongBits(kmPrice) == Double
                        .doubleToLongBits(other.kmPrice)
                && Objects.equals(places, other.places);
    }

    @Override
    public String toString() {
        return "AddSelfVehicleRequestForm [places=" + places + ", distance="
                + distance + ", kmPrice=" + kmPrice + "]";
    }
}