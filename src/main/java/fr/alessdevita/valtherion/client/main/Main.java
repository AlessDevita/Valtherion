package fr.alessdevita.valtherion.client.main;

import com.google.common.collect.ArrayListMultimap;
import fr.alessdevita.valtherion.client.Valtherion;
import fr.alessdevita.valtherion.utils.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/** Parses launcher options and starts the Valtherion client. */
public final class Main {
    static final Logger logger = LoggerFactory.getLogger(Main.class);
    private Main() { }

    public static void main(String[] args) {
        Map<String, String> options = parseArguments(args);
        try {
            int width = integer(options, "width", 854, 320, 7680);
            int height = integer(options, "height", 480, 240, 4320);
            String username = options.getOrDefault("username", "Player");
            String uuid = options.getOrDefault("uuid", "00000000-0000-0000-0000-000000000000");
            File gameDir = new File(options.getOrDefault("gameDir", ".")).getCanonicalFile();
            File assetsDir = new File(options.getOrDefault("assetsDir", new File(gameDir, "assets").getPath())).getCanonicalFile();
            File resourcePacksDir = new File(gameDir, "resourcepacks");
            new File(gameDir, options.getOrDefault("worldDir", "saves")).mkdirs();
            assetsDir.mkdirs();
            resourcePacksDir.mkdirs();

            Proxy proxy = Proxy.NO_PROXY;
            String proxyHost = options.get("proxyHost");
            if (proxyHost != null && !proxyHost.isBlank()) {
                int proxyPort = integer(options, "proxyPort", 8080, 1, 65535);
                proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyHost, proxyPort));
            }
            final Proxy selectedProxy = proxy;
            Thread clientThread = new Thread(() -> {
                try {
                    Valtherion game = new Valtherion(new Session(username, uuid), width, height,
                            options.containsKey("fullscreen"), options.containsKey("demo"), gameDir,
                            assetsDir, resourcePacksDir, selectedProxy, "1.0-ALPHA", ArrayListMultimap.create());
                    game.run();
                } catch (Throwable error) {
                    logger.error("Erreur fatale au démarrage de Valtherion", error);
                }
            }, "Client thread");
            clientThread.start();
        } catch (Exception error) {
            logger.error("Arguments de lancement invalides", error);
        }
    }

    private static Map<String, String> parseArguments(String[] args) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg == null || !arg.startsWith("--")) continue;
            String value = "true";
            int equals = arg.indexOf('=');
            if (equals >= 0) {
                value = arg.substring(equals + 1);
                arg = arg.substring(0, equals);
            } else if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                value = args[++i];
            }
            result.put(arg.substring(2), value);
        }
        return result;
    }

    private static int integer(Map<String, String> options, String key, int fallback, int min, int max) {
        String value = options.get(key);
        if (value == null) return fallback;
        int parsed = Integer.parseInt(value);
        if (parsed < min || parsed > max) throw new IllegalArgumentException(key + " doit être compris entre " + min + " et " + max);
        return parsed;
    }
}
