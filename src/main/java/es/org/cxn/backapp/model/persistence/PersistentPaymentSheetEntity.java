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

package es.org.cxn.backapp.model.persistence;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import es.org.cxn.backapp.model.PaymentSheetEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Operation Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "PaymentSheet")
@Table(name = "paymentsheet")
public class PersistentPaymentSheetEntity implements PaymentSheetEntity {

    /**
     * serial UID.
     */
    @Transient
    private static final long serialVersionUID = -8238192117130392730L;

    /**
     * Payment sheet identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    /**
     * Event reason of the payment sheet.
     *
     */
    @Column(name = "reason", nullable = false, unique = false)
    private String reason = "";

    /**
     * Event place of the payment sheet.
     *
     */
    @Column(name = "place", nullable = false, unique = false)
    private String place = "";

    /**
     * Event starting date of the payment sheet.
     *
     */
    @Column(name = "start_date", nullable = false, unique = false)
    private LocalDate startDate;

    /**
     * Event end date of the payment sheet.
     */
    @Column(name = "end_date", nullable = false, unique = false)
    private LocalDate endDate;

    /**
     * The payment sheet user owner.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private PersistentUserEntity userOwner;

    /**
     * Roles associated with this user.
     */
    @OneToMany(mappedBy = "paymentSheet")
    private Set<PersistentRegularTransportEntity> regularTransports = new HashSet<>();

    /**
     * Self vehicle transport if have. Can be null.
     */
    @OneToOne(mappedBy = "paymentSheet")
    private PersistentSelfVehicleEntity selfVehicle;

    /**
     * Food and housing if have.Can be null.
     */
    @OneToOne(mappedBy = "paymentSheet")
    private PersistentFoodHousingEntity foodHousing;

    /**
     * Constructs a company entity.
     */
    public PersistentPaymentSheetEntity() {
        super();
    }

    /**
     * @param reason
     * @param place
     * @param startDate
     * @param endDate
     * @param userOwner
     */
    public PersistentPaymentSheetEntity(
            String reason, String place, LocalDate startDate, LocalDate endDate,
            PersistentUserEntity userOwner
    ) {
        super();
        this.reason = reason;
        this.place = place;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userOwner = userOwner;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public String getPlace() {
        return place;
    }

    @Override
    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public PersistentUserEntity getUserOwner() {
        return userOwner;
    }

    @Override
    public void setId(Integer value) {
        this.id = value;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public void setUserOwner(PersistentUserEntity value) {
        this.userOwner = value;
    }

    @Override
    public PersistentSelfVehicleEntity getSelfVehicle() {
        return selfVehicle;
    }

    @Override
    public PersistentFoodHousingEntity getFoodHousing() {
        return foodHousing;
    }

    @Override
    public void setFoodHousing(PersistentFoodHousingEntity foodHousing) {
        this.foodHousing = foodHousing;
    }

    @Override
    public Set<PersistentRegularTransportEntity> getRegularTransports() {
        return new HashSet<>(regularTransports);
    }

    @Override
    public void setRegularTransports(
            Set<PersistentRegularTransportEntity> regularTransports
    ) {
        this.regularTransports = new HashSet<>(regularTransports);
    }

    @Override
    public void setSelfVehicle(PersistentSelfVehicleEntity selfVehicle) {
        this.selfVehicle = selfVehicle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(endDate, id, place, reason, startDate);
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
        var other = (PersistentPaymentSheetEntity) obj;
        return Objects.equals(endDate, other.endDate)
                && Objects.equals(id, other.id)
                && Objects.equals(place, other.place)
                && Objects.equals(reason, other.reason)
                && Objects.equals(startDate, other.startDate);
    }

    @Override
    public String toString() {
        return "PersistentPaymentSheetEntity [id=" + id + ", reason=" + reason
                + ", place=" + place + ", startDate=" + startDate + ", endDate="
                + endDate + "]";
    }

}
