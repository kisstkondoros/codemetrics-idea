package com.github.kisstkondoros.codemetrics.inlay;

import com.github.kisstkondoros.codemetrics.core.MetricsModel;
import com.github.kisstkondoros.codemetrics.util.NavigatingPicker;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseEventArea;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class InlayHighlighter {
  private final Project project;

  public InlayHighlighter(Project project) {
    this.project = project;
  }

  public void installInlayHighlighter(Editor editor, VirtualFile file) {
    editor.addEditorMouseMotionListener(
        new EditorMouseMotionListener() {
          WeakReference<Inlay<? extends MetricsHintRenderer>> previousHighlighted =
              new WeakReference<>(null);

          @Override
          public void mouseMoved(@NotNull EditorMouseEvent event) {

            Inlay<? extends MetricsHintRenderer> previousHighlightedInlay =
                previousHighlighted.get();

            if (previousHighlightedInlay != null) {
              previousHighlightedInlay.getRenderer().setHighlighted(false);
              previousHighlighted = new WeakReference<>(null);
            }

            executeWithInlayUnderPointer(
                editor,
                event,
                inlay -> {
                  previousHighlighted = new WeakReference<>(inlay);
                  inlay.getRenderer().setHighlighted(true);
                });
            if (previousHighlightedInlay != previousHighlighted.get()) {
              editor.getContentComponent().repaint();
            }
          }
        });
    editor.addEditorMouseListener(
        new EditorMouseListener() {
          @Override
          public void mouseClicked(@NotNull EditorMouseEvent event) {
            if (event.getArea().equals(EditorMouseEventArea.EDITING_AREA)) {

              Consumer<Inlay<? extends MetricsHintRenderer>> openPicker =
                  p -> {
                    MetricsModel model = p.getRenderer().getModel();
                    RelativePoint point = new RelativePoint(event.getMouseEvent());
                    NavigatingPicker.showPicker(point, project, file, model);
                  };

              executeWithInlayUnderPointer(editor, event, openPicker);
            }
          }
        });
  }

  private boolean containsPosition(Rectangle bounds, Point point) {
    return bounds.contains(point.getX(), point.getY());
  }

  private void executeWithInlayUnderPointer(
      Editor editor,
      EditorMouseEvent event,
      Consumer<Inlay<? extends MetricsHintRenderer>> openPicker) {
    InlayManager inlayManager = project.getComponent(InlayManager.class);
    List<Inlay<? extends MetricsHintRenderer>> inlays = inlayManager.getInlays(editor);

    Optional<Inlay<? extends MetricsHintRenderer>> firstInlay =
        inlays.stream()
            .filter(p -> p.getBounds() != null)
            .filter(p -> containsPosition(p.getBounds(), event.getMouseEvent().getPoint()))
            .findFirst();
    firstInlay.ifPresent(openPicker);
  }
}
