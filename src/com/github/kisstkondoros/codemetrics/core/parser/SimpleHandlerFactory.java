package com.github.kisstkondoros.codemetrics.core.parser;

import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;

import java.util.function.Function;

class SimpleHandlerFactory {

  public ComplexityHandler create(
      Function<MetricsConfiguration, Integer> incrementSupplier,
      Function<MetricsConfiguration, String> descriptionSupplier) {
    return create(incrementSupplier, descriptionSupplier, c -> false);
  }

  public ComplexityHandler create(
      Function<MetricsConfiguration, Integer> incrementSupplier,
      Function<MetricsConfiguration, String> descriptionSupplier,
      Function<MetricsConfiguration, Boolean> visibilitySupplier) {
    return (MetricsConfiguration configuration) ->
        element ->
            new ComplexityDescription(
                incrementSupplier.apply(configuration),
                descriptionSupplier.apply(configuration),
                visibilitySupplier.apply(configuration));
  }
}
