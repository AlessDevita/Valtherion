package fr.alessdevita.valtherion.save;

import fr.alessdevita.valtherion.ecs.ECSManager;
import fr.alessdevita.valtherion.save.exceptions.SaveException;

import java.util.*;

/**
 * Interface pour les gestionnaires de sauvegarde dans Valtherion.
 * Définit les méthodes de base pour sauvegarder, charger, supprimer, et lister les sauvegardes.
 */
public interface SaveHandler {

    /**
     * Sauvegarde les données.
     * @param saveData Données à sauvegarder.
     * @throws SaveException Si une erreur se produit pendant la sauvegarde.
     */
    void save(SaveData saveData) throws SaveException;

    /**
     * Charge les données de sauvegarde.
     * @param saveName Nom de la sauvegarde.
     * @return Données de sauvegarde chargées.
     * @throws SaveException Si une erreur se produit pendant le chargement.
     */
    SaveData load(String saveName) throws SaveException;

    /**
     * Supprime une sauvegarde.
     * @param saveName Nom de la sauvegarde.
     * @return true si la suppression a réussi, false sinon.
     * @throws SaveException Si une erreur se produit pendant la suppression.
     */
    boolean delete(String saveName) throws SaveException;

    /**
     * Liste toutes les sauvegardes disponibles.
     * @return Liste des noms des sauvegardes.
     * @throws SaveException Si une erreur se produit pendant la liste.
     */
    List<String> listSaves() throws SaveException;

    /**
     * Vérifie si une sauvegarde existe.
     * @param saveName Nom de la sauvegarde.
     * @return true si la sauvegarde existe, false sinon.
     * @throws SaveException Si une erreur se produit pendant la vérification.
     */
    boolean exists(String saveName) throws SaveException;

    void init(ECSManager ecsManager);
}