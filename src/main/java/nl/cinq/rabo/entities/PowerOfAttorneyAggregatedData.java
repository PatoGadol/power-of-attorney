package nl.cinq.rabo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerOfAttorneyAggregatedData implements Comparable {
    private PowerOfAttorneyDetails powerOfAttorneyDetails;
    private Cards cards;

    @Override
    public int compareTo(Object o) {
        String otherId = ((PowerOfAttorneyAggregatedData) o).getPowerOfAttorneyDetails().getId();
        return Integer.valueOf(powerOfAttorneyDetails.getId()) - Integer.valueOf(otherId);
    }
}
