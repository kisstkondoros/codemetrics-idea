package com.github.kisstkondoros.codemetrics.core.parser;

class ComplexityDescription {
  private int increment;
  private String description;
  private boolean visible;

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
