package es.org.cxn.backapp.model.form.responses;

import es.org.cxn.backapp.model.persistence.PersistentProfileImageEntity;

public record ProfileImageResponse(String imageExtension, boolean stored, String url, String file) {

    public ProfileImageResponse(PersistentProfileImageEntity profileImageEntity) {
        // Convert the ImageExtension enum to a string using name() or toString()
        this(profileImageEntity.getExtension().name(), profileImageEntity.getStored(), profileImageEntity.getUrl(),
                null);
    }

    public ProfileImageResponse(PersistentProfileImageEntity profileImageEntity, String imageFileData) {
        // Convert the ImageExtension enum to a string using name() or toString()
        this(profileImageEntity.getExtension().name(), profileImageEntity.getStored(), profileImageEntity.getUrl(),
                imageFileData);
    }

}
