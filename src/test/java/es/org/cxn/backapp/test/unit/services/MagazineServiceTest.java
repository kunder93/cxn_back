package es.org.cxn.backapp.test.unit.services;

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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import es.org.cxn.backapp.model.form.requests.member_resources.AddMagazineRequestDto;
import es.org.cxn.backapp.model.form.requests.member_resources.AuthorRequest;
import es.org.cxn.backapp.model.persistence.PersistentAuthorEntity;
import es.org.cxn.backapp.model.persistence.PersistentMagazineEntity;
import es.org.cxn.backapp.repository.AuthorEntityRepository;
import es.org.cxn.backapp.repository.MagazineEntityRepository;
import es.org.cxn.backapp.service.ImageStorageService;
import es.org.cxn.backapp.service.dto.MagazineDataImageDto;
import es.org.cxn.backapp.service.exceptions.MagazineServiceException;
import es.org.cxn.backapp.service.impl.DefaultMagazineService;
import es.org.cxn.backapp.service.impl.storage.FileLocation;

class MagazineServiceTest {

    @Mock
    private MagazineEntityRepository magazineRepository;

    @Mock
    private AuthorEntityRepository authorRepository;

    @Mock
    private ImageStorageService imageStorageService;

    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private DefaultMagazineService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new DefaultMagazineService(magazineRepository, authorRepository, imageStorageService);
    }

    @Test
    void testAddMagazine_whenImageFails_shouldThrowException() throws Exception {
        var request = new AddMagazineRequestDto("1234-5678", "Test", "Publisher", 1, "desc", 10, LocalDate.now(), "EN",
                List.of(new AuthorRequest("John", "Doe")));

        when(authorRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(null);
        when(authorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(imageStorageService.saveImage(any(), eq(FileLocation.MAGAZINE_COVERS)))
                .thenThrow(new IOException("IO error"));

        assertThrows(MagazineServiceException.class, () -> service.add(request, mockFile));
    }

    @Test
    void testAddMagazine_withExistingAuthor_shouldReuseAuthor() throws Exception {
        // Arrange
        var existingAuthor = new PersistentAuthorEntity();
        existingAuthor.setFirstName("Jane");
        existingAuthor.setLastName("Doe");

        var authorRequest = new AuthorRequest("Jane", "Doe");

        var request = new AddMagazineRequestDto("1234-5678", "Test Title", "Test Publisher", 1, "Test description", 100,
                LocalDate.of(2024, 1, 1), "EN", List.of(authorRequest));

        // Mock repository behavior
        when(authorRepository.findByFirstNameAndLastName("Jane", "Doe")).thenReturn(existingAuthor);
        when(imageStorageService.saveImage(any(), eq(FileLocation.MAGAZINE_COVERS))).thenReturn("covers/image.jpg");

        var savedMagazine = new PersistentMagazineEntity();
        savedMagazine.setIssn("1234-5678");
        when(magazineRepository.save(any())).thenReturn(savedMagazine);

        // Act
        var result = service.add(request, mockFile);

        // Assert
        assertNotNull(result);
        assertEquals("1234-5678", result.getIssn());
        verify(authorRepository, never()).save(any()); // Should not create a new author
    }

    @Test
    void testAddMagazine_withNewAuthor_shouldSaveMagazine() throws Exception {
        var authorRequest = new AuthorRequest("John", "Doe");
        var request = new AddMagazineRequestDto("1234-5678", "Test Magazine", "Test Publisher", 1, "Some description",
                100, LocalDate.now(), "English", List.of(authorRequest) // SEGURO con List.of(...)
        );

        when(authorRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(null);
        when(authorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(imageStorageService.saveImage(any(), eq(FileLocation.MAGAZINE_COVERS))).thenReturn("path/to/image");
        when(magazineRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.add(request, mockFile);

        assertEquals("1234-5678", result.getIssn());
        assertEquals("path/to/image", result.getCoverSrc());
        assertEquals(1, result.getAuthors().size());
    }

    @Test
    void testFindImage_shouldLoadImage() throws Exception {
        var magazine = new PersistentMagazineEntity();
        magazine.setIssn("1234-5678");
        magazine.setCoverSrc("cover/path");

        when(magazineRepository.findById("1234-5678")).thenReturn(Optional.of(magazine));
        when(imageStorageService.loadImage("cover/path")).thenReturn(new byte[] { 1, 2, 3 });

        var result = service.findImage("1234-5678");
        assertArrayEquals(new byte[] { 1, 2, 3 }, result);
    }

    @Test
    void testFindMagazine_existing_shouldReturn() throws Exception {
        var magazine = new PersistentMagazineEntity();
        magazine.setIssn("1234-5678");

        when(magazineRepository.findById("1234-5678")).thenReturn(Optional.of(magazine));

        var result = service.find("1234-5678");
        assertEquals("1234-5678", result.getIssn());
    }

    @Test
    void testFindMagazine_notFound_shouldThrowException() {
        when(magazineRepository.findById("notfound")).thenReturn(Optional.empty());
        assertThrows(MagazineServiceException.class, () -> service.find("notfound"));
    }

    @Test
    void testGetAllMagazines_shouldReturnList() {
        var magazine = new PersistentMagazineEntity();
        magazine.setIssn("issn1");
        magazine.setTitle("Magazine 1");
        magazine.setPublisher("Publisher");
        magazine.setEditionNumber(1);
        magazine.setDescription("desc");
        magazine.setPublishDate(LocalDate.now());
        magazine.setPagesAmount(50);
        magazine.setLanguage("ES");

        var author = new PersistentAuthorEntity();
        author.setFirstName("Ana");
        author.setLastName("Gomez");
        magazine.setAuthors(Set.of(author));

        when(magazineRepository.findAll()).thenReturn(List.of(magazine));

        List<MagazineDataImageDto> result = service.getAll();
        assertEquals(1, result.size());
        assertEquals("issn1", result.get(0).issn());
    }

    @Test
    void testRemove_existing_shouldDelete() throws Exception {
        when(magazineRepository.existsById("issn1")).thenReturn(true);
        service.remove("issn1");
        verify(magazineRepository).deleteById("issn1");
    }

    @Test
    void testRemove_notExisting_shouldThrowException() {
        when(magazineRepository.existsById("issn1")).thenReturn(false);
        assertThrows(MagazineServiceException.class, () -> service.remove("issn1"));
    }
}
