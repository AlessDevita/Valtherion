package fr.alessdevita.valtherion.client.particle;

import fr.alessdevita.valtherion.client.multiplayer.WorldClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.util.concurrent.ThreadLocalRandom;

/** État et rendu communs à toutes les particules client. */
public class EntityFX {
    protected final WorldClient world;
    protected final Vector3f position = new Vector3f();
    protected final Vector3f previousPosition = new Vector3f();
    protected final Vector3f motion = new Vector3f();
    protected float red = 1F, green = 1F, blue = 1F, alpha = 1F;
    protected float particleScale = 0.12F;
    protected float gravity;
    protected float friction = 0.98F;
    protected int particleAge;
    protected int maxAge = 20;
    protected boolean expired;
    protected boolean noClip;

    public EntityFX() { this(null, 0, 0, 0, 0, 0, 0); }

    public EntityFX(WorldClient world, double x, double y, double z,
                    double motionX, double motionY, double motionZ) {
        this.world = world;
        this.position.set((float) x, (float) y, (float) z);
        this.previousPosition.set(this.position);
        this.motion.set((float) motionX, (float) motionY, (float) motionZ);
        this.maxAge = 12 + ThreadLocalRandom.current().nextInt(12);
    }

    public void onUpdate() {
        if (this.expired) return;
        this.previousPosition.set(this.position);
        this.motion.y -= this.gravity;
        this.position.add(this.motion);
        this.motion.mul(this.friction);
        if (++this.particleAge >= this.maxAge) this.expired = true;
        if (!this.noClip && this.world != null) {
            int x = floor(this.position.x);
            int y = floor(this.position.y);
            int z = floor(this.position.z);
            if (this.world.getBlock(x, y, z) != null && this.world.getBlock(x, y, z).isSolid()) {
                this.position.set(this.previousPosition);
                this.motion.mul(-0.25F, 0.25F, -0.25F);
            }
        }
    }

    /** Dessine un quad qui fait face à la caméra, à l'origine du monde. */
    public void renderParticle(float partialTicks) {
        if (this.expired || this.alpha <= 0F) return;
        float x = this.previousPosition.x + (this.position.x - this.previousPosition.x) * partialTicks;
        float y = this.previousPosition.y + (this.position.y - this.previousPosition.y) * partialTicks;
        float z = this.previousPosition.z + (this.position.z - this.previousPosition.z) * partialTicks;
        float life = Math.min(1F, (float) this.particleAge / Math.max(1, this.maxAge));
        float size = this.particleScale * (1F - life * 0.25F);

        float rightX, rightY, rightZ, upX, upY, upZ;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer matrix = stack.mallocFloat(16);
            GL11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, matrix);
            rightX = matrix.get(0); rightY = matrix.get(1); rightZ = matrix.get(2);
            upX = matrix.get(4); upY = matrix.get(5); upZ = matrix.get(6);
        }
        float rx = rightX * size, ry = rightY * size, rz = rightZ * size;
        float ux = upX * size, uy = upY * size, uz = upZ * size;

        GL11.glColor4f(this.red, this.green, this.blue, this.alpha * (1F - life));
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(x - rx - ux, y - ry - uy, z - rz - uz);
        GL11.glVertex3f(x - rx + ux, y - ry + uy, z - rz + uz);
        GL11.glVertex3f(x + rx + ux, y + ry + uy, z + rz + uz);
        GL11.glVertex3f(x + rx - ux, y + ry - uy, z + rz - uz);
        GL11.glEnd();
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    public boolean isDead() { return this.expired; }
    public void setDead() { this.expired = true; }
    public int getAge() { return this.particleAge; }
    public int getMaxAge() { return this.maxAge; }
    public Vector3f getPosition() { return new Vector3f(this.position); }
    public Vector3f getMotion() { return new Vector3f(this.motion); }
    public float getParticleScale() { return this.particleScale; }
    public void setParticleScale(float scale) { this.particleScale = Math.max(0F, scale); }
    public void setColor(float red, float green, float blue) {
        this.red = clamp(red); this.green = clamp(green); this.blue = clamp(blue);
    }
    public void setAlpha(float alpha) { this.alpha = clamp(alpha); }
    public void setMaxAge(int maxAge) { this.maxAge = Math.max(1, maxAge); }
    public void setGravity(float gravity) { this.gravity = gravity; }
    public void setNoClip(boolean noClip) { this.noClip = noClip; }

    private static float clamp(float value) { return Math.max(0F, Math.min(1F, value)); }
    private static int floor(float value) {
        int integer = (int) value;
        return value < integer ? integer - 1 : integer;
    }
}
