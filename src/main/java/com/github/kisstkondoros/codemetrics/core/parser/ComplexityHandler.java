package com.github.kisstkondoros.codemetrics.core.parser;

import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;

import java.util.function.Function;

interface ComplexityHandler<T>
        extends Function<MetricsConfiguration, Function<T, ComplexityDescription>> {
    default Function<T, ComplexityDescription> forConfig(
            MetricsConfiguration metricsConfiguration) {
        return this.apply(metricsConfiguration);
    }
}
