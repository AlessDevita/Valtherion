package fr.alessdevita.valtherion.launch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Fenêtre de lancement pour Valtherion.
 * Utilise JavaFX pour afficher une interface graphique native.
 */
public class ValtherionClient extends Application {

    // Chemin vers le JAR de Valtherion
    private static final String JAR_PATH = "E:\\User\\Aless_Devita\\LibFX\\Mes Libs\\Valtherion\\build\\libs\\Valtherion-1.0-ALPHA.jar";

    @Override
    public void start(Stage stage) {
        Platform.setImplicitExit(false);
        stage.setTitle("Valtherion");
        stage.setResizable(false);
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.centerOnScreen();

        // Fond noir DEVITA STUDIOS
        StackPane loadingPane = new StackPane();
        loadingPane.setStyle("-fx-background-color: #000000;");

        // Charger la texture du logo DEVITA STUDIOS depuis les ressources
        try {
            java.io.InputStream logoStream = ValtherionClient.class.getResourceAsStream("/assets/valtherion/textures/gui/title/devitastudios.png");
            if (logoStream != null) {
                javafx.scene.image.Image logoImage = new javafx.scene.image.Image(logoStream);
                ImageView logoView = new ImageView(logoImage);
                // Centrer le logo
                loadingPane.getChildren().add(logoView);
                System.out.println("[ValtherionClient] Logo DEVITA STUDIOS chargé avec succès");
            } else {
                System.err.println("[ValtherionClient] Texture non trouvée : /assets/valtherion/textures/gui/title/devitastudios.png");
                // Fallback : afficher le texte si la texture n'est pas trouvée
                Label loading = new Label("Chargement de Valtherion…");
                loading.setTextFill(Color.WHITE);
                loading.setFont(Font.font("System", FontWeight.BOLD, 24));
                loadingPane.getChildren().add(loading);
            }
        } catch (Exception e) {
            System.err.println("[ValtherionClient] Erreur en chargeant le logo : " + e.getMessage());
            // Fallback : afficher le texte en cas d'erreur
            Label loading = new Label("Chargement de Valtherion…");
            loading.setTextFill(Color.WHITE);
            loading.setFont(Font.font("System", FontWeight.BOLD, 24));
            loadingPane.getChildren().add(loading);
        }

        stage.setScene(new Scene(loadingPane, 1280, 720));
        stage.show();

        new Thread(() -> {
            try {
                Thread.sleep(1800);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> launchGame(stage));
        }, "valtherion-loading").start();
    }

    /**
     * Lance le jeu Valtherion.
     */
    private void launchGame(Stage launcherStage) {
        try {
            Path workingDirectory = Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize();
            Path gameJar = workingDirectory.resolve("build/libs/Valtherion-1.0-ALPHA.jar");
            if (!Files.isRegularFile(gameJar)) {
                gameJar = Path.of(JAR_PATH).toAbsolutePath().normalize();
            }
            if (!Files.isRegularFile(gameJar)) {
                throw new IOException("JAR du jeu introuvable : " + gameJar);
            }

            Path projectDirectory = gameJar.getParent().getParent();
            Path logFile = projectDirectory.resolve("logs/launcher-game.log");
            Files.createDirectories(logFile.getParent());

            // Le JAR a pour manifest Start, qui ouvre le lanceur JavaFX.
            // Pour éviter de relancer le lanceur, on appelle directement le client du jeu.
            List<String> command = List.of(
                    Path.of(System.getProperty("java.home"), "bin", "java").toString(),
                    "-Xmx2G",
                    "-Xms1G",
                    "--enable-native-access=ALL-UNNAMED",
                    "-Djava.library.path=" + projectDirectory.resolve("libs/natives"),
                    "-cp",
                    gameJar.toString(),
                    "fr.alessdevita.valtherion.client.main.Main",
                    "--width", "1280",
                    "--height", "720",
                    "--gameDir", projectDirectory.toString(),
                    "--assetsDir", projectDirectory.resolve("assets").toString()
            );
            Process process = new ProcessBuilder(command)
                    .directory(projectDirectory.toFile())
                    .redirectErrorStream(true)
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .start();

            launcherStage.hide();
            Thread monitor = new Thread(() -> {
                try {
                    int exitCode = process.waitFor();
                    Platform.runLater(() -> {
                        if (exitCode == 0) {
                            Platform.exit();
                        } else {
                            launcherStage.show();
                            showError("Le jeu s'est arrêté avec le code " + exitCode
                                    + ". Consulte le journal : " + logFile);
                        }
                    });
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                }
            }, "valtherion-game-monitor");
            monitor.setDaemon(true);
            monitor.start();
        } catch (IOException e) {
            showError("Erreur lors du lancement du jeu : " + e.getMessage());
        }
    }

    /**
     * Affiche une erreur dans une boîte de dialogue.
     * @param message Message d'erreur.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Point d'entrée principal.
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        launch();
    }
}
