package com.github.kisstkondoros.codemetrics;

import com.intellij.codeInsight.navigation.ListBackgroundUpdaterTask;
import com.intellij.find.FindUtil;
import com.intellij.ide.PsiCopyPasteManager;
import com.intellij.ide.util.PsiElementListCellRenderer;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.JBListWithHintProvider;
import com.intellij.ui.popup.AbstractPopup;
import com.intellij.ui.popup.HintUpdateSupply;
import com.intellij.usages.UsageView;
import com.intellij.util.Consumer;
import com.intellij.util.Processor;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.util.List;


public class PsiElementList {

    private PsiElementList() {
    }

    @Nullable
    public static JBPopup navigateOrCreatePopup(final PsiElement[] targets,
                                                final String title,
                                                final String findUsagesTitle,
                                                final ListCellRenderer listRenderer,
                                                @Nullable final ListBackgroundUpdaterTask listUpdaterTask,
                                                final Consumer<Object[]> consumer) {
        if (targets.length == 0) return null;
        if (targets.length == 1) {
            consumer.consume(targets);
            return null;
        }
        final CollectionListModel<PsiElement> model = new CollectionListModel<PsiElement>(targets);
        final JBListWithHintProvider list = new JBListWithHintProvider(model) {
            @Override
            protected PsiElement getPsiElementForHint(final Object selectedValue) {
                return (PsiElement) selectedValue;
            }
        };

        list.setTransferHandler(new TransferHandler() {
            @Nullable
            @Override
            protected Transferable createTransferable(JComponent c) {
                final Object[] selectedValues = list.getSelectedValues();
                final PsiElement[] copy = new PsiElement[selectedValues.length];
                for (int i = 0; i < selectedValues.length; i++) {
                    copy[i] = (PsiElement) selectedValues[i];
                }
                return new PsiCopyPasteManager.MyTransferable(copy);
            }

            @Override
            public int getSourceActions(JComponent c) {
                return COPY;
            }
        });

        list.setCellRenderer(listRenderer);

        final PopupChooserBuilder builder = new PopupChooserBuilder(list);
        if (listRenderer instanceof PsiElementListCellRenderer) {
            ((PsiElementListCellRenderer) listRenderer).installSpeedSearch(builder);
        }

        PopupChooserBuilder popupChooserBuilder = builder.
                setTitle(title).
                setMovable(true).
                setResizable(true).
                setItemChoosenCallback(() -> {
                    int[] ids = list.getSelectedIndices();
                    if (ids == null || ids.length == 0) return;
                    Object[] selectedElements = list.getSelectedValues();
                    consumer.consume(selectedElements);
                }).
                setCancelCallback(() -> {
                    HintUpdateSupply.hideHint(list);
                    return true;
                });
        final Ref<UsageView> usageView = new Ref<UsageView>();
        if (findUsagesTitle != null) {
            popupChooserBuilder = popupChooserBuilder.setCouldPin((Processor<JBPopup>) (popup) -> {
                final List<PsiElement> items = model.getItems();
                usageView.set(FindUtil.showInUsageView(null, items.toArray(new PsiElement[items.size()]), findUsagesTitle, targets[0].getProject()));
                popup.cancel();
                return false;
            });
        }

        final JBPopup popup = popupChooserBuilder.createPopup();
        if (listUpdaterTask != null) {
            listUpdaterTask.init((AbstractPopup) popup, list, usageView);

            ProgressManager.getInstance().run(listUpdaterTask);
        }
        return popup;
    }
}

