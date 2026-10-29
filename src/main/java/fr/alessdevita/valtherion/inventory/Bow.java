package fr.alessdevita.valtherion.inventory;

/**
 * Représente un arc dans le système d'inventaire de Valtherion.
 * Un arc est une arme à distance qui utilise des munitions (flèches).
 */
public class Bow extends Item {

    // Dégâts de base de l'arc
    private int damage;

    // Durabilité actuelle de l'arc
    private int durability;

    // Durabilité maximale de l'arc
    private int maxDurability;

    // Vitesse de tir (temps entre deux tirs)
    private float fireRate;

    // Portée de l'arc
    private float range;

    // Vitesse de la flèche
    private float arrowSpeed;

    /**
     * Constructeur pour créer un nouvel arc.
     * @param id ID unique de l'arc.
     * @param name Nom de l'arc.
     * @param damage Dégâts de base.
     * @param maxDurability Durabilité maximale.
     */
    public Bow(String id, String name, int damage, int maxDurability) {
        super(id, name, ItemType.WEAPON);
        this.damage = damage;
        this.maxDurability = maxDurability;
        this.durability = maxDurability;
        this.fireRate = 1.0f;
        this.range = 50.0f;
        this.arrowSpeed = 2.0f;
        
        // Les arcs ne sont généralement pas empilables et sont utilisables
        this.setStackable(false);
        this.setUsable(true);
    }

    public Bow() {
        super("bow", "Arc", ItemType.WEAPON);
        this.damage = 10;
        this.maxDurability = 100;
    }

    /**
     * Utilise l'arc pour tirer.
     * @return true si le tir a réussi, false sinon (ex: plus de durabilité).
     */
    @Override
    public boolean use() {
        if (durability <= 0) {
            System.out.println("L'arc est brisé !");
            return false;
        }

        // Logique de tir
        System.out.println("Tir à l'arc : " + getName() + " inflige " + damage + " dégâts.");
        
        // Réduction de la durabilité
        durability--;
        
        if (durability == 0) {
            System.out.println("L'arc vient de se briser !");
        }

        return true;
    }

    // --- Getters et Setters ---

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = Math.min(durability, maxDurability);
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public void setMaxDurability(int maxDurability) {
        this.maxDurability = maxDurability;
    }

    public float getFireRate() {
        return fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public float getArrowSpeed() {
        return arrowSpeed;
    }

    public void setArrowSpeed(float arrowSpeed) {
        this.arrowSpeed = arrowSpeed;
    }

    /**
     * Répare l'arc d'un certain montant.
     * @param amount Montant de durabilité à restaurer.
     */
    public void repair(int amount) {
        this.durability = Math.min(this.durability + amount, maxDurability);
    }

    @Override
    public String toString() {
        return "Bow{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", damage=" + damage +
                ", durability=" + durability +
                "/" + maxDurability +
                '}';
    }
}
