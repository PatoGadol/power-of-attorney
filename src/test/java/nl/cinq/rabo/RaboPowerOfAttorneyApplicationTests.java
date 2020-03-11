package nl.cinq.rabo;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.cinq.rabo.entities.PowerOfAttorneyDetails;
import nl.cinq.rabo.service.CreditCardsService;
import nl.cinq.rabo.service.DebitCardsService;
import nl.cinq.rabo.service.PowerOfAttorneyService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

class RaboPowerOfAttorneyApplicationTests {

    private PowerOfAttorneyService powerOfAttorneyService;
    private CreditCardsService creditCardsService;
    private DebitCardsService debitCardsService;

    private ObjectMapper mapper = new ObjectMapper();

    public static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());
        powerOfAttorneyService = new PowerOfAttorneyService(WebClient.create(baseUrl));
    }

    @Test
    void whenGetAllPowerAttorneyDetails_ThenReturnPowerAttorneyDetails() throws Exception {
        PowerOfAttorneyDetails mockPowerOfAttorneyDetails = new PowerOfAttorneyDetails();
        mockPowerOfAttorneyDetails.setId("1234");
        mockBackEnd.enqueue(new MockResponse()
                .setBody(mapper.writeValueAsString(mockPowerOfAttorneyDetails))
                .addHeader("Content-Type", "application/json"));


        StepVerifier.create(powerOfAttorneyService.getPowerOfAttorneyDetails())
                .expectNextMatches(powerOfAttorney -> powerOfAttorney.getId()
                        .equals("1234"))
                .verifyComplete();

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    void whenGetAllPowerAttorneyDetailsForId_ThenReturnPowerAttorneyDetails() throws Exception {
        PowerOfAttorneyDetails mockPowerOfAttorneyDetails = new PowerOfAttorneyDetails();
        mockPowerOfAttorneyDetails.setId("1234");
        mockBackEnd.enqueue(new MockResponse()
                .setBody(mapper.writeValueAsString(mockPowerOfAttorneyDetails))
                .addHeader("Content-Type", "application/json"));


        StepVerifier.create(powerOfAttorneyService.getPowerOfAttorneyDetails("0001"))
                .expectNextMatches(powerOfAttorney -> powerOfAttorney.getId()
                        .equals("1234"))
                .verifyComplete();
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
    }

}
