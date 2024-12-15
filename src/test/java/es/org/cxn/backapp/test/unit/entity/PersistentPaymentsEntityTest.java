package es.org.cxn.backapp.test.unit.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.PersistentPaymentsEntity;

class PersistentPaymentsEntityTest {

    @Test
    void testGettersAndSetters() {
        final var entity = new PersistentPaymentsEntity();
        final var amount = BigDecimal.valueOf(288L);
        final var category = "Pago cuota socio";
        final var description = "Pago cuota de socio 2025";
        final var creationDate = LocalDateTime.of(2024, 12, 12, 12, 20);
        final var paidDate = LocalDateTime.of(2024, 12, 12, 14, 10);
        final var state = "unpaid";
        final var title = "cuota socio";
        final var userDni = "32897644X";

        entity.setAmount(amount);
        entity.setCategory(category);
        entity.setDescription(description);
        entity.setCreatedAt(creationDate);
        entity.setPaidAt(paidDate);
        entity.setState(state);
        entity.setTitle(title);
        entity.setUserDni(userDni);

        Assertions.assertEquals(amount, entity.getAmount(), "amount payment price.");
        Assertions.assertEquals(category, entity.getCategory(), "category payment price");
        Assertions.assertEquals(description, entity.getDescription(), "category payment price");
        Assertions.assertEquals(creationDate, entity.getCreatedAt(), "payments creation date");
        Assertions.assertEquals(paidDate, entity.getPaidAt(), "Payment date");
        Assertions.assertEquals(state, entity.getState(), "payment state");
        Assertions.assertEquals(title, entity.getTitle(), "payment title");
        Assertions.assertEquals(userDni, entity.getUserDni(), "user dni title");

    }

}
