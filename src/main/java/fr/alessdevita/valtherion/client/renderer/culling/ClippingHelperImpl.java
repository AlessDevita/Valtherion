package fr.alessdevita.valtherion.client.renderer.culling;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

/** Lit les matrices OpenGL et en extrait les plans de la caméra. */
public final class ClippingHelperImpl extends ClippingHelper {
    private static final ClippingHelperImpl INSTANCE = new ClippingHelperImpl();
    private final FloatBuffer projection = BufferUtils.createFloatBuffer(16);
    private final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);

    private ClippingHelperImpl() { }
    public static ClippingHelperImpl getInstance() { return INSTANCE; }
    public static ClippingHelper getClippingHelper() { return INSTANCE; }

    public ClippingHelperImpl init() {
        this.projection.clear();
        this.modelView.clear();
        GL11.glGetFloatv(GL11.GL_PROJECTION_MATRIX, this.projection);
        GL11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, this.modelView);
        float[] clip = multiply(this.projection, this.modelView);
        setPlane(0, clip[3] - clip[0], clip[7] - clip[4], clip[11] - clip[8], clip[15] - clip[12]);
        setPlane(1, clip[3] + clip[0], clip[7] + clip[4], clip[11] + clip[8], clip[15] + clip[12]);
        setPlane(2, clip[3] + clip[1], clip[7] + clip[5], clip[11] + clip[9], clip[15] + clip[13]);
        setPlane(3, clip[3] - clip[1], clip[7] - clip[5], clip[11] - clip[9], clip[15] - clip[13]);
        setPlane(4, clip[3] - clip[2], clip[7] - clip[6], clip[11] - clip[10], clip[15] - clip[14]);
        setPlane(5, clip[3] + clip[2], clip[7] + clip[6], clip[11] + clip[10], clip[15] + clip[14]);
        return this;
    }

    private static float[] multiply(FloatBuffer left, FloatBuffer right) {
        float[] result = new float[16];
        for (int column = 0; column < 4; column++) {
            for (int row = 0; row < 4; row++) {
                result[column * 4 + row] = left.get(row) * right.get(column * 4)
                        + left.get(4 + row) * right.get(column * 4 + 1)
                        + left.get(8 + row) * right.get(column * 4 + 2)
                        + left.get(12 + row) * right.get(column * 4 + 3);
            }
        }
        return result;
    }
}
