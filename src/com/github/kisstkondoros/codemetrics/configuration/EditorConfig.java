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
  private final MetricsConfiguration baseConfiguration = new MetricsConfiguration();
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
        baseConfiguration.anonymousClassDescription);
    numeric(
        advancedFields,
        () -> configuration.arrayAccessExpression,
        v -> configuration.arrayAccessExpression = v,
        baseConfiguration.arrayAccessExpressionDescription);
    numeric(
        advancedFields,
        () -> configuration.arrayInitializerExpression,
        v -> configuration.arrayInitializerExpression = v,
        baseConfiguration.arrayInitializerExpressionDescription);
    numeric(
        advancedFields,
        () -> configuration.assertStatement,
        v -> configuration.assertStatement = v,
        baseConfiguration.assertStatementDescription);
    numeric(
        advancedFields,
        () -> configuration.assignmentExpression,
        v -> configuration.assignmentExpression = v,
        baseConfiguration.assignmentExpressionDescription);
    numeric(
        advancedFields,
        () -> configuration.binaryExpression,
        v -> configuration.binaryExpression = v,
        baseConfiguration.binaryExpressionDescription);
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
        () -> configuration.classObjectAccessExpression,
        v -> configuration.classObjectAccessExpression = v,
        baseConfiguration.classObjectAccessExpressionDescription);
    numeric(
        advancedFields,
        () -> configuration.codeBlock,
        v -> configuration.codeBlock = v,
        baseConfiguration.codeBlockDescription);
    numeric(
        advancedFields,
        () -> configuration.conditionalExpression,
        v -> configuration.conditionalExpression = v,
        baseConfiguration.conditionalExpressionDescription);
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
        () -> configuration.docTag,
        v -> configuration.docTag = v,
        baseConfiguration.docTagDescription);
    numeric(
        advancedFields,
        () -> configuration.doWhileStatement,
        v -> configuration.doWhileStatement = v,
        baseConfiguration.doWhileStatementDescription);
    numeric(
        advancedFields,
        () -> configuration.emptyStatement,
        v -> configuration.emptyStatement = v,
        baseConfiguration.emptyStatementDescription);
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
        () -> configuration.importList,
        v -> configuration.importList = v,
        baseConfiguration.importListDescription);
    numeric(
        advancedFields,
        () -> configuration.importStatement,
        v -> configuration.importStatement = v,
        baseConfiguration.importStatementDescription);
    numeric(
        advancedFields,
        () -> configuration.importStaticStatement,
        v -> configuration.importStaticStatement = v,
        baseConfiguration.importStaticStatementDescription);
    numeric(
        advancedFields,
        () -> configuration.inlineDocTag,
        v -> configuration.inlineDocTag = v,
        baseConfiguration.inlineDocTagDescription);
    numeric(
        advancedFields,
        () -> configuration.instanceOfExpression,
        v -> configuration.instanceOfExpression = v,
        baseConfiguration.instanceOfExpressionDescription);
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
        () -> configuration.modifierList,
        v -> configuration.modifierList = v,
        baseConfiguration.modifierListDescription);
    numeric(
        advancedFields,
        () -> configuration.newExpression,
        v -> configuration.newExpression = v,
        baseConfiguration.newExpressionDescription);
    numeric(
        advancedFields,
        () -> configuration.packageStatement,
        v -> configuration.packageStatement = v,
        baseConfiguration.packageStatementDescription);
    numeric(
        advancedFields,
        () -> configuration.parameter,
        v -> configuration.parameter = v,
        baseConfiguration.parameterDescription);
    numeric(
        advancedFields,
        () -> configuration.receiverParameter,
        v -> configuration.receiverParameter = v,
        baseConfiguration.receiverParameterDescription);
    numeric(
        advancedFields,
        () -> configuration.parameterList,
        v -> configuration.parameterList = v,
        baseConfiguration.parameterListDescription);
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
        () -> configuration.referenceParameterList,
        v -> configuration.referenceParameterList = v,
        baseConfiguration.referenceParameterListDescription);
    numeric(
        advancedFields,
        () -> configuration.typeParameterList,
        v -> configuration.typeParameterList = v,
        baseConfiguration.typeParameterListDescription);
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
        () -> configuration.switchLabelStatement,
        v -> configuration.switchLabelStatement = v,
        baseConfiguration.switchLabelStatementDescription);
    numeric(
        advancedFields,
        () -> configuration.switchStatement,
        v -> configuration.switchStatement = v,
        baseConfiguration.switchStatementDescription);
    numeric(
        advancedFields,
        () -> configuration.synchronizedStatement,
        v -> configuration.synchronizedStatement = v,
        baseConfiguration.synchronizedStatementDescription);
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
        () -> configuration.resourceList,
        v -> configuration.resourceList = v,
        baseConfiguration.resourceListDescription);
    numeric(
        advancedFields,
        () -> configuration.resourceVariable,
        v -> configuration.resourceVariable = v,
        baseConfiguration.resourceVariableDescription);
    numeric(
        advancedFields,
        () -> configuration.resourceExpression,
        v -> configuration.resourceExpression = v,
        baseConfiguration.resourceExpressionDescription);
    numeric(
        advancedFields,
        () -> configuration.typeCastExpression,
        v -> configuration.typeCastExpression = v,
        baseConfiguration.typeCastExpressionDescription);
    numeric(
        advancedFields,
        () -> configuration.whileStatement,
        v -> configuration.whileStatement = v,
        baseConfiguration.whileStatementDescription);
    numeric(
        advancedFields,
        () -> configuration.typeParameter,
        v -> configuration.typeParameter = v,
        baseConfiguration.typeParameterDescription);
    numeric(
        advancedFields,
        () -> configuration.annotation,
        v -> configuration.annotation = v,
        baseConfiguration.annotationDescription);
    numeric(
        advancedFields,
        () -> configuration.annotationParameterList,
        v -> configuration.annotationParameterList = v,
        baseConfiguration.annotationParameterListDescription);
    numeric(
        advancedFields,
        () -> configuration.annotationArrayInitializer,
        v -> configuration.annotationArrayInitializer = v,
        baseConfiguration.annotationArrayInitializerDescription);
    numeric(
        advancedFields,
        () -> configuration.nameValuePair,
        v -> configuration.nameValuePair = v,
        baseConfiguration.nameValuePairDescription);
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
        () -> configuration.enumConstantInitializer,
        v -> configuration.enumConstantInitializer = v,
        baseConfiguration.enumConstantInitializerDescription);
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
        () -> configuration.module,
        v -> configuration.module = v,
        baseConfiguration.moduleDescription);
    numeric(
        advancedFields,
        () -> configuration.requiresStatement,
        v -> configuration.requiresStatement = v,
        baseConfiguration.requiresStatementDescription);

    numeric(
        advancedFields,
        () -> configuration.usesStatement,
        v -> configuration.usesStatement = v,
        baseConfiguration.usesStatementDescription);
    numeric(
        advancedFields,
        () -> configuration.providesStatement,
        v -> configuration.providesStatement = v,
        baseConfiguration.providesStatementDescription);
    numeric(
        advancedFields,
        () -> configuration.methodRefExpression,
        v -> configuration.methodRefExpression = v,
        baseConfiguration.methodRefExpressionDescription);
    numeric(
        advancedFields,
        () -> configuration.type,
        v -> configuration.type = v,
        baseConfiguration.typeDescription);
    numeric(
        advancedFields,
        () -> configuration.diamondType,
        v -> configuration.diamondType = v,
        baseConfiguration.diamondTypeDescription);
    numeric(
        advancedFields,
        () -> configuration.importStaticReference,
        v -> configuration.importStaticReference = v,
        baseConfiguration.importStaticReferenceDescription);
    numeric(
        advancedFields,
        () -> configuration.providesWithList,
        v -> configuration.providesWithList = v,
        baseConfiguration.providesWithListDescription);
    numeric(
        advancedFields,
        () -> configuration.opensStatement,
        v -> configuration.opensStatement = v,
        baseConfiguration.opensStatementDescription);
    numeric(
        advancedFields,
        () -> configuration.exportsStatement,
        v -> configuration.exportsStatement = v,
        baseConfiguration.exportsStatementDescription);
    numeric(
        advancedFields,
        () -> configuration.throwsList,
        v -> configuration.throwsList = v,
        baseConfiguration.throwsListDescription);
    numeric(
        advancedFields,
        () -> configuration.extendsBoundList,
        v -> configuration.extendsBoundList = v,
        baseConfiguration.extendsBoundListDescription);
    numeric(
        advancedFields,
        () -> configuration.implementsList,
        v -> configuration.implementsList = v,
        baseConfiguration.implementsListDescription);
    numeric(
        advancedFields,
        () -> configuration.extendsList,
        v -> configuration.extendsList = v,
        baseConfiguration.extendsListDescription);
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
        () -> configuration.switchLabeledRule,
        v -> configuration.switchLabeledRule = v,
        baseConfiguration.switchLabeledRuleDescription);
    numeric(
        advancedFields,
        () -> configuration.moduleReference,
        v -> configuration.moduleReference = v,
        baseConfiguration.moduleReferenceDescription);
    numeric(
        advancedFields,
        () -> configuration.javaCodeReference,
        v -> configuration.javaCodeReference = v,
        baseConfiguration.javaCodeReferenceDescription);
    numeric(
        advancedFields,
        () -> configuration.referenceExpression,
        v -> configuration.referenceExpression = v,
        baseConfiguration.referenceExpressionDescription);
    numeric(
        advancedFields,
        () -> configuration.parenthExpression,
        v -> configuration.parenthExpression = v,
        baseConfiguration.parenthExpressionDescription);
    numeric(
        advancedFields,
        () -> configuration.docMethodOrFieldRef,
        v -> configuration.docMethodOrFieldRef = v,
        baseConfiguration.docMethodOrFieldRefDescription);
    numeric(
        advancedFields,
        () -> configuration.docParameterRef,
        v -> configuration.docParameterRef = v,
        baseConfiguration.docParameterRefDescription);
    numeric(
        advancedFields,
        () -> configuration.docTagValueElement,
        v -> configuration.docTagValueElement = v,
        baseConfiguration.docTagValueElementDescription);
    numeric(
        advancedFields,
        () -> configuration.docReferenceHolder,
        v -> configuration.docReferenceHolder = v,
        baseConfiguration.docReferenceHolderDescription);
    numeric(
        advancedFields,
        () -> configuration.docTypeHolder,
        v -> configuration.docTypeHolder = v,
        baseConfiguration.docTypeHolderDescription);
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
