package fr.alessdevita.valtherion.world.gen;

import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.world.chunk.Chunk;

import java.util.Random;

/**
 * Classe de base pour la génération de structures dans le monde.
 * Définit l'interface commune pour les générateurs de grottes, ravins, etc.
 */
public abstract class MapGenBase {

    protected final Random random;
    protected World world;

    protected MapGenBase() {
        this.random = new Random();
    }

    /**
     * Initialise le générateur avec un monde.
     * @param world Monde cible
     */
    public void init(World world) {
        this.world = world;
    }

    /**
     * Génère la structure dans un chunk.
     * @param chunk Chunk à modifier
     * @param x Coordonnée X du chunk
     * @param z Coordonnée Z du chunk
     */
    public abstract void generate(Chunk chunk, int x, int z);

    public World getWorld() {
        return world;
    }
}
