package fr.alessdevita.valtherion.utils;

import java.util.Objects;

/**
 * Classe pour représenter un triplet de valeurs dans Valtherion.
 * @param <A> Type de la première valeur.
 * @param <B> Type de la deuxième valeur.
 * @param <C> Type de la troisième valeur.
 */
public class Triple<A, B, C> {

    // Première valeur
    private A first;

    // Deuxième valeur
    private B second;

    // Troisième valeur
    private C third;

    /**
     * Constructeur du triplet.
     */
    public Triple() {
        this.first = null;
        this.second = null;
        this.third = null;
    }

    /**
     * Constructeur du triplet avec des valeurs.
     * @param first Première valeur.
     * @param second Deuxième valeur.
     * @param third Troisième valeur.
     */
    public Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Crée un nouveau triplet.
     * @param first Première valeur.
     * @param second Deuxième valeur.
     * @param third Troisième valeur.
     * @param <A> Type de la première valeur.
     * @param <B> Type de la deuxième valeur.
     * @param <C> Type de la troisième valeur.
     * @return Nouveau triplet.
     */
    public static <A, B, C> Triple<A, B, C> of(A first, B second, C third) {
        return new Triple<>(first, second, third);
    }

    // --- Getters et Setters ---

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    public C getThird() {
        return third;
    }

    public void setThird(C third) {
        this.third = third;
    }

    /**
     * Retourne une représentation textuelle du triplet.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Triple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }

    /**
     * Vérifie si le triplet est égal à un autre objet.
     * @param obj Objet à comparer.
     * @return true si les objets sont égaux, false sinon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) obj;
        return Objects.equals(first, triple.first) &&
                Objects.equals(second, triple.second) &&
                Objects.equals(third, triple.third);
    }

    /**
     * Calcule le hash code du triplet.
     * @return Hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }
}