package com.github.kisstkondoros.codemetrics.ui;

import com.github.kisstkondoros.codemetrics.core.MetricsModel;
import com.github.kisstkondoros.codemetrics.core.ModelLookup;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MetricsCellRenderer extends DefaultPsiElementCellRenderer {

    private MetricsModel root;

    public MetricsCellRenderer(MetricsModel root) {
        this.root = root;
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
        MetricsModel metricsModel = ModelLookup.find(root, element);

        if (metricsModel != null) {
            return metricsModel.getSummary();
        } else {
            return super.getElementText(element);
        }
    }

}