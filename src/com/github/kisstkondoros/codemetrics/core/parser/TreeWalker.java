package com.github.kisstkondoros.codemetrics.core.parser;

import com.github.kisstkondoros.codemetrics.core.CollectorType;
import com.github.kisstkondoros.codemetrics.core.MetricsModel;
import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.*;
import com.intellij.psi.tree.IElementType;

import java.util.Arrays;

import static com.github.kisstkondoros.codemetrics.core.CollectorType.MAX;
import static com.github.kisstkondoros.codemetrics.core.CollectorType.SUM;

public class TreeWalker {

    private MetricsConfiguration configuration;

    TreeWalker() {
        configuration = MetricsConfiguration.getInstance();
    }

    public MetricsModel walk(PsiElement node) {
        MetricsModel model = visit(node, 0, "Collector", true);
        visitNode(node, model);
        return model;
    }

    public void visitNode(PsiElement element, MetricsModel parent) {
        MetricsModel model = getMetrics(element);
        MetricsModel updatedParent = parent;
        if (model != null) {
            parent.children.add(model);
            if (model.visible) {
                updatedParent = model;
            }
        }
        walkChildren(element, updatedParent);
    }

    private void walkChildren(PsiElement element, MetricsModel parent) {
        Arrays.stream(element.getChildren()).forEach(node -> this.visitNode(node, parent));
    }

