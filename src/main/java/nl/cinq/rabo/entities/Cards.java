package nl.cinq.rabo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cards {
    private List<DebitCardsDetails> debitCardsDetails;
    private List<CreditCardsDetails> creditCardsDetails;

}
