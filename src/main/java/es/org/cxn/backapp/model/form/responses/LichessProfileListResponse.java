package es.org.cxn.backapp.model.form.responses;

import java.util.List;

/**
 * Represents a response containing a list of Lichess profiles. This record is
 * used to encapsulate the data returned by the controller endpoint for
 * retrieving all stored Lichess profiles.
 *
 * @param profilesList A list of {@link LichessProfileResponse} objects, each
 *                     representing an individual Lichess profile and its
 *                     associated game statistics.
 */
public record LichessProfileListResponse(List<LichessProfileResponse> profilesList) {

    /**
     * Canonical constructor to ensure immutability by making a defensive copy of
     * the input list.
     *
     * @param profilesList the list of Lichess profile responses
     */
    public LichessProfileListResponse(final List<LichessProfileResponse> profilesList) {
        // Create a defensive copy of the list to ensure immutability
        this.profilesList = List.copyOf(profilesList == null ? List.of() : profilesList);
    }

    /**
     * Returns an immutable view of the profiles list.
     *
     * @return an unmodifiable list of Lichess profiles
     */
    public List<LichessProfileResponse> profilesList() {
        return List.copyOf(profilesList);
    }
}
