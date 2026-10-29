package fr.alessdevita.valtherion.ai.pathfinding;

import fr.alessdevita.valtherion.core.Logger;
import fr.alessdevita.valtherion.world.World;

import java.util.*;

/**
 * Implémentation de l'algorithme A* pour la recherche de chemin dans le monde.
 * Utilisé par AIManager pour trouver des chemins entre deux points.
 */
public class Pathfinder {

    // Logger pour les messages de debug
    private final Logger logger;

    // Référence au monde pour vérifier les collisions
    private final World world;

    // Taille d'un chunk (pour optimiser les calculs)
    private static final int CHUNK_SIZE = 16;

    /**
     * Constructeur du Pathfinder.
     * @param world Référence au monde du jeu.
     */
    public Pathfinder(World world) {
        this.logger = new Logger("Pathfinder");
        this.world = world;
    }

    /**
     * Trouve un chemin entre deux points dans le monde.
     * @param startX Coordonnée X de départ.
     * @param startY Coordonnée Y de départ.
     * @param startZ Coordonnée Z de départ.
     * @param endX Coordonnée X d'arrivée.
     * @param endY Coordonnée Y d'arrivée.
     * @param endZ Coordonnée Z d'arrivée.
     * @return Liste de nœuds représentant le chemin (ou une liste vide si aucun chemin n'est trouvé).
     */
    public List<Node> findPath(int startX, int startY, int startZ, int endX, int endY, int endZ) {
        // Créer les nœuds de départ et d'arrivée
        Node startNode = new Node(startX, startY, startZ);
        Node endNode = new Node(endX, endY, endZ);

        // Vérifier si le nœud de départ ou d'arrivée est bloqué
        if (isBlocked(startX, startY, startZ) || isBlocked(endX, endY, endZ)) {
            logger.debug("Départ ou arrivée bloqué(e) : aucun chemin possible.");
            return Collections.emptyList();
        }

        // Liste ouverte : nœuds à explorer (triés par coût F)
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(node -> node.getF()));
        openList.add(startNode);

        // Liste fermée : nœuds déjà explorés
        Set<Node> closedList = new HashSet<>();

        // Ajouter le nœud de départ à la liste fermée
        closedList.add(startNode);

        // Tant qu'il reste des nœuds à explorer
        while (!openList.isEmpty()) {
            // Récupérer le nœud avec le coût F le plus bas
            Node currentNode = openList.poll();

            // Si on a atteint le nœud d'arrivée, reconstruire le chemin
            if (currentNode.equals(endNode)) {
                return reconstructPath(currentNode);
            }

            // Explorer les voisins du nœud actuel
            for (Node neighbor : getNeighbors(currentNode)) {
                // Ignorer les nœuds bloqués ou déjà explorés
                if (isBlocked(neighbor.getX(), neighbor.getY(), neighbor.getZ()) ||
                        closedList.contains(neighbor)) {
                    continue;
                }

                // Calculer le coût G (distance depuis le départ)
                int tentativeG = currentNode.getG() + calculateDistance(currentNode, neighbor);

                // Si le voisin n'est pas dans la liste ouverte ou si on a trouvé un meilleur chemin
                if (!openList.contains(neighbor) || tentativeG < neighbor.getG()) {
                    // Mettre à jour le parent et les coûts
                    neighbor.setParent(currentNode);
                    neighbor.setG(tentativeG);
                    neighbor.setH(calculateDistance(neighbor, endNode));
                    neighbor.setF(neighbor.getG() + neighbor.getH());

                    // Ajouter à la liste ouverte si ce n'est pas déjà fait
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }

            // Ajouter le nœud actuel à la liste fermée
            closedList.add(currentNode);
        }

        // Aucun chemin trouvé
        logger.debug("Aucun chemin trouvé de (" + startX + ", " + startY + ", " + startZ + ") à (" +
                endX + ", " + endY + ", " + endZ + ").");
        return Collections.emptyList();
    }

    /**
     * Reconstruit le chemin à partir du nœud d'arrivée.
     * @param endNode Noeud d'arrivée.
     * @return Liste de nœuds représentant le chemin.
     */
    private List<Node> reconstructPath(Node endNode) {
        List<Node> path = new ArrayList<>();
        Node currentNode = endNode;

        while (currentNode != null) {
            path.add(currentNode);
            currentNode = currentNode.getParent();
        }

        // Inverser la liste pour avoir le chemin du départ à l'arrivée
        Collections.reverse(path);
        return path;
    }

    /**
     * Récupère les voisins d'un nœud.
     * @param node Noeud dont on veut les voisins.
     * @return Liste des nœuds voisins.
     */
    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int x = node.getX();
        int y = node.getY();
        int z = node.getZ();

        // Voisins dans les 6 directions (haut, bas, gauche, droite, avant, arrière)
        int[][] directions = {
                {1, 0, 0}, {-1, 0, 0},  // Droite, gauche
                {0, 1, 0}, {0, -1, 0},  // Haut, bas
                {0, 0, 1}, {0, 0, -1}   // Avant, arrière
        };

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            int newZ = z + dir[2];
            neighbors.add(new Node(newX, newY, newZ));
        }

        return neighbors;
    }

    /**
     * Calcule la distance entre deux nœuds (distance de Manhattan).
     * @param a Premier nœud.
     * @param b Deuxième nœud.
     * @return Distance entre les deux nœuds.
     */
    private int calculateDistance(Node a, Node b) {
        return Math.abs(a.getX() - b.getX()) +
                Math.abs(a.getY() - b.getY()) +
                Math.abs(a.getZ() - b.getZ());
    }

    /**
     * Vérifie si une position est bloquée (par un bloc solide).
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @return true si la position est bloquée, false sinon.
     */
    private boolean isBlocked(int x, int y, int z) {
        // Vérifier si le bloc à cette position est solide
        return world != null && world.isBlockSolid(x, y, z);
    }
}
