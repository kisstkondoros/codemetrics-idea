package com.github.kisstkondoros.codemetrics.visitor;

import com.github.kisstkondoros.codemetrics.configuration.Configuration;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.ui.ColorUtil;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ComplexityVisitor extends JavaRecursiveElementVisitor {

    private List<ComplexityInfo> complexityInfos = Lists.newArrayList();
    private Configuration configuration;

    public void visitLambdaExpression(PsiLambdaExpression expression) {
        super.visitLambdaExpression(expression);
        complexityInfos.add(new ComplexityInfo("Lambda expression", configuration.lambdaComplexity, expression));
    }

    public void visitClass(PsiClass aClass) {
        super.visitClass(aClass);
        complexityInfos.add(new ComplexityInfo("Class", configuration.classComplexity, aClass));
    }

    public ComplexityVisitor() {
    }

    @Override
    public void visitReturnStatement(PsiReturnStatement statement) {
        super.visitReturnStatement(statement);
        complexityInfos.add(new ComplexityInfo("Return statement", configuration.returnComplexity, statement));
    }

    @Override
    public void visitTryStatement(PsiTryStatement statement) {
        super.visitTryStatement(statement);
        complexityInfos.add(new ComplexityInfo("Try statement", configuration.tryComplexity, statement));
    }

    @Override
    public void visitAnonymousClass(@NotNull PsiAnonymousClass aClass) {
        super.visitAnonymousClass(aClass);
    }

    @Override
    public void visitForStatement(@NotNull PsiForStatement statement) {
        super.visitForStatement(statement);
        complexityInfos.add(new ComplexityInfo("For statement", configuration.forComplexity, statement));
    }

    @Override
    public void visitForeachStatement(@NotNull PsiForeachStatement statement) {
        super.visitForeachStatement(statement);
        complexityInfos.add(new ComplexityInfo("Foreach statement", configuration.foreachComplexity, statement));
    }

    @Override
    public void visitIfStatement(@NotNull PsiIfStatement statement) {
        super.visitIfStatement(statement);
        complexityInfos.add(new ComplexityInfo("If statement", configuration.ifComplexity, statement));
    }

    @Override
    public void visitDoWhileStatement(@NotNull PsiDoWhileStatement statement) {
        super.visitDoWhileStatement(statement);
        complexityInfos.add(new ComplexityInfo("Do While statement", configuration.doWhileComplexity, statement));
    }

    @Override
    public void visitConditionalExpression(PsiConditionalExpression expression) {
        super.visitConditionalExpression(expression);
        complexityInfos.add(new ComplexityInfo("Conditional expression", configuration.conditionalComplexity, expression));
    }

    @Override
    public void visitSwitchStatement(@NotNull PsiSwitchStatement statement) {
        super.visitSwitchStatement(statement);
        complexityInfos.add(new ComplexityInfo("Switch statement", configuration.switchComplexity, statement));
    }

    @Override
    public void visitWhileStatement(@NotNull PsiWhileStatement statement) {
        super.visitWhileStatement(statement);
        complexityInfos.add(new ComplexityInfo("While statement", configuration.whileComplexity, statement));
    }

    @Override
    public void visitPolyadicExpression(PsiPolyadicExpression expression) {
        super.visitPolyadicExpression(expression);
        final IElementType token = expression.getOperationTokenType();
        if (token.equals(JavaTokenType.ANDAND) || token.equals(JavaTokenType.OROR)) {
            int complexity = expression.getOperands().length - 1;

            complexityInfos.add(new ComplexityInfo("Polyadic expression", complexity * configuration.polyadicComplexity, expression));
        }
    }

    @Override
    public void visitBreakStatement(PsiBreakStatement statement) {
        super.visitBreakStatement(statement);
        complexityInfos.add(new ComplexityInfo("Break statement", configuration.breakComplexity, statement));
    }

    @Override
    public void visitContinueStatement(PsiContinueStatement statement) {
        super.visitContinueStatement(statement);
        complexityInfos.add(new ComplexityInfo("Continue statement", configuration.continueComplexity, statement));
    }

    @Override
    public void visitCatchSection(PsiCatchSection section) {
        super.visitCatchSection(section);
        complexityInfos.add(new ComplexityInfo("Catch", configuration.catchComplexity, section));
    }

    public ComplexitySummary getComplexity(PsiElement element) {
        configuration = Configuration.getInstance();
        if (element instanceof PsiMethod) {
            visitMethod(((PsiMethod) element));
        } else if (element instanceof PsiClass) {
            visitClass(((PsiClass) element));
        }
        return new ComplexitySummary(complexityInfos);
    }

    public static class ComplexityInfo {

        private final String name;
        private final int complexity;

        public PsiElement getElement() {
            return element;
        }

        private final PsiElement element;

        public ComplexityInfo(String name, int complexity, PsiElement element) {
            this.name = name;
            this.complexity = complexity;
            this.element = element;
        }

        public String getTooltip(Document document) {
            String lineInfo = document != null ? (" line " + getLineNumber(document)) : "";
            return "+" + complexity + ", " + name + lineInfo;
        }

        private int getLineNumber(Document document) {
            return document.getLineNumber(element.getTextOffset()) + 1;
        }

        public Comparable getOffset() {
            return element.getTextOffset();
        }
    }

    public class ComplexitySummary {

        public List<ComplexityInfo> getComplexityInfos() {
            return complexityInfos;
        }

        private List<ComplexityInfo> complexityInfos;

        public ComplexitySummary(List<ComplexityInfo> complexityInfos) {
            this.complexityInfos = complexityInfos;
        }

        public Icon getIcon() {
            final int summary = getSummary();
            return new ComplexityIcon(summary);
        }

        public int getSummary() {
            int result = 0;
            for (ComplexityInfo complexityInfo : complexityInfos) {
                result += complexityInfo.complexity;
            }
            return result;
        }

        @Override
        public String toString() {
            return Joiner.on(System.lineSeparator()).skipNulls().join(complexityInfos);
        }

        public String getTooltip(final Document document) {
            String result = "";
            List<ComplexityInfo> lineNumberOrderedComplexityInformation = getLineNumberOrderedComplexityInformation(document);
            for (ComplexityInfo complexityInfo : lineNumberOrderedComplexityInformation) {
                result += System.lineSeparator() + complexityInfo.getTooltip(document);
            }
            return result.trim();
        }

        public List<ComplexityInfo> getLineNumberOrderedComplexityInformation(final Document document) {
            final Ordering<ComplexityInfo> ordering;
            if (document != null) {
                ordering = Ordering.natural().onResultOf(complexityInfo -> complexityInfo != null ? complexityInfo.getLineNumber(document) : 0);
            } else {
                ordering = Ordering.natural().onResultOf(ComplexityInfo::getOffset);
            }
            return ordering.sortedCopy(this.complexityInfos);
        }
    }

    private static class ComplexityIcon implements Icon {
        private final String summary;
        private final Color fontColor;
        private final Color color;
        private final int width;
        private final int height;

        public ComplexityIcon(@NotNull int summary) {
            this.summary = summary + "";
            Configuration configuration = Configuration.getInstance();
            Color errorColor = new Color(configuration.errorColor, true);
            Color errorFontColor = new Color(configuration.errorTextColor, true);
            Color warningColor = new Color(configuration.warningColor, true);
            Color warningFontColor = new Color(configuration.warningTextColor, true);
            Color infoColor = new Color(configuration.informationColor, true);
            Color infoFontColor = new Color(configuration.informationTextColor, true);

            if (summary >= 5) {
                double balance = (summary - 5) / (8 - 5d);
                color = ColorUtil.mix(warningColor, errorColor, balance);
                fontColor = ColorUtil.mix(warningFontColor, errorFontColor, balance);
            } else if (summary > 0) {
                double balance = summary / 5d;
                color = ColorUtil.mix(infoColor, warningColor, balance);
                fontColor = ColorUtil.mix(infoFontColor, warningFontColor, balance);
            } else {
                color = infoColor;
                fontColor = infoFontColor;
            }
            this.width = 16;
            this.height = 16;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            UIUtil.applyRenderingHints(g);

            Font originalFont = g.getFont();
            Color originalColor = g.getColor();
            g.setColor(color);
            g.fillRect(x, y, width, height);

            final Font font = originalFont.deriveFont(Font.BOLD).deriveFont((float) getIconHeight() * 3 / 5);

            g.setFont(font);
            y += getIconHeight() - g.getFontMetrics().getDescent();
            x += (width - g.getFontMetrics(font).stringWidth(summary)) / 2;

            g.setColor(fontColor);
            g.drawString(summary, x, y);

            g.setFont(originalFont);
            g.setColor(originalColor);
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }
    }
}
