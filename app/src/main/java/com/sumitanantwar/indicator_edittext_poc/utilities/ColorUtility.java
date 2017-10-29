package com.sumitanantwar.indicator_edittext_poc.utilities;

import android.support.v4.graphics.ColorUtils;

/**
 * Created by Sumit Anantwar on 10/27/17.
 */

public class ColorUtility
{
    public static int lighterColor(int color)
    {
        float[] hslColor = new float[3];
        ColorUtils.colorToHSL(color, hslColor);
        hslColor[2] = Math.min((hslColor[2] * 1.3f), 1);

        return ColorUtils.HSLToColor(hslColor);
    }

    public static int darkerColor(int color)
    {
        float[] hslColor = new float[3];
        ColorUtils.colorToHSL(color, hslColor);
        hslColor[2] = Math.max((hslColor[2] * 0.5f), 0);

        return ColorUtils.HSLToColor(hslColor);
    }
}
