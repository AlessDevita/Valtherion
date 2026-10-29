package fr.alessdevita.valtherion.client.renderer.texture;

import fr.alessdevita.valtherion.client.renderer.StitcherException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Empaquete des sprites dans une atlas de taille puissance de deux. */
public class Stitcher {
    private final int maxWidth;
    private final int maxHeight;
    private final List<Holder> holders = new ArrayList<>();
    private int currentWidth;
    private int currentHeight;

    public Stitcher(int maxWidth, int maxHeight) {
        this.maxWidth = Math.max(16, maxWidth);
        this.maxHeight = Math.max(16, maxHeight);
    }

    public void addSprite(TextureAtlasSprite sprite) {
        if (sprite == null) throw new IllegalArgumentException("Sprite absent.");
        this.holders.add(new Holder(sprite));
    }

    public void doStitch() {
        this.holders.sort(Comparator
                .comparingInt((Holder holder) -> holder.height * holder.width)
                .reversed());
        this.currentWidth = 0;
        this.currentHeight = 0;
        List<Slot> slots = new ArrayList<>();
        slots.add(new Slot(0, 0, this.maxWidth, this.maxHeight));

        for (Holder holder : this.holders) {
            Slot fit = null;
            for (Slot slot : slots) {
                if (slot.canFit(holder.width, holder.height)) {
                    fit = slot;
                    break;
                }
            }
            if (fit == null) {
                throw new StitcherException("Impossible d'assembler l'atlas : "
                        + holder.sprite.getIconName());
            }
            holder.x = fit.x;
            holder.y = fit.y;
            this.currentWidth = Math.max(this.currentWidth, fit.x + holder.width);
            this.currentHeight = Math.max(this.currentHeight, fit.y + holder.height);
            slots.remove(fit);
            slots.addAll(fit.split(holder.width, holder.height));
        }

        this.currentWidth = nextPowerOfTwo(this.currentWidth);
        this.currentHeight = nextPowerOfTwo(this.currentHeight);
        for (Holder holder : this.holders) {
            holder.sprite.setIconWidth(holder.width);
            holder.sprite.setIconHeight(holder.height);
            holder.sprite.initSprite(this.currentWidth, this.currentHeight, holder.x, holder.y, false);
        }
    }

    public int getCurrentWidth() { return this.currentWidth; }
    public int getCurrentHeight() { return this.currentHeight; }
    public List<TextureAtlasSprite> getStitchSlots() {
        List<TextureAtlasSprite> sprites = new ArrayList<>(this.holders.size());
        for (Holder holder : this.holders) sprites.add(holder.sprite);
        return sprites;
    }

    private static int nextPowerOfTwo(int value) {
        int power = 1;
        while (power < value) power <<= 1;
        return Math.max(16, power);
    }

    private static final class Holder {
        final TextureAtlasSprite sprite;
        final int width;
        final int height;
        int x, y;

        Holder(TextureAtlasSprite sprite) {
            this.sprite = sprite;
            this.width = Math.max(1, sprite.getIconWidth());
            this.height = Math.max(1, sprite.getIconHeight());
        }
    }

    private static final class Slot {
        final int x, y, width, height;

        Slot(int x, int y, int width, int height) {
            this.x = x; this.y = y; this.width = width; this.height = height;
        }

        boolean canFit(int w, int h) {
            return w <= this.width && h <= this.height;
        }

        List<Slot> split(int usedW, int usedH) {
            List<Slot> result = new ArrayList<>(2);
            int remW = this.width - usedW;
            int remH = this.height - usedH;
            if (remW > 0) result.add(new Slot(this.x + usedW, this.y, remW, usedH));
            if (remH > 0) result.add(new Slot(this.x, this.y + usedH, this.width, remH));
            return result;
        }
    }
}
