
package es.org.cxn.backapp.service.impl;

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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.MagazineEntity;
import es.org.cxn.backapp.model.form.requests.member_resources.AddMagazineRequestDto;
import es.org.cxn.backapp.model.form.requests.member_resources.AuthorRequest;
import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;
import es.org.cxn.backapp.model.persistence.PersistentMagazineEntity;
import es.org.cxn.backapp.repository.AuthorEntityRepository;
import es.org.cxn.backapp.repository.MagazineEntityRepository;
import es.org.cxn.backapp.service.ImageStorageService;
import es.org.cxn.backapp.service.MagazineService;
import es.org.cxn.backapp.service.dto.AuthorDataDto;
import es.org.cxn.backapp.service.dto.MagazineDataImageDto;
import es.org.cxn.backapp.service.exceptions.MagazineServiceException;
import es.org.cxn.backapp.service.impl.storage.FileLocation;
import jakarta.transaction.Transactional;

/**
 * Default implementation of the {@link MagazineService}.
 *
 * @author Santiago Paz.
 *
 */
@Service
public class DefaultMagazineService implements MagazineService {

    /**
     * Repository for the magazine entities handled by the service.
     */
    private final MagazineEntityRepository magazineRepository;

    /**
     * Repository for the author entities handled by the service.
     */
    private final AuthorEntityRepository authorRepository;

    /**
     * The image storage service for save and load magazine covers.
     */
    private final ImageStorageService imageStorageService;

    /**
     * Constructs an entities service with the specified repositories.
     *
     * @param repoMagazine     The magazine
     *                         repository{@link MagazineEntityRepository}
     * @param repoAuth         The author repository{@link AuthorEntityRepository}
     * @param imageStorageServ THe image storage service.
     *
     */
    public DefaultMagazineService(final MagazineEntityRepository repoMagazine, final AuthorEntityRepository repoAuth,
            final ImageStorageService imageStorageServ) {
        super();
        imageStorageService = checkNotNull(imageStorageServ, "Received a null pointer as image service.");
        magazineRepository = checkNotNull(repoMagazine, "Received a null pointer as magazine repository");
        authorRepository = checkNotNull(repoAuth, "Received a null pointer as author repository");
    }

    /**
     * Add new magazine. Authors list in AddMagazineRequestDto cannot be null.
     *
     * @param magazineRequest The magazine data.
     * @param imageCover      The magazine image file.
     *
     * @throws MagazineServiceException When cannot add magazine.
     */
    @Transactional
    @Override
    public MagazineEntity add(final AddMagazineRequestDto magazineRequest, final MultipartFile imageCover)
            throws MagazineServiceException {
        final var magazine = new PersistentMagazineEntity();
        magazine.setIssn(magazineRequest.issn());
        magazine.setTitle(magazineRequest.title());
        magazine.setPublisher(magazineRequest.publisher());
        magazine.setEditionNumber(magazineRequest.editionNumber());
        magazine.setPublishDate(magazineRequest.publishDate());
        magazine.setDescription(magazineRequest.description());
        magazine.setPagesAmount(magazineRequest.pagesAmount());
        magazine.setLanguage(magazineRequest.language());

        final var authorsList = magazineRequest.authors();
        authorsList.forEach((AuthorRequest authorRequestDto) -> {
            final var existingAuthor = authorRepository.findByFirstNameAndLastName(authorRequestDto.firstName(),
                    authorRequestDto.lastName());
            if (existingAuthor != null) {
                final var magazineAuthors = magazine.getAuthors();
                magazineAuthors.add(existingAuthor);
                magazine.setAuthors(magazineAuthors);

            } else {
                final var authorEntity = new PersistentAuthorEntity();
                authorEntity.setFirstName(authorRequestDto.firstName());
                authorEntity.setLastName(authorRequestDto.lastName());
                final var authorSaved = authorRepository.save(authorEntity);
                final var magazineAuthors = magazine.getAuthors();
                magazineAuthors.add(authorSaved);
                magazine.setAuthors(magazineAuthors);
            }
        });
        try {
            final var imageSoruce = imageStorageService.saveImage(imageCover, FileLocation.MAGAZINE_COVERS);
            magazine.setCoverSrc(imageSoruce);
        } catch (IOException ex) {
            throw new MagazineServiceException("Magazine cover cannot be saved", ex);
        }
        try {
            return magazineRepository.save(magazine);
        } catch (DataIntegrityViolationException e) {
            throw new MagazineServiceException(e.getMessage(), e);
        }
    }

    /**
     * Find magazine using isbn.
     */
    @Override
    public MagazineEntity find(final String val) throws MagazineServiceException {
        checkNotNull(val, "Received a null val as magazine identifier isbn.");
        final var optionalMagazine = magazineRepository.findById(val);
        if (optionalMagazine.isPresent()) {
            return optionalMagazine.get();
        } else {
            throw new MagazineServiceException("aa");
        }
    }

    /**
     * Find image using magazine isbn.
     */
    @Override
    public byte[] findImage(final String isbn) throws MagazineServiceException {
        final var magazine = find(isbn);
        try {
            return imageStorageService.loadImage(magazine.getCoverSrc());
        } catch (IOException e) {
            throw new MagazineServiceException(e.getMessage(), e);
        }
    }

    /**
     * Get all magazines.
     */
    @Override
    public List<MagazineDataImageDto> getAll() {
        final var persistentMagazines = magazineRepository.findAll();

        final Set<MagazineDataImageDto> dtoSet = persistentMagazines.stream()
                .map((PersistentMagazineEntity magazine) -> {
                    // Convert PersistentMagazineEntity to MagazineDataImageDto
                    return new MagazineDataImageDto(magazine.getIssn(), magazine.getTitle(), magazine.getPublisher(),
                            magazine.getEditionNumber(), magazine.getDescription(),
                            magazine.getPublishDate().toString(), magazine.getPagesAmount(), magazine.getLanguage(),
                            magazine.getAuthors().stream()
                                    .map(author -> new AuthorDataDto(author.getFirstName(), author.getLastName()))
                                    .collect(Collectors.toSet())
                    // Include the image bytes
                    );
                }).collect(Collectors.toSet()); // Use Collectors.toSet() for immutability

        // Return the result as a list of MagazineDataImageDto
        return new ArrayList<>(dtoSet); // Convert Set to List
    }

    /**
     * Remove magazine using isbn.
     */
    @Override
    public void remove(final String issn) throws MagazineServiceException {
        try {
            // Check if the magazine exists
            if (magazineRepository.existsById(issn)) {
                // If it exists, remove the magazine
                magazineRepository.deleteById(issn);
            } else {
                throw new MagazineServiceException("Magazine not found");
            }
        } catch (IllegalArgumentException e) {
            throw new MagazineServiceException("Failed to remove the magazine", e);
        }
    }
}
