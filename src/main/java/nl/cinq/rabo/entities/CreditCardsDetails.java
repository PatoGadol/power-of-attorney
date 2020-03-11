package nl.cinq.rabo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreditCardsDetails {
    private String id;
    private Integer cardNumber;
    private Integer sequenceNumber;
    private String cardHolder;
    private Integer monthlyLimit;

}
