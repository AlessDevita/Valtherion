package fr.alessdevita.valtherion.client.renderer.culling;

/** Six normalized clip planes extracted from the active projection matrix. */
public class ClippingHelper {
    private final float[][] planes = new float[6][4];

    public void setPlane(int index, float a, float b, float c, float d) {
        float length = (float) Math.sqrt(a * a + b * b + c * c);
        if (length == 0F) length = 1F;
        this.planes[index][0] = a / length;
        this.planes[index][1] = b / length;
        this.planes[index][2] = c / length;
        this.planes[index][3] = d / length;
    }

    public boolean isBoxInFrustum(double minX, double minY, double minZ,
                                  double maxX, double maxY, double maxZ) {
        for (float[] plane : this.planes) {
            double x = plane[0] >= 0 ? maxX : minX;
            double y = plane[1] >= 0 ? maxY : minY;
            double z = plane[2] >= 0 ? maxZ : minZ;
            if (plane[0] * x + plane[1] * y + plane[2] * z + plane[3] < 0) return false;
        }
        return true;
    }

    public float[][] getPlanes() {
        float[][] copy = new float[this.planes.length][4];
        for (int i = 0; i < this.planes.length; i++) System.arraycopy(this.planes[i], 0, copy[i], 0, 4);
        return copy;
    }
}
