package fr.alessdevita.valtherion.audio;

/**
 * Classe pour les effets sonores dans Valtherion.
 * Représente un effet sonore à jouer une seule fois (ex: explosion, pas, etc.).
 */
public class SoundEffect extends Sound {

    // Durée de l'effet sonore (en secondes)
    private final float duration;

    /**
     * Constructeur de l'effet sonore.
     * @param name Nom de l'effet sonore.
     * @param soundId ID de l'effet sonore.
     * @param duration Durée de l'effet sonore (en secondes).
     */
    public SoundEffect(String name, String soundId, float duration) {
        super(name, soundId);
        this.duration = duration;
    }

    /**
     * Retourne la durée de l'effet sonore.
     * @return Durée en secondes.
     */
    public float getDuration() {
        return duration;
    }

    /**
     * Joue l'effet sonore à une position donnée.
     * @param x Coordonnée X.
     * @param y Coordonnée Y.
     * @param z Coordonnée Z.
     */
    @Override
    public void play(float x, float y, float z) {
        super.play(x, y, z);

        // Démarrer un timer pour marquer la fin de l'effet sonore
        // Exemple : Utiliser un Thread ou un système de timer pour appeler setFinished(true) après la durée
    }

    /**
     * Retourne une représentation textuelle de l'effet sonore.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "SoundEffect{" +
                "name='" + getName() + '\'' +
                ", soundId='" + getSoundId() + '\'' +
                ", duration=" + duration +
                "}" + super.toString();
    }
}
