package com.github.kisstkondoros.codemetrics.inlay;

import com.github.kisstkondoros.codemetrics.core.MetricsModel;
import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.github.kisstkondoros.codemetrics.core.parser.MetricsParser;
import com.github.kisstkondoros.codemetrics.util.ActionPicker;
import com.google.common.collect.Maps;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.InlayModel;
import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.Alarm;
import com.intellij.util.AlarmFactory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

class InlayManager implements FileEditorManagerListener {

    private final Project project;
    private final Map<String, Disposable> disposables;

    public InlayManager(Project project) {
        this.project = project;
        disposables = Maps.newHashMap();
    }

    @Override
    public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        Arrays.stream(source.getEditors(file)).forEach(fileEditor -> {
            if (fileEditor instanceof TextEditor) {
                Editor editor = ((TextEditor) fileEditor).getEditor();
                installInlayHighlighter(editor);

                MetricsParsingDocumentListener listener = new MetricsParsingDocumentListener(file, editor);
                editor.getDocument().addDocumentListener(listener);
                disposables.put(file.getUrl(), listener);
            }
        });
    }

    private void installInlayHighlighter(Editor editor) {
        editor.addEditorMouseMotionListener(new EditorMouseMotionListener() {
            WeakReference<Inlay<? extends MetricsHintRenderer>> previousHighlighted = new WeakReference<>(null);

            @Override
            public void mouseMoved(@NotNull EditorMouseEvent event) {

                Inlay<? extends MetricsHintRenderer> previousHighlightedInlay = previousHighlighted.get();

                if (previousHighlightedInlay != null) {
                    previousHighlightedInlay.getRenderer().setHighlighted(false);
                    previousHighlighted = new WeakReference<>(null);
                }

                executeWithInlayUnderPointer(editor, event, inlay -> {
                    previousHighlighted = new WeakReference<>(inlay);
                    inlay.getRenderer().setHighlighted(true);
                });
                if (previousHighlightedInlay != previousHighlighted.get()) {
                    editor.getContentComponent().repaint();
                }
            }
        });
        editor.addEditorMouseListener(new EditorMouseListener() {
            @Override
            public void mouseClicked(@NotNull EditorMouseEvent event) {
                if (event.getArea().equals(EditorMouseEventArea.EDITING_AREA)) {

                    Consumer<Inlay<? extends MetricsHintRenderer>> openPicker = p -> {
                        MetricsModel model = p.getRenderer().getModel();
                        RelativePoint point = new RelativePoint(event.getMouseEvent());
                        ActionPicker.showRefactorActionPicker(point, editor, model);
                    };

                    executeWithInlayUnderPointer(editor, event, openPicker);
                }
            }
        });
    }

    private void executeWithInlayUnderPointer(Editor editor, EditorMouseEvent event, Consumer<Inlay<?
            extends MetricsHintRenderer>> openPicker) {
        List<Inlay<? extends MetricsHintRenderer>> inlays = getInlays(editor);

        Optional<Inlay<? extends MetricsHintRenderer>> firstInlay = inlays.stream().filter(p -> p.getBounds() !=
                null).filter(p -> containsPosition(p.getBounds(), event.getMouseEvent().getPoint())).findFirst();
        firstInlay.ifPresent(openPicker);
    }

    private boolean containsPosition(Rectangle bounds, Point point) {
        return bounds.contains(point.getX(), point.getY());
    }

    @Override
    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        String url = file.getUrl();
        Disposable listener = disposables.remove(url);
        if (listener != null) {
            listener.dispose();
        }
    }

    @NotNull
    private List<Inlay<? extends MetricsHintRenderer>> getInlays(Editor editor) {
        int maxOffset = editor.getDocument().getTextLength();
        return editor.getInlayModel().getAfterLineEndElementsInRange(0, maxOffset, MetricsHintRenderer.class);
    }

    private class MetricsParsingDocumentListener implements DocumentListener, Disposable {

        private final VirtualFile file;
        private final Editor editor;
        private Alarm alarm;

        MetricsParsingDocumentListener(VirtualFile file, Editor editor) {
            this.alarm = AlarmFactory.getInstance().create();
            this.file = file;
            this.editor = editor;
            this.updateInlays();
        }

        public void dispose() {
            editor.getDocument().removeDocumentListener(this);
        }

        Stream<MetricsModel> streamMetricsModels(MetricsModel parentNode, Predicate<MetricsModel> predicate) {
            if (parentNode.getChildren().isEmpty()) {
                return Stream.of(parentNode).filter(predicate);
            } else {
                return parentNode.getChildren().stream().filter(Objects::nonNull).filter(predicate).map((MetricsModel current) -> streamMetricsModels(current, predicate)).reduce(Stream.of(parentNode), Stream::concat);
            }
        }

        @Override
        public void documentChanged(@NotNull DocumentEvent event) {
            debounce(this::updateInlays);
        }

        private void debounce(Runnable runnable) {
            alarm.cancelAllRequests();
            alarm.addRequest(() -> ApplicationManager.getApplication().runReadAction(runnable), 1000);
        }

        private void updateInlays() {
            Stream<MetricsModel> models = computeMetrics();

            Application application = ApplicationManager.getApplication();
            application.invokeLater(() -> updateInlays(models));
        }

        private Stream<MetricsModel> computeMetrics() {
            PsiFile psiFile = PsiUtilBase.getPsiFile(project, file);

            MetricsModel model = new MetricsParser().getMetrics(psiFile);
            MetricsConfiguration configuration = MetricsConfiguration.getInstance();
            Predicate<MetricsModel> metricsModelPredicate =
                    p -> p.isVisible() && p.getCollectedComplexity() >= configuration.hiddenUnder;

            return streamMetricsModels(model, metricsModelPredicate);
        }

        private void updateInlays(Stream<MetricsModel> models) {
            disposeInlays();
            models.forEach(this::installInlay);
        }

        private void disposeInlays() {
            List<Inlay<? extends MetricsHintRenderer>> inlays = getInlays(editor);
            inlays.forEach(Disposer::dispose);
        }

        private void installInlay(MetricsModel value) {
            int textOffset = value.getNode().getTextOffset();
            InlayModel inlayModel = editor.getInlayModel();
            inlayModel.addAfterLineEndElement(textOffset, false, new MetricsHintRenderer(value));
        }
    }
}
