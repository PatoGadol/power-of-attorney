package nl.cinq.rabo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.cinq.rabo.entities.enums.Authorization;
import nl.cinq.rabo.entities.enums.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class PowerOfAttorneyDetails {
    private String id;
    private String grantor;
    private String grantee;
    private String account;
    private Direction direction;
    private Set<Authorization> authorizations;
    private List<CardReference> cards = new ArrayList<>();
}
