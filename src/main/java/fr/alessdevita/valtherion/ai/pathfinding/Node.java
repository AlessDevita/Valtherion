package fr.alessdevita.valtherion.ai.pathfinding;

/**
 * Représente un nœud dans le graphe de pathfinding.
 * Utilisé par l'algorithme A* pour trouver des chemins.
 */
public class Node {

    // Coordonnées du nœud
    private final int x;
    private final int y;
    private final int z;

    // Coûts pour l'algorithme A*
    private int g; // Coût depuis le départ
    private int h; // Heuristique (distance estimée jusqu'à l'arrivée)
    private int f; // Coût total (g + h)

    // Parent du nœud (pour reconstruire le chemin)
    private Node parent;

    /**
     * Constructeur d'un nœud.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public Node(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.g = 0;
        this.h = 0;
        this.f = 0;
        this.parent = null;
    }

    // --- Getters et Setters ---

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    // --- Méthodes utilitaires ---

    /**
     * Vérifie si ce nœud est égal à un autre.
     * @param obj Objet à comparer.
     * @return true si les nœuds sont égaux, false sinon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Node other = (Node) obj;
        return x == other.x && y == other.y && z == other.z;
    }

    /**
     * Retourne le hash code du nœud.
     * @return Hash code.
     */
    @Override
    public int hashCode() {
        return 31 * (31 * x + y) + z;
    }

    /**
     * Retourne une représentation textuelle du nœud.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Node{(" + x + ", " + y + ", " + z + "), g=" + g + ", h=" + h + ", f=" + f + "}";
    }
}