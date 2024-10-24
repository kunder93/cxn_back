package es.org.cxn.backapp.model.persistence;

import es.org.cxn.backapp.model.ProfileImageEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

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

    @Override
    public ImageExtension getExtension() {
        return extension;
    }

    @Override
    public Boolean getStored() {
        return stored;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public PersistentUserEntity getUser() {
        return user;
    }

    @Override
    public String getUserDni() {
        return userDni;
    }

    @Override
    public void setExtension(ImageExtension value) {
        extension = value;

    }

    @Override
    public void setStored(Boolean value) {
        stored = value;

    }

    @Override
    public void setUrl(String value) {
        url = value;

    }

    @Override
    public void setUserDni(String value) {
        userDni = value;

    }

}