    private MetricsModel getMetrics(PsiElement element) {
        MetricsModel model = null;
        if (element instanceof PsiArrayAccessExpression) {
            model = visit(element, configuration.arrayAccessExpression, configuration.arrayAccessExpressionDescription);
        } else if (element instanceof PsiArrayInitializerExpression) {
            model = visit(element, configuration.arrayInitializerExpression, configuration.arrayInitializerExpressionDescription);
        } else if (element instanceof PsiAssertStatement) {
            model = visit(element, configuration.assertStatement, configuration.assertStatementDescription);
        } else if (element instanceof PsiAssignmentExpression) {
            model = visit(element, configuration.assignmentExpression, configuration.assignmentExpressionDescription);
        } else if (element instanceof PsiBinaryExpression) {
            model = visit(element, configuration.binaryExpression, configuration.binaryExpressionDescription);
        } else if (element instanceof PsiBlockStatement) {
            model = visit(element, configuration.blockStatement, configuration.blockStatementDescription);
        } else if (element instanceof PsiBreakStatement) {
            model = visit(element, configuration.breakStatement, configuration.breakStatementDescription);
        } else if (element instanceof PsiCodeBlock) {
            model = visit(element, configuration.codeBlock, configuration.codeBlockDescription);
        } else if (element instanceof PsiConditionalExpression) {
            model = visit(element, configuration.conditionalExpression, configuration.conditionalExpressionDescription);
        } else if (element instanceof PsiContinueStatement) {
            model = visit(element, configuration.continueStatement, configuration.continueStatementDescription);
        } else if (element instanceof PsiDeclarationStatement) {
            model = visit(element, configuration.declarationStatement, configuration.declarationStatementDescription);
        } else if (element instanceof PsiDocComment) {
            model = visit(element, configuration.docComment, configuration.docCommentDescription);
        } else if (element instanceof PsiDoWhileStatement) {
            model = visit(element, configuration.doWhileStatement, configuration.doWhileStatementDescription);
        } else if (element instanceof PsiEmptyStatement) {
            model = visit(element, configuration.emptyStatement, configuration.emptyStatementDescription);
        } else if (element instanceof PsiForStatement) {
            model = visit(element, configuration.forStatement, configuration.forStatementDescription);
        } else if (element instanceof PsiForeachStatement) {
            model = visit(element, configuration.foreachStatement, configuration.foreachStatementDescription);
        } else if (element instanceof PsiIdentifier) {
            model = visit(element, configuration.identifier, configuration.identifierDescription);
        } else if (element instanceof PsiIfStatement) {
            model = visit(element, configuration.ifStatement, configuration.ifStatementDescription);
        } else if (element instanceof PsiImportList) {
            model = visit(element, configuration.importList, configuration.importListDescription);
        } else if (element instanceof PsiImportStatement) {
            model = visit(element, configuration.importStatement, configuration.importStatementDescription);
        } else if (element instanceof PsiImportStaticStatement) {
            model = visit(element, configuration.importStaticStatement, configuration.importStaticStatementDescription);
        } else if (element instanceof PsiInlineDocTag) {
            model = visit(element, configuration.inlineDocTag, configuration.inlineDocTagDescription);
        } else if (element instanceof PsiInstanceOfExpression) {
            model = visit(element, configuration.instanceOfExpression, configuration.instanceOfExpressionDescription);
        } else if (element instanceof PsiKeyword) {
            model = visit(element, configuration.keyword, configuration.keywordDescription);
        } else if (element instanceof PsiLabeledStatement) {
            model = visit(element, configuration.labeledStatement, configuration.labeledStatementDescription);
        } else if (element instanceof PsiLiteralExpression) {
            model = visit(element, configuration.literalExpression, configuration.literalExpressionDescription);
        } else if (element instanceof PsiMethodCallExpression) {
            model = visit(element, configuration.methodCallExpression, configuration.methodCallExpressionDescription);
        } else if (element instanceof PsiModifierList) {
            model = visit(element, configuration.modifierList, configuration.modifierListDescription);
        } else if (element instanceof PsiNewExpression) {
            model = visit(element, configuration.newExpression, configuration.newExpressionDescription);
        } else if (element instanceof PsiPackage) {
            model = visit(element, configuration.aPackage, configuration.aPackageDescription);
        } else if (element instanceof PsiPackageStatement) {
            model = visit(element, configuration.packageStatement, configuration.packageStatementDescription);
        } else if (element instanceof PsiParameter) {
            model = visit(element, configuration.parameter, configuration.parameterDescription);
        } else if (element instanceof PsiReceiverParameter) {
            model = visit(element, configuration.receiverParameter, configuration.receiverParameterDescription);
        } else if (element instanceof PsiParameterList) {
            model = visit(element, configuration.parameterList, configuration.parameterListDescription);
        } else if (element instanceof PsiParenthesizedExpression) {
            model = visit(element, configuration.parenthesizedExpression, configuration.parenthesizedExpressionDescription);
        } else if (element instanceof PsiPostfixExpression) {
            model = visit(element, configuration.postfixExpression, configuration.postfixExpressionDescription);
        } else if (element instanceof PsiPrefixExpression) {
            model = visit(element, configuration.prefixExpression, configuration.prefixExpressionDescription);
        } else if (element instanceof PsiUnaryExpression) {
            model = visit(element, configuration.unaryExpression, configuration.unaryExpressionDescription);
        } else if (element instanceof PsiImportStaticReferenceElement) {
            model = visit(element, configuration.importStaticReferenceElement, configuration.importStaticReferenceElementDescription);
        } else if (element instanceof PsiMethodReferenceExpression) {
            model = visit(element, configuration.methodReferenceExpression, configuration.methodReferenceExpressionDescription);
        } else if (element instanceof PsiJavaCodeReferenceElement) {
            model = visit(element, configuration.referenceElement, configuration.referenceElementDescription);
        } else if (element instanceof PsiReferenceList) {
            model = visit(element, configuration.referenceList, configuration.referenceListDescription);
        } else if (element instanceof PsiReferenceParameterList) {
            model = visit(element, configuration.referenceParameterList, configuration.referenceParameterListDescription);
        } else if (element instanceof PsiTypeParameterList) {
            model = visit(element, configuration.typeParameterList, configuration.typeParameterListDescription);
        } else if (element instanceof PsiReturnStatement) {
            model = visit(element, configuration.returnStatement, configuration.returnStatementDescription);
        } else if (element instanceof PsiSuperExpression) {
            model = visit(element, configuration.superExpression, configuration.superExpressionDescription);
        } else if (element instanceof PsiSwitchLabelStatement) {
            model = visit(element, configuration.switchLabelStatement, configuration.switchLabelStatementDescription);
        } else if (element instanceof PsiSwitchStatement) {
            model = visit(element, configuration.switchStatement, configuration.switchStatementDescription);
        } else if (element instanceof PsiSynchronizedStatement) {
            model = visit(element, configuration.synchronizedStatement, configuration.synchronizedStatementDescription);
        } else if (element instanceof PsiThisExpression) {
            model = visit(element, configuration.thisExpression, configuration.thisExpressionDescription);
        } else if (element instanceof PsiThrowStatement) {
            model = visit(element, configuration.throwStatement, configuration.throwStatementDescription);
        } else if (element instanceof PsiTryStatement) {
            model = visit(element, configuration.tryStatement, configuration.tryStatementDescription);
        } else if (element instanceof PsiCatchSection) {
            model = visit(element, configuration.catchSection, configuration.catchSectionDescription);
        } else if (element instanceof PsiResourceList) {
            model = visit(element, configuration.resourceList, configuration.resourceListDescription);
        } else if (element instanceof PsiResourceVariable) {
            model = visit(element, configuration.resourceVariable, configuration.resourceVariableDescription);
        } else if (element instanceof PsiResourceExpression) {
            model = visit(element, configuration.resourceExpression, configuration.resourceExpressionDescription);
        } else if (element instanceof PsiTypeElement) {
            model = visit(element, configuration.typeElement, configuration.typeElementDescription);
        } else if (element instanceof PsiTypeCastExpression) {
            model = visit(element, configuration.typeCastExpression, configuration.typeCastExpressionDescription);
        } else if (element instanceof PsiWhileStatement) {
            model = visit(element, configuration.whileStatement, configuration.whileStatementDescription);
        } else if (element instanceof PsiJavaFile) {
            model = visit(element, configuration.javaFile, configuration.javaFileDescription);
        } else if (element instanceof ImplicitVariable) {
            model = visit(element, configuration.implicitVariable, configuration.implicitVariableDescription);
        } else if (element instanceof PsiDocToken) {
            model = visit(element, configuration.docToken, configuration.docTokenDescription);
        } else if (element instanceof PsiTypeParameter) {
            model = visit(element, configuration.typeParameter, configuration.typeParameterDescription);
        } else if (element instanceof PsiAnnotation) {
            model = visit(element, configuration.annotation, configuration.annotationDescription);
        } else if (element instanceof PsiAnnotationParameterList) {
            model = visit(element, configuration.annotationParameterList, configuration.annotationParameterListDescription);
        } else if (element instanceof PsiArrayInitializerMemberValue) {
            model = visit(element, configuration.annotationArrayInitializer, configuration.annotationArrayInitializerDescription);
        } else if (element instanceof PsiNameValuePair) {
            model = visit(element, configuration.nameValuePair, configuration.nameValuePairDescription);
        } else if (element instanceof PsiAnnotationMethod) {
            model = visit(element, configuration.annotationMethod, configuration.annotationMethodDescription);
        } else if (element instanceof PsiEnumConstant) {
            model = visit(element, configuration.enumConstant, configuration.enumConstantDescription);
        } else if (element instanceof PsiEnumConstantInitializer) {
            model = visit(element, configuration.enumConstantInitializer, configuration.enumConstantInitializerDescription);
        } else if (element instanceof JavaCodeFragment) {
            model = visit(element, configuration.codeFragment, configuration.codeFragmentDescription);
        } else if (element instanceof PsiPolyadicExpression) {
            PsiPolyadicExpression polyadicExpression = (PsiPolyadicExpression) element;
            final IElementType elementType = polyadicExpression.getOperationTokenType();
            if (elementType.equals(JavaTokenType.ANDAND) || elementType.equals(JavaTokenType.OROR)) {
                int complexity = polyadicExpression.getOperands().length - 1;
                model = visit(element, complexity * configuration.polyadicExpression, configuration.polyadicExpressionDescription);
            }
        } else if (element instanceof PsiLambdaExpression) {
            model = visit(element, configuration.lambdaExpression, configuration.lambdaExpressionDescription, configuration.metricsForLambdaExpression);
        } else if (element instanceof PsiJavaModule) {
            model = visit(element, configuration.module, configuration.moduleDescription);
        } else if (element instanceof PsiJavaModuleReferenceElement) {
            model = visit(element, configuration.moduleReferenceElement, configuration.moduleReferenceElementDescription);
        } else if (element instanceof PsiRequiresStatement) {
            model = visit(element, configuration.requiresStatement, configuration.requiresStatementDescription);
        } else if (element instanceof PsiPackageAccessibilityStatement) {
            model = visit(element, configuration.packageAccessibilityStatement, configuration.packageAccessibilityStatementDescription);
        } else if (element instanceof PsiUsesStatement) {
            model = visit(element, configuration.usesStatement, configuration.usesStatementDescription);
        } else if (element instanceof PsiProvidesStatement) {
            model = visit(element, configuration.providesStatement, configuration.providesStatementDescription);
        } else if (element instanceof PsiMethod) {
            model = visit(element, configuration.method, configuration.methodDescription, configuration.metricsForMethod);
        } else if (element instanceof PsiField) {
            model = visit(element, configuration.field, configuration.fieldDescription);
        } else if (element instanceof PsiAnonymousClass) {
            model = visit(element, configuration.anonymousClass, configuration.anonymousClassDescription, configuration.metricsForAnonymousClass);
        } else if (element instanceof PsiDocTag) {
            model = visit(element, configuration.docTag, configuration.docTagDescription);
        } else if (element instanceof PsiDocTagValue) {
            model = visit(element, configuration.docTagValue, configuration.docTagValueDescription);
        } else if (element instanceof PsiClass) {
            model = visit(element, configuration.aClass, configuration.aClassDescription, configuration.metricsForAClass);
        } else if (element instanceof PsiClassInitializer) {
            model = visit(element, configuration.classInitializer, configuration.classInitializerDescription);
        } else if (element instanceof PsiClassObjectAccessExpression) {
            model = visit(element, configuration.classObjectAccessExpression, configuration.classObjectAccessExpressionDescription);
        } else if (element instanceof PsiLocalVariable) {
            model = visit(element, configuration.localVariable, configuration.localVariableDescription);
        } else if (element instanceof PsiVariable) {
            model = visit(element, configuration.variable, configuration.variableDescription);
        } else if (element instanceof PsiJavaToken) {
            model = visit(element, configuration.javaToken, configuration.javaTokenDescription);
        } else if (element instanceof PsiCallExpression) {
            model = visit(element, configuration.callExpression, configuration.callExpressionDescription);
        } else if (element instanceof PsiExpression) {
            model = visit(element, configuration.expression, configuration.expressionDescription);
        } else if (element instanceof PsiExpressionList) {
            model = visit(element, configuration.expressionList, configuration.expressionListDescription);
        } else if (element instanceof PsiExpressionListStatement) {
            model = visit(element, configuration.expressionListStatement, configuration.expressionListStatementDescription);
        } else if (element instanceof PsiExpressionStatement) {
            model = visit(element, configuration.expressionStatement, configuration.expressionStatementDescription);
        } else if (element instanceof PsiStatement) {
            model = visit(element, configuration.moduleStatement, configuration.moduleStatementDescription);
        }
        return model;
    }


    private MetricsModel visit(PsiElement node, int complexity, String description) {
        return visit(node, complexity, description, false);
    }

    private MetricsModel visit(PsiElement node, int complexity, String description, boolean visible) {
        CollectorType collectorType = node instanceof PsiClass ? MAX : SUM;
        return new MetricsModel(node, complexity, description, true, visible, collectorType);
    }

}