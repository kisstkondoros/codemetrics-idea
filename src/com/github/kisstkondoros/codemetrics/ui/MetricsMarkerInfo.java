package com.github.kisstkondoros.codemetrics.ui;


import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.MarkerType;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class MetricsMarkerInfo extends LineMarkerInfo<PsiElement> {

    public MetricsMarkerInfo(@NotNull PsiElement element, @NotNull MarkerType markerType, MetricsIcon icon) {
        super(element, element.getTextRange(), icon, markerType.getTooltip(),
                markerType.getNavigationHandler(), GutterIconRenderer.Alignment.LEFT);
    }

}