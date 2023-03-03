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

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import es.org.cxn.backapp.model.OperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

/**
 * Operation Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago Paz Perez.
 */
@Entity(name = "OperationEntity")
@Table(name = "operations")
public class PersistentOperationEntity implements OperationEntity {

    /**
     * serial UID.
     */
    @Transient
    private static final long serialVersionUID = -8247094114230392000L;

    /**
     * Entity's ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id = -1;

    /**
     * price of the operation.
     *
     */
    @Column(name = "price", nullable = false, unique = false)
    private BigDecimal price;

    /**
     * Description of the operation.
     *
     */
    @Column(name = "description", nullable = false, unique = false)
    private String description = "";

    /**
     * Discount of the operation.
     *
     */
    @Column(name = "discount", nullable = false, unique = false)
    private int discount;

    /**
     * Tax rate of the operation.
     *
     */
    @Column(name = "tax_rate", nullable = false, unique = false)
    private int taxRate;

    /**
     * Date of the operation.
     *
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "operation_date", nullable = false, unique = false)
    private LocalDate operationDate;

    /**
     * Quantity of the operation.
     *
     */
    @Column(name = "quantity", nullable = false, unique = false)
    private int quantity;

    /**
     * Invoice associated with this operation.
     */
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private PersistentInvoiceEntity invoice;

    /**
     * Constructs an example entity.
     */
    public PersistentOperationEntity() {
        super();
    }

    /**
     * Main arguments contructor.
     *
     * @param priceValue         the operation price
     * @param descriptionValue   the operation description
     * @param discountValue      the operation discount
     * @param taxRateValue       the operation tax rate
     * @param operationDateValue the operation date
     * @param quantityValue      the operation quantity
     */
    public PersistentOperationEntity(
            final BigDecimal priceValue, final String descriptionValue,
            final int discountValue, final int taxRateValue,
            final LocalDate operationDateValue, final int quantityValue
    ) {
        super();
        this.price = checkNotNull(
                priceValue, "Received a null pointer as operation price"
        );
        this.description = checkNotNull(
                descriptionValue,
                "Received a null pointer as operation description"
        );
        this.discount = checkNotNull(
                discountValue, "Received a null pointer as operation discount"
        );
        this.taxRate = checkNotNull(
                taxRateValue, "Received a null pointer as tax rate"
        );
        this.operationDate = checkNotNull(
                operationDateValue, "Received a null pointer as operation date"
        );
        this.quantity = checkNotNull(
                quantityValue, "Received a null pointer as operation quantity"
        );

    }

    @Override
    public PersistentInvoiceEntity getInvoice() {
        return invoice;
    }

    @Override
    public void setInvoice(PersistentInvoiceEntity invoice) {
        this.invoice = invoice;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer identifier) {
        id = checkNotNull(identifier, "Received a null pointer as id");
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getDiscount() {
        return discount;
    }

    @Override
    public int getTaxRate() {
        return taxRate;
    }

    @Override
    public LocalDate getOperationDate() {
        return operationDate;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setPrice(BigDecimal value) {
        price = checkNotNull(value, "Received a null pointer as price");

    }

    @Override
    public void setDescription(String value) {
        description = checkNotNull(
                value, "Received a null pointer as description"
        );

    }

    @Override
    public void setDiscount(int value) {
        discount = checkNotNull(value, "Received a null pointer as discount");

    }

    @Override
    public void setTaxRate(int value) {
        taxRate = checkNotNull(value, "Received a null pointer as tax rate");

    }

    @Override
    public void setOperationDate(LocalDate value) {
        operationDate = checkNotNull(
                value, "Received a null pointer as operation date"
        );

    }

    @Override
    public void setQuantity(int value) {
        quantity = checkNotNull(
                value, "Received a null pointer as operation quantity"
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                description, discount, id, operationDate, price, quantity,
                taxRate
        );
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
        var other = (PersistentOperationEntity) obj;
        return Objects.equals(description, other.description)
                && discount == other.discount && Objects.equals(id, other.id)
                && Objects.equals(operationDate, other.operationDate)
                && Objects.equals(price, other.price)
                && quantity == other.quantity && taxRate == other.taxRate;
    }

    @Override
    public String toString() {
        return "PersistentOperationEntity [id=" + id + ", price=" + price
                + ", description=" + description + ", discount=" + discount
                + ", taxRate=" + taxRate + ", operationDate=" + operationDate
                + ", quantity=" + quantity + "]";
    }

}
