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

import java.util.Objects;

import es.org.cxn.backapp.model.RegularTransportEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * RegularTransport Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "RegularTransport")
@Table(name = "regulartransport")
public class PersistentRegularTransportEntity
        implements RegularTransportEntity {

    /**
     * serial UID.
     */
    @Transient
    private static final long serialVersionUID = -8284511511230367460L;

    /**
     * Regular transport identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    /**
     * Regular transport category.
     */
    @Column(name = "category", nullable = false, unique = false)
    private String category = "";

    /**
     * Regular transport description.
     */
    @Column(name = "description", nullable = false, unique = false)
    private String description = "";

    /**
     * The regular transport assigned invoice.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private PersistentInvoiceEntity transportInvoice;

    /**
     * The regular transport assigned payment sheet.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_sheet_id", nullable = false)
    private PersistentPaymentSheetEntity paymentSheet;

    /**
     * Constructs a company entity.
     */
    public PersistentRegularTransportEntity() {
        super();
    }

    /**
     * @param category
     * @param description
     * @param transportInvoice
     * @param paymentSheet
     */
    public PersistentRegularTransportEntity(
            String category, String description,
            PersistentInvoiceEntity transportInvoice,
            PersistentPaymentSheetEntity paymentSheet
    ) {
        super();
        this.category = category;
        this.description = description;
        this.transportInvoice = transportInvoice;
        this.paymentSheet = paymentSheet;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public PersistentInvoiceEntity getTransportInvoice() {
        return transportInvoice;
    }

    @Override
    public PersistentPaymentSheetEntity getPaymentSheet() {
        return paymentSheet;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setTransportInvoice(PersistentInvoiceEntity transportInvoice) {
        this.transportInvoice = transportInvoice;
    }

    @Override
    public void setPaymentSheet(PersistentPaymentSheetEntity paymentSheet) {
        this.paymentSheet = paymentSheet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, description, id);
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
        var other = (PersistentRegularTransportEntity) obj;
        return Objects.equals(category, other.category)
                && Objects.equals(description, other.description)
                && Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "PersistentRegularTransportEntity [id=" + id + ", category="
                + category + ", description=" + description + "]";
    }

}
