package fr.alessdevita.valtherion.core;

/**
 * Boucle de jeu principale.
 * Gère les mises à jour (UPS) et le rendu (FPS) de manière indépendante.
 */
public class GameLoop implements Runnable {

    // Constantes pour les cibles FPS et UPS
    private static final int TARGET_FPS = 60;
    private static final int TARGET_UPS = 60;
    private static final long NANOS_PER_SECOND = 1_000_000_000L;
    private static final long NANOS_PER_UPDATE = NANOS_PER_SECOND / TARGET_UPS;

    // État de la boucle
    private boolean isRunning;
    private Thread thread;

    // Référence au moteur
    private final Engine engine;

    // Compteurs pour FPS et UPS
    private int frameCount;
    private int updateCount;

    // Temps
    private long lastUpdateTime;
    private long lastRenderTime;
    private double alpha; // Pour l'interpolation

    // Logger
    private final Logger logger;

    /**
     * Constructeur de la boucle de jeu.
     * @param engine Référence au moteur.
     */
    public GameLoop(Engine engine) {
        this.engine = engine;
        this.logger = new Logger("GameLoop");
        this.isRunning = false;
        this.frameCount = 0;
        this.updateCount = 0;
        this.lastUpdateTime = System.nanoTime();
        this.lastRenderTime = System.nanoTime();
    }

    /**
     * Démarre la boucle de jeu dans un nouveau thread.
     */
    public void start() {
        if (isRunning) {
            logger.warn("La boucle de jeu est déjà en cours d'exécution.");
            return;
        }

        isRunning = true;
        thread = new Thread(this, "GameLoop");
        thread.start();
        logger.info("Boucle de jeu démarrée.");
    }

    /**
     * Arrête la boucle de jeu.
     */
    public void stop() {
        if (!isRunning) {
            logger.warn("La boucle de jeu n'est pas en cours d'exécution.");
            return;
        }

        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            logger.error("Erreur lors de l'arrêt de la boucle de jeu : " + e.getMessage());
        }
        logger.info("Boucle de jeu arrêtée.");
    }

    /**
     * Boucle principale (appelée par le thread).
     */
    @Override
    public void run() {
        while (isRunning) {
            // Mettre à jour le jeu
            update();

            // Rendre le jeu
            render();

            // Calculer les FPS/UPS
            engine.calculateFPS();

            // Synchroniser avec les cibles FPS/UPS
            sync();
        }
    }

    /**
     * Met à jour la logique du jeu (appelé à intervalle fixe).
     */
    private void update() {
        long currentTime = System.nanoTime();
        long elapsedTime = currentTime - lastUpdateTime;

        // Si assez de temps s'est écoulé pour une mise à jour
        if (elapsedTime >= NANOS_PER_UPDATE) {
            // Calculer le nombre de mises à jour à effectuer
            int updates = (int) (elapsedTime / NANOS_PER_UPDATE);

            for (int i = 0; i < updates; i++) {
                engine.update();
                updateCount++;
                lastUpdateTime += NANOS_PER_UPDATE;
            }

            // Calculer alpha pour l'interpolation (smooth movement)
            alpha = (double) (elapsedTime - (updates * NANOS_PER_UPDATE)) / NANOS_PER_UPDATE;
        }
    }

    /**
     * Rend le jeu (appelé aussi souvent que possible).
     */
    private void render() {
        // Limiter les FPS si nécessaire
        long currentTime = System.nanoTime();
        long elapsedTime = currentTime - lastRenderTime;

        if (elapsedTime < NANOS_PER_SECOND / TARGET_FPS) {
            return; // Pas encore le temps de rendre
        }

        engine.render();
        frameCount++;
        lastRenderTime = currentTime;
    }

    /**
     * Synchronise la boucle pour atteindre les cibles FPS/UPS.
     */
    private void sync() {
        long currentTime = System.nanoTime();
        long targetTime = lastUpdateTime + NANOS_PER_UPDATE;

        // Si on est en avance, attendre un peu
        while (currentTime < targetTime) {
            try {
                Thread.sleep((targetTime - currentTime) / 1_000_000); // Convertir en millisecondes
            } catch (InterruptedException e) {
                logger.error("Erreur lors de la synchronisation : " + e.getMessage());
            }
            currentTime = System.nanoTime();
        }
    }

    // --- Getters pour les compteurs ---

    public int getFrameCount() {
        return frameCount;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void resetCounters() {
        frameCount = 0;
        updateCount = 0;
    }

    public double getAlpha() {
        return alpha;
    }
}