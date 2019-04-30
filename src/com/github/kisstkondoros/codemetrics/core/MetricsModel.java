package com.github.kisstkondoros.codemetrics.core;

import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.google.common.base.Strings;
import com.intellij.openapi.util.text.LineColumn;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;

import static com.github.kisstkondoros.codemetrics.core.CollectorType.MAX;

public class MetricsModel {
    public PsiElement node;
    public int complexity;
    public boolean visible;
    public List<MetricsModel> children = new ArrayList<>();
    public String description;
    public String text;
    public CollectorType collectorType;

    public MetricsModel(PsiElement node, int complexity, String description, boolean trim, boolean visible, CollectorType collectorType) {
        this.node = node;
        this.complexity = complexity;
        this.visible = visible;
        this.description = description;
        this.collectorType = collectorType;
        this.storeText(trim);
    }

    private void storeText(boolean trim) {
        this.text = node.getText();
        if (trim) {
            int lineFeedIndex = this.text.indexOf('\r');
            lineFeedIndex = lineFeedIndex < 0 ? this.text.length() : (lineFeedIndex + 1);
            String line = this.text.substring(0, lineFeedIndex);
            if (line.length() > 20) {
                this.text = line.substring(0, 20) + "...";
            } else {
                this.text = line;
            }
        }
    }

    public long getCollectedComplexity() {
        LongSummaryStatistics statistics = this.children.stream().mapToLong(MetricsModel::getCollectedComplexity).summaryStatistics();

        if (children.isEmpty()) {
            return this.complexity;
        } else {
            if (this.collectorType == MAX) {
                return statistics.getMax() + this.complexity;
            } else {
                return statistics.getSum() + this.complexity;
            }
        }
    }

    public String toLogString(String level) {
        String textToShow = getTextToShow();
        LineColumn lineColumn = StringUtil.offsetToLineColumn(node.getContainingFile().getText(), node.getTextOffset());
        String complexity = this.pad(this.getCollectedComplexity() + "", 3);
        String line = this.pad(lineColumn.line + "", 4);
        String column = this.pad(lineColumn.column + "", 4);

        return String.format("+%s - Ln %s Col %s %s %s", complexity, line, column, level, textToShow);
    }

    private String getTextToShow() {
        if (node instanceof PsiNamedElement) {
            return ((PsiNamedElement) node).getName();
        }
        return this.text;
    }

    private String pad(String input, int lenghtToFit) {
        return Strings.padStart(input, lenghtToFit, ' ');
    }

    public String toString(MetricsConfiguration settings) {
        long complexitySum = this.getCollectedComplexity();
        String instruction = "";
        if (complexitySum > settings.complexityLevelExtreme) {
            instruction = settings.complexityLevelExtremeDescription;
        } else if (complexitySum > settings.complexityLevelHigh) {
            instruction = settings.complexityLevelHighDescription;
        } else if (complexitySum > settings.complexityLevelNormal) {
            instruction = settings.complexityLevelNormalDescription;
        } else if (complexitySum > settings.complexityLevelLow) {
            instruction = settings.complexityLevelLowDescription;
        }

        String template = settings.complexityTemplate;
        if (Strings.isNullOrEmpty(template)) {
            template = "Complexity is {0} {1}";
        }

        return template.replace("{0}", complexitySum + "").replace("{1}", instruction);
    }

}