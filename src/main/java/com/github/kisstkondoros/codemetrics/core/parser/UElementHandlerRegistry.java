package com.github.kisstkondoros.codemetrics.core.parser;

import org.jetbrains.uast.*;

public class UElementHandlerRegistry {
    static void register() {

        SimpleHandlerFactory genericHandler = new SimpleHandlerFactory();

        ComplexityHandler<UPolyadicExpression> polyadicExpressionHandler =
                genericHandler.create(c -> c.polyadicExpression, c -> c.polyadicExpressionDescription);
        register(
                UPolyadicExpression.class,
                configuration ->
                        polyadicExpression -> {
                            ComplexityDescription original =
                                    polyadicExpressionHandler.apply(configuration).apply(polyadicExpression);
                            int complexity = 0;
                            if (polyadicExpression.getOperator() instanceof UastBinaryOperator.LogicalOperator) {
                                complexity = polyadicExpression.getOperands().size() - 1;
                            }
                            return new ComplexityDescription(
                                    complexity * original.getIncrement(),
                                    original.getDescription(),
                                    original.isVisible());
                        });
        register(
                UClass.class,
                genericHandler.create(c -> c.aClass, c -> c.aClassDescription, c -> c.metricsForAClass));
        register(
                UAnonymousClass.class,
                genericHandler.create(
                        c -> c.anonymousClass,
                        c -> c.anonymousClassDescription,
                        c -> c.metricsForAnonymousClass));
        register(
                UImportStatement.class,
                genericHandler.create(c -> c.importStatement, c -> c.importStatementDescription));

        register(UAnnotation.class, genericHandler.create(c -> c.annotation, c -> c.annotationDescription));
        register(
                ULiteralExpression.class,
                genericHandler.create(c -> c.literalExpression, c -> c.literalExpressionDescription));
        register(UField.class, genericHandler.create(c -> c.field, c -> c.fieldDescription));
        register(
                UEnumConstant.class, genericHandler.create(c -> c.enumConstant, c -> c.enumConstantDescription));
        register(
                UMethod.class,
                genericHandler.create(c -> c.method, c -> c.methodDescription, c -> c.metricsForMethod));
        register(
                UAnnotationMethod.class,
                genericHandler.create(c -> c.annotationMethod, c -> c.annotationMethodDescription));
        register(
                UClassInitializer.class,
                genericHandler.create(c -> c.classInitializer, c -> c.classInitializerDescription));
        register(UParameter.class, genericHandler.create(c -> c.parameter, c -> c.parameterDescription));
        register(
                ULambdaExpression.class,
                genericHandler.create(
                        c -> c.lambdaExpression,
                        c -> c.lambdaExpressionDescription,
                        c -> c.metricsForLambdaExpression));
        register(
                ULocalVariable.class,
                genericHandler.create(c -> c.localVariable, c -> c.localVariableDescription));
        register(
                UReferenceExpression.class,
                genericHandler.create(c -> c.referenceExpression, c -> c.referenceExpressionDescription));
        register(
                UThisExpression.class,
                genericHandler.create(c -> c.thisExpression, c -> c.thisExpressionDescription));
        register(
                USuperExpression.class,
                genericHandler.create(c -> c.superExpression, c -> c.superExpressionDescription));
        register(
                UParenthesizedExpression.class,
                genericHandler.create(c -> c.parenthesizedExpression, c -> c.parenthesizedExpressionDescription));
        register(
                UCallExpression.class,
                genericHandler.create(c -> c.methodCallExpression, c -> c.methodCallExpressionDescription));
        register(
                UPrefixExpression.class,
                genericHandler.create(c -> c.prefixExpression, c -> c.prefixExpressionDescription));
        register(
                UPostfixExpression.class,
                genericHandler.create(c -> c.postfixExpression, c -> c.postfixExpressionDescription));
        register(
                UArrayAccessExpression.class,
                genericHandler.create(
                        c -> c.arrayAccessExpression, c -> c.arrayAccessExpressionDescription));
        register(
                UastEmptyExpression.class,
                genericHandler.create(c -> c.emptyExpression, c -> c.emptyExpressionDescription));
        register(
                UExpressionList.class,
                genericHandler.create(c -> c.expressionList, c -> c.expressionListDescription));
        register(
                UBlockExpression.class,
                genericHandler.create(c -> c.blockStatement, c -> c.blockStatementDescription));
        register(
                UExpression.class,
                genericHandler.create(c -> c.expressionStatement, c -> c.expressionStatementDescription));
        register(
                UExpressionList.class,
                genericHandler.create(
                        c -> c.expressionListStatement, c -> c.expressionListStatementDescription));
        register(
                UDeclaration.class,
                genericHandler.create(c -> c.declarationStatement, c -> c.declarationStatementDescription));
        register(
                UIfExpression.class, genericHandler.create(c -> c.ifStatement, c -> c.ifStatementDescription));
        register(
                UWhileExpression.class,
                genericHandler.create(c -> c.whileStatement, c -> c.whileStatementDescription));
        register(
                UForExpression.class, genericHandler.create(c -> c.forStatement, c -> c.forStatementDescription));
        register(
                UForEachExpression.class,
                genericHandler.create(c -> c.foreachStatement, c -> c.foreachStatementDescription));
        register(
                UDoWhileExpression.class,
                genericHandler.create(c -> c.doWhileStatement, c -> c.doWhileStatementDescription));
        register(
                USwitchExpression.class,
                genericHandler.create(c -> c.switchExpression, c -> c.switchExpressionDescription));
        register(
                UBreakExpression.class,
                genericHandler.create(c -> c.breakStatement, c -> c.breakStatementDescription));
        register(
                UContinueExpression.class,
                genericHandler.create(c -> c.continueStatement, c -> c.continueStatementDescription));
        register(
                UReturnExpression.class,
                genericHandler.create(c -> c.returnStatement, c -> c.returnStatementDescription));
        register(
                UThrowExpression.class,
                genericHandler.create(c -> c.throwStatement, c -> c.throwStatementDescription));
        register(
                UTryExpression.class, genericHandler.create(c -> c.tryStatement, c -> c.tryStatementDescription));
        register(
                UCatchClause.class, genericHandler.create(c -> c.catchSection, c -> c.catchSectionDescription));
        register(
                ULabeledExpression.class,
                genericHandler.create(c -> c.labeledStatement, c -> c.labeledStatementDescription));
        register(UComment.class, genericHandler.create(c -> c.docComment, c -> c.docCommentDescription));
    }

    static <T extends UElement> void register(Class<T> type, ComplexityHandler<T> handler) {
        HandlerRegistry.register(type, handler);
    }
}
