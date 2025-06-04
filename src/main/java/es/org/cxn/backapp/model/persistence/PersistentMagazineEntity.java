
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import es.org.cxn.backapp.model.MagazineEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Magazine Entity.
 * <p>
 * This class represents a magazine entity in the system, utilizing JPA
 * annotations for persistence. It includes fields for ISSN, title, gender,
 * publish year, language, and authors. The default constructor is provided by
 * Lombok's @NoArgsConstructor annotation and is required by JPA for entity
 * instantiation.
 * </p>
 *
 * @author Santiago Paz Perez.
 */
@Data
@AllArgsConstructor
@Builder
@Entity(name = "Magazine")
@Table(name = "magazine")
public class PersistentMagazineEntity implements MagazineEntity, Comparable<PersistentMagazineEntity> {

    /**
     * Serial UID for serialization.
     */
    @Serial
    private static final long serialVersionUID = 6799493225209946622L;

    /**
     * The magazine's ISSN, which acts as the unique identifier.
     */
    @Id
    @Column(name = "issn", nullable = false, unique = true)
    private String issn;

    /**
     * The title of the magazine.
     */
    @Column(name = "title", nullable = false, unique = false)
    private String title;

    /**
     * The year the magazine was published.
     */
    @Column(name = "publish_year", nullable = true, unique = false)
    private LocalDate publishDate;

    /**
     * The edition number of the magazine.
     */
    @Column(name = "edition_number", nullable = false, unique = false)
    private int editionNumber;

    /**
     * Company which has printed the magazine.
     */
    @Column(name = "publisher", nullable = false, unique = false)
    private String publisher;

    /**
     * Number of magazine pages.
     */
    @Column(name = "pages_amount", nullable = false, unique = false)
    private int pagesAmount;

    /**
     * The language in which the magazine is written.
     */
    @Column(name = "language", nullable = false, unique = false)
    private String language;

    /**
     * The magazine's description.
     */
    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * The cover source for magazine if it have one.
     */
    @Column(name = "cover_src", nullable = true)
    private String coverSrc;

    /**
     * The authors who wrote this magazine. This is a many-to-many relationship with
     * the PersistentAuthorEntity.
     */
    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(name = "magazineauthor", joinColumns = @JoinColumn(name = "magazine_issn"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @Builder.Default
    private Set<PersistentAuthorEntity> authors = new HashSet<>();

    /**
     * Default constructor for the PersistentMagazineEntity class.
     * <p>
     * This constructor initializes a new instance of the PersistentMagazineEntity
     * class. It is required for frameworks like JPA that rely on reflection to
     * create objects.
     * </p>
     */
    public PersistentMagazineEntity() {
        authors = new HashSet<>();
        // Default constructor
    }

    /**
     * Compares this magazine to another magazine based on their titles.
     *
     * @param other The other magazine to compare to.
     * @return A negative integer, zero, or a positive integer as this magazine's
     *         title is less than, equal to, or greater than the specified
     *         magazine's title.
     */
    @Override
    public int compareTo(final PersistentMagazineEntity other) {
        return this.title.compareTo(other.title);
    }
}
