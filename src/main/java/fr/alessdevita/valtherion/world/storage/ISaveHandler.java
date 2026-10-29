package fr.alessdevita.valtherion.world.storage;

import java.io.File;

public interface ISaveHandler {
    File getWorldDirectory();

    WorldInfo loadWorldInfo();

    void saveWorldInfo(WorldInfo worldInfo);
}
