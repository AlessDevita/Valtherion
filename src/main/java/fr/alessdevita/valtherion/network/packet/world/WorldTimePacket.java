package fr.alessdevita.valtherion.network.packet.world;

import fr.alessdevita.valtherion.network.core.Packet;
import fr.alessdevita.valtherion.network.core.Connection;
import fr.alessdevita.valtherion.world.WorldTime;

import java.io.*;

/**
 * Paquet de synchronisation du temps du monde dans Valtherion.
 * Utilisé pour synchroniser le temps du monde entre le serveur et les clients.
 */
public class WorldTimePacket extends Packet {

    // ID du paquet
    public static final int PACKET_ID = 3003;

    // Temps actuel du monde (en ticks)
    private long currentTime;

    // Temps total écoulé depuis la création du monde (en ticks)
    private long totalTime;

    // Heure actuelle du monde (0-23)
    private int hour;

    // Minute actuelle du monde (0-59)
    private int minute;

    // Seconde actuelle du monde (0-59)
    private int second;

    // Jour actuel du monde (1-30)
    private int day;

    // Mois actuel du monde (1-12)
    private int month;

    // Année actuelle du monde
    private int year;

    // Saison actuelle du monde
    private WorldTime.Season currentSeason;

    // Phase de la lune actuelle
    private WorldTime.MoonPhase currentMoonPhase;

    // Vitesse du temps (1.0 = temps réel)
    private float timeSpeed;

    // Indique si le temps est en pause
    private boolean isPaused;

    /**
     * Constructeur du paquet de synchronisation du temps du monde.
     */
    public WorldTimePacket() {
        super(PACKET_ID);
    }

    /**
     * Constructeur du paquet de synchronisation du temps du monde avec un objet WorldTime.
     * @param worldTime Objet WorldTime.
     */
    public WorldTimePacket(WorldTime worldTime) {
        this();
        this.currentTime = worldTime.getCurrentTime();
        this.totalTime = worldTime.getTotalTime();
        this.hour = worldTime.getHour();
        this.minute = worldTime.getMinute();
        this.second = worldTime.getSecond();
        this.day = worldTime.getDay();
        this.month = worldTime.getMonth();
        this.year = worldTime.getYear();
        this.currentSeason = worldTime.getCurrentSeason();
        this.currentMoonPhase = worldTime.getCurrentMoonPhase();
        this.timeSpeed = worldTime.getTimeSpeed();
        this.isPaused = worldTime.isPaused();
    }

    @Override
    protected void writeData(DataOutputStream dataStream) throws IOException {
        // Écrire le temps actuel
        dataStream.writeLong(currentTime);

        // Écrire le temps total écoulé
        dataStream.writeLong(totalTime);

        // Écrire l'heure, la minute et la seconde
        dataStream.writeInt(hour);
        dataStream.writeInt(minute);
        dataStream.writeInt(second);

        // Écrire le jour, le mois et l'année
        dataStream.writeInt(day);
        dataStream.writeInt(month);
        dataStream.writeInt(year);

        // Écrire la saison actuelle
        dataStream.writeByte(currentSeason != null ? currentSeason.ordinal() : 0);

        // Écrire la phase de la lune actuelle
        dataStream.writeByte(currentMoonPhase != null ? currentMoonPhase.ordinal() : 0);

        // Écrire la vitesse du temps
        dataStream.writeFloat(timeSpeed);

        // Écrire le flag de pause
        dataStream.writeBoolean(isPaused);
    }

    @Override
    protected void readData(DataInputStream dataStream) throws IOException {
        // Lire le temps actuel
        this.currentTime = dataStream.readLong();

        // Lire le temps total écoulé
        this.totalTime = dataStream.readLong();

        // Lire l'heure, la minute et la seconde
        this.hour = dataStream.readInt();
        this.minute = dataStream.readInt();
        this.second = dataStream.readInt();

        // Lire le jour, le mois et l'année
        this.day = dataStream.readInt();
        this.month = dataStream.readInt();
        this.year = dataStream.readInt();

        // Lire la saison actuelle
        this.currentSeason = WorldTime.Season.values()[dataStream.readByte()];

        // Lire la phase de la lune actuelle
        this.currentMoonPhase = WorldTime.MoonPhase.values()[dataStream.readByte()];

        // Lire la vitesse du temps
        this.timeSpeed = dataStream.readFloat();

        // Lire le flag de pause
        this.isPaused = dataStream.readBoolean();
    }

    @Override
    public void handleClient(Connection connection) {
        // Le client traite la synchronisation du temps du monde
        System.out.println("Temps du monde synchronisé : " +
                String.format("%02d:%02d:%02d", hour, minute, second) +
                " (Jour " + day + "/" + month + "/" + year + ")");

        // Ici, tu peux mettre à jour le temps du monde côté client
        // Exemple :
        // WorldTime worldTime = world.getWorldTime();
        // worldTime.setTime(hour, minute, second, day, month, year);
        // worldTime.setTimeSpeed(timeSpeed);
        // if (isPaused) {
        //     worldTime.pause();
        // } else {
        //     worldTime.resume();
        // }
    }

    @Override
    public void handleServer(Connection connection) {
        // Le serveur ne traite pas ce paquet (il l'envoie)
    }

    // --- Getters et Setters ---

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public WorldTime.Season getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(WorldTime.Season currentSeason) {
        this.currentSeason = currentSeason;
    }

    public WorldTime.MoonPhase getCurrentMoonPhase() {
        return currentMoonPhase;
    }

    public void setCurrentMoonPhase(WorldTime.MoonPhase currentMoonPhase) {
        this.currentMoonPhase = currentMoonPhase;
    }

    public float getTimeSpeed() {
        return timeSpeed;
    }

    public void setTimeSpeed(float timeSpeed) {
        this.timeSpeed = timeSpeed;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    /**
     * Retourne une représentation textuelle du paquet de synchronisation du temps du monde.
     * @return Chaîne de caractères.
     */
    @Override
    public String toString() {
        return "WorldTimePacket{" +
                "hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", currentSeason=" + currentSeason +
                ", currentMoonPhase=" + currentMoonPhase +
                ", timeSpeed=" + timeSpeed +
                ", isPaused=" + isPaused +
                '}';
    }
}