package nl.cinq.rabo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.cinq.rabo.entities.enums.PeriodUnit;

@Data
@NoArgsConstructor
public class Limit {
    private Integer limit;
    private PeriodUnit periodUnit;
}
