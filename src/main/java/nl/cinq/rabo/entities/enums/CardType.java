package nl.cinq.rabo.entities.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
@Getter
public enum CardType {
    DEBIT_CARD("DEBIT CARD"),
    CREDIT_CARD("CREDIT CARD");

    private String value;

    CardType(String value) {
        this.value = value;
    }

    public String getName() {
        return name();
    }
}
