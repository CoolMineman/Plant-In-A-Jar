package io.github.coolmineman.plantinajar.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;

import io.github.coolmineman.plantinajar.GrowsMultiblockPlantBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FungusBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(FungusBlock.class)
public class FungusBlockMixin implements GrowsMultiblockPlantBlock {
    @Override
    public void doTheGrow(ServerWorld world, BlockPos pos, BlockState state, Random random) {
        ((FungusBlock)(Object)this).grow(world, random, pos, state);
    }
}
