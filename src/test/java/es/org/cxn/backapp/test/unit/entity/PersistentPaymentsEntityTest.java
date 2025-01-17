package es.org.cxn.backapp.test.unit.entity;

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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.payments.PaymentsCategory;
import es.org.cxn.backapp.model.persistence.payments.PaymentsState;
import es.org.cxn.backapp.model.persistence.payments.PersistentPaymentsEntity;

/**
 * Unit test class for {@link PersistentPaymentsEntity}. This class verifies the
 * functionality of getters and setters for the fields in the
 * {@link PersistentPaymentsEntity} class.
 */
class PersistentPaymentsEntityTest {

    /**
     * Test the getters and setters of the {@link PersistentPaymentsEntity} class.
     * This test verifies that all the fields of the
     * {@link PersistentPaymentsEntity} class are set correctly through the setters
     * and retrieved properly using the getters.
     */
    @Test
    @DisplayName("Test Getters and Setters for PersistentPaymentsEntity")
    void testGettersAndSetters() {
        // Create the entity and initialize fields
        final var entity = new PersistentPaymentsEntity();
        final var amount = BigDecimal.valueOf(288L);
        final PaymentsCategory category = PaymentsCategory.MEMBERSHIP_PAYMENT;
        final var description = "Pago cuota de socio 2025";
        final var creationDate = LocalDateTime.of(2024, 12, 12, 12, 20);
        final var paidDate = LocalDateTime.of(2024, 12, 12, 14, 10);
        final PaymentsState state = PaymentsState.UNPAID;
        final var title = "cuota socio";
        final var userDni = "32897644X";

        // Set values using setters
        entity.setAmount(amount);
        entity.setCategory(category);
        entity.setDescription(description);
        entity.setCreatedAt(creationDate);
        entity.setPaidAt(paidDate);
        entity.setState(state);
        entity.setTitle(title);
        entity.setUserDni(userDni);

        // Validate values using getters
        Assertions.assertEquals(amount, entity.getAmount(), "The amount should match the set value.");
        Assertions.assertEquals(category, entity.getCategory(), "The category should match the set value.");
        Assertions.assertEquals(description, entity.getDescription(), "The description should match the set value.");
        Assertions.assertEquals(creationDate, entity.getCreatedAt(), "The creation date should match the set value.");
        Assertions.assertEquals(paidDate, entity.getPaidAt(), "The paid date should match the set value.");
        Assertions.assertEquals(state, entity.getState(), "The payment state should match the set value.");
        Assertions.assertEquals(title, entity.getTitle(), "The payment title should match the set value.");
        Assertions.assertEquals(userDni, entity.getUserDni(), "The user's DNI should match the set value.");
    }
}
