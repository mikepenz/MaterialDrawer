package com.mikepenz.materialdrawer.util;

import android.graphics.Color;

/**
 * Created by mikepenz on 22.04.14.
 */
public class ColorUtil {
    public static int darkenColor(int color, float darkenBy) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= darkenBy;
        color = Color.HSVToColor(hsv);
        return color;
    }

    public static int lightUpnColor(int color, float lightUpBy) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = 1.0f - lightUpBy * (1.0f - hsv[2]);
        color = Color.HSVToColor(hsv);
        return color;
    }

    public static boolean textContrast(int color) {
        byte R = (byte) (color & 0x000000FF);
        byte G = (byte) ((color & 0x0000FF00) >> 8);
        byte B = (byte) ((color & 0x00FF0000) >> 16);
        byte A = (byte) ((color & 0xFF000000) >> 24);

        return (R * 0.299 + G * 0.587 + B * 0.114) > 186;
    }
}
