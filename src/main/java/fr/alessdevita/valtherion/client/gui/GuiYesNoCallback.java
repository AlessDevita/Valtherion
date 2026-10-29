package fr.alessdevita.valtherion.client.gui;

/**
 * GuiYesNoCallback — interface implementee par les ecrans qui veulent
 * recevoir la reponse d'un GuiYesNo.
 */
public interface GuiYesNoCallback
{
    /**
     * Appele quand l'utilisateur repond au dialogue de confirmation.
     * @param result true = "Oui", false = "Non"
     * @param id l'identifiant passe au constructeur du GuiYesNo
     */
    void confirmClicked(boolean result, int id);
}