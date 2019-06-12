package com.github.kisstkondoros.codemetrics.util;

import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.intellij.ui.ColorUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

import static com.github.kisstkondoros.codemetrics.util.ColorContrastUtil.getContrastColor;

public final class ComplexityColorUtil {

    public static ComplexityColorScheme getColorSchemeForComplexity(long summary) {
        Color fontColor;
        Color color;
        MetricsConfiguration configuration = MetricsConfiguration.getInstance();
        ArrayList<Integer> complexities = new ArrayList<>();
        complexities.add(configuration.complexityLevelLow);
        complexities.add(configuration.complexityLevelNormal);
        complexities.add(configuration.complexityLevelHigh);
        complexities.add(configuration.complexityLevelExtreme);
        complexities.sort(Comparator.naturalOrder());

        Integer complexityLevelLow = complexities.get(0);
        Integer complexityLevelNormal = complexities.get(1);
        Integer complexityLevelHigh = complexities.get(2);
        Integer complexityLevelExtreme = complexities.get(3);

        Color extremeColor = new Color(configuration.complexityColorExtreme, true);
        Color highColor = new Color(configuration.complexityColorHigh, true);
        Color normalColor = new Color(configuration.complexityColorNormal, true);
        Color lowColor = new Color(configuration.complexityColorLow, true);

        if (summary >= complexityLevelExtreme) {
            double balance = (summary - complexityLevelHigh) / (double) (complexityLevelExtreme - complexityLevelHigh);
            color = ColorUtil.mix(extremeColor, highColor, 1d / balance);
        } else if (summary >= complexityLevelHigh) {
            double balance = (summary - complexityLevelNormal) / (double) (complexityLevelHigh - complexityLevelNormal);
            color = ColorUtil.mix(highColor, normalColor, 1d / balance);
        } else if (summary >= complexityLevelNormal) {
            double balance = (summary - complexityLevelLow) / (double) (complexityLevelNormal - complexityLevelLow);
            color = ColorUtil.mix(normalColor, lowColor, 1d / balance);
        } else {
            color = lowColor;
        }
        fontColor = getContrastColor(color);
        return new ComplexityColorScheme(color, fontColor);
    }

    public static class ComplexityColorScheme {
        private Color color;
        private Color fontColor;

        public ComplexityColorScheme(Color color, Color fontColor) {
            this.color = color;
            this.fontColor = fontColor;
        }

        public Color getColor() {
            return color;
        }

        public Color getFontColor() {
            return fontColor;
        }
    }
}
