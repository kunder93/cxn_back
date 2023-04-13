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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import es.org.cxn.backapp.model.FoodHousingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Self vehicle entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "FoodHousing")
@Table(name = "foodhousing")
public class PersistentFoodHousingEntity implements FoodHousingEntity {

    /**
     * serial UID.
     */
    @Transient
    private static final long serialVersionUID = -8275074649983253300L;

    /**
     * Entity's identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    /**
     * Places where travel with the vehicle.
     *
     */
    @Column(name = "amountdays", nullable = false, unique = false)
    private Integer amountDays;

    /**
     *
     *
     */
    @Column(name = "dayprice", nullable = false, unique = false)
    private double dayPrice;

    /**
     *
     */
    @Column(name = "overnight", nullable = false, unique = false)
    private Boolean overnight;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_sheet_id", nullable = true)
    private PersistentPaymentSheetEntity paymentSheet;

    /**
     * Roles associated with this user.
     */
    @OneToMany(mappedBy = "foodHousing")
    private Set<PersistentInvoiceEntity> invoices = new HashSet<>();

    /**
     * Constructs a company entity.
     */
    public PersistentFoodHousingEntity() {
        super();
    }

    /**
     * @param amountDays
     * @param dayPrice
     * @param overnight
     * @param paymentSheet
     */
    public PersistentFoodHousingEntity(
            Integer amountDays, double dayPrice, Boolean overnight,
            PersistentPaymentSheetEntity paymentSheet
    ) {
        super();
        this.amountDays = amountDays;
        this.dayPrice = dayPrice;
        this.overnight = overnight;
        this.paymentSheet = paymentSheet;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Integer getAmountDays() {
        return amountDays;
    }

    @Override
    public double getDayPrice() {
        return dayPrice;
    }

    @Override
    public Boolean getOvernight() {
        return overnight;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setAmountDays(Integer amountDays) {
        this.amountDays = amountDays;
    }

    @Override
    public void setDayPrice(double dayPrice) {
        this.dayPrice = dayPrice;
    }

    @Override
    public void setOvernight(Boolean overnight) {
        this.overnight = overnight;
    }

    @Override
    public PersistentPaymentSheetEntity getPaymentSheet() {
        return paymentSheet;
    }

    @Override
    public Set<PersistentInvoiceEntity> getInvoices() {
        return new HashSet<>(invoices);
    }

    @Override
    public void setPaymentSheet(PersistentPaymentSheetEntity paymentSheet) {
        this.paymentSheet = paymentSheet;
    }

    @Override
    public void setInvoices(Set<PersistentInvoiceEntity> invoices) {
        this.invoices = new HashSet<>(invoices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountDays, dayPrice, id, overnight);
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
        var other = (PersistentFoodHousingEntity) obj;
        return Objects.equals(amountDays, other.amountDays)
                && Double.doubleToLongBits(dayPrice) == Double
                        .doubleToLongBits(other.dayPrice)
                && Objects.equals(id, other.id)
                && Objects.equals(overnight, other.overnight);
    }

    @Override
    public String toString() {
        return "PersistentFoodHousingEntity [id=" + id + ", amountDays="
                + amountDays + ", dayPrice=" + dayPrice + ", overnight="
                + overnight + "]";
    }

}
