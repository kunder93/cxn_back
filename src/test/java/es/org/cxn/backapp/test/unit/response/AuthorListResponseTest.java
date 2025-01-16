
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.AuthorEntity;
import es.org.cxn.backapp.model.form.responses.AuthorListResponse;
import es.org.cxn.backapp.model.form.responses.AuthorResponse;
import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;

class AuthorListResponseTest {
    private PersistentAuthorEntity authorEntityMock1;
    private PersistentAuthorEntity authorEntityMock2;
    private List<AuthorEntity> authorEntities;

    @BeforeEach
    void setUp() {
        authorEntityMock1 = mock(PersistentAuthorEntity.class);
        authorEntityMock2 = mock(PersistentAuthorEntity.class);

        // Mock some data
        when(authorEntityMock1.getIdentifier()).thenReturn(1L);
        when(authorEntityMock1.getFirstName()).thenReturn("Author One");

        when(authorEntityMock2.getIdentifier()).thenReturn(2L);
        when(authorEntityMock2.getFirstName()).thenReturn("Author Two");

        authorEntities = Arrays.asList(authorEntityMock1, authorEntityMock2);
    }

    @Test
    void testAuthorListResponseConstructor() {
        // Act
        AuthorListResponse response = AuthorListResponse.from(authorEntities);

        // Assert
        assertNotNull(response.authorList());
        assertEquals(2, response.authorList().size());
    }

    @Test
    void testAuthorListResponseEquality() {
        // Arrange
        AuthorListResponse response1 = AuthorListResponse.from(authorEntities);
        AuthorListResponse response2 = AuthorListResponse.from(authorEntities);

        // Act & Assert
        assertEquals(response1, response2, "Two responses with the same data should be equal");
    }

    @Test
    void testAuthorListResponseFactoryMethod() {
        // Act
        AuthorListResponse response = AuthorListResponse.from(authorEntities);

        // Assert
        assertNotNull(response.authorList());
        assertEquals(2, response.authorList().size());
    }

    @Test
    void testAuthorListResponseWithDuplicateAuthors() {
        // Arrange
        List<AuthorEntity> duplicateAuthors = Arrays.asList(authorEntityMock1, authorEntityMock1);

        // Act
        AuthorListResponse response = AuthorListResponse.from(duplicateAuthors);

        // Assert
        assertNotNull(response.authorList());
        assertEquals(1, response.authorList().size(), "Set should eliminate duplicates");
    }

    @Test
    void testAuthorListResponseWithEmptyList() {
        // Act
        AuthorListResponse response = AuthorListResponse.from(Collections.emptyList());

        // Assert
        assertNotNull(response.authorList());
        assertEquals(0, response.authorList().size());
    }

    @Test
    void testAuthorListResponseWithNullList() {
        // Act
        AuthorListResponse response = new AuthorListResponse(null);

        // Assert
        assertNotNull(response.authorList());
        assertEquals(0, response.authorList().size());
    }

    @Test
    void testAuthorListResponseWithOneAuthor() {
        // Act
        AuthorListResponse response = AuthorListResponse.from(Collections.singletonList(authorEntityMock1));

        // Assert
        assertNotNull(response.authorList());
        assertEquals(1, response.authorList().size());
    }

    @Test
    void testImmutableSet() {
        // Arrange
        AuthorListResponse response = AuthorListResponse.from(authorEntities);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> response.authorList().add(new AuthorResponse(authorEntityMock1)),
                "Should throw UnsupportedOperationException when trying to modify the set");
    }
}