package fr.alessdevita.valtherion.utils;

import java.util.Arrays;
import java.util.Objects;

/**
 * Classe générique pour représenter un tuple de valeurs dans Valtherion.
 * @param <T> Type des valeurs du tuple.
 */
public class Tuple<T> {

    // Tableau des valeurs
    private final T[] values;

    /**
     * Constructeur du tuple.
     * @param values Valeurs du tuple.
     */
    @SafeVarargs
    public Tuple(T... values) {
        this.values = values;
    }

    /**
     * Crée un nouveau tuple.
     * @param values Valeurs du tuple.
     * @param <T> Type des valeurs.
     * @return Nouveau tuple.
     */
    @SafeVarargs
    public static <T> Tuple<T> of(T... values) {
        return new Tuple<>(values);
    }

    /**
     * Récupère une valeur du tuple.
     * @param index Index de la valeur.
     * @return Valeur à l'index spécifié.
     */
    public T get(int index) {
        if (index < 0 || index >= values.length) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for tuple of size " + values.length);
        }
        return values[index];
    }

    /**
     * Récupère la taille du tuple.
     * @return Taille du tuple.
     */
    public int size() {
        return values.length;
    }

    /**
     * Vérifie si le tuple est vide.
     * @return true si le tuple est vide, false sinon.
     */
    public boolean isEmpty() {
        return values.length == 0;
    }

    /**
     * Convertit le tuple en tableau.
     * @return Tableau des valeurs.
     */
    public T[] toArray() {
        return Arrays.copyOf(values, values.length);
    }

    /**
     * Retourne une représentation textuelle du tuple.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Tuple{" +
                "values=" + Arrays.toString(values) +
                '}';
    }

    /**
     * Vérifie si le tuple est égal à un autre objet.
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
        Tuple<?> tuple = (Tuple<?>) obj;
        return Arrays.equals(values, tuple.values);
    }

    /**
     * Calcule le hash code du tuple.
     * @return Hash code.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
}