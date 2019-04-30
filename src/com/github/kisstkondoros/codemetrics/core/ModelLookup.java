package com.github.kisstkondoros.codemetrics.core;

import com.intellij.psi.PsiElement;

public final class ModelLookup {

    private ModelLookup() {
    }

    public static MetricsModel find(MetricsModel parent, PsiElement element) {
        if (parent.node == element) {
            return parent;
        }
        for (MetricsModel childModel : parent.children) {
            MetricsModel model = find(childModel, element);
            if (model != null) {
                return model;
            }
        }
        return null;
    }
}
