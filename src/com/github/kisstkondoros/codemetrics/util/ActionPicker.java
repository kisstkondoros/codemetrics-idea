package com.github.kisstkondoros.codemetrics.util;

import com.github.kisstkondoros.codemetrics.core.MetricsModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.ui.awt.RelativePoint;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActionPicker {
    public static void showRefactorActionPicker(RelativePoint point, Editor editor, MetricsModel model) {
        Map<PsiElement, MetricsModel> psiElementToModelMap =
                Stream.concat(Stream.of(model), model.getChildren().stream()).filter(p -> p.getCollectedComplexity() >
                        0).collect(Collectors.toMap(MetricsModel::getNode, Function.identity()));

        Comparator<MetricsModel> metricsModelComparator =
                Comparator.comparing((MetricsModel p) -> p.getNode().getTextOffset());

        List<PsiElement> psiElements =
                psiElementToModelMap.values().stream().sorted(metricsModelComparator).map(MetricsModel::getNode).collect(Collectors.toList());

        JBPopup jbPopup =
                PsiElementPicker.navigateOrCreatePopup(psiElements, "Complexity increasing elements", "Elements",
                        new MetricsCellRenderer(psiElementToModelMap), ActionPicker::onItemPicked);
        if (jbPopup != null && editor != null) {
            jbPopup.show(point);
        }
    }

    private static void onItemPicked(List<PsiElement> selectedElements) {
        for (PsiElement element : selectedElements) {
            if (element instanceof Navigatable) {
                Navigatable navigatable = (Navigatable) element;
                if (navigatable.canNavigate()) {
                    navigatable.navigate(true);
                }
            }
        }
    }
}
