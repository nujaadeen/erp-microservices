package com.zamzamsuper.product_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Unit {
    PIECE,
    BOX;

    @JsonCreator
    public static Unit from(String value) {
        return Unit.valueOf(value.toUpperCase());
    }
}
