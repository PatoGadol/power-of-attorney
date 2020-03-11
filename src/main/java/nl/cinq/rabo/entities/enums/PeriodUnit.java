package nl.cinq.rabo.entities.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
@Getter
public enum PeriodUnit {
    PER_DAY("PER DAY"),
    PER_WEEK("PER WEEK"),
    PER_MONTH("PER MONTH");

    private String value;

    PeriodUnit(String value) {
        this.value = value;
    }

    public String getName() {
        return name();
    }
}
