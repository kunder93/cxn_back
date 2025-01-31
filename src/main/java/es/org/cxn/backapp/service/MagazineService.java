
package es.org.cxn.backapp.service;

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

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.MagazineEntity;
import es.org.cxn.backapp.model.form.requests.member_resources.AddMagazineRequestDto;
import es.org.cxn.backapp.service.dto.MagazineDataImageDto;
import es.org.cxn.backapp.service.exceptions.MagazineServiceException;

/**
 * Interface for the service that handles magazines operations.
 *
 * @author Santiago Paz.
 */
public interface MagazineService {

    /**
     * Adds a new magazine.
     *
     * @param magazineRequest The {@link AddMagazineRequestDto} containing the new
     *                        magazine's data.
     * @param imageCover      The magazine image cover file.
     * @return The newly created {@link MagazineEntity}.
     * @throws MagazineServiceException If the magazine cannot be added.
     */
    MagazineEntity add(AddMagazineRequestDto magazineRequest, MultipartFile imageCover) throws MagazineServiceException;

    /**
     * Finds magazine using its ISSN number.
     *
     * @param issn The ISSN of the magazine to find.
     * @return The {@link MagazineEntity} corresponding to the provided ISSN.
     * @throws MagazineServiceException If the magazine cannot be found.
     */
    MagazineEntity find(String issn) throws MagazineServiceException;

    /**
     * Find magazine's image.
     *
     * @param issn The magazine issn aka identifier.
     * @return The magazine image that matched provided issn.
     * @throws MagazineServiceException When magazine image cannot be loaded.
     */
    byte[] findImage(String issn) throws MagazineServiceException;

    /**
     * Retrieves a list of all magazines.
     *
     * @return A list of all {@link MagazineDataImageDto} objects representing the
     *         magazines in the library.
     */
    List<MagazineDataImageDto> getAll();

    /**
     * Removes a magazine using its ISSN number.
     *
     * @param val The ISSN of the magazine to be removed.
     * @throws MagazineServiceException If the magazine cannot be found or removed.
     */
    void remove(String val) throws MagazineServiceException;

}
