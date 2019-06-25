package com.github.kisstkondoros.codemetrics.core.parser;

import com.google.common.collect.Maps;
import com.intellij.psi.JavaTokenType;
import com.intellij.psi.PsiPolyadicExpression;
import com.intellij.psi.tree.IElementType;

import java.util.HashMap;

import static com.intellij.psi.impl.source.tree.JavaDocElementType.*;
import static com.intellij.psi.impl.source.tree.JavaElementType.*;

public class HandlerRegistry {
  private static final HashMap<IElementType, ComplexityHandler> handlers;

  static {
    SimpleHandlerFactory genericHandler = new SimpleHandlerFactory();

    handlers = Maps.newHashMap();

    ComplexityHandler polyadicExpressionHandler =
        genericHandler.create(c -> c.polyadicExpression, c -> c.polyadicExpressionDescription);
    register(
        POLYADIC_EXPRESSION,
        configuration ->
            e -> {
              ComplexityDescription original =
                  polyadicExpressionHandler.apply(configuration).apply(e);

              PsiPolyadicExpression polyadicExpression = (PsiPolyadicExpression) e;
              final IElementType operationTokenType = polyadicExpression.getOperationTokenType();
              int complexity = 0;
              if (operationTokenType.equals(JavaTokenType.ANDAND)
                  || operationTokenType.equals(JavaTokenType.OROR)) {
                complexity = polyadicExpression.getOperands().length - 1;
              }
              return new ComplexityDescription(
                  complexity * original.getIncrement(),
                  original.getDescription(),
                  original.isVisible());
            });
    register(
        CLASS,
        genericHandler.create(c -> c.aClass, c -> c.aClassDescription, c -> c.metricsForAClass));
    register(
        ANONYMOUS_CLASS,
        genericHandler.create(
            c -> c.anonymousClass,
            c -> c.anonymousClassDescription,
            c -> c.metricsForAnonymousClass));
    register(
        ENUM_CONSTANT_INITIALIZER,
        genericHandler.create(
            c -> c.enumConstantInitializer, c -> c.enumConstantInitializerDescription));
    register(
        TYPE_PARAMETER_LIST,
        genericHandler.create(c -> c.typeParameterList, c -> c.typeParameterListDescription));
    register(
        TYPE_PARAMETER,
        genericHandler.create(c -> c.typeParameter, c -> c.typeParameterDescription));
    register(IMPORT_LIST, genericHandler.create(c -> c.importList, c -> c.importListDescription));
    register(
        IMPORT_STATEMENT,
        genericHandler.create(c -> c.importStatement, c -> c.importStatementDescription));
    register(
        IMPORT_STATIC_STATEMENT,
        genericHandler.create(
            c -> c.importStaticStatement, c -> c.importStaticStatementDescription));
    register(
        MODIFIER_LIST, genericHandler.create(c -> c.modifierList, c -> c.modifierListDescription));
    register(ANNOTATION, genericHandler.create(c -> c.annotation, c -> c.annotationDescription));
    register(
        NAME_VALUE_PAIR,
        genericHandler.create(c -> c.nameValuePair, c -> c.nameValuePairDescription));
    register(
        LITERAL_EXPRESSION,
        genericHandler.create(c -> c.literalExpression, c -> c.literalExpressionDescription));
    register(
        ANNOTATION_PARAMETER_LIST,
        genericHandler.create(
            c -> c.annotationParameterList, c -> c.annotationParameterListDescription));
    register(
        EXTENDS_LIST, genericHandler.create(c -> c.extendsList, c -> c.extendsListDescription));
    register(
        IMPLEMENTS_LIST,
        genericHandler.create(c -> c.implementsList, c -> c.implementsListDescription));
    register(FIELD, genericHandler.create(c -> c.field, c -> c.fieldDescription));
    register(
        ENUM_CONSTANT, genericHandler.create(c -> c.enumConstant, c -> c.enumConstantDescription));
    register(
        METHOD,
        genericHandler.create(c -> c.method, c -> c.methodDescription, c -> c.metricsForMethod));
    register(
        ANNOTATION_METHOD,
        genericHandler.create(c -> c.annotationMethod, c -> c.annotationMethodDescription));
    register(
        CLASS_INITIALIZER,
        genericHandler.create(c -> c.classInitializer, c -> c.classInitializerDescription));
    register(PARAMETER, genericHandler.create(c -> c.parameter, c -> c.parameterDescription));
    register(
        PARAMETER_LIST,
        genericHandler.create(c -> c.parameterList, c -> c.parameterListDescription));
    register(
        EXTENDS_BOUND_LIST,
        genericHandler.create(c -> c.extendsBoundList, c -> c.extendsBoundListDescription));
    register(THROWS_LIST, genericHandler.create(c -> c.throwsList, c -> c.throwsListDescription));
    register(
        LAMBDA_EXPRESSION,
        genericHandler.create(
            c -> c.lambdaExpression,
            c -> c.lambdaExpressionDescription,
            c -> c.metricsForLambdaExpression));
    register(
        METHOD_REF_EXPRESSION,
        genericHandler.create(c -> c.methodRefExpression, c -> c.methodRefExpressionDescription));
    register(MODULE, genericHandler.create(c -> c.module, c -> c.moduleDescription));
    register(
        REQUIRES_STATEMENT,
        genericHandler.create(c -> c.requiresStatement, c -> c.requiresStatementDescription));
    register(
        EXPORTS_STATEMENT,
        genericHandler.create(c -> c.exportsStatement, c -> c.exportsStatementDescription));
    register(
        OPENS_STATEMENT,
        genericHandler.create(c -> c.opensStatement, c -> c.opensStatementDescription));
    register(
        USES_STATEMENT,
        genericHandler.create(c -> c.usesStatement, c -> c.usesStatementDescription));
    register(
        PROVIDES_STATEMENT,
        genericHandler.create(c -> c.providesStatement, c -> c.providesStatementDescription));
    register(
        PROVIDES_WITH_LIST,
        genericHandler.create(c -> c.providesWithList, c -> c.providesWithListDescription));
    register(
        IMPORT_STATIC_REFERENCE,
        genericHandler.create(
            c -> c.importStaticReference, c -> c.importStaticReferenceDescription));
    register(TYPE, genericHandler.create(c -> c.type, c -> c.typeDescription));
    register(
        DIAMOND_TYPE, genericHandler.create(c -> c.diamondType, c -> c.diamondTypeDescription));
    register(
        REFERENCE_PARAMETER_LIST,
        genericHandler.create(
            c -> c.referenceParameterList, c -> c.referenceParameterListDescription));
    register(
        JAVA_CODE_REFERENCE,
        genericHandler.create(c -> c.javaCodeReference, c -> c.javaCodeReferenceDescription));
    register(
        PACKAGE_STATEMENT,
        genericHandler.create(c -> c.packageStatement, c -> c.packageStatementDescription));
    register(
        LOCAL_VARIABLE,
        genericHandler.create(c -> c.localVariable, c -> c.localVariableDescription));
    register(
        REFERENCE_EXPRESSION,
        genericHandler.create(c -> c.referenceExpression, c -> c.referenceExpressionDescription));
    register(
        THIS_EXPRESSION,
        genericHandler.create(c -> c.thisExpression, c -> c.thisExpressionDescription));
    register(
        SUPER_EXPRESSION,
        genericHandler.create(c -> c.superExpression, c -> c.superExpressionDescription));
    register(
        PARENTH_EXPRESSION,
        genericHandler.create(c -> c.parenthExpression, c -> c.parenthExpressionDescription));
    register(
        METHOD_CALL_EXPRESSION,
        genericHandler.create(c -> c.methodCallExpression, c -> c.methodCallExpressionDescription));
    register(
        TYPE_CAST_EXPRESSION,
        genericHandler.create(c -> c.typeCastExpression, c -> c.typeCastExpressionDescription));
    register(
        PREFIX_EXPRESSION,
        genericHandler.create(c -> c.prefixExpression, c -> c.prefixExpressionDescription));
    register(
        POSTFIX_EXPRESSION,
        genericHandler.create(c -> c.postfixExpression, c -> c.postfixExpressionDescription));
    register(
        BINARY_EXPRESSION,
        genericHandler.create(c -> c.binaryExpression, c -> c.binaryExpressionDescription));
    register(
        CONDITIONAL_EXPRESSION,
        genericHandler.create(
            c -> c.conditionalExpression, c -> c.conditionalExpressionDescription));
    register(
        ASSIGNMENT_EXPRESSION,
        genericHandler.create(c -> c.assignmentExpression, c -> c.assignmentExpressionDescription));
    register(
        NEW_EXPRESSION,
        genericHandler.create(c -> c.newExpression, c -> c.newExpressionDescription));
    register(
        ARRAY_ACCESS_EXPRESSION,
        genericHandler.create(
            c -> c.arrayAccessExpression, c -> c.arrayAccessExpressionDescription));
    register(
        ARRAY_INITIALIZER_EXPRESSION,
        genericHandler.create(
            c -> c.arrayInitializerExpression, c -> c.arrayInitializerExpressionDescription));
    register(
        INSTANCE_OF_EXPRESSION,
        genericHandler.create(c -> c.instanceOfExpression, c -> c.instanceOfExpressionDescription));
    register(
        CLASS_OBJECT_ACCESS_EXPRESSION,
        genericHandler.create(
            c -> c.classObjectAccessExpression, c -> c.classObjectAccessExpressionDescription));
    register(
        EMPTY_EXPRESSION,
        genericHandler.create(c -> c.emptyExpression, c -> c.emptyExpressionDescription));
    register(
        EXPRESSION_LIST,
        genericHandler.create(c -> c.expressionList, c -> c.expressionListDescription));
    register(
        EMPTY_STATEMENT,
        genericHandler.create(c -> c.emptyStatement, c -> c.emptyStatementDescription));
    register(
        BLOCK_STATEMENT,
        genericHandler.create(c -> c.blockStatement, c -> c.blockStatementDescription));
    register(
        EXPRESSION_STATEMENT,
        genericHandler.create(c -> c.expressionStatement, c -> c.expressionStatementDescription));
    register(
        EXPRESSION_LIST_STATEMENT,
        genericHandler.create(
            c -> c.expressionListStatement, c -> c.expressionListStatementDescription));
    register(
        DECLARATION_STATEMENT,
        genericHandler.create(c -> c.declarationStatement, c -> c.declarationStatementDescription));
    register(
        IF_STATEMENT, genericHandler.create(c -> c.ifStatement, c -> c.ifStatementDescription));
    register(
        WHILE_STATEMENT,
        genericHandler.create(c -> c.whileStatement, c -> c.whileStatementDescription));
    register(
        FOR_STATEMENT, genericHandler.create(c -> c.forStatement, c -> c.forStatementDescription));
    register(
        FOREACH_STATEMENT,
        genericHandler.create(c -> c.foreachStatement, c -> c.foreachStatementDescription));
    register(
        DO_WHILE_STATEMENT,
        genericHandler.create(c -> c.doWhileStatement, c -> c.doWhileStatementDescription));
    register(
        SWITCH_STATEMENT,
        genericHandler.create(c -> c.switchStatement, c -> c.switchStatementDescription));
    register(
        SWITCH_EXPRESSION,
        genericHandler.create(c -> c.switchExpression, c -> c.switchExpressionDescription));
    register(
        SWITCH_LABEL_STATEMENT,
        genericHandler.create(c -> c.switchLabelStatement, c -> c.switchLabelStatementDescription));
    register(
        SWITCH_LABELED_RULE,
        genericHandler.create(c -> c.switchLabeledRule, c -> c.switchLabeledRuleDescription));
    register(
        BREAK_STATEMENT,
        genericHandler.create(c -> c.breakStatement, c -> c.breakStatementDescription));
    register(
        CONTINUE_STATEMENT,
        genericHandler.create(c -> c.continueStatement, c -> c.continueStatementDescription));
    register(
        RETURN_STATEMENT,
        genericHandler.create(c -> c.returnStatement, c -> c.returnStatementDescription));
    register(
        THROW_STATEMENT,
        genericHandler.create(c -> c.throwStatement, c -> c.throwStatementDescription));
    register(
        SYNCHRONIZED_STATEMENT,
        genericHandler.create(
            c -> c.synchronizedStatement, c -> c.synchronizedStatementDescription));
    register(
        TRY_STATEMENT, genericHandler.create(c -> c.tryStatement, c -> c.tryStatementDescription));
    register(
        RESOURCE_LIST, genericHandler.create(c -> c.resourceList, c -> c.resourceListDescription));
    register(
        RESOURCE_VARIABLE,
        genericHandler.create(c -> c.resourceVariable, c -> c.resourceVariableDescription));
    register(
        RESOURCE_EXPRESSION,
        genericHandler.create(c -> c.resourceExpression, c -> c.resourceExpressionDescription));
    register(
        CATCH_SECTION, genericHandler.create(c -> c.catchSection, c -> c.catchSectionDescription));
    register(
        LABELED_STATEMENT,
        genericHandler.create(c -> c.labeledStatement, c -> c.labeledStatementDescription));
    register(
        ASSERT_STATEMENT,
        genericHandler.create(c -> c.assertStatement, c -> c.assertStatementDescription));
    register(
        ANNOTATION_ARRAY_INITIALIZER,
        genericHandler.create(
            c -> c.annotationArrayInitializer, c -> c.annotationArrayInitializerDescription));
    register(
        RECEIVER_PARAMETER,
        genericHandler.create(c -> c.receiverParameter, c -> c.receiverParameterDescription));
    register(
        MODULE_REFERENCE,
        genericHandler.create(c -> c.moduleReference, c -> c.moduleReferenceDescription));
    register(CODE_BLOCK, genericHandler.create(c -> c.codeBlock, c -> c.codeBlockDescription));
    register(DOC_COMMENT, genericHandler.create(c -> c.docComment, c -> c.docCommentDescription));
    register(DOC_TAG, genericHandler.create(c -> c.docTag, c -> c.docTagDescription));
    register(
        DOC_INLINE_TAG, genericHandler.create(c -> c.inlineDocTag, c -> c.inlineDocTagDescription));
    register(
        DOC_METHOD_OR_FIELD_REF,
        genericHandler.create(c -> c.docMethodOrFieldRef, c -> c.docMethodOrFieldRefDescription));
    register(
        DOC_PARAMETER_REF,
        genericHandler.create(c -> c.docParameterRef, c -> c.docParameterRefDescription));
    register(
        DOC_TAG_VALUE_ELEMENT,
        genericHandler.create(c -> c.docTagValueElement, c -> c.docTagValueElementDescription));
    register(
        DOC_REFERENCE_HOLDER,
        genericHandler.create(c -> c.docReferenceHolder, c -> c.docReferenceHolderDescription));
    register(
        DOC_TYPE_HOLDER,
        genericHandler.create(c -> c.docTypeHolder, c -> c.docTypeHolderDescription));
  }

  static ComplexityHandler get(IElementType type) {
    return handlers.get(type);
  }

  static void register(IElementType type, ComplexityHandler handler) {
    handlers.put(type, handler);
  }
}
