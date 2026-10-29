package fr.alessdevita.valtherion.client.gui.achievement;

import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.client.gui.Gui;

/**
 * GuiAchievement — la notification toast qui glisse en haut a droite
 * de l'ecran quand un haut-fait est debloque. Une seule a la fois,
 * file d'attente sinon.
 */
public class GuiAchievement extends Gui
{
    private final Valtherion mc;
    /** Haut-fait affiche. */
    private Achievement theAchievement;
    /** Timer de vie de la notification (en ms). */
    private long notificationTime;
    private final GuiAchievement.AchievementObserver observer;
    /** File d'attente des notifications. */
    private java.util.Deque<Achievement> queue = new java.util.ArrayDeque<Achievement>();

    public GuiAchievement(Valtherion mc)
    {
        this.mc = mc;
        this.observer = new GuiAchievement.AchievementObserver();
        Achievement.setNotificationObserver((Achievement.IAchievementObserver) this.observer);
    }

    /** Observer : le stats system pousse les achievements debloques ici. */
    class AchievementObserver
    {
        public void notifyAchievementGet(Achievement achievement)
        {
            GuiAchievement.this.queue.add(achievement);
        }
    }

    public void updateAchievementWindow()
    {
        // Enfile un achievement si la notification courante est finie
        if (this.notificationTime != 0L && System.currentTimeMillis() - this.notificationTime > 2500L)
        {
            this.notificationTime = 0L;
        }

        if (this.notificationTime == 0L && !this.queue.isEmpty())
        {
            this.theAchievement = this.queue.poll();
            this.notificationTime = System.currentTimeMillis();
            // TODO : jouer le son d'achievement (achievement.get)
        }
    }

    /** Rendu du toast (appele par GuiIngame). */
    public void renderAchievementNotification(int screenWidth, int screenHeight)
    {
        if (this.theAchievement == null || this.notificationTime == 0L)
        {
            return;
        }

        long elapsed = System.currentTimeMillis() - this.notificationTime;
        // Le toast reste 2500 ms : glisse 600 ms a l'entree et 600 ms en sortie
        float visibility = elapsed < 600L
                ? (float) elapsed / 600.0F
                : (elapsed > 1900L ? 1.0F - (float) (elapsed - 1900L) / 600.0F : 1.0F);

        if (visibility > 0.0F && visibility <= 1.0F)
        {
            int toastWidth = 250;
            int toastHeight = 24;
            int x = screenWidth - (int) ((float) toastWidth * visibility);
            int y = 12;
            // Fond jaune fonce du toast
            Gui.drawRect(x + 2, y + 2, x + toastWidth, y + toastHeight, 0xC0000000);
            Gui.drawRect(x, y, x + toastWidth - 2, y + toastHeight - 2, 0xFF212121);
            // Zone icone
            Gui.drawRect(x + 5, y + 5, x + 19, y + 19, 0xFF8A8A8A);
            // Textes
            this.mc.fontRenderer.drawString(
                    "Haut-fait obtenu !", x + 24, y + 5, 0xFFFFFF);
            this.mc.fontRenderer.drawString(
                    this.theAchievement.getStatName(), x + 24, y + 14, 0xFFFFAA00);
        }
        else if (visibility <= 0.0F)
        {
            this.notificationTime = 0L;
        }
    }
}