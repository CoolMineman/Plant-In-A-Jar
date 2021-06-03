package io.github.coolmineman.plantinajar.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;

import io.github.coolmineman.plantinajar.GrowsMultiblockPlantBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(SaplingBlock.class)
public class SaplingBlockMixin implements GrowsMultiblockPlantBlock {
    @Override
    public void doTheGrow(ServerWorld world, BlockPos pos, BlockState state, Random random) {
        ((SaplingBlock)(Object)this).generate(world, pos, state, random);
    }
}
