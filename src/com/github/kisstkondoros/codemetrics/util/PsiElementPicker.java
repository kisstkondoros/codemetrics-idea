package com.github.kisstkondoros.codemetrics.util;

import com.google.common.collect.ImmutableList;
import com.intellij.find.FindUtil;
import com.intellij.ide.util.PsiElementListCellRenderer;
import com.intellij.openapi.ui.popup.IPopupChooserBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.popup.HintUpdateSupply;
import com.intellij.usages.UsageView;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class PsiElementPicker {

  private PsiElementPicker() {}

  @Nullable
  public static JBPopup navigateOrCreatePopup(
      final List<PsiElement> targets,
      final String title,
      final String findUsagesTitle,
      final ListCellRenderer<PsiElement> listRenderer,
      final Consumer<List<PsiElement>> consumer) {
    if (targets.isEmpty()) return null;
    if (targets.size() == 1) {
      consumer.consume(targets);
      return null;
    }
    final CollectionListModel<PsiElement> model = new CollectionListModel<>(targets);
    final JList<PsiElement> list = new JBList<>(model);
    HintUpdateSupply.installHintUpdateSupply(list, selectedValue -> (PsiElement) selectedValue);

    list.setCellRenderer(listRenderer);

    final IPopupChooserBuilder<PsiElement> builder = new PopupChooserBuilder<>(list);
    if (listRenderer instanceof PsiElementListCellRenderer) {
      ((PsiElementListCellRenderer) listRenderer).installSpeedSearch(builder);
    }

    IPopupChooserBuilder<PsiElement> popupChooserBuilder =
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
    final Ref<UsageView> usageView = new Ref<>();
    if (findUsagesTitle != null) {
      popupChooserBuilder =
          popupChooserBuilder.setCouldPin(
              (popup) -> {
                final List<PsiElement> items = model.getItems();
                usageView.set(
                    FindUtil.showInUsageView(
                        null,
                        items.toArray(new PsiElement[0]),
                        findUsagesTitle,
                        targets.get(0).getProject()));
                popup.cancel();
                return false;
              });
    }

    return popupChooserBuilder.createPopup();
  }
}
