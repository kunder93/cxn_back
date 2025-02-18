
package es.org.cxn.backapp.test.unit.response;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.form.responses.UserUpdateResponseForm;
import es.org.cxn.backapp.model.persistence.user.UserProfile;

/**
 * Unit tests for the {@link UserUpdateResponse} class.
 *
 * <p>
 * This test class verifies the correctness of user update responses by testing
 * different scenarios, including name, surname, gender, and birthdate updates.
 * </p>
 *
 * <p>
 * Test data constants are defined for consistency and readability.
 * </p>
 */
class UserUpdateResponseTest {
    /** The year used for a sample birthdate: 1985. */
    private static final int TEST_YEAR_1985 = 1985;

    /** The month used for a sample birthdate: May (5). */
    private static final int TEST_MONTH_MAY = 5;

    /** The day used for a sample birthdate: 15. */
    private static final int TEST_DAY_15 = 15;

    /** The year used for another sample birthdate: 1990. */
    private static final int TEST_YEAR_1990 = 1990;

    /** The month used for another sample birthdate: January (1). */
    private static final int TEST_MONTH_JANUARY = 1;

    /** The day used for another sample birthdate: 1. */
    private static final int TEST_DAY_1 = 1;

    /** A sample first name: Alice. */
    private static final String NAME_ALICE = "Alice";

    /** A sample surname: Brown. */
    private static final String SURNAME_BROWN = "Brown";

    /** Another sample surname: Johnson. */
    private static final String SURNAME_JOHNSON = "Johnson";

    /** A sample gender: Female. */
    private static final String GENDER_FEMALE = "Female";

    /** A sample gender: Male. */
    private static final String GENDER_MALE = "Male";

    /** A common test birthdate: May 15, 1985. */
    private static final LocalDate BIRTHDATE_1985 = LocalDate.of(TEST_YEAR_1985, TEST_MONTH_MAY, TEST_DAY_15);

    /** Another common test birthdate: January 1, 1990. */
    private static final LocalDate BIRTHDATE_1990 = LocalDate.of(TEST_YEAR_1990, TEST_MONTH_JANUARY, TEST_DAY_1);

    @Test
    void testConstructorWithNullValues() {
        UserUpdateResponseForm responseForm = new UserUpdateResponseForm(null, null, null, null, null);

        assertEquals(null, responseForm.name());
        assertEquals(null, responseForm.firstSurname());
        assertEquals(null, responseForm.secondSurname());
        assertEquals(null, responseForm.birthDate());
        assertEquals(null, responseForm.gender());
    }

    @Test
    void testConstructorWithUserEntity() {
        UserEntity userEntity = Mockito.mock(UserEntity.class);
        UserProfile profile = Mockito.mock(UserProfile.class);

        Mockito.when(userEntity.getProfile()).thenReturn(profile);
        Mockito.when(profile.getName()).thenReturn(NAME_ALICE);
        Mockito.when(profile.getFirstSurname()).thenReturn(SURNAME_BROWN);
        Mockito.when(profile.getSecondSurname()).thenReturn(SURNAME_JOHNSON);
        Mockito.when(profile.getBirthDate()).thenReturn(BIRTHDATE_1985);
        Mockito.when(profile.getGender()).thenReturn(GENDER_FEMALE);

        UserUpdateResponseForm responseForm = new UserUpdateResponseForm(userEntity);

        assertEquals(NAME_ALICE, responseForm.name());
        assertEquals(SURNAME_BROWN, responseForm.firstSurname());
        assertEquals(SURNAME_JOHNSON, responseForm.secondSurname());
        assertEquals(BIRTHDATE_1985, responseForm.birthDate());
        assertEquals(GENDER_FEMALE, responseForm.gender());
    }

    @Test
    void testConstructorWithUserEntityAndNullProfile() {
        UserEntity userEntity = Mockito.mock(UserEntity.class);
        Mockito.when(userEntity.getProfile()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> new UserUpdateResponseForm(userEntity));

    }

    @Test
    void testConstructorWithValidValues() {
        UserUpdateResponseForm responseForm = new UserUpdateResponseForm(NAME_ALICE, SURNAME_BROWN, SURNAME_JOHNSON,
                BIRTHDATE_1990, GENDER_MALE);

        assertEquals(NAME_ALICE, responseForm.name());
        assertEquals(SURNAME_BROWN, responseForm.firstSurname());
        assertEquals(SURNAME_JOHNSON, responseForm.secondSurname());
        assertEquals(BIRTHDATE_1990, responseForm.birthDate());
        assertEquals(GENDER_MALE, responseForm.gender());
    }
}
