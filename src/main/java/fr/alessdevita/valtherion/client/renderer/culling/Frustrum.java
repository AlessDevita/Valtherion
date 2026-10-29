package fr.alessdevita.valtherion.client.renderer.culling;

/** Camera frustum wrapper with a movable world-space origin. */
public class Frustrum implements ICamera {
    private final ClippingHelper clippingHelper;
    private double xPosition, yPosition, zPosition;

    public Frustrum() {
        this(ClippingHelperImpl.getInstance().init());
    }

    public Frustrum(ClippingHelper clippingHelper) {
        if (clippingHelper == null) throw new IllegalArgumentException("ClippingHelper absent.");
        this.clippingHelper = clippingHelper;
    }

    /** Relit les matrices OpenGL courantes avant un test de frustum. */
    public void init() {
        if (this.clippingHelper instanceof ClippingHelperImpl) {
            ((ClippingHelperImpl) this.clippingHelper).init();
        }
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.xPosition = x; this.yPosition = y; this.zPosition = z;
    }

    @Override
    public boolean isBoundingBoxInFrustum(double minX, double minY, double minZ,
                                          double maxX, double maxY, double maxZ) {
        return this.clippingHelper.isBoxInFrustum(minX - this.xPosition, minY - this.yPosition,
                minZ - this.zPosition, maxX - this.xPosition, maxY - this.yPosition, maxZ - this.zPosition);
    }

    public boolean isBoundingBoxInFrustum(fr.alessdevita.valtherion.physics.AABB box) {
        if (box == null) return false;
        org.joml.Vector3f min = box.getMin(), max = box.getMax();
        return isBoundingBoxInFrustum(min.x, min.y, min.z, max.x, max.y, max.z);
    }
}
