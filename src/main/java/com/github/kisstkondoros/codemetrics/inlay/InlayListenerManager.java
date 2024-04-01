package com.github.kisstkondoros.codemetrics.inlay;

import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.github.kisstkondoros.codemetrics.util.Debouncer;
import com.google.common.collect.Maps;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

class InlayListenerManager implements FileEditorManagerListener, Disposable {

  private final Project project;
  private final Map<String, Disposable> disposables;

  public InlayListenerManager(Project project) {
    this.project = project;
    disposables = Maps.newHashMap();
  }

  @Override
  public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
    Arrays.stream(source.getEditors(file))
        .forEach(fileEditor -> installInlayHandler(file, fileEditor));
  }

  private void installInlayHandler(@NotNull VirtualFile file, FileEditor fileEditor) {
    if (fileEditor instanceof TextEditor) {
      Editor editor = ((TextEditor) fileEditor).getEditor();
      PsiFile psiFile = PsiUtilBase.getPsiFile(project, file);
      if (!(psiFile instanceof PsiJavaFile)) {
        return;
      }

      InlayHighlighter inlayHighlighter = new InlayHighlighter(project);
      inlayHighlighter.installInlayHighlighter(editor, file);

      DocumentListener listener = registerDocumentListener(file, editor);
      MetricsConfiguration configuration = MetricsConfiguration.getInstance();
      Disposable refreshListener =
          configuration.addListener(
              () -> {
                InlayManager inlayManager = project.getComponent(InlayManager.class);
                inlayManager.updateInlays(editor, file);
              });
      disposables.put(
          file.getUrl(),
          () -> {
            editor.getDocument().removeDocumentListener(listener);
            refreshListener.dispose();
          });
    }
  }

  @NotNull
  private DocumentListener registerDocumentListener(@NotNull VirtualFile file, Editor editor) {
    InlayManager inlayManager = project.getComponent(InlayManager.class);
    DocumentListener listener =
        new DocumentListener() {
          Debouncer debouncer = new Debouncer();

          @Override
          public void documentChanged(@NotNull DocumentEvent event) {
            debouncer.debounce(() -> inlayManager.updateInlays(editor, file));
          }
        };
    inlayManager.updateInlays(editor, file);
    editor.getDocument().addDocumentListener(listener);
    return listener;
  }

  @Override
  public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
    String url = file.getUrl();
    Disposable listener = disposables.remove(url);
    if (listener != null) {
      listener.dispose();
    }
  }

  @Override
  public void dispose() {
    disposables.values().forEach(Disposable::dispose);
    disposables.clear();
  }
}
