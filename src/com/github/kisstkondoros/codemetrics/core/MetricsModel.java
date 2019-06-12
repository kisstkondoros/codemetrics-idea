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
  private PsiElement node;
  private int complexity;
  private boolean visible;
  private List<MetricsModel> children = new ArrayList<>();
  private String description;
  private String text;
  private CollectorType collectorType;
  private Supplier<Long> memoizedComplexityComputation =
      Suppliers.memoize(this::computeCollectedComplexity);

  public MetricsModel(
      PsiElement node,
      int complexity,
      String description,
      boolean trim,
      boolean visible,
      CollectorType collectorType) {
    this.setNode(node);
    this.setComplexity(complexity);
    this.setVisible(visible);
    this.setDescription(description);
    this.setCollectorType(collectorType);
    this.storeText(trim);
  }

  private void storeText(boolean trim) {
    this.setText(getNode().getText());
    if (trim) {
      int lineFeedIndex = this.getText().indexOf('\r');
      lineFeedIndex = lineFeedIndex < 0 ? this.getText().length() : (lineFeedIndex + 1);
      String line = this.getText().substring(0, lineFeedIndex);
      if (line.length() > 20) {
        this.setText(line.substring(0, 20) + "...");
      } else {
        this.setText(line);
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
    return String.format(
        "+%s %s (%s)", getCollectedComplexity(), getTextToShow(), getDescription());
  }

  public String getTextToShow() {
    if (getNode() instanceof PsiNamedElement) {
      return ((PsiNamedElement) getNode()).getName();
    }
    return this.getText();
  }

  private String pad(String input, int lenghtToFit) {
    return Strings.padStart(input, lenghtToFit, ' ');
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

  public PsiElement getNode() {
    return node;
  }

  public void setNode(PsiElement node) {
    this.node = node;
  }

  public int getComplexity() {
    return complexity;
  }

  public void setComplexity(int complexity) {
    this.complexity = complexity;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public List<MetricsModel> getChildren() {
    return children;
  }

  public void setChildren(List<MetricsModel> children) {
    this.children = children;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public CollectorType getCollectorType() {
    return collectorType;
  }

  public void setCollectorType(CollectorType collectorType) {
    this.collectorType = collectorType;
  }
}
