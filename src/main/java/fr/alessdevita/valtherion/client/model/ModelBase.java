package fr.alessdevita.valtherion.client.model;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.alessdevita.valtherion.entity.Entity;

public class ModelBase {
    /** Rotation 90° sur Y, appliquee sur les modeles enfants comme dans Minecraft. */
    public float onGround;
    public boolean isRiding;
    public boolean isChild = true;

    /** Taille d'un "pixel" de texture, 16x16 par defaut comme MC. */
    public int textureWidth = 64;
    public int textureHeight = 32;

    /** Liste des cubes du modele. */
    protected List<ModelBox> cubeList = Lists.newArrayList();
    private final Map<String, TextureOffset> modelTextureMap = Maps.newHashMap();
    public List<ModelRenderer> boxList = Lists.newArrayList();

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float age,
                       float headYaw, float headPitch, float scale)
    {
        for (ModelRenderer modelrenderer : this.boxList)
        {
            modelrenderer.render(scale);
        }
    }

    /**
     * Copie les rotations d'un modele vers un autre (utilise pour les armures).
     */
    public void copyModelAnglesTo(ModelBase target)
    {
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float age,
                                  float headYaw, float headPitch, float scale, Entity entity)
    {
    }

    /**
     * Enregistre l'emplacement de texture d'une piece, identique a setTextureOffset.
     */
    public void setTextureOffset(String partName, int x, int y)
    {
        this.modelTextureMap.put(partName, new TextureOffset(x, y));
    }

    public TextureOffset getTextureOffset(String partName)
    {
        return this.modelTextureMap.get(partName);
    }

    /**
     * Retourne une position aleatoire d'une box (utilise pour les particules de mort).
     */
    public ModelRenderer getRandomModelBox(Random rand)
    {
        return this.boxList.get(rand.nextInt(this.boxList.size()));
    }

    /**
     * Efface les allocations GL pour eviter les fuites memoire (display lists).
     */
    public void deleteDisplayLists()
    {
        for (ModelRenderer modelrenderer : this.boxList)
        {
            modelrenderer.deleteDisplayList();
        }
    }

    /** Conteneur d'un decalage de texture (classe interne comme dans MC). */
    public static class TextureOffset
    {
        public final int textureOffsetX;
        public final int textureOffsetY;

        public TextureOffset(int x, int y)
        {
            this.textureOffsetX = x;
            this.textureOffsetY = y;
        }
    }
}