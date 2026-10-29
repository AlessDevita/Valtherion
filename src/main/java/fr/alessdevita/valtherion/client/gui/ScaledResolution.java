package fr.alessdevita.valtherion.client.gui;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.utils.MathHelper;

/**
 * ScaledResolution — conversion pixels reels -> pixels GUI selon le
 * scale factor du GameSettings (identique MC 1.7/1.8).
 */
public class ScaledResolution
{
    private final double scaledWidthD;
    private final double scaledHeightD;
    private int scaledWidth;
    private int scaledHeight;
    private int scaleFactor;

    public ScaledResolution(Valtherion valtherion, int width, int height)
    {
        this.scaleFactor = 1;
        int guiScale = valtherion.gameSettings.guiScale;
        boolean flag = valtherion.isJava64bit();
        int max = flag ? 512 : 256;
        guiScale = MathHelper.clamp_int(guiScale, 0, max);

        if (guiScale == 0)
        {
            guiScale = 1000;
        }

        while (this.scaleFactor < guiScale
                && this.scaleFactor < width && this.scaleFactor < height
                && width / (this.scaleFactor + 1) >= 320
                && height / (this.scaleFactor + 1) >= 240)
        {
            ++this.scaleFactor;
        }

        // GUI scale "auto" ne doit jamais depasser 4 en pratique
        this.scaledWidth = width / this.scaleFactor;
        this.scaledHeight = height / this.scaleFactor;
        this.scaledWidthD = (double) width / (double) this.scaleFactor;
        this.scaledHeightD = (double) height / (double) this.scaleFactor;
    }

    public int getScaledWidth()
    {
        return this.scaledWidth;
    }

    public int getScaledHeight()
    {
        return this.scaledHeight;
    }

    public double getScaledWidthDouble()
    {
        return this.scaledWidthD;
    }

    public double getScaledHeightDouble()
    {
        return this.scaledHeightD;
    }

    public int getScaleFactor()
    {
        return this.scaleFactor;
    }
}