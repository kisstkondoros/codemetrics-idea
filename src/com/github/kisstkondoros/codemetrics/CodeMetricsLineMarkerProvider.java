package com.github.kisstkondoros.codemetrics;

import com.github.kisstkondoros.codemetrics.visitor.ComplexityVisitor;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.daemon.MergeableLineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.LineMarkerNavigator;
import com.intellij.codeInsight.daemon.impl.MarkerType;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.refactoring.JavaRefactoringActionHandlerFactory;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.Consumer;
import com.intellij.util.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CodeMetricsLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {

        if (element instanceof PsiMethod || element instanceof PsiAnonymousClass) {
            final ComplexityVisitor.ComplexitySummary summary = analyze(element);

            PsiFile file = element.getContainingFile();
            final Document document = file == null ? null : PsiDocumentManager.getInstance(file.getProject())
                    .getDocument(file);

            final Function<PsiElement, String> tooltip = psiElement -> summary.getTooltip(document);
            if (summary.getSummary() > 0) {
                return new MetricsMarkerInfo(element, summary.getIcon(),
                        new MarkerType(tooltip, new LineMarkerNavigator() {

                            @Override
                            public void browse(final MouseEvent e, final PsiElement element) {
                                final Editor editor = PsiUtilBase.findEditor(element);
                                PsiElement[] psiElements = getNavigatablePsiElements(document);
                                JBPopup jbPopup = PsiElementList
                                        .navigateOrCreatePopup(psiElements, "Complexity increasing elements",
                                                "Elements", new MetricsCellRenderer(summary), null,
                                                new Consumer<Object[]>() {
                                                    @Override
                                                    public void consume(Object[] selectedElements) {
                                                        for (Object element : selectedElements) {
                                                            PsiElement selected = (PsiElement) element;
                                                            if (selected instanceof NavigatablePsiElement) {
                                                                NavigatablePsiElement navigatablePsiElement =
                                                                        (NavigatablePsiElement) selected;
                                                                navigatablePsiElement.navigate(true);
                                                            }

                                                            if (editor instanceof EditorImpl) {
                                                                DataManager mgr = DataManager.getInstance();
                                                                EditorImpl editorImpl = (EditorImpl) editor;
                                                                DataContext dataContext = mgr.getDataContext(
                                                                        editorImpl.getContentComponent());
                                                                RefactoringActionHandler handler =
                                                                        getRefactoringActionHandler(
                                                                                element);

                                                                if (handler != null) {
                                                                    handler.invoke(selected.getProject(),
                                                                            new PsiElement[]{selected}, dataContext);
                                                                }
                                                            }
                                                        }
                                                    }

                                                    @Nullable
                                                    private RefactoringActionHandler getRefactoringActionHandler(
                                                            Object element) {
                                                        RefactoringActionHandler refactoringActionHandler = null;
                                                        if (element instanceof PsiClass) {
                                                            refactoringActionHandler =
                                                                    JavaRefactoringActionHandlerFactory
                                                                            .getInstance()
                                                                            .createAnonymousToInnerHandler();
                                                        } else if (isMethodExtractionCandidate(element)) {
                                                            refactoringActionHandler =
                                                                    JavaRefactoringActionHandlerFactory
                                                                            .getInstance().createExtractMethodHandler();
                                                        }
                                                        return refactoringActionHandler;
                                                    }

                                                    private boolean isMethodExtractionCandidate(Object element) {
                                                        boolean b = element instanceof PsiPolyadicExpression ||
                                                                element instanceof PsiLambdaExpression ||
                                                                element instanceof PsiLoopStatement ||
                                                                element instanceof PsiSwitchStatement ||
                                                                element instanceof PsiIfStatement ||
                                                                element instanceof PsiConditionalExpression;
                                                        return b;
                                                    }
                                                });
                                if (jbPopup != null && editor != null) {
                                    jbPopup.show(new RelativePoint(e));
                                }
                            }

                            @NotNull
                            private PsiElement[] getNavigatablePsiElements(Document document) {
                                List<ComplexityVisitor.ComplexityInfo> complexityInfos = summary
                                        .getLineNumberOrderedComplexityInformation(document);
                                Iterable<PsiElement> castedElements = complexityInfos.stream().map(ComplexityVisitor.ComplexityInfo::getElement).collect(Collectors.toList());
                                return Iterables.toArray(castedElements, PsiElement.class);
                            }
                        }));
            }
        }
        return null;
    }

    private ComplexityVisitor.ComplexitySummary analyze(PsiElement element) {
        return new ComplexityVisitor().getComplexity(element);
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }

    private static class MetricsMarkerInfo extends MergeableLineMarkerInfo<PsiElement> {
        private MetricsMarkerInfo(@NotNull PsiElement element, Icon icon, @NotNull MarkerType markerType) {
            super(element, element.getTextRange(), icon, Pass.UPDATE_ALL, markerType.getTooltip(),
                    markerType.getNavigationHandler(), GutterIconRenderer.Alignment.RIGHT);
        }

        @Override
        public boolean canMergeWith(@NotNull MergeableLineMarkerInfo<?> info) {
            if (!(info instanceof MetricsMarkerInfo))
                return false;
            PsiElement otherElement = info.getElement();
            PsiElement myElement = getElement();
            return otherElement != null && myElement != null;
        }


        @Override
        public Icon getCommonIcon(@NotNull List<MergeableLineMarkerInfo> infos) {
            return myIcon;
        }

        @Override
        public Function<? super PsiElement, String> getCommonTooltip(
                @NotNull final List<MergeableLineMarkerInfo> infos) {
            return (Function<PsiElement, String>) element -> Joiner.on(System.lineSeparator()).skipNulls().join(infos);
        }
    }
}
