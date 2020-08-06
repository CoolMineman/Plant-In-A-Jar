package io.github.coolmineman.plantinajar.compat;

import java.util.List;
import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public interface Compat {
    public BlockState getTreeBlockWood(BlockState sappling);
    public BlockState getTreeBlockLeaf(BlockState sappling);
    public List<ItemStack> getExtraDrops(ItemStack plant);
    public Optional<Boolean> isTree(BlockState plant);
}