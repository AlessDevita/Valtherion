package fr.alessdevita.valtherion.entity.living;

import fr.alessdevita.valtherion.entity.EntityType;
import fr.alessdevita.valtherion.world.World;
import fr.alessdevita.valtherion.inventory.Item;
import fr.alessdevita.valtherion.inventory.Bow;

/**
 * Classe pour les squelettes dans Valtherion.
 * Un mob hostile qui attaque les joueurs avec une épée ou un arc.
 */
public class Skeleton extends HostileMob {

    // Type d'arme utilisée par le squelette (SWORD ou BOW)
    private SkeletonWeapon weaponType;

    // Arc du squelette (si le squelette utilise un arc)
    private Bow bow;

    // Flèche actuelle (si le squelette tire)
    private Item currentArrow;

    // Cooldown entre les tirs (en ticks)
    private int shootCooldown;
    private int currentShootCooldown;

    // Énumération des types d'armes du squelette
    public enum SkeletonWeapon {
        SWORD,  // Épée
        BOW     // Arc
    }

    /**
     * Constructeur du squelette.
     * @param world Monde dans lequel le squelette existe.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    public Skeleton(World world, double x, double y, double z) {
        super(
                EntityType.MOB,
                world,
                x, y, z
        );

        // Configurer les propriétés spécifiques du squelette
        setName("Squelette");
        this.weaponType = SkeletonWeapon.BOW; // Par défaut, le squelette utilise un arc
        this.shootCooldown = 40; // 2 secondes (40 ticks)
        this.currentShootCooldown = 0;

        // Configurer les drops du squelette
        addDrop(new Drop("BONE", 0.8f, 1, 3));
        addDrop(new Drop("BOW", 0.2f, 1, 1));
        addDrop(new Drop("ARROW", 0.5f, 1, 5));

        // Configurer l'arme du squelette
        if (weaponType == SkeletonWeapon.BOW) {
            this.bow = new Bow();
            this.currentArrow = new Item("ARROW");
        }
    }

    /**
     * Met à jour le squelette.
     * Appelé à chaque tick du jeu.
     */
    @Override
    public void update() {
        super.update();

        // Mettre à jour le cooldown de tir
        if (currentShootCooldown > 0) {
            currentShootCooldown--;
        }
    }

    /**
     * Attaque une cible.
     * @param target Cible à attaquer.
     */
    @Override
    public void attack(LivingEntity target) {
        if (!isAlive() || target == null || !target.isAlive()) {
            return;
        }

        // Attaquer en fonction du type d'arme
        switch (weaponType) {
            case SWORD:
                // Attaque avec une épée
                super.attack(target);
                break;
            case BOW:
                // Attaque avec un arc
                if (currentShootCooldown == 0 && currentArrow != null) {
                    shootArrow(target);
                    currentShootCooldown = shootCooldown;
                }
                break;
        }
    }

    /**
     * Tire une flèche sur une cible.
     * @param target Cible à attaquer.
     */
    private void shootArrow(LivingEntity target) {
        if (bow == null || currentArrow == null) {
            return;
        }

        // Logique pour tirer une flèche
        // Exemple : getWorld().spawnProjectile(new Arrow(this, target, getX(), getY(), getZ()));
        System.out.println(getName() + " tire une flèche sur " + target.getName() + " !");

        // Consommer une flèche
        currentArrow = null; // À remplacer par une logique de gestion des flèches
    }

    /**
     * Méthode appelée lorsque le squelette meurt.
     */
    @Override
    public void onDeath() {
        super.onDeath();
        System.out.println(getName() + " est mort et lâche des os, un arc, et des flèches.");
    }

    // --- Getters et Setters ---

    public SkeletonWeapon getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(SkeletonWeapon weaponType) {
        this.weaponType = weaponType;
    }

    public Bow getBow() {
        return bow;
    }

    public void setBow(Bow bow) {
        this.bow = bow;
    }

    public Item getCurrentArrow() {
        return currentArrow;
    }

    public void setCurrentArrow(Item currentArrow) {
        this.currentArrow = currentArrow;
    }

    public int getShootCooldown() {
        return shootCooldown;
    }

    public void setShootCooldown(int shootCooldown) {
        this.shootCooldown = shootCooldown;
    }

    /**
     * Retourne une représentation textuelle du squelette.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Skeleton{" +
                "name='" + getName() + '\'' +
                ", health=" + getHealth() + "/" + getMaxHealth() +
                ", weaponType=" + weaponType +
                '}';
    }
}