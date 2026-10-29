package fr.alessdevita.valtherion.entity.living;

import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.inventory.Item;

/**
 * Classe pour les cochons dans Valtherion.
 * Un mob passif qui peut être tué pour obtenir de la viande de porc.
 */
public class Pig extends PassiveMob {

    // Indique si le cochon est en train de chercher de la nourriture
    private boolean isSearchingForFood;

    /**
     * Constructeur du cochon.
     * @param world Monde dans lequel le cochon existe.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public Pig(World world, double x, double y, double z) {
        super(
                EntityType.MOB,
                world,
                x, y, z,
                PassiveMobType.PIG
        );

        // Configurer les propriétés spécifiques du cochon
        setName("Cochon");
        setCanBeFed(true);
        setFavoriteFood("CARROT");

        // Configurer les drops du cochon
        addDrop(new Drop("PORK", 0.8f, 1, 3));

        // État initial
        this.isSearchingForFood = false;
    }

    /**
     * Met à jour le cochon.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        super.update();

        // Logique spécifique pour le cochon
        if (isSearchingForFood) {
            // Rechercher de la nourriture
            searchForFood();
        }
    }

    /**
     * Recherche de la nourriture.
     */
    private void searchForFood() {
        // Logique pour rechercher de la nourriture (ex: se déplacer vers des carottes)
        // Exemple : Trouver une carotte dans le monde et se déplacer vers elle
    }

    /**
     * Nourrit le cochon.
     * @param food Objet utilisé pour nourrir le cochon.
     * @return true si le cochon a été nourri, false sinon.
     */
    @Override
    public boolean feed(Item food) {
        boolean fed = super.feed(food);

        if (fed) {
            // Le cochon arrête de chercher de la nourriture après avoir mangé
            isSearchingForFood = false;
            System.out.println(getName() + " a été nourri et arrête de chercher de la nourriture.");
        }

        return fed;
    }

    /**
     * Méthode appelée lorsque le cochon meurt.
     */
    @Override
    public void onDeath() {
        super.onDeath();
        System.out.println(getName() + " est mort et lâche de la viande de porc.");
    }

    // --- Getters et Setters ---

    public boolean isSearchingForFood() {
        return isSearchingForFood;
    }

    public void setSearchingForFood(boolean searchingForFood) {
        isSearchingForFood = searchingForFood;
    }

    /**
     * Retourne une représentation textuelle du cochon.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Pig{" +
                "name='" + getName() + '\'' +
                ", health=" + getHealth() + "/" + getMaxHealth() +
                ", isSearchingForFood=" + isSearchingForFood +
                '}';
    }
}