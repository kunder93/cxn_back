package es.org.cxn.backapp.model.persistence.payments;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import es.org.cxn.backapp.model.PaymentsEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersistentPaymentsEntity is a JPA entity representing a payment record in the
 * database. This class implements the PaymentsEntity interface and maps to the
 * "payments" table.
 *
 * <p>
 * Attributes of this entity include an identifier, title, description, amount,
 * and more. Each attribute corresponds to a column in the database table.
 *
 * <p>
 * This class is annotated with the {@code @Entity} annotation and managed by
 * the JPA persistence provider. It uses Lombok's {@code @Data} to automatically
 * generate getters, setters, and other common methods.
 *
 * <p>
 * This class is licensed under the MIT License.
 *
 * @see PaymentsEntity
 */
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersistentPaymentsEntity implements PaymentsEntity {

    /**
     * The maximum allowed length for the title of the payment. This corresponds to
     * the {@code title} column in the database.
     */
    private static final int TITLE_MAX_LENGTH = 80;

    /**
     * The maximum allowed length for the user DNI (identifier). This corresponds to
     * the {@code user_dni} column in the database.
     */
    private static final int USER_DNI_MAX_LENGTH = 20;

    /**
     * The precision of the amount field, representing the total number of digits
     * that can be stored, both to the left and right of the decimal point. This
     * corresponds to the {@code amount} column in the database.
     */
    private static final int AMOUNT_PRECISION = 10;

    /**
     * The scale of the amount field, representing the number of digits to the right
     * of the decimal point. This corresponds to the {@code amount} column in the
     * database.
     */
    private static final int AMOUNT_SCALE = 2;

    /**
     * Serial version UID for serialization compatibility.
     */
    @Transient
    private static final long serialVersionUID = -6782220123937248455L;

    /**
     * The unique identifier for each payment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    /**
     * Title of the payment.
     */
    @Column(name = "title", nullable = false, length = TITLE_MAX_LENGTH)
    private String title;

    /**
     * Detailed description of the payment.
     */
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * State of the payment.
     */
    @Column(name = "state", nullable = false)
    private PaymentsState state;

    /**
     * Category of the payment.
     */
    @Column(name = "category", nullable = false)
    private PaymentsCategory category;

    /**
     * Date and time when the payment was made.
     */
    @Column(name = "paid_at", nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime paidAt;

    /**
     * Monetary amount of the payment.
     */
    @Column(name = "amount", nullable = false, precision = AMOUNT_PRECISION, scale = AMOUNT_SCALE)
    private BigDecimal amount;

    /**
     * The DNI of the user associated with the payment.
     */
    @Column(name = "user_dni", nullable = false, length = USER_DNI_MAX_LENGTH)
    private String userDni;

    /**
     * The payment time when is created.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
