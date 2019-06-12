package com.github.kisstkondoros.codemetrics.util;

import java.awt.*;

public class ColorContrastUtil {

    public static Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000.0;
        return y >= 128 ? Color.black : Color.white;
    }
}
