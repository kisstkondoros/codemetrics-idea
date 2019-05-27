package com.github.kisstkondoros.codemetrics.configuration;

import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.intellij.ide.plugins.newui.HorizontalLayout;
import com.intellij.ide.plugins.newui.VerticalLayout;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ColorPanel;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

public class EditorConfig implements Configurable {

    private final MetricsConfiguration configuration;
    private java.util.List<BeanField> basicFields;
    private java.util.List<BeanField> advancedFields;

    protected EditorConfig() {
        configuration = MetricsConfiguration.getInstance();
        basicFields = new ArrayList<>();
        advancedFields = new ArrayList<>();

        /* basic fields */
        colorPicker(basicFields, "complexityColorLow", "Complexity color low");
        colorPicker(basicFields, "complexityColorNormal", "Complexity color normal");
        colorPicker(basicFields, "complexityColorHigh", "Complexity color high");
        colorPicker(basicFields, "complexityColorExtreme", "Complexity color extreme");

        numeric(basicFields, "complexityLevelLow", "Complexity level low");
        numeric(basicFields, "complexityLevelNormal", "Complexity level normal");
        numeric(basicFields, "complexityLevelHigh", "Complexity level high");
        numeric(basicFields, "complexityLevelExtreme", "Complexity level extreme");

        numeric(basicFields, "hiddenUnder", "Show metrics above complexity");

        checkBox(basicFields, "metricsForAnonymousClass", "Show metrics for anonymous classes");
        checkBox(basicFields, "metricsForAClass", "Show metrics for classes");
        checkBox(basicFields, "metricsForMethod", "Show metrics for methods");
        checkBox(basicFields, "metricsForLambdaExpression", "Show metrics for lambda expressions");

        /* advanced fields */
        text(advancedFields, "complexityLevelExtremeDescription", "Description for extreme complexity");
        text(advancedFields, "complexityLevelHighDescription", "Description for high complexity");
        text(advancedFields, "complexityLevelNormalDescription", "Description for normal complexity");
        text(advancedFields, "complexityLevelLowDescription", "Description for low complexity");
        text(advancedFields, "complexityTemplate", "Template of complexity description");

        numeric(advancedFields, "anonymousClass", "Anonymous Class");
        numeric(advancedFields, "arrayAccessExpression", "Array Access Expression");
        numeric(advancedFields, "arrayInitializerExpression", "Array Initializer Expression");
        numeric(advancedFields, "assertStatement", "Assert Statement");
        numeric(advancedFields, "assignmentExpression", "Assignment Expression");
        numeric(advancedFields, "binaryExpression", "Binary Expression");
        numeric(advancedFields, "blockStatement", "Block Statement");
        numeric(advancedFields, "breakStatement", "Break Statement");
        numeric(advancedFields, "aClass", "Class");
        numeric(advancedFields, "classInitializer", "Class Initializer");
        numeric(advancedFields, "classObjectAccessExpression", "Class Object Access Expression");
        numeric(advancedFields, "codeBlock", "Code Block");
        numeric(advancedFields, "conditionalExpression", "Conditional Expression");
        numeric(advancedFields, "continueStatement", "Continue Statement");
        numeric(advancedFields, "declarationStatement", "Declaration Statement");
        numeric(advancedFields, "docComment", "Doc Comment");
        numeric(advancedFields, "docTag", "Doc Tag");
        numeric(advancedFields, "docTagValue", "Doc Tag Value");
        numeric(advancedFields, "doWhileStatement", "Do While Statement");
        numeric(advancedFields, "emptyStatement", "Empty Statement");
        numeric(advancedFields, "expression", "Expression");
        numeric(advancedFields, "expressionList", "Expression List");
        numeric(advancedFields, "expressionListStatement", "Expression List Statement");
        numeric(advancedFields, "expressionStatement", "Expression Statement");
        numeric(advancedFields, "field", "Field");
        numeric(advancedFields, "forStatement", "For Statement");
        numeric(advancedFields, "foreachStatement", "Foreach Statement");
        numeric(advancedFields, "identifier", "Identifier");
        numeric(advancedFields, "ifStatement", "If Statement");
        numeric(advancedFields, "importList", "Import List");
        numeric(advancedFields, "importStatement", "Import Statement");
        numeric(advancedFields, "importStaticStatement", "Import Static Statement");
        numeric(advancedFields, "inlineDocTag", "Inline Doc Tag");
        numeric(advancedFields, "instanceOfExpression", "Instance Of Expression");
        numeric(advancedFields, "javaToken", "Java Token");
        numeric(advancedFields, "keyword", "Keyword");
        numeric(advancedFields, "labeledStatement", "Labeled Statement");
        numeric(advancedFields, "literalExpression", "Literal Expression");
        numeric(advancedFields, "localVariable", "Local Variable");
        numeric(advancedFields, "method", "Method");
        numeric(advancedFields, "methodCallExpression", "Method Call Expression");
        numeric(advancedFields, "callExpression", "Call Expression");
        numeric(advancedFields, "modifierList", "Modifier List");
        numeric(advancedFields, "newExpression", "New Expression");
        numeric(advancedFields, "aPackage", "Package");
        numeric(advancedFields, "packageStatement", "Package Statement");
        numeric(advancedFields, "parameter", "Parameter");
        numeric(advancedFields, "receiverParameter", "Receiver Parameter");
        numeric(advancedFields, "parameterList", "Parameter List");
        numeric(advancedFields, "parenthesizedExpression", "Parenthesized Expression");
        numeric(advancedFields, "unaryExpression", "Unary Expression");
        numeric(advancedFields, "postfixExpression", "Postfix Expression");
        numeric(advancedFields, "prefixExpression", "Prefix Expression");
        numeric(advancedFields, "referenceElement", "Reference Element");
        numeric(advancedFields, "importStaticReferenceElement", "Import Static Reference Element");
        numeric(advancedFields, "methodReferenceExpression", "Method Reference Expression");
        numeric(advancedFields, "referenceList", "Reference List");
        numeric(advancedFields, "referenceParameterList", "Reference Parameter List");
        numeric(advancedFields, "typeParameterList", "Type Parameter List");
        numeric(advancedFields, "returnStatement", "Return Statement");
        numeric(advancedFields, "superExpression", "Super Expression");
        numeric(advancedFields, "switchLabelStatement", "Switch Label Statement");
        numeric(advancedFields, "switchStatement", "Switch Statement");
        numeric(advancedFields, "synchronizedStatement", "Synchronized Statement");
        numeric(advancedFields, "thisExpression", "This Expression");
        numeric(advancedFields, "throwStatement", "Throw Statement");
        numeric(advancedFields, "tryStatement", "Try Statement");
        numeric(advancedFields, "catchSection", "Catch Section");
        numeric(advancedFields, "resourceList", "Resource List");
        numeric(advancedFields, "resourceVariable", "Resource Variable");
        numeric(advancedFields, "resourceExpression", "Resource Expression");
        numeric(advancedFields, "typeElement", "Type Element");
        numeric(advancedFields, "typeCastExpression", "Type Cast Expression");
        numeric(advancedFields, "variable", "Variable");
        numeric(advancedFields, "whileStatement", "While Statement");
        numeric(advancedFields, "javaFile", "Java File");
        numeric(advancedFields, "implicitVariable", "Implicit Variable");
        numeric(advancedFields, "docToken", "Doc Token");
        numeric(advancedFields, "typeParameter", "Type Parameter");
        numeric(advancedFields, "annotation", "Annotation");
        numeric(advancedFields, "annotationParameterList", "Annotation Parameter List");
        numeric(advancedFields, "annotationArrayInitializer", "Annotation Array Initializer");
        numeric(advancedFields, "nameValuePair", "Name Value Pair");
        numeric(advancedFields, "annotationMethod", "Annotation Method");
        numeric(advancedFields, "enumConstant", "Enum Constant");
        numeric(advancedFields, "enumConstantInitializer", "Enum Constant Initializer");
        numeric(advancedFields, "codeFragment", "Code Fragment");
        numeric(advancedFields, "polyadicExpression", "Polyadic Expression");
        numeric(advancedFields, "lambdaExpression", "Lambda Expression");
        numeric(advancedFields, "module", "Module");
        numeric(advancedFields, "moduleReferenceElement", "Module Reference Element");
        numeric(advancedFields, "moduleStatement", "Module Statement");
        numeric(advancedFields, "requiresStatement", "Requires Statement");
        numeric(advancedFields, "packageAccessibilityStatement", "Package Accessibility Statement");
        numeric(advancedFields, "usesStatement", "Uses Statement");
        numeric(advancedFields, "providesStatement", "Provides Statement");

    }

