package com.github.kisstkondoros.codemetrics.inlay;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

public class EditorListener implements ProjectComponent {
  private Project project;

  public EditorListener(Project project) {
    this.project = project;
  }

  @Override
  public void projectOpened() {
    InlayListenerManager handler = new InlayListenerManager(project);
    project
        .getMessageBus()
        .connect()
        .subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, handler);
    ProjectManager.getInstance().addProjectManagerListener(project, new ProjectManagerListener() {
      @Override
      public void projectClosing(@NotNull Project project) {
        handler.dispose();
      }
    });
  }

  @NotNull
  @Override
  public String getComponentName() {
    return "CodeMetrics.EditorListener";
  }
}
