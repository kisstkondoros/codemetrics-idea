package com.github.kisstkondoros.codemetrics.inlay;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class EditorListener implements ProjectComponent {
  private Project project;

  public EditorListener(Project project) {
    this.project = project;
  }

  @Override
  public void projectOpened() {
    project
        .getMessageBus()
        .connect()
        .subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new InlayListenerManager(project));
  }

  @NotNull
  @Override
  public String getComponentName() {
    return "CodeMetrics.EditorListener";
  }
}
