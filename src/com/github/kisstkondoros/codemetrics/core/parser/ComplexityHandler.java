package com.github.kisstkondoros.codemetrics.core.parser;

import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.intellij.psi.PsiElement;

import java.util.function.Function;

interface ComplexityHandler
    extends Function<MetricsConfiguration, Function<PsiElement, ComplexityDescription>> {
  default Function<PsiElement, ComplexityDescription> forConfig(
      MetricsConfiguration metricsConfiguration) {
    return this.apply(metricsConfiguration);
  }
}
