package fr.alessdevita.valtherion.utils;

import java.util.*;
import java.util.regex.*;

/**
 * Classe d'utilitaires pour manipuler les chaînes de caractères dans Valtherion.
 */
public class StringUtils {

    // Expression régulière pour les espaces
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    // Expression régulière pour les lignes vides
    private static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("^\\s*$");

    /**
     * Vérifie si une chaîne est null ou vide.
     * @param str Chaîne à vérifier.
     * @return true si la chaîne est null ou vide, false sinon.
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Vérifie si une chaîne est null, vide ou ne contient que des espaces.
     * @param str Chaîne à vérifier.
     * @return true si la chaîne est null, vide ou ne contient que des espaces, false sinon.
     */
    public static boolean isNullOrWhitespace(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Vérifie si une chaîne contient seulement des espaces.
     * @param str Chaîne à vérifier.
     * @return true si la chaîne contient seulement des espaces, false sinon.
     */
    public static boolean isWhitespace(String str) {
        return str != null && str.trim().isEmpty();
    }

    /**
     * Convertit une chaîne en camelCase.
     * @param str Chaîne à convertir.
     * @return Chaîne en camelCase.
     */
    public static String toCamelCase(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (char c : str.toCharArray()) {
            if (Character.isWhitespace(c) || c == '_' || c == '-' || c == '.') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }

    /**
     * Convertit une chaîne en PascalCase.
     * @param str Chaîne à convertir.
     * @return Chaîne en PascalCase.
     */
    public static String toPascalCase(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }

        String camelCase = toCamelCase(str);
        return Character.toUpperCase(camelCase.charAt(0)) + camelCase.substring(1);
    }

    /**
     * Convertit une chaîne en snake_case.
     * @param str Chaîne à convertir.
     * @return Chaîne en snake_case.
     */
    public static String toSnakeCase(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * Convertit une chaîne en kebab-case.
     * @param str Chaîne à convertir.
     * @return Chaîne en kebab-case.
     */
    public static String toKebabCase(String str) {
        return toSnakeCase(str).replace('_', '-');
    }

    /**
     * Répète une chaîne un certain nombre de fois.
     * @param str Chaîne à répéter.
     * @param count Nombre de répétitions.
     * @return Chaîne répétée.
     */
    public static String repeat(String str, int count) {
        if (isNullOrEmpty(str) || count <= 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(str);
        }
        return result.toString();
    }

    /**
     * Inverse une chaîne.
     * @param str Chaîne à inverser.
     * @return Chaîne inversée.
     */
    public static String reverse(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }

        return new StringBuilder(str).reverse().toString();
    }

    /**
     * Supprime les espaces au début et à la fin d'une chaîne.
     * @param str Chaîne à trimmer.
     * @return Chaîne trimée.
     */
    public static String trim(String str) {
        return str != null ? str.trim() : null;
    }

    /**
     * Supprime tous les espaces d'une chaîne.
     * @param str Chaîne à trimmer.
     * @return Chaîne sans espaces.
     */
    public static String trimAll(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return WHITESPACE_PATTERN.matcher(str).replaceAll("");
    }

    /**
     * Remplace toutes les occurrences d'une sous-chaîne dans une chaîne.
     * @param str Chaîne d'origine.
     * @param target Sous-chaîne à remplacer.
     * @param replacement Chaîne de remplacement.
     * @return Chaîne avec les remplacements.
     */
    public static String replaceAll(String str, String target, String replacement) {
        if (isNullOrEmpty(str) || isNullOrEmpty(target)) {
            return str;
        }
        return str.replace(target, replacement);
    }

    /**
     * Remplace toutes les occurrences d'une expression régulière dans une chaîne.
     * @param str Chaîne d'origine.
     * @param regex Expression régulière.
     * @param replacement Chaîne de remplacement.
     * @return Chaîne avec les remplacements.
     */
    public static String replaceAllRegex(String str, String regex, String replacement) {
        if (isNullOrEmpty(str) || isNullOrEmpty(regex)) {
            return str;
        }
        return str.replaceAll(regex, replacement);
    }

    /**
     * Vérifie si une chaîne commence par un préfixe (ignore la casse).
     * @param str Chaîne à vérifier.
     * @param prefix Préfixe.
     * @return true si la chaîne commence par le préfixe, false sinon.
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (isNullOrEmpty(str) || isNullOrEmpty(prefix)) {
            return false;
        }
        return str.toLowerCase().startsWith(prefix.toLowerCase());
    }

    /**
     * Vérifie si une chaîne se termine par un suffixe (ignore la casse).
     * @param str Chaîne à vérifier.
     * @param suffix Suffixe.
     * @return true si la chaîne se termine par le suffixe, false sinon.
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        if (isNullOrEmpty(str) || isNullOrEmpty(suffix)) {
            return false;
        }
        return str.toLowerCase().endsWith(suffix.toLowerCase());
    }

    /**
     * Vérifie si une chaîne contient une sous-chaîne (ignore la casse).
     * @param str Chaîne à vérifier.
     * @param substring Sous-chaîne.
     * @return true si la chaîne contient la sous-chaîne, false sinon.
     */
    public static boolean containsIgnoreCase(String str, String substring) {
        if (isNullOrEmpty(str) || isNullOrEmpty(substring)) {
            return false;
        }
        return str.toLowerCase().contains(substring.toLowerCase());
    }

    /**
     * Vérifie si une chaîne est égale à une autre (ignore la casse).
     * @param str1 Première chaîne.
     * @param str2 Deuxième chaîne.
     * @return true si les chaînes sont égales, false sinon.
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return str1 == str2;
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * Convertit une chaîne en majuscules.
     * @param str Chaîne à convertir.
     * @return Chaîne en majuscules.
     */
    public static String toUpperCase(String str) {
        return str != null ? str.toUpperCase() : null;
    }

    /**
     * Convertit une chaîne en minuscules.
     * @param str Chaîne à convertir.
     * @return Chaîne en minuscules.
     */
    public static String toLowerCase(String str) {
        return str != null ? str.toLowerCase() : null;
    }

    /**
     * Capitalise la première lettre d'une chaîne.
     * @param str Chaîne à capitaliser.
     * @return Chaîne capitalisée.
     */
    public static String capitalize(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }

    /**
     * Décapitalise la première lettre d'une chaîne.
     * @param str Chaîne à décapitaliser.
     * @return Chaîne décapitalisée.
     */
    public static String uncapitalize(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * Divise une chaîne en lignes.
     * @param str Chaîne à diviser.
     * @param lineLength Longueur maximale d'une ligne.
     * @return Liste des lignes.
     */
    public static List<String> splitIntoLines(String str, int lineLength) {
        List<String> lines = new ArrayList<>();
        if (isNullOrEmpty(str) || lineLength <= 0) {
            return lines;
        }

        int index = 0;
        while (index < str.length()) {
            int endIndex = Math.min(index + lineLength, str.length());
            lines.add(str.substring(index, endIndex));
            index = endIndex;
        }

        return lines;
    }

    /**
     * Supprime les lignes vides d'une chaîne.
     * @param str Chaîne à nettoyer.
     * @return Chaîne sans lignes vides.
     */
    public static String removeEmptyLines(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return EMPTY_LINE_PATTERN.matcher(str).replaceAll("");
    }

    /**
     * Compte le nombre d'occurrences d'une sous-chaîne dans une chaîne.
     * @param str Chaîne à vérifier.
     * @param substring Sous-chaîne à compter.
     * @return Nombre d'occurrences.
     */
    public static int countOccurrences(String str, String substring) {
        if (isNullOrEmpty(str) || isNullOrEmpty(substring)) {
            return 0;
        }

        int count = 0;
        int index = 0;
        while ((index = str.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }

    /**
     * Vérifie si une chaîne est un nombre valide.
     * @param str Chaîne à vérifier.
     * @return true si la chaîne est un nombre valide, false sinon.
     */
    public static boolean isNumeric(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Vérifie si une chaîne est un entier valide.
     * @param str Chaîne à vérifier.
     * @return true si la chaîne est un entier valide, false sinon.
     */
    public static boolean isInteger(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}