package fr.alessdevita.valtherion.utils;

import java.time.*;
import java.time.format.*;
import java.util.concurrent.TimeUnit;

/**
 * Classe d'utilitaires pour manipuler le temps dans Valtherion.
 */
public class TimeUtils {

    // Format de date par défaut
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Format de temps par défaut
    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Format de date et temps par défaut
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Nombre de millisecondes dans une seconde
    public static final long MILLIS_PER_SECOND = 1000;

    // Nombre de millisecondes dans une minute
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;

    // Nombre de millisecondes dans une heure
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

    // Nombre de millisecondes dans un jour
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    // Nombre de secondes dans une minute
    public static final int SECONDS_PER_MINUTE = 60;

    // Nombre de secondes dans une heure
    public static final int SECONDS_PER_HOUR = 60 * SECONDS_PER_MINUTE;

    // Nombre de secondes dans un jour
    public static final int SECONDS_PER_DAY = 24 * SECONDS_PER_HOUR;

    /**
     * Récupère le timestamp actuel en millisecondes.
     * @return Timestamp actuel.
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * Récupère le timestamp actuel en secondes.
     * @return Timestamp actuel en secondes.
     */
    public static long getCurrentTimestampSeconds() {
        return System.currentTimeMillis() / MILLIS_PER_SECOND;
    }

