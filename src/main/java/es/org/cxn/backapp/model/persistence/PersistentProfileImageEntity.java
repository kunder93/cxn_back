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

import es.org.cxn.backapp.model.ProfileImageEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.io.Serial;

/**
 * PersistentProfileImageEntity represents the image profile data of a user, to
 * be persisted in the database. This class is a JPA entity that maps to the
 * "user_profile_image" table, with a one-to-one relationship with
 * PersistentUserEntity.
 * <p>
 * It implements the {@link ProfileImageEntity} interface and uses Lombok's
 * {@code @Data} annotation to automatically generate getters, setters, and
 * other utility methods.
 * </p>
 *
 * <p>
 * This entity includes fields to store the URL, extension, and a flag to
 * indicate whether the image is stored in the database. The user's DNI
 * (Document Number of Identification) is used as the primary key.
 * </p>
 *
 * <p>
 * The class also defines a relationship with {@link PersistentUserEntity},
 * where the userDni serves as a foreign key in the "user_profile_image" table
 * to establish a one-to-one mapping between the user and their profile image.
 * </p>
 *
 * <p>
 * This entity also includes a static field {@code serialVersionUID} for
 * serialization purposes, though it is marked as {@code Transient} and is not
 * persisted in the database.
 * </p>
 *
 * <p>
 * Example usage:
 *
 * <pre>{@code
 * PersistentProfileImageEntity profileImage = new PersistentProfileImageEntity();
 * profileImage.setUserDni("12345678X");
 * profileImage.setUrl("https://example.com/image.jpg");
 * profileImage.setExtension(".jpg");
 * profileImage.setStored(true);
 * }</pre>
 *
 * @author Santiago
 */
@Entity
@Table(name = "user_profile_image")
public class PersistentProfileImageEntity implements ProfileImageEntity {

    /**
     * The serialization version UID. This is not persisted in the database.
     */
    @Serial
    @Transient
    private static final long serialVersionUID = 1314429995556611251L;

    /**
     * The user's DNI (Document Number of Identification), serving as the primary
     * key for this entity.
     */
    @Id
    @Column(name = "user_dni", nullable = false)
    private String userDni;

    /**
     * The URL where the profile image is stored.
     */
    @Column(name = "url", nullable = true, unique = false)
    private String url;

    /**
     * The file extension of the profile image (e.g., .jpg, .png).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "extension", nullable = true, unique = false)
    private ImageExtension extension;

    /**
     * A boolean flag indicating whether the profile image is stored in the
     * database.
     */
    @Column(name = "stored", nullable = true, unique = false)
    private Boolean stored;

    /**
     * The associated user entity, identified by the user's DNI. This establishes a
     * one-to-one relationship between the profile image and the user.
     */
    @OneToOne
    @JoinColumn(name = "user_dni")
    private PersistentUserEntity user;

    /**
     * Default constructor. This is used by JPA for entity instantiation.
     */
    public PersistentProfileImageEntity() {
        // Default constructor
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageExtension getExtension() {
        return extension;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersistentUserEntity getUser() {
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserDni() {
        return userDni;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isStored() {
        return stored;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExtension(final ImageExtension value) {
        extension = value;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStored(final Boolean value) {
        stored = value;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUrl(final String value) {
        url = value;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserDni(final String value) {
        userDni = value;

    }

}
