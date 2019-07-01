package com.github.kisstkondoros.codemetrics.core.parser;

import com.github.kisstkondoros.codemetrics.core.CollectorType;
import com.github.kisstkondoros.codemetrics.core.MetricsModel;
import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

import java.util.Arrays;

import static com.github.kisstkondoros.codemetrics.core.CollectorType.MAX;
import static com.github.kisstkondoros.codemetrics.core.CollectorType.SUM;

public class TreeWalker {

  private MetricsConfiguration configuration;

  TreeWalker() {
    configuration = MetricsConfiguration.getInstance();
  }

  public MetricsModel walk(PsiElement node) {
    MetricsModel model = visit(node, 0, "Collector", false);
    visitNode(node, model);
    return model;
  }

  public void visitNode(PsiElement element, MetricsModel parent) {
    MetricsModel model = getMetrics(element);
    MetricsModel updatedParent = parent;
    if (model != null) {
      parent.getChildren().add(model);
      if (model.isVisible()) {
        updatedParent = model;
      }
    }
    walkChildren(element, updatedParent);
  }

  private void walkChildren(PsiElement element, MetricsModel parent) {
    Arrays.stream(element.getChildren()).forEach(node -> this.visitNode(node, parent));
  }

  private MetricsModel getMetrics(PsiElement element) {
    MetricsModel model = null;
    if (element != null) {
      ASTNode node = element.getNode();
      if (node != null) {
        IElementType elementType = node.getElementType();

        ComplexityHandler handler = HandlerRegistry.get(elementType);

        if (handler != null) {
          model =
              handler
                  .forConfig(configuration)
                  .andThen(p -> visit(element, p.getIncrement(), p.getDescription(), p.isVisible()))
                  .apply(element);
        }
      }
    }
    return model;
  }

  private MetricsModel visit(PsiElement node, int complexity, String description, boolean visible) {
    CollectorType collectorType = node instanceof PsiClass ? MAX : SUM;
    return new MetricsModel(node, complexity, description, true, visible, collectorType);
  }
}
