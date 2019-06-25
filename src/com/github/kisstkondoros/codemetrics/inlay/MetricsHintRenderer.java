package com.github.kisstkondoros.codemetrics.inlay;

import com.github.kisstkondoros.codemetrics.core.MetricsModel;
import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.github.kisstkondoros.codemetrics.util.ComplexityColorUtil;
import com.intellij.codeInsight.daemon.impl.HintRenderer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.ColorUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;

import static com.github.kisstkondoros.codemetrics.util.ColorContrastUtil.getContrastColor;

public class MetricsHintRenderer extends HintRenderer {
  private MetricsModel model;
  private boolean highlighted;

  MetricsHintRenderer(MetricsModel model) {
    super(model.toString(MetricsConfiguration.getInstance()));
    this.model = model;
  }

  @Nullable
  protected TextAttributes getTextAttributes(@NotNull Editor editor) {
    TextAttributesKey textAttributesKey =
        this.highlighted
            ? DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT_HIGHLIGHTED
            : DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT;

    TextAttributes clone = editor.getColorsScheme().getAttributes(textAttributesKey).clone();

    ComplexityColorUtil.ComplexityColorScheme colorSchemeForComplexity =
        ComplexityColorUtil.getColorSchemeForComplexity(model.getCollectedComplexity());
    int backgroundAlpha = colorSchemeForComplexity.getColor().getAlpha();

    Color backgroundColor = colorSchemeForComplexity.getColor();

    double highlightMultiplier = this.highlighted ? 1 : 0.8d;

    Color defaultBackground = editor.getColorsScheme().getDefaultBackground();
    Color backgroundColorWithAlpha =
        new Color(
            backgroundColor.getRed(),
            backgroundColor.getGreen(),
            backgroundColor.getBlue(),
            (int) (backgroundAlpha * highlightMultiplier));

    Color fontColor =
        getContrastColor(
            ColorUtil.mix(defaultBackground, backgroundColorWithAlpha, highlightMultiplier));
    clone.setForegroundColor(
        new Color(
            fontColor.getRed(),
            fontColor.getGreen(),
            fontColor.getBlue(),
            (int) (fontColor.getAlpha() * highlightMultiplier)));
    clone.setBackgroundColor(backgroundColorWithAlpha);

    return clone;
  }

  @Nullable
  public String getContextMenuGroupId(@NotNull Inlay inlay) {
    return "CodeMetricsHints";
  }

  public void setHighlighted(boolean highlighted) {
    this.highlighted = highlighted;
  }

  public MetricsModel getModel() {
    return model;
  }
}
