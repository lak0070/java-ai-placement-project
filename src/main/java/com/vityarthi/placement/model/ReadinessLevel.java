package com.vityarthi.placement.model;

public enum ReadinessLevel {
    NOT_READY("Not Ready"),
    MODERATELY_READY("Moderately Ready"),
    PLACEMENT_READY("Placement Ready");

    private final String displayName;

    ReadinessLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
