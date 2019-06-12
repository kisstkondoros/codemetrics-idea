package com.github.kisstkondoros.codemetrics.inlay;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class InlayInstaller implements ProjectComponent {
    private Project project;

    public InlayInstaller(Project project) {
        this.project = project;
    }

    @Override
    public void projectOpened() {
        project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,
                new InlayManager(project));
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "CodeMetrics.InlayInstaller";
    }
}
