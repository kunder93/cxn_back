package es.org.cxn.backapp.model.form.responses.lichess;

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
