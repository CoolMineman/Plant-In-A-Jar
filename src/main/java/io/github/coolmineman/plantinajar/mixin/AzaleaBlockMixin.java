package io.github.coolmineman.plantinajar.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;

import io.github.coolmineman.plantinajar.GrowsMultiblockPlantBlock;
import net.minecraft.block.AzaleaBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(AzaleaBlock.class)
public class AzaleaBlockMixin implements GrowsMultiblockPlantBlock {
    @Override
    public void doTheGrow(ServerWorld world, BlockPos pos, BlockState state, Random random) {
        ((AzaleaBlock)(Object)this).grow(world, random, pos, state);
    }
}
