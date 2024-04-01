package com.github.kisstkondoros.codemetrics.util;

import com.github.kisstkondoros.codemetrics.core.MetricsModel;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NavigatingPicker {
  public static void showPicker(
      RelativePoint point, Project project, VirtualFile virtualFile, MetricsModel root) {
    Comparator<MetricsModel> metricsModelComparator =
        Comparator.comparing(MetricsModel::getTextOffset);

    List<MetricsModel> elements =
        Stream.concat(Stream.of(root), root.getChildren().stream())
            .filter(p -> p.getCollectedComplexity() > 0)
            .sorted(metricsModelComparator)
            .collect(Collectors.toList());

    JBPopup jbPopup =
        Picker.navigateOrCreatePopup(
            elements,
            "Complexity increasing elements",
            new ColoredListCellRenderer<MetricsModel>() {

              @Override
              protected void customizeCellRenderer(
                  @NotNull JList list,
                  MetricsModel value,
                  int index,
                  boolean selected,
                  boolean hasFocus) {
                this.append(root == value ? root.getSummary(false) : value.getSummary());
              }
            },
            (List<MetricsModel> selectedElements) ->
                onItemsPicked(project, virtualFile, selectedElements));
    if (jbPopup != null) {
      jbPopup.show(point);
    }
  }

  private static void onItemsPicked(
      Project project, VirtualFile virtualFile, List<MetricsModel> selectedElements) {
    selectedElements.stream()
        .findFirst()
        .ifPresent(selected -> navigate(project, virtualFile, selected));
  }

  private static void navigate(Project project, VirtualFile virtualFile, MetricsModel selected) {
    ApplicationManager.getApplication()
        .invokeLater(
            () -> {
              FileEditorManager instance = FileEditorManager.getInstance(project);
              instance.openTextEditor(
                  new OpenFileDescriptor(project, virtualFile, selected.getTextOffset()), true);
            });
  }
}
