
package es.org.cxn.backapp.controller.entity.member_resources;

import java.util.List;

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

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.org.cxn.backapp.model.form.requests.member_resources.AddMagazineRequestDto;
import es.org.cxn.backapp.model.form.responses.member_resources.MagazineResponse;
import es.org.cxn.backapp.service.MagazineService;
import es.org.cxn.backapp.service.dto.MagazineDataImageDto;
import es.org.cxn.backapp.service.exceptions.MagazineServiceException;
import jakarta.validation.Valid;

/**
 * Controller for handle request related to library magazines.
 *
 * @author Santiago Paz.
 *
 */
@RestController
@RequestMapping("/api/resources/magazine")
public class MagazinesController {

    /**
     * The magazine service.
     */
    private final MagazineService magazineService;

    /**
     * Constructs a controller with the specified dependencies.
     *
     * @param service magazine service.
     */
    public MagazinesController(final MagazineService service) {
        magazineService = Objects.requireNonNull(service, "Magazine service must not be null.");
    }

    /**
     * Add new magazine.
     *
     * @param magazineData form with data to add magazine.
     *                     {@link AddMagazineRequestDto}.
     * @param imageFile    the magazine image cover file.
     * @return magazine's data created.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRESIDENTE') or hasRole('SECRETARIO')")
    @PostMapping()
    public ResponseEntity<String> addMagazine(@RequestPart("data")
    @Valid final AddMagazineRequestDto magazineData, @RequestPart(value = "imageFile", required = false)
    /* @ValidImageFile */ final MultipartFile imageFile) {
        try {

            magazineService.add(magazineData, imageFile);
            return new ResponseEntity<>("Created.", HttpStatus.CREATED);
        } catch (MagazineServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Returns all magazines with their authors.
     *
     * @return Magazine list.
     */
    @GetMapping()
    public ResponseEntity<List<MagazineDataImageDto>> getAllMagazines() {
        final var magazineList = magazineService.getAll();
        return new ResponseEntity<>(magazineList, HttpStatus.OK);
    }

    /**
     * Get magazine using issn number.
     *
     * @param issn The issn number,
     * @return Http Ok or Bad Request.
     */
    @GetMapping("/{issn}")
    public ResponseEntity<MagazineResponse> getMagazine(@PathVariable final String issn) {
        try {
            final var magazine = magazineService.find(issn);
            return new ResponseEntity<>(new MagazineResponse(magazine), HttpStatus.OK);
        } catch (MagazineServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Endpoint to retrieve the cover image of a Magazine by its ISSN.
     * <p>
     * This method accepts a magazine's ISSN as a path variable, fetches the cover
     * image for that magazine from the service layer, and returns the image as a
     * byte array in the HTTP response. If the image cannot be found or an error
     * occurs, it throws a {@link ResponseStatusException} with a
     * {@link HttpStatus#BAD_REQUEST} status.
     * </p>
     *
     * @param issn The ISSN of the magazine whose cover image is to be retrieved.
     * @return A {@link ResponseEntity} containing the cover image as a byte array
     *         in the response body with an {@link HttpStatus#OK} status.
     * @throws ResponseStatusException if there is an error fetching the image
     *                                 (e.g., magazine not found or other service
     *                                 exceptions).
     */
    @GetMapping("/{issn}/coverImage")
    public ResponseEntity<byte[]> getMagazineImage(@PathVariable final String issn) {
        try {
            final var image = magazineService.findImage(issn);
            return new ResponseEntity<>(image, HttpStatus.OK);
        } catch (MagazineServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Remove a magazine using issn number.
     *
     * @param issn The issn number,
     * @return Http Ok or Bad Request.
     */
    @DeleteMapping("/{issn}")
    public ResponseEntity<String> removeMagazine(@PathVariable final String issn) {
        try {
            magazineService.remove(issn);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (MagazineServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
