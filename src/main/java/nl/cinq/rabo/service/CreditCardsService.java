package nl.cinq.rabo.service;

import lombok.extern.slf4j.Slf4j;
import nl.cinq.rabo.entities.CardReference;
import nl.cinq.rabo.entities.CreditCardsDetails;
import nl.cinq.rabo.entities.enums.CardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toCollection;

@Slf4j
@Service
public class CreditCardsService {

    private final WebClient webClient;

    @Autowired
    public CreditCardsService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<CreditCardsDetails> fetchCreditCards(List<String> creditCardIds) {
        return Flux.fromIterable(creditCardIds)
                .parallel()
                .runOn(Schedulers.elastic())
                .flatMap(this::getCreditCardDetails)
                .ordered(Comparator.comparingInt(cd -> Integer.valueOf(cd.getId())));
    }

    private Mono<CreditCardsDetails> getCreditCardDetails(String id) {
        log.info(String.format("Calling getCreditCardDetails (%s)", id));

        return webClient.get()
                .uri("/credit-cards/{id}", id)
                .retrieve()
                .bodyToMono(CreditCardsDetails.class);
    }

    public ArrayList<String> getCreditCardsReferences(Optional<List<CardReference>> cardReferenceList) {
        return cardReferenceList
                .orElse(Collections.emptyList())
                .stream()
                .filter(cardRef -> cardRef.getType().equals(CardType.CREDIT_CARD))
                .map(cardReference -> cardReference.getId())
                .collect(toCollection(ArrayList::new));
    }
}
