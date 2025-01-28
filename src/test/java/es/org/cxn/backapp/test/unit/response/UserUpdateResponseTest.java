
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

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import es.org.cxn.backapp.model.UserEntity;
import es.org.cxn.backapp.model.form.responses.UserUpdateResponseForm;
import es.org.cxn.backapp.model.persistence.user.UserProfile;

class UserUpdateResponseTest {

    @Test
    void testConstructorWithNullValues() {
        // Given
        String name = null;
        String firstSurname = null;
        String secondSurname = null;
        LocalDate birthDate = null;
        String gender = null;

        // When
        UserUpdateResponseForm responseForm = new UserUpdateResponseForm(name, firstSurname, secondSurname, birthDate,
                gender);

        // Then
        assertEquals(null, responseForm.name());
        assertEquals(null, responseForm.firstSurname());
        assertEquals(null, responseForm.secondSurname());
        assertEquals(null, responseForm.birthDate());
        assertEquals(null, responseForm.gender());
    }

    @Test
    void testConstructorWithUserEntity() {
        // Given
        UserEntity userEntity = Mockito.mock(UserEntity.class);
        UserProfile profile = Mockito.mock(UserProfile.class);

        Mockito.when(userEntity.getProfile()).thenReturn(profile);
        Mockito.when(profile.getName()).thenReturn("Alice");
        Mockito.when(profile.getFirstSurname()).thenReturn("Brown");
        Mockito.when(profile.getSecondSurname()).thenReturn("Johnson");
        Mockito.when(profile.getBirthDate()).thenReturn(LocalDate.of(1985, 5, 15));
        Mockito.when(profile.getGender()).thenReturn("Female");

        // When
        UserUpdateResponseForm responseForm = new UserUpdateResponseForm(userEntity);

        // Then
        assertEquals("Alice", responseForm.name());
        assertEquals("Brown", responseForm.firstSurname());
        assertEquals("Johnson", responseForm.secondSurname());
        assertEquals(LocalDate.of(1985, 5, 15), responseForm.birthDate());
        assertEquals("Female", responseForm.gender());
    }

    @Test
    void testConstructorWithUserEntityAndNullProfile() {
        // Given
        UserEntity userEntity = Mockito.mock(UserEntity.class);

        Mockito.when(userEntity.getProfile()).thenReturn(null);

        // When / Then
        try {
            new UserUpdateResponseForm(userEntity);
        } catch (NullPointerException e) {
            assertEquals(
                    "Cannot invoke \"es.org.cxn.backapp.model.persistence.user.UserProfile.getName()\" because the return value of \"es.org.cxn.backapp.model.UserEntity.getProfile()\" is null",
                    e.getMessage());
        }
    }

    @Test
    void testConstructorWithValidValues() {
        // Given
        String name = "John";
        String firstSurname = "Doe";
        String secondSurname = "Smith";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String gender = "Male";

        // When
        UserUpdateResponseForm responseForm = new UserUpdateResponseForm(name, firstSurname, secondSurname, birthDate,
                gender);

        // Then
        assertEquals(name, responseForm.name());
        assertEquals(firstSurname, responseForm.firstSurname());
        assertEquals(secondSurname, responseForm.secondSurname());
        assertEquals(birthDate, responseForm.birthDate());
        assertEquals(gender, responseForm.gender());
    }
}