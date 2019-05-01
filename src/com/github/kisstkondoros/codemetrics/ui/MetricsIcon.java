package com.github.kisstkondoros.codemetrics.ui;

import com.github.kisstkondoros.codemetrics.core.config.MetricsConfiguration;
import com.intellij.ui.ColorUtil;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

import static com.github.kisstkondoros.codemetrics.ui.ColorContrastUtil.getContrastColor;

public class MetricsIcon implements Icon {
    private final String summary;
    private final Color fontColor;
    private final Color color;
    private final int width;
    private final int height;

    public MetricsIcon(long summary, int size) {
        this.summary = summary + "";
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
        Color extremeFontColor = getContrastColor(extremeColor);
        Color highColor = new Color(configuration.complexityColorHigh, true);
        Color highFontColor = getContrastColor(highColor);
        Color normalColor = new Color(configuration.complexityColorNormal, true);
        Color normalFontColor = getContrastColor(normalColor);
        Color lowColor = new Color(configuration.complexityColorLow, true);
        Color lowFontColor = getContrastColor(lowColor);

        if (summary >= complexityLevelExtreme) {
            double balance = (summary - complexityLevelHigh) / (double) (complexityLevelExtreme - complexityLevelHigh);
            color = ColorUtil.mix(highColor, extremeColor, balance);
            fontColor = ColorUtil.mix(highFontColor, extremeFontColor, balance);
        } else if (summary >= complexityLevelHigh) {
            double balance = (summary - complexityLevelNormal) / (double) (complexityLevelHigh - complexityLevelNormal);
            color = ColorUtil.mix(normalColor, highColor, balance);
            fontColor = ColorUtil.mix(normalFontColor, highFontColor, balance);
        } else if (summary >= complexityLevelNormal) {
            double balance = (summary - complexityLevelLow) / (double) (complexityLevelNormal - complexityLevelLow);
            color = ColorUtil.mix(lowColor, normalColor, balance);
            fontColor = ColorUtil.mix(lowFontColor, normalFontColor, balance);
        } else {
            color = lowColor;
            fontColor = lowFontColor;
        }

        this.width = size;
        this.height = size;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        UIUtil.applyRenderingHints(g);

        Font originalFont = g.getFont();
        Color originalColor = g.getColor();
        g.setColor(color);
        g.fillRect(x, y, width, height);

        final Font font = originalFont.deriveFont(Font.BOLD).deriveFont((float) Math.floor(getIconHeight() * 3 / 5d));

        g.setFont(font);
        y += getIconHeight() - g.getFontMetrics().getDescent();
        x += (width - g.getFontMetrics(font).stringWidth(summary)) / 2;

        g.setColor(fontColor);
        g.drawString(summary, x, y);

        g.setFont(originalFont);
        g.setColor(originalColor);
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}