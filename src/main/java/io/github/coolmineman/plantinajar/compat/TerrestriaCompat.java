package io.github.coolmineman.plantinajar.compat;

import com.terraformersmc.terrestria.init.TerrestriaBlocks;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.terraformersmc.terraform.block.BareSmallLogBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

public class TerrestriaCompat implements Compat {

    @Override
    public BlockState getTreeBlockWood(BlockState sappling) {
        if (sappling.isOf(TerrestriaBlocks.BRYCE_SAPLING)) {
            return TerrestriaBlocks.SMALL_OAK_LOG.getDefaultState().with(BareSmallLogBlock.UP, true)
                    .with(BareSmallLogBlock.DOWN, true);
        }
        if (sappling.isOf(TerrestriaBlocks.REDWOOD_SAPLING)) {
            return TerrestriaBlocks.REDWOOD.log.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.HEMLOCK_SAPLING)) {
            return TerrestriaBlocks.HEMLOCK.log.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.RUBBER_SAPLING)) {
            return TerrestriaBlocks.RUBBER.log.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.CYPRESS_SAPLING)) {
            return TerrestriaBlocks.CYPRESS.log.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.WILLOW_SAPLING)) {
            return TerrestriaBlocks.WILLOW.log.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.JAPANESE_MAPLE_SAPLING)) {
            return TerrestriaBlocks.JAPANESE_MAPLE.log.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.JAPANESE_MAPLE_SHRUB_SAPLING)) {
            return TerrestriaBlocks.JAPANESE_MAPLE.log.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.DARK_JAPANESE_MAPLE_SAPLING)) {
            return TerrestriaBlocks.JAPANESE_MAPLE.log.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.RAINBOW_EUCALYPTUS_SAPLING)) {
            return TerrestriaBlocks.RAINBOW_EUCALYPTUS.log.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.SAKURA_SAPLING)) {
            return TerrestriaBlocks.SAKURA.log.getDefaultState().with(BareSmallLogBlock.UP, true)
                    .with(BareSmallLogBlock.DOWN, true);
        }
        if (sappling.isOf(TerrestriaBlocks.JUNGLE_PALM_SAPLING)) {
            return Blocks.JUNGLE_LOG.getDefaultState();
        }
        // todo ~~What is this?~~ add support?
        // if (sappling.isOf(TerrestriaBlocks.SAGUARO_CACTUS_SAPLING)) {
        // return null;
        // }
        if (sappling.isOf(TerrestriaBlocks.YUCCA_PALM_SAPLING)) {
            return TerrestriaBlocks.YUCCA_PALM.log.getDefaultState().with(BareSmallLogBlock.UP, true)
                    .with(BareSmallLogBlock.DOWN, true);
        }
        return null;
    }

    @Override
    public BlockState getTreeBlockLeaf(BlockState sappling) {
        if (sappling.isOf(TerrestriaBlocks.BRYCE_SAPLING)) {
            return Blocks.OAK_LEAVES.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.REDWOOD_SAPLING)) {
            return TerrestriaBlocks.REDWOOD.leaves.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.HEMLOCK_SAPLING)) {
            return TerrestriaBlocks.HEMLOCK.leaves.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.RUBBER_SAPLING)) {
            return TerrestriaBlocks.RUBBER.leaves.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.CYPRESS_SAPLING)) {
            return TerrestriaBlocks.CYPRESS.leaves.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.WILLOW_SAPLING)) {
            return TerrestriaBlocks.WILLOW.leaves.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.JAPANESE_MAPLE_SAPLING)) {
            return TerrestriaBlocks.JAPANESE_MAPLE.leaves.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.JAPANESE_MAPLE_SHRUB_SAPLING)) {
            return TerrestriaBlocks.JAPANESE_MAPLE.leaves.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.DARK_JAPANESE_MAPLE_SAPLING)) {
            return TerrestriaBlocks.JAPANESE_MAPLE.leaves.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.RAINBOW_EUCALYPTUS_SAPLING)) {
            return TerrestriaBlocks.RAINBOW_EUCALYPTUS.leaves.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.SAKURA_SAPLING)) {
            return TerrestriaBlocks.SAKURA.leaves.getDefaultState();
        }
        if (sappling.isOf(TerrestriaBlocks.JUNGLE_PALM_SAPLING)) {
            return Blocks.JUNGLE_LEAVES.getDefaultState();
        }
        // todo ~~What is this?~~ se above
        // if (sappling.isOf(TerrestriaBlocks.SAGUARO_CACTUS_SAPLING)) {
        // return null;
        // }
        if (sappling.isOf(TerrestriaBlocks.YUCCA_PALM_SAPLING)) {
            return TerrestriaBlocks.YUCCA_PALM.leaves.getDefaultState();
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