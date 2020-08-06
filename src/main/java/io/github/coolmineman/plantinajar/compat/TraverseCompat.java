package io.github.coolmineman.plantinajar.compat;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.terraformersmc.traverse.block.TraverseBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

public class TraverseCompat implements Compat {

    @Override
    public BlockState getTreeBlockWood(BlockState sappling) {
        if (sappling.isOf(TraverseBlocks.BROWN_AUTUMNAL_SAPLING)) {
            return Blocks.OAK_LOG.getDefaultState();
        }
        if (sappling.isOf(TraverseBlocks.FIR_SAPLING)) {
            return TraverseBlocks.FIR_LOG.getDefaultState();
        }
        if (sappling.isOf(TraverseBlocks.RED_AUTUMNAL_SAPLING)) {
            return Blocks.DARK_OAK_LOG.getDefaultState();
        }
        if (sappling.isOf(TraverseBlocks.ORANGE_AUTUMNAL_SAPLING)) {
            return Blocks.OAK_LOG.getDefaultState();
        }
        if (sappling.isOf(TraverseBlocks.YELLOW_AUTUMNAL_SAPLING)) {
            return Blocks.BIRCH_LOG.getDefaultState();
        }
        return null;
    }

    @Override
    public BlockState getTreeBlockLeaf(BlockState sappling) {
        if (sappling.isOf(TraverseBlocks.BROWN_AUTUMNAL_SAPLING)) {
            return TraverseBlocks.BROWN_AUTUMNAL_LEAVES.getDefaultState();
        }
        if (sappling.isOf(TraverseBlocks.FIR_SAPLING)) {
            return TraverseBlocks.FIR_LEAVES.getDefaultState();
        }
        if (sappling.isOf(TraverseBlocks.RED_AUTUMNAL_SAPLING)) {
            return TraverseBlocks.RED_AUTUMNAL_LEAVES.getDefaultState();
        }
        if (sappling.isOf(TraverseBlocks.ORANGE_AUTUMNAL_SAPLING)) {
            return TraverseBlocks.ORANGE_AUTUMNAL_LEAVES.getDefaultState();
        }
        if (sappling.isOf(TraverseBlocks.YELLOW_AUTUMNAL_SAPLING)) {
            return TraverseBlocks.YELLOW_AUTUMNAL_LEAVES.getDefaultState();
        }
        return null;
    }

    @Override
    public List<ItemStack> getExtraDrops(ItemStack plant) {
        return Collections.emptyList();
    }

    @Override
    public Optional<Boolean> isTree(BlockState plant) {
        return Optional.empty();
    }
    
}