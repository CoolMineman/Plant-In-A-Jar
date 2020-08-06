package io.github.coolmineman.plantinajar.compat;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import techreborn.init.TRContent;

public class TechRebornCompat implements Compat {

    @Override
    public BlockState getTreeBlockWood(BlockState sappling) {
        if (sappling.isOf(TRContent.RUBBER_SAPLING)) {
            return TRContent.RUBBER_LOG.getDefaultState();
        }
        return null;
    }

    @Override
    public BlockState getTreeBlockLeaf(BlockState sappling) {
        if (sappling.isOf(TRContent.RUBBER_SAPLING)) {
            return TRContent.RUBBER_LEAVES.getDefaultState();
        }
        return null;
    }

    @Override
    public List<ItemStack> getExtraDrops(ItemStack plant) {
        if (plant.getItem().equals(TRContent.RUBBER_SAPLING.asItem())
                && ThreadLocalRandom.current().nextInt(0, 4) == 0) {
            return Collections.singletonList(TRContent.Parts.SAP.getStack(1));
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Boolean> isTree(BlockState plant) {
        return Optional.empty();
    }
    
}