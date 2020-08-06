package io.github.coolmineman.plantinajar.compat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import com.terraformersmc.cinderscapes.init.CinderscapesBlocks;

public class CinderscapesCompat implements Compat {

    @Override
    public BlockState getTreeBlockWood(BlockState sappling) {
        if (sappling.isOf(CinderscapesBlocks.UMBRAL_FUNGUS)) {
            return CinderscapesBlocks.UMBRAL_STEM.getDefaultState();
        }
        return null;
    }

    @Override
    public BlockState getTreeBlockLeaf(BlockState sappling) {
        if (sappling.isOf(CinderscapesBlocks.UMBRAL_FUNGUS)) {
            return CinderscapesBlocks.UMBRAL_WART_BLOCK.getDefaultState();
        }
        return null;
    }

    @Override
    public List<ItemStack> getExtraDrops(ItemStack plant) {
        ArrayList<ItemStack> result = new ArrayList<>();
        if (plant.getItem() instanceof BlockItem
                && ((BlockItem) (plant.getItem())).getBlock().equals(CinderscapesBlocks.UMBRAL_FUNGUS)) {
            result.add(new ItemStack(Item.BLOCK_ITEMS.get(CinderscapesBlocks.UMBRAL_FLESH_BLOCK)));
            if (ThreadLocalRandom.current().nextInt(0, 99) < 20) {
                result.add(new ItemStack(Items.SHROOMLIGHT));
            }
        }
        return result;
    }

    @Override
    public Optional<Boolean> isTree(BlockState plant) {
        if (plant.isOf(CinderscapesBlocks.UMBRAL_FUNGUS)) {
            return Optional.of(true);
        }
        return Optional.empty();
    }
    
}