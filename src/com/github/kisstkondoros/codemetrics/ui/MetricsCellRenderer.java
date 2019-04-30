package com.github.kisstkondoros.codemetrics.ui;

import com.github.kisstkondoros.codemetrics.core.MetricsModel;
import com.github.kisstkondoros.codemetrics.core.ModelLookup;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.psi.PsiElement;

import java.awt.*;

public class MetricsCellRenderer extends DefaultPsiElementCellRenderer {

    private MetricsModel root;

    public MetricsCellRenderer(MetricsModel root) {
        this.root = root;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        int preferredSizeHeight = (int) preferredSize.getHeight();
        return new Dimension(800, preferredSizeHeight);
    }

    @Override
    public String getElementText(PsiElement element) {
        MetricsModel metricsModel = ModelLookup.find(root, element);

        if (metricsModel != null) {
            return metricsModel.toLogString("") + " " + metricsModel.description;
        } else {
            return super.getElementText(element);
        }
    }

}