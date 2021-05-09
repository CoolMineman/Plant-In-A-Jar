package io.github.coolmineman.plantinajar.tree;

import java.util.List;

import net.minecraft.block.BlockState;

public final class Tree {
    public final QuadWithColor[][][][] quads;
    public final List<BlockState> drops;

    public Tree(QuadWithColor[][][][] quads, List<BlockState> drops) {
        this.quads = quads;
        this.drops = drops;
    }
}
