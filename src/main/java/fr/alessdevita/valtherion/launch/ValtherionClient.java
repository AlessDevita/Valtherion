package fr.alessdevita.valtherion.launch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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

        Label loading = new Label("Chargement de Valtherion…");
        loading.setTextFill(Color.WHITE);
        loading.setFont(Font.font("System", FontWeight.BOLD, 24));
        StackPane loadingPane = new StackPane(loading);
        loadingPane.setStyle("-fx-background-color: #151923;");
        stage.setScene(new Scene(loadingPane, 1280, 720));
        stage.show();

        new Thread(() -> {
            try {
                Thread.sleep(1800);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> showMainMenu(stage));
        }, "valtherion-loading").start();
    }

    private void showMainMenu(Stage stage) {
        Label title = new Label("VALTHERION");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 42));
        Button play = new Button("Jouer");
        play.setOnAction(event -> launchGame(stage));
        Button quit = new Button("Quitter");
        quit.setOnAction(event -> Platform.exit());
        VBox menu = new VBox(20, title, play, quit);
        menu.setAlignment(Pos.CENTER);
        menu.setStyle("-fx-background-color: #151923;");
        stage.setScene(new Scene(menu, 1280, 720));
        stage.show();
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
                    .redirectOutput(ProcessBuilder.Redirect.appendTo(logFile.toFile()))
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
