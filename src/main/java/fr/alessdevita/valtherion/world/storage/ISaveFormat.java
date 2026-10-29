package fr.alessdevita.valtherion.world.storage;

import java.io.IOException;
import java.util.List;

public interface ISaveFormat {
    ISaveHandler getSaveLoader(String saveName, boolean storePlayerData);

    WorldInfo getWorldInfo(String saveName);

    void flushCache();

    boolean deleteWorldDirectory(String saveName);

    boolean isSinglePlayer();

    String getName();

    List<String> getWorldList();
}
