package io.github.coolmineman.plantinajar.tree;

import java.util.List;

import net.minecraft.block.BlockState;

public final class Tree {
    public final BlockState[][][] tree;
    public final List<BlockState> drops;

    public Tree(BlockState[][][] tree, List<BlockState> drops) {
        this.tree = tree;
        this.drops = drops;
    }
}
