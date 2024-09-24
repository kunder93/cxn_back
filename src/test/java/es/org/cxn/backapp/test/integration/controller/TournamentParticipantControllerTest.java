
package es.org.cxn.backapp.test.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.org.cxn.backapp.controller.entity.TournamentParticipantController;
import es.org.cxn.backapp.model.form.requests.AddTournamentParticipantRequest;
import es.org.cxn.backapp.model.persistence.PersistentTournamentParticipantEntity;
import es.org.cxn.backapp.service.TournamentParticipantService;
import es.org.cxn.backapp.test.utils.LocalDateAdapter;

/**
 * Integration tests for {@link TournamentParticipantController}.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class TournamentParticipantControllerTest {

    /**
     * FIDE ID for the participant John Doe.
     */
    private static final BigInteger FIDE_ID_JOHN = BigInteger.valueOf(1234567);

    /**
     * FIDE ID for the participant Jane Smith.
     */
    private static final BigInteger FIDE_ID_JANE = BigInteger.valueOf(2345678);

    /**
     * FIDE ID for the participant Alice Johnson.
     */
    private static final BigInteger FIDE_ID_ALICE = BigInteger.valueOf(3456789);

    /**
     * Birth date for the participant John Doe.
     */
    private static final LocalDate BIRTH_DATE_JOHN = LocalDate.of(2010, 5, 20);

    /**
     * Birth date for the participant Jane Smith.
     */
    private static final LocalDate BIRTH_DATE_JANE = LocalDate.of(2008, 3, 15);

    /**
     * Birth date for the participant Alice Johnson.
     */
    private static final LocalDate BIRTH_DATE_ALICE = LocalDate.of(2005, 10, 30);

    /** MockMvc for making HTTP requests in tests. */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked mail sender.
     */
    @MockBean
    private JavaMailSender javaMailSender;

    /** Gson instance for JSON serialization/deserialization. */
    private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

    /** Service for handling tournament participant operations. */
    @Autowired
    private TournamentParticipantService service;

    /**
     * Tests the creation of a new tournament participant. Verifies that the
     * participant is correctly added and the response is OK.
     */
    @Test
    void testAddParticipant() throws Exception {
        // Create participant data
        var request = new AddTournamentParticipantRequest(FIDE_ID_JOHN, "John Doe", "Chess Club", BIRTH_DATE_JOHN,
                PersistentTournamentParticipantEntity.TournamentCategory.SUB12, " 1");
        var requestJson = gson.toJson(request);

        // Perform POST request and verify response
        mockMvc.perform(post("/api/participants").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fideId").value(FIDE_ID_JOHN))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.club").value("Chess Club"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value(BIRTH_DATE_JOHN.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("SUB12"));
    }

    /**
     * Tests the case when invalid participant data is submitted. Verifies that the
     * request is rejected with a bad request status.
     */
    @Test
    void testAddParticipantWithInvalidData() throws Exception {
        // Create participant data with missing name
        var request = new AddTournamentParticipantRequest(FIDE_ID_JOHN, "", "Chess Club", BIRTH_DATE_JOHN,
                PersistentTournamentParticipantEntity.TournamentCategory.SUB12, "1");
        var requestJson = gson.toJson(request);

        // Perform POST request and expect BadRequest status
        mockMvc.perform(post("/api/participants").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests retrieval of all participants. Verifies that the list of participants
     * is correctly returned.
     */
    @Test
    void testGetAllParticipants() throws Exception {
        // Create sample participant data
        var participant1 = new PersistentTournamentParticipantEntity(FIDE_ID_JOHN, "John Doe", "Chess Club",
                BIRTH_DATE_JOHN, PersistentTournamentParticipantEntity.TournamentCategory.SUB12, "1");
        var participant2 = new PersistentTournamentParticipantEntity(FIDE_ID_JANE, "Jane Smith", "Elite Club",
                BIRTH_DATE_JANE, PersistentTournamentParticipantEntity.TournamentCategory.SUB14, "1");
        var participant3 = new PersistentTournamentParticipantEntity(FIDE_ID_ALICE, "Alice Johnson", "Pro Club",
                BIRTH_DATE_ALICE, PersistentTournamentParticipantEntity.TournamentCategory.SUB10, "1");
        service.addParticipant(participant1.getFideId(), participant1.getName(), participant1.getClub(),
                participant1.getBirthDate(), participant1.getCategory(), "1");
        service.addParticipant(participant2.getFideId(), participant2.getName(), participant2.getClub(),
                participant2.getBirthDate(), participant2.getCategory(), "1");
        service.addParticipant(participant3.getFideId(), participant3.getName(), participant3.getClub(),
                participant3.getBirthDate(), participant3.getCategory(), "1");

        // Perform GET request and verify response
        mockMvc.perform(get("/api/participants").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fideId").value(FIDE_ID_JOHN))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].club").value("Chess Club"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthDate").value(BIRTH_DATE_JOHN.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value("SUB12"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].fideId").value(FIDE_ID_JANE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].club").value("Elite Club"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].birthDate").value(BIRTH_DATE_JANE.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].category").value("SUB14"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].fideId").value(FIDE_ID_ALICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Alice Johnson"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].club").value("Pro Club"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].birthDate").value(BIRTH_DATE_ALICE.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].category").value("SUB10"));
    }

    /**
     * Tests the case when no participants exist. Verifies that an empty list is
     * returned.
     */
    @Test
    void testGetAllParticipantsWhenNoneExist() throws Exception {
        // Perform GET request and verify response
        mockMvc.perform(get("/api/participants").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }
}
