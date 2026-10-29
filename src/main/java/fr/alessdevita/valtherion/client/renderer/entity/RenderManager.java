package fr.alessdevita.valtherion.client.renderer.entity;

import fr.alessdevita.valtherion.client.renderer.ItemRenderer;
import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.entity.bosses.DragonBoss;
import fr.alessdevita.valtherion.entity.living.Chicken;
import fr.alessdevita.valtherion.entity.living.Cow;
import fr.alessdevita.valtherion.entity.living.Pig;
import fr.alessdevita.valtherion.entity.living.Sheep;
import fr.alessdevita.valtherion.entity.living.Skeleton;
import fr.alessdevita.valtherion.entity.living.Zombie;
import fr.alessdevita.valtherion.client.entity.AbstractClientPlayer;
import fr.alessdevita.valtherion.entity.NPC;
import fr.alessdevita.valtherion.entity.living.Player;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

/** Registre et orchestrateur du rendu des entités. */
public class RenderManager {
    public static final RenderManager instance = new RenderManager();

    public ItemRenderer itemRenderer;
    public Entity livingPlayer;
    public double viewerPosX, viewerPosY, viewerPosZ;
    public float playerViewY, playerViewX;

    private final Map<Class<? extends Entity>, Render> entityRenderMap = new HashMap<>();
    private final Render fallback = new RenderEntity();

    private RenderManager() {
        RenderPlayer playerRenderer = new RenderPlayer();
        this.entityRenderMap.put(Player.class, playerRenderer);
        this.entityRenderMap.put(AbstractClientPlayer.class, playerRenderer);
        this.entityRenderMap.put(Zombie.class, new RenderZombie());
        this.entityRenderMap.put(Skeleton.class, new RenderSkeleton());
        this.entityRenderMap.put(Cow.class, new RenderCow());
        this.entityRenderMap.put(Pig.class, new RenderPig());
        this.entityRenderMap.put(Sheep.class, new RenderSheep());
        this.entityRenderMap.put(Chicken.class, new RenderChicken());
        this.entityRenderMap.put(DragonBoss.class, new RenderDragon());
        this.entityRenderMap.put(NPC.class, new RenderVillager());
    }

    public <T extends Entity> void register(Class<T> type, Render renderer) {
        if (type == null || renderer == null) return;
        this.entityRenderMap.put(type, renderer);
    }

    public Render getEntityRenderObject(Entity entity) {
        if (entity == null) return this.fallback;
        Class<?> type = entity.getClass();
        while (type != null && Entity.class.isAssignableFrom(type)) {
            Render renderer = this.entityRenderMap.get(type);
            if (renderer != null) return renderer;
            type = type.getSuperclass();
        }
        return this.fallback;
    }

    public void cacheActiveRenderInfo(Entity viewer, float partialTicks) {
        this.livingPlayer = viewer;
        if (viewer == null) return;
        Vector3f position = viewer.getPosition();
        this.viewerPosX = position.x;
        this.viewerPosY = position.y;
        this.viewerPosZ = position.z;
    }

    public boolean renderEntitySimple(Entity entity, float partialTicks) {
        if (entity == null) return false;
        Vector3f position = entity.getPosition();
        double x = position.x - this.viewerPosX;
        double y = position.y - this.viewerPosY;
        double z = position.z - this.viewerPosZ;
        return renderEntityWithPosYaw(entity, x, y, z, 0F, partialTicks);
    }

    public boolean renderEntityWithPosYaw(Entity entity, double x, double y, double z,
                                          float yaw, float partialTicks) {
        Render renderer = getEntityRenderObject(entity);
        if (renderer == null) return false;
        renderer.doRender(entity, x, y, z, yaw, partialTicks);
        return true;
    }
}
