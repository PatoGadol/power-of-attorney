package nl.cinq.rabo.service;

import lombok.extern.slf4j.Slf4j;
import nl.cinq.rabo.entities.CardReference;
import nl.cinq.rabo.entities.DebitCardsDetails;
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
public class DebitCardsService {

    private final WebClient webClient;

    @Autowired
    public DebitCardsService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<DebitCardsDetails> fetchDebitCards(List<String> debitCardIds) {
        return Flux.fromIterable(debitCardIds)
                .parallel()
                .runOn(Schedulers.elastic())
                .flatMap(this::getDebitCardDetails)
                .ordered(Comparator.comparingInt(cd -> Integer.valueOf(cd.getId())));
    }

    private Mono<DebitCardsDetails> getDebitCardDetails(String id) {
        log.info(String.format("Calling getDebitCardDetails (%s)", id));

        return webClient.get()
                .uri("/debit-cards/{id}", id)
                .retrieve()
                .bodyToMono(DebitCardsDetails.class);
    }

    public ArrayList<String> getDebitCardsReferences(Optional<List<CardReference>> cardReferenceList) {
        return cardReferenceList
                .orElse(Collections.emptyList())
                .stream()
                .filter(cardRef -> cardRef.getType().equals(CardType.DEBIT_CARD))
                .map(cardReference -> cardReference.getId())
                .collect(toCollection(ArrayList::new));
    }
}
