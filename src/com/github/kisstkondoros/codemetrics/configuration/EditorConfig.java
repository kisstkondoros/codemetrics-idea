package com.github.kisstkondoros.codemetrics.configuration;

import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.intellij.ide.plugins.newui.HorizontalLayout;
import com.intellij.ide.plugins.newui.VerticalLayout;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.Comparing;
import com.intellij.ui.ColorPanel;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EditorConfig implements Configurable {

  private final MetricsConfiguration configuration;
  private java.util.List<BeanField> basicFields;
  private java.util.List<BeanField> advancedFields;

  protected EditorConfig() {
    configuration = MetricsConfiguration.getInstance();
    basicFields = new ArrayList<>();
    advancedFields = new ArrayList<>();

    /* basic fields */
    this.colorPicker(
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

    numeric(
        advancedFields,
        () -> configuration.anonymousClass,
        v -> configuration.anonymousClass = v,
        "Anonymous Class");
    numeric(
        advancedFields,
        () -> configuration.arrayAccessExpression,
        v -> configuration.arrayAccessExpression = v,
        "Array Access Expression");
    numeric(
        advancedFields,
        () -> configuration.arrayInitializerExpression,
        v -> configuration.arrayInitializerExpression = v,
        "Array Initializer Expression");
    numeric(
        advancedFields,
        () -> configuration.assertStatement,
        v -> configuration.assertStatement = v,
        "Assert Statement");
    numeric(
        advancedFields,
        () -> configuration.assignmentExpression,
        v -> configuration.assignmentExpression = v,
        "Assignment Expression");
    numeric(
        advancedFields,
        () -> configuration.binaryExpression,
        v -> configuration.binaryExpression = v,
        "Binary Expression");
    numeric(
        advancedFields,
        () -> configuration.blockStatement,
        v -> configuration.blockStatement = v,
        "Block Statement");
    numeric(
        advancedFields,
        () -> configuration.breakStatement,
        v -> configuration.breakStatement = v,
        "Break Statement");
    numeric(advancedFields, () -> configuration.aClass, v -> configuration.aClass = v, "Class");
    numeric(
        advancedFields,
        () -> configuration.classInitializer,
        v -> configuration.classInitializer = v,
        "Class Initializer");
    numeric(
        advancedFields,
        () -> configuration.classObjectAccessExpression,
        v -> configuration.classObjectAccessExpression = v,
        "Class Object Access Expression");
    numeric(
        advancedFields,
        () -> configuration.codeBlock,
        v -> configuration.codeBlock = v,
        "Code Block");
    numeric(
        advancedFields,
        () -> configuration.conditionalExpression,
        v -> configuration.conditionalExpression = v,
        "Conditional Expression");
    numeric(
        advancedFields,
        () -> configuration.continueStatement,
        v -> configuration.continueStatement = v,
        "Continue Statement");
    numeric(
        advancedFields,
        () -> configuration.declarationStatement,
        v -> configuration.declarationStatement = v,
        "Declaration Statement");
    numeric(
        advancedFields,
        () -> configuration.docComment,
        v -> configuration.docComment = v,
        "Doc Comment");
    numeric(advancedFields, () -> configuration.docTag, v -> configuration.docTag = v, "Doc Tag");
    numeric(
        advancedFields,
        () -> configuration.doWhileStatement,
        v -> configuration.doWhileStatement = v,
        "Do While Statement");
    numeric(
        advancedFields,
        () -> configuration.emptyStatement,
        v -> configuration.emptyStatement = v,
        "Empty Statement");
    numeric(
        advancedFields,
        () -> configuration.expressionList,
        v -> configuration.expressionList = v,
        "Expression List");
    numeric(
        advancedFields,
        () -> configuration.expressionListStatement,
        v -> configuration.expressionListStatement = v,
        "Expression List Statement");
    numeric(
        advancedFields,
        () -> configuration.expressionStatement,
        v -> configuration.expressionStatement = v,
        "Expression Statement");
    numeric(advancedFields, () -> configuration.field, v -> configuration.field = v, "Field");
    numeric(
        advancedFields,
        () -> configuration.forStatement,
        v -> configuration.forStatement = v,
        "For Statement");
    numeric(
        advancedFields,
        () -> configuration.foreachStatement,
        v -> configuration.foreachStatement = v,
        "Foreach Statement");
    numeric(
        advancedFields,
        () -> configuration.ifStatement,
        v -> configuration.ifStatement = v,
        "If Statement");
    numeric(
        advancedFields,
        () -> configuration.importList,
        v -> configuration.importList = v,
        "Import List");
    numeric(
        advancedFields,
        () -> configuration.importStatement,
        v -> configuration.importStatement = v,
        "Import Statement");
    numeric(
        advancedFields,
        () -> configuration.importStaticStatement,
        v -> configuration.importStaticStatement = v,
        "Import Static Statement");
    numeric(
        advancedFields,
        () -> configuration.inlineDocTag,
        v -> configuration.inlineDocTag = v,
        "Inline Doc Tag");
    numeric(
        advancedFields,
        () -> configuration.instanceOfExpression,
        v -> configuration.instanceOfExpression = v,
        "Instance Of Expression");
    numeric(
        advancedFields,
        () -> configuration.labeledStatement,
        v -> configuration.labeledStatement = v,
        "Labeled Statement");
    numeric(
        advancedFields,
        () -> configuration.literalExpression,
        v -> configuration.literalExpression = v,
        "Literal Expression");
    numeric(
        advancedFields,
        () -> configuration.localVariable,
        v -> configuration.localVariable = v,
        "Local Variable");
    numeric(advancedFields, () -> configuration.method, v -> configuration.method = v, "Method");
    numeric(
        advancedFields,
        () -> configuration.methodCallExpression,
        v -> configuration.methodCallExpression = v,
        "Method Call Expression");
    numeric(
        advancedFields,
        () -> configuration.modifierList,
        v -> configuration.modifierList = v,
        "Modifier List");
    numeric(
        advancedFields,
        () -> configuration.newExpression,
        v -> configuration.newExpression = v,
        "New Expression");
    numeric(
        advancedFields,
        () -> configuration.packageStatement,
        v -> configuration.packageStatement = v,
        "Package Statement");
    numeric(
        advancedFields,
        () -> configuration.parameter,
        v -> configuration.parameter = v,
        "Parameter");
    numeric(
        advancedFields,
        () -> configuration.receiverParameter,
        v -> configuration.receiverParameter = v,
        "Receiver Parameter");
    numeric(
        advancedFields,
        () -> configuration.parameterList,
        v -> configuration.parameterList = v,
        "Parameter List");
    numeric(
        advancedFields,
        () -> configuration.postfixExpression,
        v -> configuration.postfixExpression = v,
        "Postfix Expression");
    numeric(
        advancedFields,
        () -> configuration.prefixExpression,
        v -> configuration.prefixExpression = v,
        "Prefix Expression");
    numeric(
        advancedFields,
        () -> configuration.referenceParameterList,
        v -> configuration.referenceParameterList = v,
        "Reference Parameter List");
    numeric(
        advancedFields,
        () -> configuration.typeParameterList,
        v -> configuration.typeParameterList = v,
        "Type Parameter List");
    numeric(
        advancedFields,
        () -> configuration.returnStatement,
        v -> configuration.returnStatement = v,
        "Return Statement");
    numeric(
        advancedFields,
        () -> configuration.superExpression,
        v -> configuration.superExpression = v,
        "Super Expression");
    numeric(
        advancedFields,
        () -> configuration.switchLabelStatement,
        v -> configuration.switchLabelStatement = v,
        "Switch Label Statement");
    numeric(
        advancedFields,
        () -> configuration.switchStatement,
        v -> configuration.switchStatement = v,
        "Switch Statement");
    numeric(
        advancedFields,
        () -> configuration.synchronizedStatement,
        v -> configuration.synchronizedStatement = v,
        "Synchronized Statement");
    numeric(
        advancedFields,
        () -> configuration.thisExpression,
        v -> configuration.thisExpression = v,
        "This Expression");
    numeric(
        advancedFields,
        () -> configuration.throwStatement,
        v -> configuration.throwStatement = v,
        "Throw Statement");
    numeric(
        advancedFields,
        () -> configuration.tryStatement,
        v -> configuration.tryStatement = v,
        "Try Statement");
    numeric(
        advancedFields,
        () -> configuration.catchSection,
        v -> configuration.catchSection = v,
        "Catch Section");
    numeric(
        advancedFields,
        () -> configuration.resourceList,
        v -> configuration.resourceList = v,
        "Resource List");
    numeric(
        advancedFields,
        () -> configuration.resourceVariable,
        v -> configuration.resourceVariable = v,
        "Resource Variable");
    numeric(
        advancedFields,
        () -> configuration.resourceExpression,
        v -> configuration.resourceExpression = v,
        "Resource Expression");
    numeric(
        advancedFields,
        () -> configuration.typeCastExpression,
        v -> configuration.typeCastExpression = v,
        "Type Cast Expression");
    numeric(
        advancedFields,
        () -> configuration.whileStatement,
        v -> configuration.whileStatement = v,
        "While Statement");
    numeric(
        advancedFields,
        () -> configuration.typeParameter,
        v -> configuration.typeParameter = v,
        "Type Parameter");
    numeric(
        advancedFields,
        () -> configuration.annotation,
        v -> configuration.annotation = v,
        "Annotation");
    numeric(
        advancedFields,
        () -> configuration.annotationParameterList,
        v -> configuration.annotationParameterList = v,
        "Annotation Parameter List");
    numeric(
        advancedFields,
        () -> configuration.annotationArrayInitializer,
        v -> configuration.annotationArrayInitializer = v,
        "Annotation Array Initializer");
    numeric(
        advancedFields,
        () -> configuration.nameValuePair,
        v -> configuration.nameValuePair = v,
        "Name Value Pair");
    numeric(
        advancedFields,
        () -> configuration.annotationMethod,
        v -> configuration.annotationMethod = v,
        "Annotation Method");
    numeric(
        advancedFields,
        () -> configuration.enumConstant,
        v -> configuration.enumConstant = v,
        "Enum Constant");
    numeric(
        advancedFields,
        () -> configuration.enumConstantInitializer,
        v -> configuration.enumConstantInitializer = v,
        "Enum Constant Initializer");
    numeric(
        advancedFields,
        () -> configuration.polyadicExpression,
        v -> configuration.polyadicExpression = v,
        "Polyadic Expression");
    numeric(
        advancedFields,
        () -> configuration.lambdaExpression,
        v -> configuration.lambdaExpression = v,
        "Lambda Expression");
    numeric(advancedFields, () -> configuration.module, v -> configuration.module = v, "Module");
    numeric(
        advancedFields,
        () -> configuration.requiresStatement,
        v -> configuration.requiresStatement = v,
        "Requires Statement");

    numeric(
        advancedFields,
        () -> configuration.usesStatement,
        v -> configuration.usesStatement = v,
        "Uses Statement");
    numeric(
        advancedFields,
        () -> configuration.providesStatement,
        v -> configuration.providesStatement = v,
        "Provides Statement");
    numeric(
        advancedFields,
        () -> configuration.methodRefExpression,
        v -> configuration.methodRefExpression = v,
        "Method Reference Expression");
    numeric(advancedFields, () -> configuration.type, v -> configuration.type = v, "Type");
    numeric(
        advancedFields,
        () -> configuration.diamondType,
        v -> configuration.diamondType = v,
        "Diamond Type");
    numeric(
        advancedFields,
        () -> configuration.importStaticReference,
        v -> configuration.importStaticReference = v,
        "Import Static Reference");
    numeric(
        advancedFields,
        () -> configuration.providesWithList,
        v -> configuration.providesWithList = v,
        "Provides With List");
    numeric(
        advancedFields,
        () -> configuration.opensStatement,
        v -> configuration.opensStatement = v,
        "Opens Statement");
    numeric(
        advancedFields,
        () -> configuration.exportsStatement,
        v -> configuration.exportsStatement = v,
        "Exports Statement");
    numeric(
        advancedFields,
        () -> configuration.throwsList,
        v -> configuration.throwsList = v,
        "Throws List");
    numeric(
        advancedFields,
        () -> configuration.extendsBoundList,
        v -> configuration.extendsBoundList = v,
        "Extends Bound List");
    numeric(
        advancedFields,
        () -> configuration.implementsList,
        v -> configuration.implementsList = v,
        "Implements List");
    numeric(
        advancedFields,
        () -> configuration.extendsList,
        v -> configuration.extendsList = v,
        "Extends List");
    numeric(
        advancedFields,
        () -> configuration.emptyExpression,
        v -> configuration.emptyExpression = v,
        "Empty Expression");
    numeric(
        advancedFields,
        () -> configuration.switchExpression,
        v -> configuration.switchExpression = v,
        "Switch Expression");
    numeric(
        advancedFields,
        () -> configuration.switchLabeledRule,
        v -> configuration.switchLabeledRule = v,
        "Switch Labeled Rule");
    numeric(
        advancedFields,
        () -> configuration.moduleReference,
        v -> configuration.moduleReference = v,
        "Module Reference");
    numeric(
        advancedFields,
        () -> configuration.javaCodeReference,
        v -> configuration.javaCodeReference = v,
        "JavaCode Reference");
    numeric(
        advancedFields,
        () -> configuration.referenceExpression,
        v -> configuration.referenceExpression = v,
        "Reference Expression");
    numeric(
        advancedFields,
        () -> configuration.parenthExpression,
        v -> configuration.parenthExpression = v,
        "Parenthesized Expression");
    numeric(
        advancedFields,
        () -> configuration.docMethodOrFieldRef,
        v -> configuration.docMethodOrFieldRef = v,
        "Documentation Method Or Field Ref");
    numeric(
        advancedFields,
        () -> configuration.docParameterRef,
        v -> configuration.docParameterRef = v,
        "Documentation Parameter Ref");
    numeric(
        advancedFields,
        () -> configuration.docTagValueElement,
        v -> configuration.docTagValueElement = v,
        "Documentation Tag Value Element");
    numeric(
        advancedFields,
        () -> configuration.docReferenceHolder,
        v -> configuration.docReferenceHolder = v,
        "Documentation Reference Holder");
    numeric(
        advancedFields,
        () -> configuration.docTypeHolder,
        v -> configuration.docTypeHolder = v,
        "Documentation Type Holder");
  }

  private void checkBox(
      java.util.List<BeanField> fields,
      Supplier<Boolean> getter,
      Consumer<Boolean> setter,
      String title) {
    fields.add(new CheckBoxField(getter, setter, title));
  }

  private void text(
      java.util.List<BeanField> fields,
      Supplier<String> getter,
      Consumer<String> setter,
      String title) {
    fields.add(new TextField(getter, setter, title));
  }

  private void numeric(
      java.util.List<BeanField> fields,
      Supplier<Integer> getter,
      Consumer<Integer> setter,
      String title) {
    fields.add(new NumericField(getter, setter, title));
  }

  private void colorPicker(
      java.util.List<BeanField> fields,
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
    return basicFields.stream().anyMatch(BeanField::isModified)
        || advancedFields.stream().anyMatch(BeanField::isModified);
  }

  public void apply() {
    basicFields.forEach(BeanField::apply);
    advancedFields.forEach(BeanField::apply);
    configuration.notifyListeners();
  }

  public void reset() {
    basicFields.forEach(BeanField::reset);
    advancedFields.forEach(BeanField::reset);
  }

  @Override
  public void disposeUIResources() {}

  private abstract static class BeanField<PROPTYPE, T extends JComponent> {
    private Supplier<PROPTYPE> getter;
    private Consumer<PROPTYPE> setter;

    T myComponent;

    private BeanField(Supplier<PROPTYPE> getter, Consumer<PROPTYPE> setter) {
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

    abstract PROPTYPE getComponentValue();

    abstract void setComponentValue(PROPTYPE instance);

    PROPTYPE getBeanValue() {
      return getter.get();
    }

    void setBeanValue(PROPTYPE value) {
      setter.accept(value);
    }
  }

  private static class ColorPickerField extends BeanField<Integer, JPanel> {

    private String title;
    private ColorPanel colorPanel;

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
      return colorPanel.getSelectedColor().getRGB();
    }

    void setComponentValue(final Integer instance) {
      Color color = new Color(instance, true);
      colorPanel.setSelectedColor(color);
    }
  }

  private class NumericField extends BeanField<Integer, JPanel> {

    private JBTextField jbTextField;
    private String title;

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

      }

      return result;
    }

    @Override
    void setComponentValue(Integer instance) {
      jbTextField.setText(Objects.toString(instance, "0"));
    }
  }

  private class TextField extends BeanField<String, JPanel> {

    private JBTextField jbTextField;
    private String title;

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

    private JBCheckBox jbCheckBox;
    private String title;

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