    /**
     * Convertit un timestamp en date locale.
     * @param timestamp Timestamp en millisecondes.
     * @return Date locale.
     */
    public static LocalDate timestampToLocalDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Convertit un timestamp en temps local.
     * @param timestamp Timestamp en millisecondes.
     * @return Temps local.
     */
    public static LocalTime timestampToLocalTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * Convertit un timestamp en date et temps local.
     * @param timestamp Timestamp en millisecondes.
     * @return Date et temps local.
     */
    public static LocalDateTime timestampToLocalDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Convertit une date locale en timestamp.
     * @param date Date locale.
     * @return Timestamp en millisecondes.
     */
    public static long localDateToTimestamp(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Convertit un temps local en timestamp.
     * @param time Temps local.
     * @return Timestamp en millisecondes.
     */
    public static long localTimeToTimestamp(LocalTime time) {
        return time.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Convertit une date et temps local en timestamp.
     * @param dateTime Date et temps local.
     * @return Timestamp en millisecondes.
     */
    public static long localDateTimeToTimestamp(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Formate un timestamp en chaîne de date.
     * @param timestamp Timestamp en millisecondes.
     * @return Chaîne de date formatée.
     */
    public static String formatTimestampAsDate(long timestamp) {
        return timestampToLocalDate(timestamp).format(DEFAULT_DATE_FORMATTER);
    }

    /**
     * Formate un timestamp en chaîne de temps.
     * @param timestamp Timestamp en millisecondes.
     * @return Chaîne de temps formatée.
     */
    public static String formatTimestampAsTime(long timestamp) {
        return timestampToLocalTime(timestamp).format(DEFAULT_TIME_FORMATTER);
    }

    /**
     * Formate un timestamp en chaîne de date et temps.
     * @param timestamp Timestamp en millisecondes.
     * @return Chaîne de date et temps formatée.
     */
    public static String formatTimestampAsDateTime(long timestamp) {
        return timestampToLocalDateTime(timestamp).format(DEFAULT_DATE_TIME_FORMATTER);
    }

    /**
     * Formate un timestamp avec un format personnalisé.
     * @param timestamp Timestamp en millisecondes.
     * @param formatter Formateur de date et temps.
     * @return Chaîne formatée.
     */
    public static String formatTimestamp(long timestamp, DateTimeFormatter formatter) {
        return timestampToLocalDateTime(timestamp).format(formatter);
    }

    /**
     * Calcule le temps écoulé entre deux timestamps en millisecondes.
     * @param startTimestamp Timestamp de début.
     * @param endTimestamp Timestamp de fin.
     * @return Temps écoulé en millisecondes.
     */
    public static long getElapsedTimeMillis(long startTimestamp, long endTimestamp) {
        return endTimestamp - startTimestamp;
    }

    /**
     * Calcule le temps écoulé entre deux timestamps en secondes.
     * @param startTimestamp Timestamp de début.
     * @param endTimestamp Timestamp de fin.
     * @return Temps écoulé en secondes.
     */
    public static long getElapsedTimeSeconds(long startTimestamp, long endTimestamp) {
        return getElapsedTimeMillis(startTimestamp, endTimestamp) / MILLIS_PER_SECOND;
    }

    /**
     * Calcule le temps écoulé entre deux timestamps en minutes.
     * @param startTimestamp Timestamp de début.
     * @param endTimestamp Timestamp de fin.
     * @return Temps écoulé en minutes.
     */
    public static long getElapsedTimeMinutes(long startTimestamp, long endTimestamp) {
        return getElapsedTimeMillis(startTimestamp, endTimestamp) / MILLIS_PER_MINUTE;
    }

    /**
     * Calcule le temps écoulé entre deux timestamps en heures.
     * @param startTimestamp Timestamp de début.
     * @param endTimestamp Timestamp de fin.
     * @return Temps écoulé en heures.
     */
    public static long getElapsedTimeHours(long startTimestamp, long endTimestamp) {
        return getElapsedTimeMillis(startTimestamp, endTimestamp) / MILLIS_PER_HOUR;
    }

    /**
     * Calcule le temps écoulé entre deux timestamps en jours.
     * @param startTimestamp Timestamp de début.
     * @param endTimestamp Timestamp de fin.
     * @return Temps écoulé en jours.
     */
    public static long getElapsedTimeDays(long startTimestamp, long endTimestamp) {
        return getElapsedTimeMillis(startTimestamp, endTimestamp) / MILLIS_PER_DAY;
    }

    /**
     * Formate un temps écoulé en chaîne lisible.
     * @param millis Temps écoulé en millisecondes.
     * @return Chaîne formatée (ex: "1h 23m 45s").
     */
    public static String formatElapsedTime(long millis) {
        if (millis < 0) {
            return "0s";
        }

        long seconds = millis / MILLIS_PER_SECOND;
        long minutes = seconds / SECONDS_PER_MINUTE;
        long hours = minutes / SECONDS_PER_MINUTE;
        long days = hours / SECONDS_PER_HOUR;

        seconds %= SECONDS_PER_MINUTE;
        minutes %= SECONDS_PER_MINUTE;
        hours %= SECONDS_PER_HOUR;

        StringBuilder result = new StringBuilder();

        if (days > 0) {
            result.append(days).append("d ");
        }
        if (hours > 0) {
            result.append(hours).append("h ");
        }
        if (minutes > 0) {
            result.append(minutes).append("m ");
        }
        result.append(seconds).append("s");

        return result.toString().trim();
    }

    /**
     * Formate un temps écoulé en chaîne lisible avec des unités complètes.
     * @param millis Temps écoulé en millisecondes.
     * @return Chaîne formatée (ex: "01:23:45").
     */
    public static String formatElapsedTimeFull(long millis) {
        if (millis < 0) {
            return "00:00:00";
        }

        long seconds = millis / MILLIS_PER_SECOND;
        long minutes = seconds / SECONDS_PER_MINUTE;
        long hours = minutes / SECONDS_PER_MINUTE;

        seconds %= SECONDS_PER_MINUTE;
        minutes %= SECONDS_PER_MINUTE;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Convertit des secondes en millisecondes.
     * @param seconds Secondes.
     * @return Millisecondes.
     */
    public static long secondsToMillis(long seconds) {
        return seconds * MILLIS_PER_SECOND;
    }

    /**
     * Convertit des minutes en millisecondes.
     * @param minutes Minutes.
     * @return Millisecondes.
     */
    public static long minutesToMillis(long minutes) {
        return minutes * MILLIS_PER_MINUTE;
    }

    /**
     * Convertit des heures en millisecondes.
     * @param hours Heures.
     * @return Millisecondes.
     */
    public static long hoursToMillis(long hours) {
        return hours * MILLIS_PER_HOUR;
    }

    /**
     * Convertit des jours en millisecondes.
     * @param days Jours.
     * @return Millisecondes.
     */
    public static long daysToMillis(long days) {
        return days * MILLIS_PER_DAY;
    }

    /**
     * Convertit des millisecondes en secondes.
     * @param millis Millisecondes.
     * @return Secondes.
     */
    public static long millisToSeconds(long millis) {
        return millis / MILLIS_PER_SECOND;
    }

    /**
     * Convertit des millisecondes en minutes.
     * @param millis Millisecondes.
     * @return Minutes.
     */
    public static long millisToMinutes(long millis) {
        return millis / MILLIS_PER_MINUTE;
    }

    /**
     * Convertit des millisecondes en heures.
     * @param millis Millisecondes.
     * @return Heures.
     */
    public static long millisToHours(long millis) {
        return millis / MILLIS_PER_HOUR;
    }

    /**
     * Convertit des millisecondes en jours.
     * @param millis Millisecondes.
     * @return Jours.
     */
    public static long millisToDays(long millis) {
        return millis / MILLIS_PER_DAY;
    }

    /**
     * Attend un certain nombre de millisecondes.
     * @param millis Millisecondes à attendre.
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Récupère le temps actuel en nanosecondes.
     * @return Temps actuel en nanosecondes.
     */
    public static long nanoTime() {
        return System.nanoTime();
    }

    /**
     * Calcule le temps écoulé en nanosecondes.
     * @param startTime Temps de début en nanosecondes.
     * @return Temps écoulé en nanosecondes.
     */
    public static long getElapsedNanoTime(long startTime) {
        return nanoTime() - startTime;
    }

    /**
     * Calcule le temps écoulé en secondes depuis un timestamp en nanosecondes.
     * @param startTime Temps de début en nanosecondes.
     * @return Temps écoulé en secondes.
     */
    public static double getElapsedTimeSecondsFromNano(long startTime) {
        return getElapsedNanoTime(startTime) / 1_000_000_000.0;
    }
}