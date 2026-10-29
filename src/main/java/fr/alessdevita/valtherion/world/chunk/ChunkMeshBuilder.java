package fr.alessdevita.valtherion.world.chunk;

import fr.alessdevita.valtherion.block.Block;
import fr.alessdevita.valtherion.block.BlockType;
import org.joml.Vector3f;
import java.util.*;

/**
 * Construit le mesh visuel 3D d'un chunk pour le rendu.
 */
public class ChunkMeshBuilder {
    private Chunk chunk;
    private List<Float> vertices;
    private List<Integer> indices;
    private List<Float> textureCoordinates;

    /**
     * Crée un constructeur de mesh pour un chunk.
     * @param chunk Chunk à maillifier
     */
    public ChunkMeshBuilder(Chunk chunk) {
        this.chunk = chunk;
        this.vertices = new ArrayList<>();
        this.indices = new ArrayList<>();
        this.textureCoordinates = new ArrayList<>();
    }

    /**
     * Construit le mesh du chunk.
     * @return true si le mesh a été construit avec succès
     */
    public boolean buildMesh() {
        vertices.clear();
        indices.clear();
        textureCoordinates.clear();

        for (int x = 0; x < Chunk.SIZE; x++) {
            for (int y = 0; y < Chunk.HEIGHT; y++) {
                for (int z = 0; z < Chunk.SIZE; z++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block != null && !block.getType().equals(BlockType.AIR)) {
                        addBlockFaces(x, y, z, block);
                    }
                }
            }
        }

        return !vertices.isEmpty();
    }

    /**
     * Ajoute les faces visibles d'un bloc au mesh.
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     * @param block Bloc à ajouter
     */
    private void addBlockFaces(int x, int y, int z, Block block) {
        Vector3f pos = new Vector3f(x, y, z);

        // Face avant (z+)
        if (shouldAddFace(x, y, z + 1)) {
            addFace(pos, 0);
        }
        // Face arrière (z-)
        if (shouldAddFace(x, y, z - 1)) {
            addFace(pos, 1);
        }
        // Face droite (x+)
        if (shouldAddFace(x + 1, y, z)) {
            addFace(pos, 2);
        }
        // Face gauche (x-)
        if (shouldAddFace(x - 1, y, z)) {
            addFace(pos, 3);
        }
        // Face haut (y+)
        if (shouldAddFace(x, y + 1, z)) {
            addFace(pos, 4);
        }
        // Face bas (y-)
        if (shouldAddFace(x, y - 1, z)) {
            addFace(pos, 5);
        }
    }

    /**
     * Détermine si une face doit être ajoutée.
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     * @return true si la face est visible
     */
    private boolean shouldAddFace(int x, int y, int z) {
        if (y < 0 || y >= Chunk.HEIGHT) {
            return true;
        }
        if (x < 0 || x >= Chunk.SIZE || z < 0 || z >= Chunk.SIZE) {
            return true;
        }
        Block neighbor = chunk.getBlock(x, y, z);
        return neighbor == null || neighbor.getType().equals(BlockType.AIR);
    }

    /**
     * Ajoute une face de cube au mesh.
     * @param pos Position du bloc
     * @param face Indice de la face (0-5)
     */
    private void addFace(Vector3f pos, int face) {
        int baseIndex = vertices.size() / 3;
        float x = pos.x();
        float y = pos.y();
        float z = pos.z();

        switch (face) {
            case 0: // Front (z+)
                addVertex(x, y, z + 1);
                addVertex(x + 1, y, z + 1);
                addVertex(x + 1, y + 1, z + 1);
                addVertex(x, y + 1, z + 1);
                break;
            case 1: // Back (z-)
                addVertex(x + 1, y, z);
                addVertex(x, y, z);
                addVertex(x, y + 1, z);
                addVertex(x + 1, y + 1, z);
                break;
            case 2: // Right (x+)
                addVertex(x + 1, y, z);
                addVertex(x + 1, y, z + 1);
                addVertex(x + 1, y + 1, z + 1);
                addVertex(x + 1, y + 1, z);
                break;
            case 3: // Left (x-)
                addVertex(x, y, z + 1);
                addVertex(x, y, z);
                addVertex(x, y + 1, z);
                addVertex(x, y + 1, z + 1);
                break;
            case 4: // Top (y+)
                addVertex(x, y + 1, z + 1);
                addVertex(x + 1, y + 1, z + 1);
                addVertex(x + 1, y + 1, z);
                addVertex(x, y + 1, z);
                break;
            case 5: // Bottom (y-)
                addVertex(x, y, z);
                addVertex(x + 1, y, z);
                addVertex(x + 1, y, z + 1);
                addVertex(x, y, z + 1);
                break;
        }

        // Ajouter les indices pour les deux triangles
        indices.add(baseIndex);
        indices.add(baseIndex + 1);
        indices.add(baseIndex + 2);
        indices.add(baseIndex);
        indices.add(baseIndex + 2);
        indices.add(baseIndex + 3);

        // Ajouter les coordonnées de texture
        for (int i = 0; i < 4; i++) {
            textureCoordinates.add((i % 2) == 0 ? 0.0f : 1.0f);
            textureCoordinates.add((i / 2) == 0 ? 0.0f : 1.0f);
        }
    }

    /**
     * Ajoute un vertex au mesh.
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param z Coordonnée Z
     */
    private void addVertex(float x, float y, float z) {
        vertices.add(x);
        vertices.add(y);
        vertices.add(z);
    }

    /**
     * Retourne les vertices du mesh.
     * @return Liste des vertices
     */
    public List<Float> getVertices() {
        return vertices;
    }

    /**
     * Retourne les indices du mesh.
     * @return Liste des indices
     */
    public List<Integer> getIndices() {
        return indices;
    }

    /**
     * Retourne les coordonnées de texture.
     * @return Liste des coordonnées de texture
     */
    public List<Float> getTextureCoordinates() {
        return textureCoordinates;
    }

    /**
     * Retourne le nombre de faces du mesh.
     * @return Nombre de faces
     */
    public int getFaceCount() {
        return indices.size() / 6;
    }
}
