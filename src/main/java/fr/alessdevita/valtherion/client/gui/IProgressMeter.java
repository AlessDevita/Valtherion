package fr.alessdevita.valtherion.client.gui;

/**
 * IProgressMeter — interface des ecrans qui affichent une progression
 * (tunnel de connection, sauvegarde de monde, telechargement de terrain).
 * Valtherion l'utilise via GuiScreenWorking.
 */
public interface IProgressMeter
{
    /** Affiche un message de progression (nom MC : displayLoadingString). */
    void displayProgressMessage(String message);

    /** Met a jour la progression en % (nom MC : setLoadingProgress). */
    void setLoadingProgress(int progress);
}