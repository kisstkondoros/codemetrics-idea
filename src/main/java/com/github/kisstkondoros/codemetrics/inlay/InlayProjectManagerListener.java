package com.github.kisstkondoros.codemetrics.inlay;

import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class InlayProjectManagerListener implements StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {
        InlayManager inlayManager = new InlayManager();
        InlayHighlighter inlayHighlighter = new InlayHighlighter(project, inlayManager);
        InlayFileEditorManagerListener handler = new InlayFileEditorManagerListener(project, inlayManager, inlayHighlighter);
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
}
