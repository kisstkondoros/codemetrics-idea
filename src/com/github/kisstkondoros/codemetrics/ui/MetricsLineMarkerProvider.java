package com.github.kisstkondoros.codemetrics.ui;

import com.github.kisstkondoros.codemetrics.core.CollectorType;
import com.github.kisstkondoros.codemetrics.core.MetricsModel;
import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.github.kisstkondoros.codemetrics.core.parser.MetricsParser;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.daemon.impl.LineMarkerNavigator;
import com.intellij.codeInsight.daemon.impl.MarkerType;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.FontPreferences;
import com.intellij.openapi.editor.colors.impl.AppEditorFontOptions;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.refactoring.JavaRefactoringActionHandlerFactory;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.Consumer;
import com.intellij.util.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MetricsLineMarkerProvider implements LineMarkerProvider {

    private MetricsParser metricsParser;

    public MetricsLineMarkerProvider() {
        metricsParser = new MetricsParser();
    }

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        return null;
    }


    public Stream<MetricsModel> streamMetricsModels(MetricsModel parentNode) {
        if (parentNode.children.isEmpty()) {
            return Stream.of(parentNode);
        } else {
            return parentNode.children.stream().filter(Objects::nonNull)
                    .map(this::streamMetricsModels)
                    .reduce(Stream.of(parentNode), Stream::concat);
        }
    }

    @Override
    public void collectSlowLineMarkers
            (@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
        if (elements.isEmpty()) {
            return;
        }

        PsiElement firstElement = elements.get(0);
        Project project = firstElement.getProject();
        if (DumbService.getInstance(project).isDumb()) {
            return;
        }

        MetricsConfiguration configuration = MetricsConfiguration.getInstance();
        MetricsModel metrics = metricsParser.getMetrics(firstElement.getContainingFile());
        FontPreferences fontPreferences = AppEditorFontOptions.getInstance().getFontPreferences();
        int lineHeight = fontPreferences.getSize(fontPreferences.getFontFamily());

        Map<PsiElement, MetricsModel> anchorToModelMap = streamMetricsModels(metrics) //
                .filter(p -> p.visible && CollectorType.SUM.equals(p.collectorType) && p.getCollectedComplexity() >= configuration.hiddenUnder)
                .collect(Collectors.toMap(p -> PsiTreeUtil.getDeepestVisibleFirst(p.node), p -> p, (duplicate1, duplicate2) -> duplicate1));

        anchorToModelMap.entrySet().stream()
                .map(entry -> {
                    PsiElement anchor = entry.getKey();
                    if (elements.contains(anchor)) {
                        MetricsModel model = entry.getValue();
                        return createMarker(configuration, lineHeight, anchor, model);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .forEach(result::add);
    }

    @NotNull
    private MetricsMarkerInfo createMarker(MetricsConfiguration configuration, int lineHeight, PsiElement anchor, MetricsModel model) {
        final Function<PsiElement, String> tooltip = psiElement -> model.toString(configuration);
        return new MetricsMarkerInfo(anchor, new MarkerType("CodeMetricsLineMarker", tooltip, new MyLineMarkerNavigator(model)), new MetricsIcon(model.getCollectedComplexity(), lineHeight));
    }

    private static class MyLineMarkerNavigator extends LineMarkerNavigator {

        private final MetricsModel model;

        public MyLineMarkerNavigator(MetricsModel model) {
            this.model = model;
        }

        @Override
        public void browse(final MouseEvent e, final PsiElement element) {
            final Editor editor = PsiUtilBase.findEditor(element);

            List<PsiElement> psiElements = model.children.stream()
                    .filter(p -> p.getCollectedComplexity() > 0).map(p -> p.node).collect(Collectors.toList());
            JBPopup jbPopup = PsiElementPicker
                    .navigateOrCreatePopup(psiElements, "Complexity increasing elements",
                            "Elements", new MetricsCellRenderer(model),
                            new Consumer<List<PsiElement>>() {
                                @Override
                                public void consume(List<PsiElement> selectedElements) {
                                    for (PsiElement element : selectedElements) {
                                        if (element instanceof Navigatable) {
                                            Navigatable navigatable = (Navigatable) element;
                                            if (navigatable.canNavigate()) {
                                                navigatable.navigate(true);
                                            }
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
                                                handler.invoke(element.getProject(),
                                                        new PsiElement[]{element}, dataContext);
                                            }
                                        }
                                    }
                                }

                                @Nullable
                                private RefactoringActionHandler getRefactoringActionHandler(
                                        PsiElement element) {
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

                                private boolean isMethodExtractionCandidate(PsiElement element) {
                                    return element instanceof PsiPolyadicExpression ||
                                            element instanceof PsiLambdaExpression ||
                                            element instanceof PsiLoopStatement ||
                                            element instanceof PsiSwitchStatement ||
                                            element instanceof PsiIfStatement ||
                                            element instanceof PsiConditionalExpression;
                                }
                            });
            if (jbPopup != null && editor != null) {
                jbPopup.show(new RelativePoint(e));
            }
        }
    }
}
