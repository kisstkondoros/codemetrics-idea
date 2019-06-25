package com.github.kisstkondoros.codemetrics.util;

import com.github.kisstkondoros.codemetrics.core.MetricsModel;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;
import java.util.Optional;

public class MetricsCellRenderer extends DefaultPsiElementCellRenderer {

  private final MetricsModel root;
  private final Map<PsiElement, MetricsModel> models;

  public MetricsCellRenderer(MetricsModel root, Map<PsiElement, MetricsModel> models) {
    this.root = root;
    this.models = models;
  }

  @Override
  public String getContainerText(PsiElement element, String name) {
    return null;
  }

  @Override
  protected Icon getIcon(PsiElement element) {
    return null;
  }

  @Nullable
  @Override
  protected DefaultListCellRenderer getRightCellRenderer(Object value) {
    return null;
  }

  @Override
  public String getElementText(PsiElement element) {
    return Optional.ofNullable(models.get(element))
        .map(metricsModel -> getSummary(metricsModel))
        .orElseGet(() -> super.getElementText(element));
  }

  private String getSummary(MetricsModel metricsModel) {
    return metricsModel == root ? root.getSummary(false) : metricsModel.getSummary();
  }
}
