package com.github.kisstkondoros.codemetrics.configuration;

import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.Comparing;
import com.intellij.ui.ColorPanel;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.panels.HorizontalLayout;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class EditorConfig implements Configurable {
    private final MetricsConfiguration configuration;
    private final java.util.List<BeanField<?, ?>> basicFields;
    private final java.util.List<BeanField<?, ?>> advancedFields;
    private final java.util.List<BeanField<?, ?>> miscFields;

    protected EditorConfig() {
        configuration = MetricsConfiguration.getInstance();
        basicFields = new ArrayList<>();
        advancedFields = new ArrayList<>();
        miscFields = new ArrayList<>();

        /* basic fields */
        colorPicker(
                basicFields,
                () -> configuration.complexityColorLow,
                v -> configuration.complexityColorLow = v,
                "Complexity color low");
        colorPicker(
                basicFields,
                () -> configuration.complexityColorNormal,
                v -> configuration.complexityColorNormal = v,
                "Complexity color normal");
        colorPicker(
                basicFields,
                () -> configuration.complexityColorHigh,
                v -> configuration.complexityColorHigh = v,
                "Complexity color high");
        colorPicker(
                basicFields,
                () -> configuration.complexityColorExtreme,
                v -> configuration.complexityColorExtreme = v,
                "Complexity color extreme");

        numeric(
                basicFields,
                () -> configuration.complexityLevelLow,
                v -> configuration.complexityLevelLow = v,
                "Complexity level low");
        numeric(
                basicFields,
                () -> configuration.complexityLevelNormal,
                v -> configuration.complexityLevelNormal = v,
                "Complexity level normal");
        numeric(
                basicFields,
                () -> configuration.complexityLevelHigh,
                v -> configuration.complexityLevelHigh = v,
                "Complexity level high");
        numeric(
                basicFields,
                () -> configuration.complexityLevelExtreme,
                v -> configuration.complexityLevelExtreme = v,
                "Complexity level extreme");

        numeric(
                basicFields,
                () -> configuration.hiddenUnder,
                v -> configuration.hiddenUnder = v,
                "Show metrics above complexity");

        checkBox(
                basicFields,
                () -> configuration.metricsForAnonymousClass,
                v -> configuration.metricsForAnonymousClass = v,
                "Show metrics for anonymous classes");
        checkBox(
                basicFields,
                () -> configuration.metricsForAClass,
                v -> configuration.metricsForAClass = v,
                "Show metrics for classes");
        checkBox(
                basicFields,
                () -> configuration.metricsForMethod,
                v -> configuration.metricsForMethod = v,
                "Show metrics for methods");
        checkBox(
                basicFields,
                () -> configuration.metricsForLambdaExpression,
                v -> configuration.metricsForLambdaExpression = v,
                "Show metrics for lambda expressions");

        /* advanced fields */
        text(
                advancedFields,
                () -> configuration.complexityLevelExtremeDescription,
                (v) -> configuration.complexityLevelExtremeDescription = v,
                "Description for extreme complexity");
        text(
                advancedFields,
                () -> configuration.complexityLevelHighDescription,
                (v) -> configuration.complexityLevelHighDescription = v,
                "Description for high complexity");
        text(
                advancedFields,
                () -> configuration.complexityLevelNormalDescription,
                (v) -> configuration.complexityLevelNormalDescription = v,
                "Description for normal complexity");
        text(
                advancedFields,
                () -> configuration.complexityLevelLowDescription,
                (v) -> configuration.complexityLevelLowDescription = v,
                "Description for low complexity");
        text(
                advancedFields,
                () -> configuration.complexityTemplate,
                (v) -> configuration.complexityTemplate = v,
                "Template of complexity description");

        MetricsConfiguration baseConfiguration = new MetricsConfiguration();
        numeric(
                advancedFields,
                () -> configuration.anonymousClass,
                v -> configuration.anonymousClass = v,
                baseConfiguration.anonymousClassDescription);
        numeric(
                advancedFields,
                () -> configuration.arrayAccessExpression,
                v -> configuration.arrayAccessExpression = v,
                baseConfiguration.arrayAccessExpressionDescription);
        numeric(
                advancedFields,
                () -> configuration.blockStatement,
                v -> configuration.blockStatement = v,
                baseConfiguration.blockStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.breakStatement,
                v -> configuration.breakStatement = v,
                baseConfiguration.breakStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.aClass,
                v -> configuration.aClass = v,
                baseConfiguration.aClassDescription);
        numeric(
                advancedFields,
                () -> configuration.classInitializer,
                v -> configuration.classInitializer = v,
                baseConfiguration.classInitializerDescription);
        numeric(
                advancedFields,
                () -> configuration.continueStatement,
                v -> configuration.continueStatement = v,
                baseConfiguration.continueStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.declarationStatement,
                v -> configuration.declarationStatement = v,
                baseConfiguration.declarationStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.docComment,
                v -> configuration.docComment = v,
                baseConfiguration.docCommentDescription);
        numeric(
                advancedFields,
                () -> configuration.doWhileStatement,
                v -> configuration.doWhileStatement = v,
                baseConfiguration.doWhileStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.expressionList,
                v -> configuration.expressionList = v,
                baseConfiguration.expressionListDescription);
        numeric(
                advancedFields,
                () -> configuration.expressionListStatement,
                v -> configuration.expressionListStatement = v,
                baseConfiguration.expressionListStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.expressionStatement,
                v -> configuration.expressionStatement = v,
                baseConfiguration.expressionStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.field,
                v -> configuration.field = v,
                baseConfiguration.fieldDescription);
        numeric(
                advancedFields,
                () -> configuration.forStatement,
                v -> configuration.forStatement = v,
                baseConfiguration.forStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.foreachStatement,
                v -> configuration.foreachStatement = v,
                baseConfiguration.foreachStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.ifStatement,
                v -> configuration.ifStatement = v,
                baseConfiguration.ifStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.importStatement,
                v -> configuration.importStatement = v,
                baseConfiguration.importStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.labeledStatement,
                v -> configuration.labeledStatement = v,
                baseConfiguration.labeledStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.literalExpression,
                v -> configuration.literalExpression = v,
                baseConfiguration.literalExpressionDescription);
        numeric(
                advancedFields,
                () -> configuration.localVariable,
                v -> configuration.localVariable = v,
                baseConfiguration.localVariableDescription);
        numeric(
                advancedFields,
                () -> configuration.method,
                v -> configuration.method = v,
                baseConfiguration.methodDescription);
        numeric(
                advancedFields,
                () -> configuration.methodCallExpression,
                v -> configuration.methodCallExpression = v,
                baseConfiguration.methodCallExpressionDescription);
        numeric(
                advancedFields,
                () -> configuration.parameter,
                v -> configuration.parameter = v,
                baseConfiguration.parameterDescription);
        numeric(
                advancedFields,
                () -> configuration.postfixExpression,
                v -> configuration.postfixExpression = v,
                baseConfiguration.postfixExpressionDescription);
        numeric(
                advancedFields,
                () -> configuration.prefixExpression,
                v -> configuration.prefixExpression = v,
                baseConfiguration.prefixExpressionDescription);
        numeric(
                advancedFields,
                () -> configuration.returnStatement,
                v -> configuration.returnStatement = v,
                baseConfiguration.returnStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.superExpression,
                v -> configuration.superExpression = v,
                baseConfiguration.superExpressionDescription);
        numeric(
                advancedFields,
                () -> configuration.thisExpression,
                v -> configuration.thisExpression = v,
                baseConfiguration.thisExpressionDescription);
        numeric(
                advancedFields,
                () -> configuration.throwStatement,
                v -> configuration.throwStatement = v,
                baseConfiguration.throwStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.tryStatement,
                v -> configuration.tryStatement = v,
                baseConfiguration.tryStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.catchSection,
                v -> configuration.catchSection = v,
                baseConfiguration.catchSectionDescription);
        numeric(
                advancedFields,
                () -> configuration.whileStatement,
                v -> configuration.whileStatement = v,
                baseConfiguration.whileStatementDescription);
        numeric(
                advancedFields,
                () -> configuration.annotation,
                v -> configuration.annotation = v,
                baseConfiguration.annotationDescription);
        numeric(
                advancedFields,
                () -> configuration.annotationMethod,
                v -> configuration.annotationMethod = v,
                baseConfiguration.annotationMethodDescription);
        numeric(
                advancedFields,
                () -> configuration.enumConstant,
                v -> configuration.enumConstant = v,
                baseConfiguration.enumConstantDescription);
        numeric(
                advancedFields,
                () -> configuration.polyadicExpression,
                v -> configuration.polyadicExpression = v,
                baseConfiguration.polyadicExpressionDescription);
        numeric(
                advancedFields,
                () -> configuration.lambdaExpression,
                v -> configuration.lambdaExpression = v,
                baseConfiguration.lambdaExpressionDescription);
        numeric(
                advancedFields,
                () -> configuration.emptyExpression,
                v -> configuration.emptyExpression = v,
                baseConfiguration.emptyExpressionDescription);
        numeric(
                advancedFields,
                () -> configuration.switchExpression,
                v -> configuration.switchExpression = v,
                baseConfiguration.switchExpressionDescription);
        numeric(
                advancedFields,
                () -> configuration.referenceExpression,
                v -> configuration.referenceExpression = v,
                baseConfiguration.referenceExpressionDescription);
        numeric(
                advancedFields,
                () -> configuration.parenthesizedExpression,
                v -> configuration.parenthesizedExpression = v,
                baseConfiguration.parenthesizedExpressionDescription);

        text(
                miscFields,
                () -> configuration.anonymousClassDescription,
                v -> configuration.anonymousClassDescription = v,
                baseConfiguration.anonymousClassDescription);
        text(
                miscFields,
                () -> configuration.arrayAccessExpressionDescription,
                v -> configuration.arrayAccessExpressionDescription = v,
                baseConfiguration.arrayAccessExpressionDescription);
        text(
                miscFields,
                () -> configuration.blockStatementDescription,
                v -> configuration.blockStatementDescription = v,
                baseConfiguration.blockStatementDescription);
        text(
                miscFields,
                () -> configuration.breakStatementDescription,
                v -> configuration.breakStatementDescription = v,
                baseConfiguration.breakStatementDescription);
        text(
                miscFields,
                () -> configuration.aClassDescription,
                v -> configuration.aClassDescription = v,
                baseConfiguration.aClassDescription);
        text(
                miscFields,
                () -> configuration.classInitializerDescription,
                v -> configuration.classInitializerDescription = v,
                baseConfiguration.classInitializerDescription);
        text(
                miscFields,
                () -> configuration.continueStatementDescription,
                v -> configuration.continueStatementDescription = v,
                baseConfiguration.continueStatementDescription);
        text(
                miscFields,
                () -> configuration.declarationStatementDescription,
                v -> configuration.declarationStatementDescription = v,
                baseConfiguration.declarationStatementDescription);
        text(
                miscFields,
                () -> configuration.docCommentDescription,
                v -> configuration.docCommentDescription = v,
                baseConfiguration.docCommentDescription);
        text(
                miscFields,
                () -> configuration.doWhileStatementDescription,
                v -> configuration.doWhileStatementDescription = v,
                baseConfiguration.doWhileStatementDescription);
        text(
                miscFields,
                () -> configuration.expressionListDescription,
                v -> configuration.expressionListDescription = v,
                baseConfiguration.expressionListDescription);
        text(
                miscFields,
                () -> configuration.expressionListStatementDescription,
                v -> configuration.expressionListStatementDescription = v,
                baseConfiguration.expressionListStatementDescription);
        text(
                miscFields,
                () -> configuration.expressionStatementDescription,
                v -> configuration.expressionStatementDescription = v,
                baseConfiguration.expressionStatementDescription);
        text(
                miscFields,
                () -> configuration.fieldDescription,
                v -> configuration.fieldDescription = v,
                baseConfiguration.fieldDescription);
        text(
                miscFields,
                () -> configuration.forStatementDescription,
                v -> configuration.forStatementDescription = v,
                baseConfiguration.forStatementDescription);
        text(
                miscFields,
                () -> configuration.foreachStatementDescription,
                v -> configuration.foreachStatementDescription = v,
                baseConfiguration.foreachStatementDescription);
        text(
                miscFields,
                () -> configuration.ifStatementDescription,
                v -> configuration.ifStatementDescription = v,
                baseConfiguration.ifStatementDescription);
        text(
                miscFields,
                () -> configuration.importStatementDescription,
                v -> configuration.importStatementDescription = v,
                baseConfiguration.importStatementDescription);
        text(
                miscFields,
                () -> configuration.labeledStatementDescription,
                v -> configuration.labeledStatementDescription = v,
                baseConfiguration.labeledStatementDescription);
        text(
                miscFields,
                () -> configuration.literalExpressionDescription,
                v -> configuration.literalExpressionDescription = v,
                baseConfiguration.literalExpressionDescription);
        text(
                miscFields,
                () -> configuration.localVariableDescription,
                v -> configuration.localVariableDescription = v,
                baseConfiguration.localVariableDescription);
        text(
                miscFields,
                () -> configuration.methodDescription,
                v -> configuration.methodDescription = v,
                baseConfiguration.methodDescription);
        text(
                miscFields,
                () -> configuration.methodCallExpressionDescription,
                v -> configuration.methodCallExpressionDescription = v,
                baseConfiguration.methodCallExpressionDescription);
        text(
                miscFields,
                () -> configuration.parameterDescription,
                v -> configuration.parameterDescription = v,
                baseConfiguration.parameterDescription);
        text(
                miscFields,
                () -> configuration.postfixExpressionDescription,
                v -> configuration.postfixExpressionDescription = v,
                baseConfiguration.postfixExpressionDescription);
        text(
                miscFields,
                () -> configuration.prefixExpressionDescription,
                v -> configuration.prefixExpressionDescription = v,
                baseConfiguration.prefixExpressionDescription);
        text(
                miscFields,
                () -> configuration.returnStatementDescription,
                v -> configuration.returnStatementDescription = v,
                baseConfiguration.returnStatementDescription);
        text(
                miscFields,
                () -> configuration.superExpressionDescription,
                v -> configuration.superExpressionDescription = v,
                baseConfiguration.superExpressionDescription);
        text(
                miscFields,
                () -> configuration.thisExpressionDescription,
                v -> configuration.thisExpressionDescription = v,
                baseConfiguration.thisExpressionDescription);
        text(
                miscFields,
                () -> configuration.throwStatementDescription,
                v -> configuration.throwStatementDescription = v,
                baseConfiguration.throwStatementDescription);
        text(
                miscFields,
                () -> configuration.tryStatementDescription,
                v -> configuration.tryStatementDescription = v,
                baseConfiguration.tryStatementDescription);
        text(
                miscFields,
                () -> configuration.catchSectionDescription,
                v -> configuration.catchSectionDescription = v,
                baseConfiguration.catchSectionDescription);
        text(
                miscFields,
                () -> configuration.whileStatementDescription,
                v -> configuration.whileStatementDescription = v,
                baseConfiguration.whileStatementDescription);
        text(
                miscFields,
                () -> configuration.annotationDescription,
                v -> configuration.annotationDescription = v,
                baseConfiguration.annotationDescription);
        text(
                miscFields,
                () -> configuration.annotationMethodDescription,
                v -> configuration.annotationMethodDescription = v,
                baseConfiguration.annotationMethodDescription);
        text(
                miscFields,
                () -> configuration.enumConstantDescription,
                v -> configuration.enumConstantDescription = v,
                baseConfiguration.enumConstantDescription);
        text(
                miscFields,
                () -> configuration.polyadicExpressionDescription,
                v -> configuration.polyadicExpressionDescription = v,
                baseConfiguration.polyadicExpressionDescription);
        text(
                miscFields,
                () -> configuration.lambdaExpressionDescription,
                v -> configuration.lambdaExpressionDescription = v,
                baseConfiguration.lambdaExpressionDescription);
        text(
                miscFields,
                () -> configuration.emptyExpressionDescription,
                v -> configuration.emptyExpressionDescription = v,
                baseConfiguration.emptyExpressionDescription);
        text(
                miscFields,
                () -> configuration.switchExpressionDescription,
                v -> configuration.switchExpressionDescription = v,
                baseConfiguration.switchExpressionDescription);
        text(
                miscFields,
                () -> configuration.referenceExpressionDescription,
                v -> configuration.referenceExpressionDescription = v,
                baseConfiguration.referenceExpressionDescription);
        text(
                miscFields,
                () -> configuration.parenthesizedExpressionDescription,
                v -> configuration.parenthesizedExpressionDescription = v,
                baseConfiguration.parenthesizedExpressionDescription);
    }

    private void checkBox(
            java.util.List<BeanField<?, ?>> fields,
            Supplier<Boolean> getter,
            Consumer<Boolean> setter,
            String title) {
        fields.add(new CheckBoxField(getter, setter, title));
    }

    private void text(
            java.util.List<BeanField<?, ?>> fields,
            Supplier<String> getter,
            Consumer<String> setter,
            String title) {
        fields.add(new TextField(getter, setter, title));
    }

    private void numeric(
            java.util.List<BeanField<?, ?>> fields,
            Supplier<Integer> getter,
            Consumer<Integer> setter,
            String title) {
        fields.add(new NumericField(getter, setter, title));
    }

    private void colorPicker(
            java.util.List<BeanField<?, ?>> fields,
            Supplier<Integer> getter,
            Consumer<Integer> setter,
            String title) {
        fields.add(new ColorPickerField(getter, setter, title));
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Code Metrics";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "code.metrics";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JTabbedPane tabbedPane = new JBTabbedPane();

        final JPanel panel = new JPanel(new VerticalLayout(UIUtil.DEFAULT_VGAP));

        for (BeanField<?, ?> field : basicFields) {
            panel.add(field.getComponent());
        }

        final JPanel advanced = new JPanel(new VerticalLayout(UIUtil.DEFAULT_VGAP));

        for (BeanField<?, ?> field : advancedFields) {
            advanced.add(field.getComponent());
        }

        final JPanel misc = new JPanel(new VerticalLayout(UIUtil.DEFAULT_VGAP));
        for (BeanField<?, ?> field : miscFields) {
            misc.add(field.getComponent());
        }

        tabbedPane.add("Basics", wrapIntoScrollPane(panel));
        tabbedPane.add("Advanced", wrapIntoScrollPane(advanced));
        tabbedPane.add("Miscellaneous", wrapIntoScrollPane(misc));

        return tabbedPane;
    }

    @NotNull
    private JScrollPane wrapIntoScrollPane(JPanel misc) {
        JBScrollPane scrollPane = new JBScrollPane(misc, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(300, 300));
        return scrollPane;
    }

    public boolean isModified() {
        return basicFields.stream().anyMatch(BeanField::isModified)
                || advancedFields.stream().anyMatch(BeanField::isModified)
                || miscFields.stream().anyMatch(BeanField::isModified);
    }

    public void apply() {
        basicFields.forEach(BeanField::apply);
        advancedFields.forEach(BeanField::apply);
        miscFields.forEach(BeanField::apply);
        configuration.notifyListeners();
    }

    public void reset() {
        basicFields.forEach(BeanField::reset);
        advancedFields.forEach(BeanField::reset);
        miscFields.forEach(BeanField::reset);
    }

    private abstract static class BeanField<PROP_TYPE, T extends JComponent> {
        private final Supplier<PROP_TYPE> getter;
        private final Consumer<PROP_TYPE> setter;

        T myComponent;

        private BeanField(Supplier<PROP_TYPE> getter, Consumer<PROP_TYPE> setter) {
            this.getter = getter;
            this.setter = setter;
        }

        T getComponent() {
            if (myComponent == null) {
                myComponent = createComponent();
            }
            return myComponent;
        }

        abstract T createComponent();

        boolean isModified() {
            final Object componentValue = getComponentValue();
            final Object beanValue = getBeanValue();
            return !Comparing.equal(componentValue, beanValue);
        }

        void apply() {
            setBeanValue(getComponentValue());
        }

        void reset() {
            setComponentValue(getBeanValue());
        }

        abstract PROP_TYPE getComponentValue();

        abstract void setComponentValue(PROP_TYPE instance);

        PROP_TYPE getBeanValue() {
            return getter.get();
        }

        void setBeanValue(PROP_TYPE value) {
            setter.accept(value);
        }
    }

    private static class ColorPickerField extends BeanField<Integer, JPanel> {

        private final String title;
        private final ColorPanel colorPanel;

        private ColorPickerField(
                Supplier<Integer> getter, Consumer<Integer> setter, final String title) {
            super(getter, setter);
            this.title = title;
            colorPanel = new ColorPanel();
        }

        JPanel createComponent() {
            JPanel jPanel = new JPanel(new HorizontalLayout(8));
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(300, 20));
            label.setText(title);

            jPanel.add(label);
            jPanel.add(colorPanel);
            return jPanel;
        }

        Integer getComponentValue() {
            Color selectedColor = colorPanel.getSelectedColor();
            return selectedColor == null ? 0 : selectedColor.getRGB();
        }

        @SuppressWarnings("UseJBColor")
        void setComponentValue(final Integer instance) {
            Color color = new Color(instance, true);
            colorPanel.setSelectedColor(color);
        }
    }

    private class NumericField extends BeanField<Integer, JPanel> {

        private final JBTextField jbTextField;
        private final String title;

        public NumericField(Supplier<Integer> getter, Consumer<Integer> setter, String title) {
            super(getter, setter);
            this.title = title;
            jbTextField = new JBTextField();
        }

        @Override
        JPanel createComponent() {
            JPanel jPanel = new JPanel(new HorizontalLayout(8));
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(300, 20));
            label.setText(title);

            jPanel.add(label);
            jPanel.add(jbTextField);
            return jPanel;
        }

        @Override
        Integer getComponentValue() {
            String text = jbTextField.getText();
            int result = 0;
            try {
                result = Integer.parseInt(text);
            } catch (Exception e) {
                // intentionally empty
            }

            return result;
        }

        @Override
        void setComponentValue(Integer instance) {
            jbTextField.setText(Objects.toString(instance, "0"));
        }
    }

    private class TextField extends BeanField<String, JPanel> {

        private final JBTextField jbTextField;
        private final String title;

        public TextField(Supplier<String> getter, Consumer<String> setter, String title) {
            super(getter, setter);
            this.title = title;
            jbTextField = new JBTextField();
        }

        @Override
        JPanel createComponent() {
            JPanel jPanel = new JPanel(new HorizontalLayout(8));
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(300, 20));
            label.setText(title);
            int defaultHeight = (int) jbTextField.getPreferredSize().getHeight();
            jbTextField.setPreferredSize(new Dimension(200, defaultHeight));

            jPanel.add(label);
            jPanel.add(jbTextField);
            return jPanel;
        }

        @Override
        String getComponentValue() {
            return jbTextField.getText();
        }

        @Override
        void setComponentValue(String instance) {
            jbTextField.setText(instance);
        }
    }

    private class CheckBoxField extends BeanField<Boolean, JPanel> {

        private final JBCheckBox jbCheckBox;
        private final String title;

        public CheckBoxField(Supplier<Boolean> getter, Consumer<Boolean> setter, String title) {
            super(getter, setter);
            this.title = title;
            jbCheckBox = new JBCheckBox();
        }

        @Override
        JPanel createComponent() {
            JPanel jPanel = new JPanel(new HorizontalLayout(8));

            JLabel label = new JLabel();
            label.setText(title);

            jPanel.add(jbCheckBox);
            jPanel.add(label);
            return jPanel;
        }

        @Override
        Boolean getComponentValue() {
            return jbCheckBox.isSelected();
        }

        @Override
        void setComponentValue(Boolean instance) {
            jbCheckBox.setSelected(Boolean.TRUE.equals(instance));
        }
    }
}
