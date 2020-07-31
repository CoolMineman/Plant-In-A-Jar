package io.github.coolmineman.plantinajar;

import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.Direction;

public class GoodIdeaDetector {
    private GoodIdeaDetector(){}

    public static boolean isGoodIdeaPlant(ItemStack i) {
        BlockState block;
        try {
            block = ((BlockItem)i.getItem()).getBlock().getDefaultState();
        } catch (Exception no) {
            return false;
        }

        if (block.getBlock().isIn(BlockTags.FLOWERS)) {
            return true;
        }
        if (block.getBlock() instanceof PlantBlock) {
            return true;
        }
        if (block.isOf(Blocks.CHORUS_FLOWER)) {
            return true;
        }
        if (block.isOf(Blocks.COCOA)) {
            return true;
        }
        if (block.isOf(Blocks.SEAGRASS) || block.isOf(Blocks.KELP) || block.isOf(Blocks.SEA_PICKLE)) {
            return true;
        }
        if (block.isOf(Blocks.LILY_PAD)) {
            return true;
        }
        if (block.getBlock() instanceof CropBlock || block.getBlock() instanceof NetherWartBlock) {
            return true;
        }
        if (JarBlockEntity.isTree(block) ||
                    block.getBlock() instanceof CactusBlock || 
                    block.getBlock() instanceof BambooBlock || 
                    block.getBlock() instanceof SugarCaneBlock ||
                    block.isOf(Blocks.RED_MUSHROOM) ||
                    block.isOf(Blocks.BROWN_MUSHROOM)
                ) {
            return true;
        }

        return false;
    }

    public static boolean isGoodIdeaBase(ItemStack i) {
        if (isGoodIdeaPlant(i)) {
            return false;
        }
        if (i.getItem() instanceof IBucketItem) return true;
        BlockState block;
        try {
            block = ((BlockItem)i.getItem()).getBlock().getDefaultState();
        } catch (Exception no) {
            return false;
        }
        try {
            if (block.isSideSolidFullSquare(null, null, Direction.DOWN) /*&& block.isSideSolidFullSquare(null, null, Direction.UP)*/) {
                return true;
            }
        } catch (Exception no) {
            no.printStackTrace();
        }
        return false;
    }
    
}