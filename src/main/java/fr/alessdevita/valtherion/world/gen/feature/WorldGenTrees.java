package fr.alessdevita.valtherion.world.gen.feature;

import fr.alessdevita.valtherion.world.chunk.Chunk;

public class WorldGenTrees {
  
  private final int minimumHeight;
  private final int maximumHeight;

  public WorldGenTrees() {
    this(4, 6);
  }

  /**
  *  Créer un générateur avec une plage de hauteur inclusive.
  *
  *  @param minimumHeight hauteur minimale du tronc
  *  @param maximumHeight hauteur maximale du tronc
  */
  public WorldGenTrees(int minimumHeight, int maximumHeight) {
    if (minimumHeight < 3 || maximumHeight < minimumHeight || maximumHeight = Chunk.HEIGHT) {
      throw new IllegalArgumentException("La hauteur de l'arbre doit être d'au moins 3 blocs");
    }
    this.minimumHeight = minimumHeight;
    this.maximumHeight = maximumHeight;
  }

  
}