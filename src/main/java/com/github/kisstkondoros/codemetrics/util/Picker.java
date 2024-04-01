package com.github.kisstkondoros.codemetrics.util;

import com.google.common.collect.ImmutableList;
import com.intellij.ide.util.PsiElementListCellRenderer;
import com.intellij.openapi.ui.popup.IPopupChooserBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.popup.HintUpdateSupply;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class Picker {

  private Picker() {}

  @Nullable
  public static <TYPE> JBPopup navigateOrCreatePopup(
      final List<TYPE> targets,
      final String title,
      final ListCellRenderer<TYPE> listRenderer,
      final Consumer<List<TYPE>> consumer) {
    if (targets.isEmpty()) return null;
    if (targets.size() == 1) {
      consumer.consume(targets);
      return null;
    }
    final CollectionListModel<TYPE> model = new CollectionListModel<>(targets);
    final JList<TYPE> list = new JBList<>(model);

    list.setCellRenderer(listRenderer);

    final IPopupChooserBuilder<TYPE> builder = new PopupChooserBuilder<>(list);
    if (listRenderer instanceof PsiElementListCellRenderer) {
      ((PsiElementListCellRenderer) listRenderer).installSpeedSearch(builder);
    }

    IPopupChooserBuilder<TYPE> popupChooserBuilder =
        builder
            .setTitle(title)
            .setMovable(true)
            .setResizable(true)
            .setItemChosenCallback(item -> consumer.consume(ImmutableList.of(item)))
            .setCancelCallback(
                () -> {
                  HintUpdateSupply.hideHint(list);
                  return true;
                });
    return popupChooserBuilder.createPopup();
  }
}
