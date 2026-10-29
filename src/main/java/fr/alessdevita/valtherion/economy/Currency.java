package fr.alessdevita.valtherion.economy;

/**
 * Classe pour gérer les devises dans Valtherion.
 * Représente une devise (or, pièces, gemmes, etc.) avec un nom, un symbole et une valeur.
 */
public class Currency {

    // Nom de la devise (ex: "Or", "Pièces", "Gemmes")
    private final String name;

    // Symbole de la devise (ex: "₧", "$", "♦")
    private final String symbol;

    // Valeur de la devise par rapport à une devise de base (ex: 1 Or = 100 Pièces)
    private final float value;

    // Description de la devise
    private final String description;

    /**
     * Constructeur de la devise.
     * @param name Nom de la devise.
     * @param symbol Symbole de la devise.
     * @param value Valeur de la devise.
     * @param description Description de la devise.
     */
    public Currency(String name, String symbol, float value, String description) {
        this.name = name;
        this.symbol = symbol;
        this.value = value;
        this.description = description;
    }

    // --- Getters ---

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public float getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Convertit un montant dans cette devise vers une autre devise.
     * @param amount Montant à convertir.
     * @param targetCurrency Devise cible.
     * @return Montant converti.
     */
    public float convert(float amount, Currency targetCurrency) {
        if (targetCurrency == null) {
            throw new IllegalArgumentException("La devise cible ne peut pas être nulle.");
        }
        return amount * (this.value / targetCurrency.getValue());
    }

    /**
     * Retourne une représentation textuelle de la devise.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Currency{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", value=" + value +
                ", description='" + description + '\'' +
                '}';
    }
}
