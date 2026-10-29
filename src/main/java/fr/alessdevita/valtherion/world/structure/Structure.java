package fr.alessdevita.valtherion.world.structure;

import fr.alessdevita.valtherion.world.World;
import org.joml.Vector3f;

/**
 * Classe de base abstraite pour les structures générées.
 * Représente les bâtiments, donjons, villages, etc.
 */
public abstract class Structure {
    protected String name;
    protected int width;
    protected int height;
    protected int depth;
    protected Vector3f position;

    /**
     * Crée une structure.
     * @param name Nom de la structure
     * @param width Largeur de la structure
     * @param height Hauteur de la structure
     * @param depth Profondeur de la structure
     */
    public Structure(String name, int width, int height, int depth) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.position = new Vector3f();
    }

    /**
     * Génère la structure dans le monde.
     * @param world Monde où générer la structure
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     */
    public abstract void generate(World world, int x, int y, int z);

    /**
     * Retourne le nom de la structure.
     * @return Nom
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne la largeur de la structure.
     * @return Largeur
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retourne la hauteur de la structure.
     * @return Hauteur
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retourne la profondeur de la structure.
     * @return Profondeur
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Retourne la position de la structure.
     * @return Position
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Définit la position de la structure.
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     */
    public void setPosition(int x, int y, int z) {
        this.position.set(x, y, z);
    }

    /**
     * Retourne le volume de la structure.
     * @return Volume
     */
    public int getVolume() {
        return width * height * depth;
    }
}