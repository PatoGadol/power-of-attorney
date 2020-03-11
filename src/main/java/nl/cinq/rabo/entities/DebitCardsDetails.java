package nl.cinq.rabo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DebitCardsDetails {
    private String id;
    private Integer cardNumber;
    private Integer sequenceNumber;
    private String cardHolder;
    private Limit atmLimit;
    private Limit posLimit;
    private Boolean contactless;
}
