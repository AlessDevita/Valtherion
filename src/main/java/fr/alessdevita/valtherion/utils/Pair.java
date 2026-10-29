package fr.alessdevita.valtherion.utils;

import java.util.*;

/**
 * Classe pour représenter une paire de valeurs dans Valtherion.
 * @param <A> Type de la première valeur.
 * @param <B> Type de la deuxième valeur.
 */
public class Pair<A, B> implements Map.Entry<A, B> {

    // Première valeur
    private A first;

    // Deuxième valeur
    private B second;

    /**
     * Constructeur de la paire.
     */
    public Pair() {
        this.first = null;
        this.second = null;
    }

    /**
     * Constructeur de la paire avec des valeurs.
     * @param first Première valeur.
     * @param second Deuxième valeur.
     */
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Crée une nouvelle paire.
     * @param first Première valeur.
     * @param second Deuxième valeur.
     * @param <A> Type de la première valeur.
     * @param <B> Type de la deuxième valeur.
     * @return Nouvelle paire.
     */
    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    @Override
    public A getKey() {
        return first;
    }

    @Override
    public B getValue() {
        return second;
    }

    @Override
    public B setValue(B value) {
        B oldValue = second;
        second = value;
        return oldValue;
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

    /**
     * Échange les valeurs de la paire.
     */
    public void swap() {
        A tempFirst = first;
        first = (A) second;
        second = (B) tempFirst;
    }

    /**
     * Retourne une représentation textuelle de la paire.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    /**
     * Vérifie si la paire est égale à un autre objet.
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
        Pair<?, ?> pair = (Pair<?, ?>) obj;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    /**
     * Calcule le hash code de la paire.
     * @return Hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}