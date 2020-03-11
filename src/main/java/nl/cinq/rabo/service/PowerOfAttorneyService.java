package nl.cinq.rabo.service;

import lombok.extern.slf4j.Slf4j;
import nl.cinq.rabo.entities.PowerOfAttorneyDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PowerOfAttorneyService {

    private final WebClient webClient;

    @Autowired
    public PowerOfAttorneyService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<PowerOfAttorneyDetails> getPowerOfAttorneyDetails() {

        return webClient.get()
                .uri("/power-of-attorneys")
                .retrieve()
                .bodyToFlux(PowerOfAttorneyDetails.class);
    }

    public Mono<PowerOfAttorneyDetails> getPowerOfAttorneyDetails(String id) {
        log.info(String.format("Calling power of attorney details (%s)", id));

        return webClient.get()
                .uri("/power-of-attorneys/{id}", id)
                .retrieve()
                .bodyToMono(PowerOfAttorneyDetails.class);
    }

}
