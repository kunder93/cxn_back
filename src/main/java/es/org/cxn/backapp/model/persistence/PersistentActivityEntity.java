package es.org.cxn.backapp.model.persistence;

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

import java.time.LocalDateTime;

import es.org.cxn.backapp.model.ActivityEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

/**
 * PersistentActivityEntity is a JPA entity representing an activity record in
 * the database. This class implements the ActivityEntity interface and maps to
 * the "activities" table.
 *
 * <p>
 * Attributes of this entity include an identifier, title, description, start
 * date, end date, creation timestamp, and category of the activity. Each
 * attribute corresponds to a column in the database table.
 *
 * <p>
 * This class is annotated with the {@code @Entity} annotation and managed by
 * the JPA persistence provider. It uses Lombok's {@code @Data} to automatically
 * generate getters, setters, and other common methods.
 *
 * <p>
 * This class is licensed under the MIT License.
 *
 * @see ActivityEntity
 */
@Entity(name = "ActivityEntity")
@Table(name = "activities")
@Data
public class PersistentActivityEntity implements ActivityEntity {

    /**
     * Serial version UID for serialization compatibility.
     */
    @Transient
    private static final long serialVersionUID = -6081730398931758455L;

    /**
     * Title of the activity. Cannot be null and is not unique.
     */
    @Id
    @Column(name = "title", nullable = false, unique = true)
    private String title = "";

    /**
     * Description of the activity. Cannot be null and is not unique.
     */
    @Column(name = "description", nullable = false, unique = false)
    private String description = "";

    /**
     * Start date and time of the activity. Cannot be null and is not unique.
     */
    @Column(name = "start_date", nullable = false, unique = false)
    private LocalDateTime startDate;

    /**
     * End date and time of the activity. Cannot be null and is not unique.
     */
    @Column(name = "end_date", nullable = false, unique = false)
    private LocalDateTime endDate;

    /**
     * Creation timestamp of the activity. Cannot be null and is not unique.
     */
    @Column(name = "created_at", nullable = false, unique = false)
    private LocalDateTime createdAt;

    /**
     * Category of the activity (e.g., TORNEO, ENTRENAMIENTO). Cannot be null and is
     * not unique.
     */
    @Column(name = "category", nullable = false, unique = false)
    private String category = "";

    /**
     * Image sourace path where is stored.
     */
    @Column(name = "image_src", nullable = true, unique = false)
    private String imageSrc;

    /**
     * Default constructor for PersistentActivityEntity.
     */
    public PersistentActivityEntity() {
        // Default constructor
    }
}
