package fr.alessdevita.valtherion.utils;

import java.util.*;
import java.util.function.*;

/**
 * Classe d'utilitaires pour manipuler les collections dans Valtherion.
 */
public class CollectionUtils {

    /**
     * Vérifie si une collection est null ou vide.
     * @param collection Collection à vérifier.
     * @return true si la collection est null ou vide, false sinon.
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Vérifie si une map est null ou vide.
     * @param map Map à vérifier.
     * @return true si la map est null ou vide, false sinon.
     */
    public static boolean isNullOrEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Vérifie si un tableau est null ou vide.
     * @param array Tableau à vérifier.
     * @return true si le tableau est null ou vide, false sinon.
     */
    public static boolean isNullOrEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Ajoute un élément à une collection si ce dernier n'est pas déjà présent.
     * @param collection Collection.
     * @param element Élément à ajouter.
     * @param <T> Type de l'élément.
     * @return true si l'élément a été ajouté, false sinon.
     */
    public static <T> boolean addIfAbsent(Collection<T> collection, T element) {
        if (collection == null || element == null) {
            return false;
        }
        if (collection.contains(element)) {
            return false;
        }
        return collection.add(element);
    }

    /**
     * Ajoute tous les éléments d'une collection à une autre collection si ces derniers ne sont pas déjà présents.
     * @param targetCollection Collection cible.
     * @param sourceCollection Collection source.
     * @param <T> Type des éléments.
     * @return Nombre d'éléments ajoutés.
     */
    public static <T> int addAllIfAbsent(Collection<T> targetCollection, Collection<T> sourceCollection) {
        if (targetCollection == null || sourceCollection == null) {
            return 0;
        }

        int count = 0;
        for (T element : sourceCollection) {
            if (addIfAbsent(targetCollection, element)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Fusionne deux listes.
     * @param list1 Première liste.
     * @param list2 Deuxième liste.
     * @param <T> Type des éléments.
     * @return Nouvelle liste contenant les éléments des deux listes.
     */
    public static <T> List<T> mergeLists(List<T> list1, List<T> list2) {
        List<T> mergedList = new ArrayList<>();
        if (list1 != null) {
            mergedList.addAll(list1);
        }
        if (list2 != null) {
            mergedList.addAll(list2);
        }
        return mergedList;
    }

    /**
     * Fusionne deux maps.
     * @param map1 Première map.
     * @param map2 Deuxième map.
     * @param <K> Type des clés.
     * @param <V> Type des valeurs.
     * @return Nouvelle map contenant les entrées des deux maps.
     */
    public static <K, V> Map<K, V> mergeMaps(Map<K, V> map1, Map<K, V> map2) {
        Map<K, V> mergedMap = new HashMap<>();
        if (map1 != null) {
            mergedMap.putAll(map1);
        }
        if (map2 != null) {
            mergedMap.putAll(map2);
        }
        return mergedMap;
    }

    /**
     * Filtre une collection en fonction d'un prédicat.
     * @param collection Collection à filtrer.
     * @param predicate Prédicat de filtrage.
     * @param <T> Type des éléments.
     * @return Nouvelle liste contenant les éléments qui correspondent au prédicat.
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        List<T> filteredList = new ArrayList<>();
        if (collection != null && predicate != null) {
            for (T element : collection) {
                if (predicate.test(element)) {
                    filteredList.add(element);
                }
            }
        }
        return filteredList;
    }

    /**
     * Transforme une collection en une nouvelle liste en appliquant une fonction.
     * @param collection Collection à transformer.
     * @param function Fonction de transformation.
     * @param <T> Type des éléments de la collection.
     * @param <R> Type des éléments de la nouvelle liste.
     * @return Nouvelle liste contenant les éléments transformés.
     */
    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> function) {
        List<R> mappedList = new ArrayList<>();
        if (collection != null && function != null) {
            for (T element : collection) {
                mappedList.add(function.apply(element));
            }
        }
        return mappedList;
    }

    /**
     * Réduit une collection en une seule valeur en appliquant une fonction d'accumulation.
     * @param collection Collection à réduire.
     * @param identity Valeur initiale.
     * @param accumulator Fonction d'accumulation.
     * @param <T> Type des éléments de la collection.
     * @param <R> Type de la valeur accumulée.
     * @return Valeur accumulée.
     */
    public static <T, R> R reduce(Collection<T> collection, R identity, BiFunction<R, T, R> accumulator) {
        if (collection == null || accumulator == null) {
            return identity;
        }

        R result = identity;
        for (T element : collection) {
            result = accumulator.apply(result, element);
        }
        return result;
    }

    /**
     * Trouve le premier élément d'une collection qui correspond à un prédicat.
     * @param collection Collection à parcourir.
     * @param predicate Prédicat de recherche.
     * @param <T> Type des éléments.
     * @return Premier élément qui correspond au prédicat ou null si non trouvé.
     */
    public static <T> T findFirst(Collection<T> collection, Predicate<T> predicate) {
        if (collection == null || predicate == null) {
            return null;
        }

        for (T element : collection) {
            if (predicate.test(element)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Vérifie si une collection contient un élément qui correspond à un prédicat.
     * @param collection Collection à vérifier.
     * @param predicate Prédicat de vérification.
     * @param <T> Type des éléments.
     * @return true si la collection contient un élément qui correspond au prédicat, false sinon.
     */
    public static <T> boolean anyMatch(Collection<T> collection, Predicate<T> predicate) {
        return findFirst(collection, predicate) != null;
    }

    /**
     * Vérifie si tous les éléments d'une collection correspondent à un prédicat.
     * @param collection Collection à vérifier.
     * @param predicate Prédicat de vérification.
     * @param <T> Type des éléments.
     * @return true si tous les éléments correspondent au prédicat, false sinon.
     */
    public static <T> boolean allMatch(Collection<T> collection, Predicate<T> predicate) {
        if (collection == null || predicate == null) {
            return false;
        }

        for (T element : collection) {
            if (!predicate.test(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Trouve l'élément maximum d'une collection en fonction d'un comparateur.
     * @param collection Collection à parcourir.
     * @param comparator Comparateur.
     * @param <T> Type des éléments.
     * @return Élément maximum ou null si la collection est vide.
     */
    public static <T> T max(Collection<T> collection, Comparator<? super T> comparator) {
        if (collection == null || collection.isEmpty() || comparator == null) {
            return null;
        }

        T maxElement = null;
        for (T element : collection) {
            if (maxElement == null || comparator.compare(element, maxElement) > 0) {
                maxElement = element;
            }
        }
        return maxElement;
    }

    /**
     * Trouve l'élément minimum d'une collection en fonction d'un comparateur.
     * @param collection Collection à parcourir.
     * @param comparator Comparateur.
     * @param <T> Type des éléments.
     * @return Élément minimum ou null si la collection est vide.
     */
    public static <T> T min(Collection<T> collection, Comparator<? super T> comparator) {
        if (collection == null || collection.isEmpty() || comparator == null) {
            return null;
        }

        T minElement = null;
        for (T element : collection) {
            if (minElement == null || comparator.compare(element, minElement) < 0) {
                minElement = element;
            }
        }
        return minElement;
    }

    /**
     * Trie une collection en fonction d'un comparateur.
     * @param collection Collection à trier.
     * @param comparator Comparateur.
     * @param <T> Type des éléments.
     * @return Nouvelle liste triée.
     */
    public static <T> List<T> sort(Collection<T> collection, Comparator<? super T> comparator) {
        if (collection == null || comparator == null) {
            return new ArrayList<>();
        }

        List<T> sortedList = new ArrayList<>(collection);
        sortedList.sort(comparator);
        return sortedList;
    }

    /**
     * Inverse une liste.
     * @param list Liste à inverser.
     * @param <T> Type des éléments.
     * @return Nouvelle liste inversée.
     */
    public static <T> List<T> reverse(List<T> list) {
        if (list == null) {
            return new ArrayList<>();
        }

        List<T> reversedList = new ArrayList<>(list);
        Collections.reverse(reversedList);
        return reversedList;
    }

    /**
     * Mélange une liste.
     * @param list Liste à mélanger.
     * @param <T> Type des éléments.
     * @return Nouvelle liste mélangée.
     */
    public static <T> List<T> shuffle(List<T> list) {
        if (list == null) {
            return new ArrayList<>();
        }

        List<T> shuffledList = new ArrayList<>(list);
        Collections.shuffle(shuffledList);
        return shuffledList;
    }

    /**
     * Extrait une sous-liste d'une liste.
     * @param list Liste d'origine.
     * @param startIndex Index de début.
     * @param endIndex Index de fin.
     * @param <T> Type des éléments.
     * @return Sous-liste.
     */
    public static <T> List<T> subList(List<T> list, int startIndex, int endIndex) {
        if (list == null || startIndex < 0 || endIndex > list.size() || startIndex > endIndex) {
            return new ArrayList<>();
        }
        return new ArrayList<>(list.subList(startIndex, endIndex));
    }

    /**
     * Divise une liste en sous-listes de taille fixe.
     * @param list Liste à diviser.
     * @param chunkSize Taille des sous-listes.
     * @param <T> Type des éléments.
     * @return Liste des sous-listes.
     */
    public static <T> List<List<T>> partition(List<T> list, int chunkSize) {
        List<List<T>> partitions = new ArrayList<>();
        if (list == null || chunkSize <= 0) {
            return partitions;
        }

        for (int i = 0; i < list.size(); i += chunkSize) {
            partitions.add(subList(list, i, Math.min(i + chunkSize, list.size())));
        }
        return partitions;
    }
}