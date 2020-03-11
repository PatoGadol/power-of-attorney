package nl.cinq.rabo.entities.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
@Getter
public enum Direction {
    GIVEN("GIVEN"),
    RECEIVED("RECEIVED");

    private String value;

    Direction(String value) {
        this.value = value;
    }

    public String getName() {
        return name();
    }
}