    private void checkBox(java.util.List<BeanField> fields, String fieldName, String title) {
        fields.add(new CheckBoxField(fieldName, title));
    }

    private void text(java.util.List<BeanField> fields, String fieldName, String title) {
        fields.add(new TextField(fieldName, title));
    }

    private void numeric(java.util.List<BeanField> fields, String fieldName, String title) {
        fields.add(new NumericField(fieldName, title));
    }

    private void colorPicker(java.util.List<BeanField> fields, String fieldName, String title) {
        fields.add(new ColorPickerField(fieldName, title));
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

        final JPanel panel = new JPanel(new VerticalLayout(8));

        for (BeanField field : basicFields) {
            panel.add(field.getComponent());
        }

        final JPanel advanced = new JPanel(new VerticalLayout(8));

        for (BeanField field : advancedFields) {
            advanced.add(field.getComponent());
        }

        tabbedPane.add("Basics", panel);
        tabbedPane.add("Advanced", advanced);

        return tabbedPane;
    }


    public boolean isModified() {
        return basicFields.stream().anyMatch(f -> f.isModified(configuration)) ||
                advancedFields.stream().anyMatch(f -> f.isModified(configuration));
    }

    public void apply() {
        basicFields.forEach(p -> p.apply(configuration));
        advancedFields.forEach(p -> p.apply(configuration));
    }

