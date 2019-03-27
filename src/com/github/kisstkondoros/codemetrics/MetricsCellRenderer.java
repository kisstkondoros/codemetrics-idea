package com.github.kisstkondoros.codemetrics;

import com.github.kisstkondoros.codemetrics.visitor.ComplexityVisitor;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Optional;

public class MetricsCellRenderer extends DefaultPsiElementCellRenderer {
    private ComplexityVisitor.ComplexitySummary summary;

    public MetricsCellRenderer(ComplexityVisitor.ComplexitySummary summary) {
        this.summary = summary;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        int preferredSizeHeight = (int) preferredSize.getHeight();
        return new Dimension(800, preferredSizeHeight);
    }

    @Override
    public String getElementText(final PsiElement element) {
        Optional<ComplexityVisitor.ComplexityInfo> complexityInfoOptional = summary.getComplexityInfos().stream().filter(complexityInfo -> complexityInfo.getElement() == element).findFirst();
        return complexityInfoOptional.isPresent() ? complexityInfoOptional.get().getTooltip(null) : super.getElementText(element);
    }

    @Override
    public String getContainerText(PsiElement element, String name) {
        return getSymbolContainerText(name, element);
    }

    private String getSymbolContainerText(String name, PsiElement element) {
        String text = element.getText();

        if (text == null) return null;

        final FontMetrics fm = getFontMetrics(getFont());
        final int maxWidth = 800;

        final LinkedList<String> parts = new LinkedList<String>(StringUtil.split(text, ";"));
        int index;
        while (parts.size() > 1) {
            index = parts.size() - 1;
            parts.remove(index);
            if (fm.stringWidth(StringUtil.join(parts, ";") + "...") < maxWidth) {
                if (index == 0) {
                    parts.add(index, "...");
                }

                return StringUtil.join(parts, ";");
            }
        }

        return "...";
    }

    @Nullable
    @Override
    protected DefaultListCellRenderer getRightCellRenderer(Object value) {
        return null;
    }
}