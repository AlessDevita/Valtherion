package fr.alessdevita.valtherion.client.multiplayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Liste des serveurs enregistrée en JSON dans le dossier du jeu. */
public class ServerList {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type SERVER_LIST_TYPE = new TypeToken<List<ServerData>>() { }.getType();

    private final Path file;
    private final List<ServerData> servers = new ArrayList<>();

    public ServerList() {
        this(Path.of(System.getProperty("user.dir", "."), "servers.json"));
    }

    public ServerList(Path file) {
        if (file == null) throw new IllegalArgumentException("Le chemin du fichier est requis.");
        this.file = file.toAbsolutePath().normalize();
        loadServerList();
    }

    public synchronized void loadServerList() {
        this.servers.clear();
        if (!Files.isRegularFile(this.file)) return;
        try {
            String json = Files.readString(this.file, StandardCharsets.UTF_8);
            List<ServerData> loaded = GSON.fromJson(json, SERVER_LIST_TYPE);
            if (loaded != null) {
                for (ServerData server : loaded) {
                    if (server != null && server.getServerName() != null && server.getServerIP() != null) {
                        this.servers.add(server);
                    }
                }
            }
        } catch (IOException | JsonParseException | IllegalArgumentException exception) {
            System.err.println("Impossible de lire la liste des serveurs " + this.file + " : " + exception.getMessage());
        }
    }

    public synchronized void saveServerList() {
        try {
            Path parent = this.file.getParent();
            if (parent != null) Files.createDirectories(parent);
            Path temporaryFile = this.file.resolveSibling(this.file.getFileName() + ".tmp");
            Files.writeString(temporaryFile, GSON.toJson(this.servers, SERVER_LIST_TYPE), StandardCharsets.UTF_8);
            try {
                Files.move(temporaryFile, this.file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException atomicMoveUnsupported) {
                Files.move(temporaryFile, this.file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            System.err.println("Impossible d'enregistrer la liste des serveurs " + this.file + " : " + exception.getMessage());
        }
    }

    public synchronized int countServers() { return this.servers.size(); }
    public synchronized int getServerCount() { return this.servers.size(); }
    public synchronized ServerData getServerData(int index) { return this.servers.get(index); }
    public synchronized List<ServerData> getServers() { return Collections.unmodifiableList(new ArrayList<>(this.servers)); }

    public synchronized void addServerData(ServerData server) {
        if (server == null) throw new IllegalArgumentException("Le serveur est requis.");
        this.servers.add(server);
        saveServerList();
    }

    public synchronized void setServerData(int index, ServerData server) {
        if (server == null) throw new IllegalArgumentException("Le serveur est requis.");
        this.servers.set(index, server);
        saveServerList();
    }

    public synchronized void removeServerData(int index) {
        this.servers.remove(index);
        saveServerList();
    }

    public synchronized void removeServerData(ServerData server) {
        if (this.servers.remove(server)) saveServerList();
    }

    public synchronized void swapServers(int first, int second) {
        Collections.swap(this.servers, first, second);
        saveServerList();
    }

    public Path getFile() { return this.file; }
}
