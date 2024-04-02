package com.github.kisstkondoros.codemetrics.core.config;

import com.google.common.collect.ImmutableList;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

@State(
        name = "CodeMetricsConfiguration",
        storages = {@Storage("CodeMetricsConfiguration.xml")})
public class MetricsConfiguration implements PersistentStateComponent<MetricsConfiguration> {
    private final transient List<Runnable> listeners = new LinkedList<>();

    public void notifyListeners() {
        ImmutableList.copyOf(listeners).forEach(Runnable::run);
    }

    public Disposable addListener(Runnable listener) {
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    public static MetricsConfiguration getInstance() {
        return ApplicationManager.getApplication().getService(MetricsConfiguration.class);
    }

    @Nullable
    @Override
    public MetricsConfiguration getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull MetricsConfiguration state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public Integer complexityColorLow = 0xFF4bb14f;
    public Integer complexityColorNormal = 0xFFffc208;
    public Integer complexityColorHigh = 0xFFf44034;
    public Integer complexityColorExtreme = 0xFFff0000;

    public boolean metricsForAnonymousClass = true;
    public boolean metricsForAClass = true;
    public boolean metricsForMethod = true;
    public boolean metricsForLambdaExpression = false;

    public int complexityLevelExtreme = 25;
    public int complexityLevelHigh = 10;
    public int complexityLevelNormal = 5;
    public int complexityLevelLow = 0;

    public int hiddenUnder = 3;

    public String complexityLevelExtremeDescription = "Bloody hell...";
    public String complexityLevelHighDescription = "You must be kidding";
    public String complexityLevelNormalDescription = "It's time to do something...";
    public String complexityLevelLowDescription = "Everything is cool!";
    public String complexityTemplate = "Complexity is {0} {1}";

    public int anonymousClass = 1;
    public String anonymousClassDescription = "Anonymous Class";
    public int arrayAccessExpression = 0;
    public String arrayAccessExpressionDescription = "Array Access Expression";
    public int blockStatement = 0;
    public String blockStatementDescription = "Block Statement";
    public int breakStatement = 1;
    public String breakStatementDescription = "Break Statement";
    public int aClass = 1;
    public String aClassDescription = "Class";
    public int classInitializer = 0;
    public String classInitializerDescription = "Class Initializer";
    public int continueStatement = 1;
    public String continueStatementDescription = "Continue Statement";
    public int declarationStatement = 0;
    public String declarationStatementDescription = "Declaration Statement";
    public int docComment = 0;
    public String docCommentDescription = "Doc Comment";
    public int doWhileStatement = 1;
    public String doWhileStatementDescription = "Do While Statement";
    public int expressionList = 0;
    public String expressionListDescription = "Expression List";
    public int expressionListStatement = 0;
    public String expressionListStatementDescription = "Expression List Statement";
    public int expressionStatement = 0;
    public String expressionStatementDescription = "Expression Statement";
    public int field = 0;
    public String fieldDescription = "Field";
    public int forStatement = 1;
    public String forStatementDescription = "For Statement";
    public int foreachStatement = 1;
    public String foreachStatementDescription = "Foreach Statement";
    public int ifStatement = 1;
    public String ifStatementDescription = "If Statement";
    public int importStatement = 0;
    public String importStatementDescription = "Import Statement";
    public int labeledStatement = 1;
    public String labeledStatementDescription = "Labeled Statement";
    public int literalExpression = 0;
    public String literalExpressionDescription = "Literal Expression";
    public int localVariable = 0;
    public String localVariableDescription = "Local Variable";
    public int method = 1;
    public String methodDescription = "Method";
    public int methodCallExpression = 0;
    public String methodCallExpressionDescription = "Method Call Expression";
    public int parameter = 0;
    public String parameterDescription = "Parameter";
    public int postfixExpression = 0;
    public String postfixExpressionDescription = "Postfix Expression";
    public int prefixExpression = 0;
    public String prefixExpressionDescription = "Prefix Expression";
    public int returnStatement = 1;
    public String returnStatementDescription = "Return Statement";
    public int superExpression = 0;
    public String superExpressionDescription = "Super Expression";
    public int thisExpression = 0;
    public String thisExpressionDescription = "This Expression";
    public int throwStatement = 1;
    public String throwStatementDescription = "Throw Statement";
    public int tryStatement = 0;
    public String tryStatementDescription = "Try Statement";
    public int catchSection = 1;
    public String catchSectionDescription = "Catch Section";
    public int whileStatement = 1;
    public String whileStatementDescription = "While Statement";
    public int annotation = 0;
    public String annotationDescription = "Annotation";
    public int annotationMethod = 0;
    public String annotationMethodDescription = "Annotation Method";
    public int enumConstant = 0;
    public String enumConstantDescription = "Enum Constant";
    public int polyadicExpression = 1;
    public String polyadicExpressionDescription = "Polyadic Expression";
    public int lambdaExpression = 1;
    public String lambdaExpressionDescription = "Lambda Expression";
    public int emptyExpression = 0;
    public String emptyExpressionDescription = "Empty Expression";
    public int switchExpression = 1;
    public String switchExpressionDescription = "Switch Expression";
    public int referenceExpression = 0;
    public String referenceExpressionDescription = "Reference Expression";
    public int parenthesizedExpression = 0;
    public String parenthesizedExpressionDescription = "Parenthesized Expression";
}
