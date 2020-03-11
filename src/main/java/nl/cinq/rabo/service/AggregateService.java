package nl.cinq.rabo.service;

import lombok.extern.slf4j.Slf4j;
import nl.cinq.rabo.entities.CardReference;
import nl.cinq.rabo.entities.Cards;
import nl.cinq.rabo.entities.CreditCardsDetails;
import nl.cinq.rabo.entities.DebitCardsDetails;
import nl.cinq.rabo.entities.PowerOfAttorneyAggregatedData;
import nl.cinq.rabo.entities.PowerOfAttorneyDetails;
import nl.cinq.rabo.entities.enums.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AggregateService {

    private final PowerOfAttorneyService powerOfAttorneyService;
    private final CreditCardsService creditCardsService;
    private final DebitCardsService debitCardsService;

    @Autowired
    public AggregateService(PowerOfAttorneyService powerOfAttorneyService,
                            CreditCardsService creditCardsService,
                            DebitCardsService debitCardsService) {
        this.powerOfAttorneyService = powerOfAttorneyService;
        this.creditCardsService = creditCardsService;
        this.debitCardsService = debitCardsService;
    }

    public Mono<List<PowerOfAttorneyAggregatedData>> getAllAggregatedDetails() {
        return powerOfAttorneyService
                .getPowerOfAttorneyDetails()
                .flatMap(powerOfAttorneyDetails -> getPowerOfAttorneyAggregatedDetails(powerOfAttorneyDetails.getId()))
                .sort()
                .collectList();

    }

    public Mono<PowerOfAttorneyAggregatedData> getPowerOfAttorneyAggregatedDetails(String id) {
        Mono<PowerOfAttorneyDetails> powerOfAttorneyDetailsMono = powerOfAttorneyService.getPowerOfAttorneyDetails(id);
        Mono<Cards> cards = getCards(powerOfAttorneyDetailsMono);
        return powerOfAttorneyDetailsMono.zipWith(
                cards,
                PowerOfAttorneyAggregatedData::new);
    }

    private Flux<DebitCardsDetails> getDebitCardsDetails(List<CardReference> cardReferenceList) {
        return debitCardsService.fetchDebitCards(debitCardsService.getDebitCardsReferences(
                Optional.ofNullable(cardReferenceList)));
    }

    private Flux<CreditCardsDetails> getCreditCardsDetails(List<CardReference> cardReferenceList) {
        return creditCardsService.fetchCreditCards(creditCardsService.getCreditCardsReferences(
                Optional.ofNullable(cardReferenceList)));
    }

    public Mono<Cards> getCardsAggregatedDetails(String id) {
        return getCards(powerOfAttorneyService.getPowerOfAttorneyDetails(id));
    }

    private Mono<Cards> getCards(Mono<PowerOfAttorneyDetails> powerOfAttorneyDetailsMono) {
        return powerOfAttorneyDetailsMono.flatMapMany(
                powerOfAttorneyDetails -> {
                    boolean isDebitAuthorized = powerOfAttorneyDetails
                            .getAuthorizations()
                            .contains(Authorization.DEBIT_CARD);
                    return isDebitAuthorized
                            ? getDebitCardsDetails(powerOfAttorneyDetails.getCards())
                            : Flux.empty();
                }
        )
                .collectList()
                .zipWith(powerOfAttorneyDetailsMono.flatMapMany(
                        powerOfAttorneyDetails -> {
                            boolean isCreditAuthorized = powerOfAttorneyDetails
                                    .getAuthorizations()
                                    .contains(Authorization.CREDIT_CARD);
                            return isCreditAuthorized
                                    ? getCreditCardsDetails(powerOfAttorneyDetails.getCards())
                                    : Flux.empty();
                        }
                        ).collectList(),
                        Cards::new);
    }
}
