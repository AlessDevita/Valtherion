package fr.alessdevita.valtherion.client.renderer.culling;

public interface ICamera {
    void setPosition(double x, double y, double z);
    boolean isBoundingBoxInFrustum(double minX, double minY, double minZ,
                                   double maxX, double maxY, double maxZ);
}
