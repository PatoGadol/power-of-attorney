package nl.cinq.rabo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.cinq.rabo.entities.enums.CardType;

@Data
@NoArgsConstructor
public class CardReference {
    private String id;
    private CardType type;
}
