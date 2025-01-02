
package es.org.cxn.backapp.test.unit.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;
import es.org.cxn.backapp.model.persistence.PersistentBookEntity;

class PersistentAuthorEntityTest {

    @Test
    void testBooksGetterAndSetter() {
        // Crear una instancia de PersistentAuthorEntity
        var author = new PersistentAuthorEntity();

        // Mockear un objeto PersistentBookEntity
        var book = mock(PersistentBookEntity.class);

        // Agregar el libro al autor
        author.getBooks().add(book);

        assertTrue(author.getBooks().contains(book), "el libro está en la colección de libros del autor");

        // Eliminar el libro de la colección
        author.getBooks().remove(book);

        assertFalse(author.getBooks().contains(book), "el libro ya no está en la colección");
    }

    @Test
    void testEqualsAndHashCode() {
        // Crear dos instancias de PersistentAuthorEntity con los mismos atributos
        var author1 = new PersistentAuthorEntity();
        author1.setIdentifier(1L);
        author1.setFirstName("John");
        author1.setLastName("Doe");
        author1.setNationality("American");

        var author2 = new PersistentAuthorEntity();
        author2.setIdentifier(1L);
        author2.setFirstName("John");
        author2.setLastName("Doe");
        author2.setNationality("American");

        assertEquals(author1, author2, "author1 y author2 son iguales según equals()");
        assertEquals(author2, author1, "author1 y author2 son iguales según equals()");

        assertEquals(author1.hashCode(), author2.hashCode(), "hash codes de author1 y author2 son iguales");

        // Modificar un atributo en author2
        author2.setFirstName("Jane");

        assertNotEquals(author1, author2, "author1 y author2 ya no son iguales");
        assertNotEquals(author2, author1, "author1 y author2 ya no son iguales");

        assertNotEquals(author1.hashCode(), author2.hashCode(),
                "hash codes de author1 y author2 son diferentes después de " + "modificar author2");

        PersistentAuthorEntity authorNull = null;
        assertNotEquals(author1, authorNull, "notEquals con un valor nulo");

        var otherObject = "This is not a PersistentAuthorEntity";
        assertNotEquals(author1, otherObject, "notEquals con otro tipo de objeto");
    }

    @Test
    void testGettersAndSetters() {
        // Crear una instancia de PersistentAuthorEntity
        var author = new PersistentAuthorEntity();

        // Establecer valores utilizando los métodos setter
        author.setIdentifier(1L);
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setNationality("American");

        // Verificar que los valores se establecieron correctamente utilizando
        // los métodos getter
        assertEquals(1L, author.getIdentifier(), "getter");
        assertEquals("John", author.getFirstName(), "getter");
        assertEquals("Doe", author.getLastName(), "getter");
        assertEquals("American", author.getNationality(), "getter");
    }

}
