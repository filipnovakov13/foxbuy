package com.blue.foxbuy.models;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("Administrator"),     // value 0
    VIP_USER("VIP User"),       // value 1
    USER("Regular User");       // value 2

    private final String description;

    Role(String description) {
        this.description = description;
    }

}