    public void reset() {
        basicFields.forEach(p -> p.reset(configuration));
        advancedFields.forEach(p -> p.reset(configuration));
    }

    @Override
    public void disposeUIResources() {

    }


    private static abstract class BeanField<T extends JComponent> {
        String myFieldName;
        T myComponent;

        private BeanField(final String fieldName) {
            myFieldName = fieldName;
        }

        T getComponent() {
            if (myComponent == null) {
                myComponent = createComponent();
            }
            return myComponent;
        }

        abstract T createComponent();

        boolean isModified(Object instance) {
            final Object componentValue = getComponentValue();
            final Object beanValue = getBeanValue(instance);
            return !Comparing.equal(componentValue, beanValue);
        }

        void apply(Object instance) {
            setBeanValue(instance, getComponentValue());
        }

        void reset(Object instance) {
            setComponentValue(getBeanValue(instance));
        }

        abstract Object getComponentValue();

        abstract void setComponentValue(Object instance);

        @NonNls
        protected String getterName() {
            return "get" + StringUtil.capitalize(myFieldName);
        }

        @NotNull
        private String setterName() {
            return "set" + StringUtil.capitalize(myFieldName);
        }

        Object getBeanValue(Object instance) {
            try {
                Field field = instance.getClass().getField(myFieldName);
                return field.get(instance);
            } catch (NoSuchFieldException e) {
                try {
                    final Method method = instance.getClass().getMethod(getterName());
                    return method.invoke(instance);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        void setBeanValue(Object instance, Object value) {
            try {
                Field field = instance.getClass().getField(myFieldName);
                field.set(instance, value);
            } catch (NoSuchFieldException e) {
                try {
                    final Method method = instance.getClass().getMethod(setterName(), getValueClass());
                    method.invoke(instance, value);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        protected abstract Class getValueClass();
    }

    private static class ColorPickerField extends BeanField<JPanel> {

        private String title;
        private ColorPanel colorPanel;

        private ColorPickerField(final String fieldName, final String title) {
            super(fieldName);
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

        Object getComponentValue() {
            return colorPanel.getSelectedColor().getRGB();
        }

        void setComponentValue(final Object instance) {
            Color color = new Color(((int) instance), true);
            colorPanel.setSelectedColor(color);
        }

        @Override
        protected String getterName() {
            return "get" + StringUtil.capitalize(myFieldName);
        }

        protected Class getValueClass() {
            return Integer.class;
        }
    }

    private class NumericField extends BeanField<JPanel> {

        private JBTextField jbTextField;
        private String title;

        public NumericField(String fieldName, String title) {
            super(fieldName);
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
        Object getComponentValue() {
            String text = jbTextField.getText();
            int result = 0;
            try {
                result = Integer.parseInt(text);
            } catch (Exception e) {

            }

            return result;
        }

        @Override
        void setComponentValue(Object instance) {
            jbTextField.setText(Objects.toString(instance, "0"));
        }

        @Override
        protected Class getValueClass() {
            return Integer.class;
        }
    }


    private class TextField extends BeanField<JPanel> {

        private JBTextField jbTextField;
        private String title;

        public TextField(String fieldName, String title) {
            super(fieldName);
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
        Object getComponentValue() {
            return jbTextField.getText();
        }

        @Override
        void setComponentValue(Object instance) {
            jbTextField.setText(instance == null ? null : String.valueOf(instance));
        }

        @Override
        protected Class getValueClass() {
            return String.class;
        }
    }

    private class CheckBoxField extends BeanField<JPanel> {

        private JBCheckBox jbCheckBox;
        private String title;

        public CheckBoxField(String fieldName, String title) {
            super(fieldName);
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
        Object getComponentValue() {
            return jbCheckBox.isSelected();
        }

        @Override
        void setComponentValue(Object instance) {
            jbCheckBox.setSelected(Boolean.TRUE.equals(instance));
        }

        @Override
        protected Class getValueClass() {
            return Boolean.class;
        }
    }
}
