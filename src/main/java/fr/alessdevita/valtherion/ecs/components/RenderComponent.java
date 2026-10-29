package fr.alessdevita.valtherion.ecs.components;

import fr.alessdevita.valtherion.ecs.Component;
import fr.alessdevita.valtherion.ecs.Entity;
import fr.alessdevita.valtherion.rendering.Mesh;
import fr.alessdevita.valtherion.rendering.Material;
import fr.alessdevita.valtherion.rendering.Texture;

import org.joml.Vector4f;

/**
 * Composant pour le rendu d'une entité.
 */
public class RenderComponent implements Component {

    // Mesh de l'entité
    private Mesh mesh;

    // Matériau de l'entité
    private Material material;

    // Texture de l'entité
    private Texture texture;

    // Couleur de l'entité
    private Vector4f color;

    // Indique si l'entité est visible
    private boolean isVisible;

    // Entité associée
    private Entity entity;

    // Type de rendu (MESH, SPRITE, PARTICLE, etc.)
    private RenderType renderType;

    // Énumération des types de rendu
    public enum RenderType {
        MESH,      // Rendu 3D avec un maillage
        SPRITE,    // Rendu 2D avec un sprite
        PARTICLE,  // Rendu de particule
        TEXT,      // Rendu de texte
        LINE,      // Rendu de ligne
        POINT      // Rendu de point
    }

    /**
     * Constructeur du composant de rendu.
     */
    public RenderComponent() {
        this.mesh = null;
        this.material = null;
        this.texture = null;
        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f); // Blanc par défaut
        this.isVisible = true;
        this.renderType = RenderType.MESH;
    }

    /**
     * Constructeur du composant de rendu avec un mesh.
     * @param mesh Mesh de l'entité.
     */
    public RenderComponent(Mesh mesh) {
        this();
        this.mesh = mesh;
    }

    /**
     * Constructeur du composant de rendu avec un mesh et un matériau.
     * @param mesh Mesh de l'entité.
     * @param material Matériau de l'entité.
     */
    public RenderComponent(Mesh mesh, Material material) {
        this(mesh);
        this.material = material;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void cleanup() {
        if (mesh != null) {
            mesh.cleanup();
        }
        if (material != null) {
            material.cleanup();
        }
        if (texture != null) {
            texture.cleanup();
        }
    }

    // --- Getters et Setters ---

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public RenderType getRenderType() {
        return renderType;
    }

    public void setRenderType(RenderType renderType) {
        this.renderType = renderType;
    }

    /**
     * Retourne une représentation textuelle du composant.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "RenderComponent{" +
                "mesh=" + (mesh != null ? mesh.getClass().getSimpleName() : "null") +
                ", material=" + (material != null ? material.getName() : "null") +
                ", texture=" + (texture != null ? texture.getPath() : "null") +
                ", color=" + color +
                ", isVisible=" + isVisible +
                '}';
    }
}