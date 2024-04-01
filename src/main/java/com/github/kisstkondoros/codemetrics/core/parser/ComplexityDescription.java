package com.github.kisstkondoros.codemetrics.core.parser;

class ComplexityDescription {
    private final int increment;
    private final String description;
    private final boolean visible;

    ComplexityDescription(Integer increment, String description, Boolean visible) {
        this.increment = increment == null ? 0 : increment;
        this.description = description;
        this.visible = Boolean.TRUE.equals(visible);
    }

    int getIncrement() {
        return increment;
    }

    String getDescription() {
        return description;
    }

    boolean isVisible() {
        return visible;
    }
}
