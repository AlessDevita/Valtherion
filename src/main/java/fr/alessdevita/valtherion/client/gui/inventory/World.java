package fr.alessdevita.valtherion.client.gui.inventory;

public class World {
    public boolean isRemote;

    public BlockState getBlockState(BlockPos pos) {
        return new BlockState();
    }
}

class BlockState {
    public Object getBlock() {
        return null;
    }
}
