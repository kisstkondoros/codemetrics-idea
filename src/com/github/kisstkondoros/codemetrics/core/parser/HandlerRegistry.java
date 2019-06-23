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
    handlers.put(
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
    handlers.put(
        CLASS,
        genericHandler.create(c -> c.aClass, c -> c.aClassDescription, c -> c.metricsForAClass));
    handlers.put(
        ANONYMOUS_CLASS,
        genericHandler.create(
            c -> c.anonymousClass,
            c -> c.anonymousClassDescription,
            c -> c.metricsForAnonymousClass));
    handlers.put(
        ENUM_CONSTANT_INITIALIZER,
        genericHandler.create(
            c -> c.enumConstantInitializer, c -> c.enumConstantInitializerDescription));
    handlers.put(
        TYPE_PARAMETER_LIST,
        genericHandler.create(c -> c.typeParameterList, c -> c.typeParameterListDescription));
    handlers.put(
        TYPE_PARAMETER,
        genericHandler.create(c -> c.typeParameter, c -> c.typeParameterDescription));
    handlers.put(
        IMPORT_LIST, genericHandler.create(c -> c.importList, c -> c.importListDescription));
    handlers.put(
        IMPORT_STATEMENT,
        genericHandler.create(c -> c.importStatement, c -> c.importStatementDescription));
    handlers.put(
        IMPORT_STATIC_STATEMENT,
        genericHandler.create(
            c -> c.importStaticStatement, c -> c.importStaticStatementDescription));
    handlers.put(
        MODIFIER_LIST, genericHandler.create(c -> c.modifierList, c -> c.modifierListDescription));
    handlers.put(
        ANNOTATION, genericHandler.create(c -> c.annotation, c -> c.annotationDescription));
    handlers.put(
        NAME_VALUE_PAIR,
        genericHandler.create(c -> c.nameValuePair, c -> c.nameValuePairDescription));
    handlers.put(
        LITERAL_EXPRESSION,
        genericHandler.create(c -> c.literalExpression, c -> c.literalExpressionDescription));
    handlers.put(
        ANNOTATION_PARAMETER_LIST,
        genericHandler.create(
            c -> c.annotationParameterList, c -> c.annotationParameterListDescription));
    handlers.put(
        EXTENDS_LIST, genericHandler.create(c -> c.extendsList, c -> c.extendsListDescription));
    handlers.put(
        IMPLEMENTS_LIST,
        genericHandler.create(c -> c.implementsList, c -> c.implementsListDescription));
    handlers.put(FIELD, genericHandler.create(c -> c.field, c -> c.fieldDescription));
    handlers.put(
        ENUM_CONSTANT, genericHandler.create(c -> c.enumConstant, c -> c.enumConstantDescription));
    handlers.put(
        METHOD,
        genericHandler.create(c -> c.method, c -> c.methodDescription, c -> c.metricsForMethod));
    handlers.put(
        ANNOTATION_METHOD,
        genericHandler.create(c -> c.annotationMethod, c -> c.annotationMethodDescription));
    handlers.put(
        CLASS_INITIALIZER,
        genericHandler.create(c -> c.classInitializer, c -> c.classInitializerDescription));
    handlers.put(PARAMETER, genericHandler.create(c -> c.parameter, c -> c.parameterDescription));
    handlers.put(
        PARAMETER_LIST,
        genericHandler.create(c -> c.parameterList, c -> c.parameterListDescription));
    handlers.put(
        EXTENDS_BOUND_LIST,
        genericHandler.create(c -> c.extendsBoundList, c -> c.extendsBoundListDescription));
    handlers.put(
        THROWS_LIST, genericHandler.create(c -> c.throwsList, c -> c.throwsListDescription));
    handlers.put(
        LAMBDA_EXPRESSION,
        genericHandler.create(
            c -> c.lambdaExpression,
            c -> c.lambdaExpressionDescription,
            c -> c.metricsForLambdaExpression));
    handlers.put(
        METHOD_REF_EXPRESSION,
        genericHandler.create(c -> c.methodRefExpression, c -> c.methodRefExpressionDescription));
    handlers.put(MODULE, genericHandler.create(c -> c.module, c -> c.moduleDescription));
    handlers.put(
        REQUIRES_STATEMENT,
        genericHandler.create(c -> c.requiresStatement, c -> c.requiresStatementDescription));
    handlers.put(
        EXPORTS_STATEMENT,
        genericHandler.create(c -> c.exportsStatement, c -> c.exportsStatementDescription));
    handlers.put(
        OPENS_STATEMENT,
        genericHandler.create(c -> c.opensStatement, c -> c.opensStatementDescription));
    handlers.put(
        USES_STATEMENT,
        genericHandler.create(c -> c.usesStatement, c -> c.usesStatementDescription));
    handlers.put(
        PROVIDES_STATEMENT,
        genericHandler.create(c -> c.providesStatement, c -> c.providesStatementDescription));
    handlers.put(
        PROVIDES_WITH_LIST,
        genericHandler.create(c -> c.providesWithList, c -> c.providesWithListDescription));
    handlers.put(
        IMPORT_STATIC_REFERENCE,
        genericHandler.create(
            c -> c.importStaticReference, c -> c.importStaticReferenceDescription));
    handlers.put(TYPE, genericHandler.create(c -> c.type, c -> c.typeDescription));
    handlers.put(
        DIAMOND_TYPE, genericHandler.create(c -> c.diamondType, c -> c.diamondTypeDescription));
    handlers.put(
        REFERENCE_PARAMETER_LIST,
        genericHandler.create(
            c -> c.referenceParameterList, c -> c.referenceParameterListDescription));
    handlers.put(
        JAVA_CODE_REFERENCE,
        genericHandler.create(c -> c.javaCodeReference, c -> c.javaCodeReferenceDescription));
    handlers.put(
        PACKAGE_STATEMENT,
        genericHandler.create(c -> c.packageStatement, c -> c.packageStatementDescription));
    handlers.put(
        LOCAL_VARIABLE,
        genericHandler.create(c -> c.localVariable, c -> c.localVariableDescription));
    handlers.put(
        REFERENCE_EXPRESSION,
        genericHandler.create(c -> c.referenceExpression, c -> c.referenceExpressionDescription));
    handlers.put(
        THIS_EXPRESSION,
        genericHandler.create(c -> c.thisExpression, c -> c.thisExpressionDescription));
    handlers.put(
        SUPER_EXPRESSION,
        genericHandler.create(c -> c.superExpression, c -> c.superExpressionDescription));
    handlers.put(
        PARENTH_EXPRESSION,
        genericHandler.create(c -> c.parenthExpression, c -> c.parenthExpressionDescription));
    handlers.put(
        METHOD_CALL_EXPRESSION,
        genericHandler.create(c -> c.methodCallExpression, c -> c.methodCallExpressionDescription));
    handlers.put(
        TYPE_CAST_EXPRESSION,
        genericHandler.create(c -> c.typeCastExpression, c -> c.typeCastExpressionDescription));
    handlers.put(
        PREFIX_EXPRESSION,
        genericHandler.create(c -> c.prefixExpression, c -> c.prefixExpressionDescription));
    handlers.put(
        POSTFIX_EXPRESSION,
        genericHandler.create(c -> c.postfixExpression, c -> c.postfixExpressionDescription));
    handlers.put(
        BINARY_EXPRESSION,
        genericHandler.create(c -> c.binaryExpression, c -> c.binaryExpressionDescription));
    handlers.put(
        CONDITIONAL_EXPRESSION,
        genericHandler.create(
            c -> c.conditionalExpression, c -> c.conditionalExpressionDescription));
    handlers.put(
        ASSIGNMENT_EXPRESSION,
        genericHandler.create(c -> c.assignmentExpression, c -> c.assignmentExpressionDescription));
    handlers.put(
        NEW_EXPRESSION,
        genericHandler.create(c -> c.newExpression, c -> c.newExpressionDescription));
    handlers.put(
        ARRAY_ACCESS_EXPRESSION,
        genericHandler.create(
            c -> c.arrayAccessExpression, c -> c.arrayAccessExpressionDescription));
    handlers.put(
        ARRAY_INITIALIZER_EXPRESSION,
        genericHandler.create(
            c -> c.arrayInitializerExpression, c -> c.arrayInitializerExpressionDescription));
    handlers.put(
        INSTANCE_OF_EXPRESSION,
        genericHandler.create(c -> c.instanceOfExpression, c -> c.instanceOfExpressionDescription));
    handlers.put(
        CLASS_OBJECT_ACCESS_EXPRESSION,
        genericHandler.create(
            c -> c.classObjectAccessExpression, c -> c.classObjectAccessExpressionDescription));
    handlers.put(
        EMPTY_EXPRESSION,
        genericHandler.create(c -> c.emptyExpression, c -> c.emptyExpressionDescription));
    handlers.put(
        EXPRESSION_LIST,
        genericHandler.create(c -> c.expressionList, c -> c.expressionListDescription));
    handlers.put(
        EMPTY_STATEMENT,
        genericHandler.create(c -> c.emptyStatement, c -> c.emptyStatementDescription));
    handlers.put(
        BLOCK_STATEMENT,
        genericHandler.create(c -> c.blockStatement, c -> c.blockStatementDescription));
    handlers.put(
        EXPRESSION_STATEMENT,
        genericHandler.create(c -> c.expressionStatement, c -> c.expressionStatementDescription));
    handlers.put(
        EXPRESSION_LIST_STATEMENT,
        genericHandler.create(
            c -> c.expressionListStatement, c -> c.expressionListStatementDescription));
    handlers.put(
        DECLARATION_STATEMENT,
        genericHandler.create(c -> c.declarationStatement, c -> c.declarationStatementDescription));
    handlers.put(
        IF_STATEMENT, genericHandler.create(c -> c.ifStatement, c -> c.ifStatementDescription));
    handlers.put(
        WHILE_STATEMENT,
        genericHandler.create(c -> c.whileStatement, c -> c.whileStatementDescription));
    handlers.put(
        FOR_STATEMENT, genericHandler.create(c -> c.forStatement, c -> c.forStatementDescription));
    handlers.put(
        FOREACH_STATEMENT,
        genericHandler.create(c -> c.foreachStatement, c -> c.foreachStatementDescription));
    handlers.put(
        DO_WHILE_STATEMENT,
        genericHandler.create(c -> c.doWhileStatement, c -> c.doWhileStatementDescription));
    handlers.put(
        SWITCH_STATEMENT,
        genericHandler.create(c -> c.switchStatement, c -> c.switchStatementDescription));
    handlers.put(
        SWITCH_EXPRESSION,
        genericHandler.create(c -> c.switchExpression, c -> c.switchExpressionDescription));
    handlers.put(
        SWITCH_LABEL_STATEMENT,
        genericHandler.create(c -> c.switchLabelStatement, c -> c.switchLabelStatementDescription));
    handlers.put(
        SWITCH_LABELED_RULE,
        genericHandler.create(c -> c.switchLabeledRule, c -> c.switchLabeledRuleDescription));
    handlers.put(
        BREAK_STATEMENT,
        genericHandler.create(c -> c.breakStatement, c -> c.breakStatementDescription));
    handlers.put(
        CONTINUE_STATEMENT,
        genericHandler.create(c -> c.continueStatement, c -> c.continueStatementDescription));
    handlers.put(
        RETURN_STATEMENT,
        genericHandler.create(c -> c.returnStatement, c -> c.returnStatementDescription));
    handlers.put(
        THROW_STATEMENT,
        genericHandler.create(c -> c.throwStatement, c -> c.throwStatementDescription));
    handlers.put(
        SYNCHRONIZED_STATEMENT,
        genericHandler.create(
            c -> c.synchronizedStatement, c -> c.synchronizedStatementDescription));
    handlers.put(
        TRY_STATEMENT, genericHandler.create(c -> c.tryStatement, c -> c.tryStatementDescription));
    handlers.put(
        RESOURCE_LIST, genericHandler.create(c -> c.resourceList, c -> c.resourceListDescription));
    handlers.put(
        RESOURCE_VARIABLE,
        genericHandler.create(c -> c.resourceVariable, c -> c.resourceVariableDescription));
    handlers.put(
        RESOURCE_EXPRESSION,
        genericHandler.create(c -> c.resourceExpression, c -> c.resourceExpressionDescription));
    handlers.put(
        CATCH_SECTION, genericHandler.create(c -> c.catchSection, c -> c.catchSectionDescription));
    handlers.put(
        LABELED_STATEMENT,
        genericHandler.create(c -> c.labeledStatement, c -> c.labeledStatementDescription));
    handlers.put(
        ASSERT_STATEMENT,
        genericHandler.create(c -> c.assertStatement, c -> c.assertStatementDescription));
    handlers.put(
        ANNOTATION_ARRAY_INITIALIZER,
        genericHandler.create(
            c -> c.annotationArrayInitializer, c -> c.annotationArrayInitializerDescription));
    handlers.put(
        RECEIVER_PARAMETER,
        genericHandler.create(c -> c.receiverParameter, c -> c.receiverParameterDescription));
    handlers.put(
        MODULE_REFERENCE,
        genericHandler.create(c -> c.moduleReference, c -> c.moduleReferenceDescription));
    handlers.put(CODE_BLOCK, genericHandler.create(c -> c.codeBlock, c -> c.codeBlockDescription));
    handlers.put(
        DOC_COMMENT, genericHandler.create(c -> c.docComment, c -> c.docCommentDescription));
    handlers.put(DOC_TAG, genericHandler.create(c -> c.docTag, c -> c.docTagDescription));
    handlers.put(
        DOC_INLINE_TAG, genericHandler.create(c -> c.inlineDocTag, c -> c.inlineDocTagDescription));
    handlers.put(
        DOC_METHOD_OR_FIELD_REF,
        genericHandler.create(c -> c.docMethodOrFieldRef, c -> c.docMethodOrFieldRefDescription));
    handlers.put(
        DOC_PARAMETER_REF,
        genericHandler.create(c -> c.docParameterRef, c -> c.docParameterRefDescription));
    handlers.put(
        DOC_TAG_VALUE_ELEMENT,
        genericHandler.create(c -> c.docTagValueElement, c -> c.docTagValueElementDescription));
    handlers.put(
        DOC_REFERENCE_HOLDER,
        genericHandler.create(c -> c.docReferenceHolder, c -> c.docReferenceHolderDescription));
    handlers.put(
        DOC_TYPE_HOLDER,
        genericHandler.create(c -> c.docTypeHolder, c -> c.docTypeHolderDescription));
  }

  static ComplexityHandler get(IElementType type) {
    return handlers.get(type);
  }
}
