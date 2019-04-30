package com.github.kisstkondoros.codemetrics.ui;


import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.MarkerType;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import static com.intellij.codeHighlighting.Pass.UPDATE_ALL;

public class MetricsMarkerInfo extends LineMarkerInfo<PsiElement> {

    public MetricsMarkerInfo(@NotNull PsiElement element, long collectedComplexity, @NotNull MarkerType markerType) {
        super(element, element.getTextRange(), new MetricsIcon(collectedComplexity), UPDATE_ALL, markerType.getTooltip(),
                markerType.getNavigationHandler(), GutterIconRenderer.Alignment.LEFT);
    }

}