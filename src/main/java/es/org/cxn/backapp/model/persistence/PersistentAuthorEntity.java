
package es.org.cxn.backapp.model.persistence;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

import es.org.cxn.backapp.model.AuthorEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Book Entity.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 * <p>
 * The default constructor is provided by Lombok's @NoArgsConstructor
 * annotation. This default constructor is required by JPA for entity
 * instantiation.
 *
 * @author Santiago Paz Perez.
 */
@Data
@AllArgsConstructor
@Builder
@Entity(name = "Author")
@Table(name = "author")
public class PersistentAuthorEntity implements AuthorEntity {

    /**
     * Serial UID.
     */
    @Serial
    private static final long serialVersionUID = 5777796129999170661L;

    /**
     * Author identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifier;

    /**
     * Author first name.
     */
    @Column(name = "first_name", nullable = false, unique = false)
    private String firstName;

    /**
     * Author last name.
     */
    @Column(name = "last_name", nullable = false, unique = false)
    private String lastName;

    /**
     * Author nationality.
     */
    @Column(name = "nationality", nullable = true, unique = false)
    private String nationality;

    /**
     * Books written by this author.
     */
    @ManyToMany(mappedBy = "authors")
    @Builder.Default
    private Set<PersistentBookEntity> books = new HashSet<>();

    /**
     * Default constructor for the PersistentAuthorEntity class.
     * <p>
     * This constructor initializes the PersistentAuthorEntity class, which is used
     * for the Spring Boot application.
     * </p>
     */
    public PersistentAuthorEntity() {
        books = new HashSet<>();
        // Default constructor
    }
}
