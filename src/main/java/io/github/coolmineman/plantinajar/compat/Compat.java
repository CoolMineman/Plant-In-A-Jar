package io.github.coolmineman.plantinajar.compat;

import net.minecraft.block.BlockState;

public interface Compat {
    public BlockState getTreeBlockWood(BlockState sappling);
    public BlockState getTreeBlockLeaf(BlockState sappling);
}