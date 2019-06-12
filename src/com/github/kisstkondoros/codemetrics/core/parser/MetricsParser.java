package com.github.kisstkondoros.codemetrics.core.parser;

import com.github.kisstkondoros.codemetrics.core.MetricsModel;
import com.intellij.psi.PsiElement;

public class MetricsParser {

    public MetricsModel getMetrics(PsiElement root) {
        TreeWalker treeWalker = new TreeWalker();
        return treeWalker.walk(root);
    }
}
