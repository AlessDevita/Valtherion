package fr.alessdevita.valtherion.entity.non_living;

import fr.alessdevita.valtherion.entity.Entity;
import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.inventory.Item;

/**
 * Classe pour les objets tombés au sol dans Valtherion.
 * Représente un objet qui peut être ramassé par les joueurs.
 */
public class ItemEntity extends Entity {

    // Objet représenté par cette entité
    private Item item;

    // Quantité de l'objet
    private int quantity;

    // Temps avant que l'objet ne disparaisse (en ticks)
    private int despawnTime;
    private int currentDespawnTime;

    // Indique si l'objet peut être ramassé
    private boolean canBePickedUp;

    // Vitesse de rotation de l'objet (pour l'animation)
    private float rotationSpeed;
    private float currentRotation;

    /**
     * Constructeur de l'entité d'objet.
     * @param world Monde dans lequel l'objet existe.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     * @param item Objet représenté par cette entité.
     * @param quantity Quantité de l'objet.
     */
    public ItemEntity(World world, double x, double y, double z, Item item, int quantity) {
        super(EntityType.ITEM, x, y, z);
        this.item = item;
        this.quantity = quantity;
        this.despawnTime = 6000; // 5 minutes (6000 ticks)
        this.currentDespawnTime = 0;
        this.canBePickedUp = true;
        this.rotationSpeed = 1.0f;
        this.currentRotation = 0.0f;
    }

    /**
     * Met à jour l'entité d'objet.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        super.update();

        // Mettre à jour le temps avant disparition
        if (canBePickedUp) {
            currentDespawnTime++;
            if (currentDespawnTime >= despawnTime) {
                destroy();
            }
        }

        // Mettre à jour la rotation de l'objet
        currentRotation += rotationSpeed;
        if (currentRotation >= 360.0f) {
            currentRotation = 0.0f;
        }
    }

    @Override
    public void update(float deltaTime) {

        // Mettre à jour le temps avant disparition
        if (canBePickedUp) {
            currentDespawnTime++;
            if (currentDespawnTime >= despawnTime) {
                destroy();
            }
        }

        // Mettre à jour la rotation de l'objet
        currentRotation += rotationSpeed * deltaTime;
        if (currentRotation >= 360.0f) {
            currentRotation = 0.0f;
        }
    }

    /**
     * Ramasse l'objet.
     * @param picker Entité qui ramasse l'objet (généralement un joueur).
     * @return true si l'objet a été ramassé, false sinon.
     */
    public boolean pickUp(fr.alessdevita.valtherion.entity.living.LivingEntity picker) {
        if (!canBePickedUp || picker == null) {
            return false;
        }

        // Ajouter l'objet à l'inventaire du ramasseur
        if (picker instanceof fr.alessdevita.valtherion.entity.living.Player) {
            fr.alessdevita.valtherion.entity.living.Player player = (fr.alessdevita.valtherion.entity.living.Player) picker;
            player.addItem(item, quantity);
        }

        // Détruire l'entité d'objet
        destroy();
        return true;
    }

    /**
     * Définit si l'objet peut être ramassé.
     * @param canBePickedUp true si l'objet peut être ramassé, false sinon.
     */
    public void setCanBePickedUp(boolean canBePickedUp) {
        this.canBePickedUp = canBePickedUp;
    }

    /**
     * Définit le temps avant disparition.
     * @param despawnTime Temps avant disparition (en ticks).
     */
    public void setDespawnTime(int despawnTime) {
        this.despawnTime = despawnTime;
    }

    /**
     * Définit la vitesse de rotation.
     * @param rotationSpeed Vitesse de rotation.
     */
    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    // --- Getters ---

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDespawnTime() {
        return despawnTime;
    }

    public int getCurrentDespawnTime() {
        return currentDespawnTime;
    }

    public boolean canBePickedUp() {
        return canBePickedUp;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public float getCurrentRotation() {
        return currentRotation;
    }

    /**
     * Retourne une représentation textuelle de l'entité d'objet.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "ItemEntity{" +
                "item=" + (item != null ? item.getName() : "null") +
                ", quantity=" + quantity +
                ", position=(" + getX() + ", " + getY() + ", " + getZ() + ")" +
                '}';
    }
}