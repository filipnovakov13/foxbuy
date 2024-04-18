package com.blue.foxbuy.models;

public enum Role {
    ADMIN("Administrator"),
    VIP_USER("VIP User"),
    USER("Regular User");
    private final String description;

    // Constructor
    Role(String description) {
        this.description = description;
    }

    // Method to get the description
    public String getDescription() {
        return description;
    }
}
