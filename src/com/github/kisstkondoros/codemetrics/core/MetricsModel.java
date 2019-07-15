package com.github.kisstkondoros.codemetrics.core;

import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;

import static com.github.kisstkondoros.codemetrics.core.CollectorType.MAX;

public class MetricsModel {
  private int complexity;
  private boolean visible;
  private List<MetricsModel> children = new ArrayList<>();
  private String description;
  private String text;
  private String name;
  private CollectorType collectorType;
  private Supplier<Long> memoizedComplexityComputation =
      Suppliers.memoize(this::computeCollectedComplexity);
  private int textOffset;

  public MetricsModel(
      PsiElement node,
      int complexity,
      String description,
      boolean trim,
      boolean visible,
      CollectorType collectorType) {
    this.complexity = complexity;
    this.visible = visible;
    this.description = description;
    this.collectorType = collectorType;
    this.storeText(node, trim);
    this.textOffset = node.getTextOffset();
    if (node instanceof PsiNamedElement) {
      this.name = ((PsiNamedElement) node).getName();
    }
  }

  private void storeText(PsiElement node, boolean trim) {
    text = node.getText();
    if (trim) {
      int lineFeedIndex = this.getText().indexOf('\r');
      lineFeedIndex = lineFeedIndex < 0 ? this.getText().length() : (lineFeedIndex + 1);
      String line = this.getText().substring(0, lineFeedIndex);
      if (line.length() > 20) {
        text = line.substring(0, 20) + "...";
      } else {
        text = line;
      }
    }
  }

  public long getCollectedComplexity() {
    return memoizedComplexityComputation.get();
  }

  private long computeCollectedComplexity() {
    LongSummaryStatistics statistics =
        this.getChildren().stream()
            .mapToLong(MetricsModel::getCollectedComplexity)
            .summaryStatistics();

    if (getChildren().isEmpty()) {
      return this.getComplexity();
    } else {
      if (this.getCollectorType() == MAX) {
        return statistics.getMax() + this.getComplexity();
      } else {
        return statistics.getSum() + this.getComplexity();
      }
    }
  }

  public String getSummary() {
    return getSummary(true);
  }

  public String getSummary(boolean includeChildNodes) {
    return String.format(
        "+%s %s (%s)",
        includeChildNodes ? getCollectedComplexity() : getComplexity(),
        getTextToShow(),
        getDescription());
  }

  public String getTextToShow() {
    if (this.name != null) {
      return this.name;
    }
    return this.getText();
  }

  public String toString(MetricsConfiguration settings) {
    long complexitySum = this.getCollectedComplexity();
    String instruction = "";
    if (complexitySum > settings.complexityLevelExtreme) {
      instruction = settings.complexityLevelExtremeDescription;
    } else if (complexitySum > settings.complexityLevelHigh) {
      instruction = settings.complexityLevelHighDescription;
    } else if (complexitySum > settings.complexityLevelNormal) {
      instruction = settings.complexityLevelNormalDescription;
    } else if (complexitySum > settings.complexityLevelLow) {
      instruction = settings.complexityLevelLowDescription;
    }

    String template = settings.complexityTemplate;
    if (Strings.isNullOrEmpty(template)) {
      template = "Complexity is {0} {1}";
    }

    return template.replace("{0}", complexitySum + "").replace("{1}", instruction);
  }

  public int getComplexity() {
    return complexity;
  }

  public boolean isVisible() {
    return visible;
  }

  public List<MetricsModel> getChildren() {
    return children;
  }

  public String getDescription() {
    return description;
  }

  public String getText() {
    return text;
  }

  public CollectorType getCollectorType() {
    return collectorType;
  }

  public int getTextOffset() {
    return textOffset;
  }
}
