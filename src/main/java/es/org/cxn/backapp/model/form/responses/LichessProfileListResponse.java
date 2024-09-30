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
}