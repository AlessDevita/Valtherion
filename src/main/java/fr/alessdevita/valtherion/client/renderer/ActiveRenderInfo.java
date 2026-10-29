package fr.alessdevita.valtherion.client.renderer;

import fr.alessdevita.valtherion.entity.Entity;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

/** Snapshot des matrices et de la position de la caméra active. */
public final class ActiveRenderInfo {
    private static final FloatBuffer MODEL_VIEW = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer PROJECTION = BufferUtils.createFloatBuffer(16);
    private static final float[] MODEL_VIEW_VALUES = new float[16];
    private static final float[] PROJECTION_VALUES = new float[16];
    private static float posX, posY, posZ;
    private static float rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ;

    private ActiveRenderInfo() { }

    public static void updateRenderInfo(Entity viewEntity, boolean thirdPerson) {
        MODEL_VIEW.clear();
        PROJECTION.clear();
        GL11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, MODEL_VIEW);
        GL11.glGetFloatv(GL11.GL_PROJECTION_MATRIX, PROJECTION);
        for (int i = 0; i < 16; i++) {
            MODEL_VIEW_VALUES[i] = MODEL_VIEW.get(i);
            PROJECTION_VALUES[i] = PROJECTION.get(i);
        }

        float scale = (float) Math.sqrt(
                MODEL_VIEW_VALUES[0] * MODEL_VIEW_VALUES[0]
                        + MODEL_VIEW_VALUES[1] * MODEL_VIEW_VALUES[1]
                        + MODEL_VIEW_VALUES[2] * MODEL_VIEW_VALUES[2]);
        if (scale == 0F) scale = 1F;
        float invert = thirdPerson ? -1F : 1F;
        rotationX = MODEL_VIEW_VALUES[2] / scale * invert;
        rotationZ = MODEL_VIEW_VALUES[6] / scale * invert;
        rotationYZ = MODEL_VIEW_VALUES[10] / scale * invert;
        rotationXY = MODEL_VIEW_VALUES[9] / scale;
        rotationXZ = MODEL_VIEW_VALUES[8] / scale;

        if (viewEntity != null) {
            Vector3f position = viewEntity.getPosition();
            posX = position.x;
            posY = position.y;
            posZ = position.z;
        }
    }

    public static float[] getModelViewMatrix() { return MODEL_VIEW_VALUES.clone(); }
    public static float[] getProjectionMatrix() { return PROJECTION_VALUES.clone(); }
    public static float getPositionX() { return posX; }
    public static float getPositionY() { return posY; }
    public static float getPositionZ() { return posZ; }
    public static float getRotationX() { return rotationX; }
    public static float getRotationZ() { return rotationZ; }
    public static float getRotationYZ() { return rotationYZ; }
    public static float getRotationXY() { return rotationXY; }
    public static float getRotationXZ() { return rotationXZ; }

    /** Projette un point monde vers l'espace écran (x, y, profondeur normalisée). */
    public static Vector3f projectWorldPosOntoScreen(double worldX, double worldY, double worldZ) {
        float[] clip = new float[4];
        for (int row = 0; row < 4; row++) {
            clip[row] = (float) (
                    MODEL_VIEW_VALUES[row] * worldX
                            + MODEL_VIEW_VALUES[4 + row] * worldY
                            + MODEL_VIEW_VALUES[8 + row] * worldZ
                            + MODEL_VIEW_VALUES[12 + row]);
        }
        float[] ndc = new float[4];
        for (int row = 0; row < 4; row++) {
            ndc[row] = PROJECTION_VALUES[row] * clip[0]
                    + PROJECTION_VALUES[4 + row] * clip[1]
                    + PROJECTION_VALUES[8 + row] * clip[2]
                    + PROJECTION_VALUES[12 + row] * clip[3];
        }
        if (ndc[3] == 0F) return new Vector3f(0F, 0F, 0F);
        float invW = 1F / ndc[3];
        return new Vector3f(ndc[0] * invW * 0.5F + 0.5F, ndc[1] * invW * 0.5F + 0.5F, ndc[2] * invW);
    }
}
